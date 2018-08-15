package com.github.neone35.enalyzer.ui.scan;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.ToastUtils;
import com.github.neone35.enalyzer.HelpUtils;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.data.models.localjson.ecodelist.EcodeListItem;
import com.github.neone35.enalyzer.ui.OnAsyncEventListener;
import com.github.neone35.enalyzer.ui.scan.camera.ScanCameraFragment;
import com.github.neone35.enalyzer.ui.scan.chips.ScanChipsListFragment;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanActivity extends AppCompatActivity {

    @BindView(R.id.inc_fab_camera)
    FloatingActionButton cameraFab;
    @BindView(R.id.flash_fab)
    FloatingActionButton flashFab;
    @BindView(R.id.inc_fab_done)
    FloatingActionButton doneFab;
    @BindView(R.id.pb_scan)
    ProgressBar pbScan;

    @BindDrawable(R.drawable.ic_flash_on_grey_24dp)
    Drawable mFlashOnDrawable;
    @BindDrawable(R.drawable.ic_flash_off_grey_24dp)
    Drawable mFlashOffDrawable;

    public static boolean TEXT_DETECTED = false;
    public static boolean ECODES_MATCHED = false;
    private ArrayList<String> mEcodeList;
    private String mFoundText;
    private ArrayList<String> mMatchedEcodesList;
    private FragmentManager mFragmentManager;
    private Camera mScanCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

//        postponeTransitions();
        mFragmentManager = getSupportFragmentManager();

        askForScanShortcutPin();
        mEcodeList = initEcodeList();
        listenForFABclick();
    }

    private void askForScanShortcutPin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Context ctx = ScanActivity.this;
            AppWidgetManager appWidgetManager = ctx.getSystemService(AppWidgetManager.class);
            ComponentName scanShortcutWidgetProvider = new ComponentName(ctx, ScanShortcutWidgetProvider.class);
            if (appWidgetManager != null && appWidgetManager.isRequestPinAppWidgetSupported()) {
                appWidgetManager.requestPinAppWidget(scanShortcutWidgetProvider, null, null);
            }
        }
    }

    private ArrayList<String> initEcodeList() {
        String ecodesJsonString = HelpUtils.readJSONStringFromAsset(this, "ecodes.json");
        List<EcodeListItem> ecodeObjects = HelpUtils.getLocalEcodeObjectList(ecodesJsonString);
        Logger.d(HelpUtils.getEcodes(ecodeObjects));
        Logger.d(HelpUtils.getWikiDataQCodes(ecodeObjects));
        Logger.d(HelpUtils.getWikiDataNames(ecodeObjects));
        return HelpUtils.getEcodes(ecodeObjects);
    }

    private void listenForFABclick() {
        // initial done FAB listener
        doneFab.setOnClickListener(v -> ToastUtils.showShort("Nothing was detected. Try again"));

        flashFab.setOnClickListener(v -> {
            mScanCamera = ScanCameraFragment.mCamera;
            Camera.Parameters camParams = mScanCamera.getParameters();
            String flashMode = camParams.getFlashMode();
            String torchFlashOn = Camera.Parameters.FLASH_MODE_TORCH;
            String flashOff = Camera.Parameters.FLASH_MODE_OFF;

            if (flashMode.equals(flashOff)) {
                camParams.setFlashMode(torchFlashOn);
                mScanCamera.setParameters(camParams);
                flashFab.setImageDrawable(mFlashOffDrawable);
            } else if (flashMode.equals(torchFlashOn)) {
                camParams.setFlashMode(flashOff);
                mScanCamera.setParameters(camParams);
                flashFab.setImageDrawable(mFlashOnDrawable);
            }
        });

        // camera FAB listener
        cameraFab.setOnClickListener((View v) -> {
            // erase list made on last photo scan
            mMatchedEcodesList = new ArrayList<>();
            mFoundText = null;
            pbScan.setVisibility(View.VISIBLE);
            mScanCamera = ScanCameraFragment.mCamera;
            if (mScanCamera != null) {
                // focus camera first
                mScanCamera.autoFocus((boolean success, Camera camera) -> {
                    // flash turns off
                    flashFab.setImageDrawable(mFlashOnDrawable);
                    // take picture only when focused
                    if (success) {
                        // get an image from the camera
                        mScanCamera.takePicture(null, null, (byte[] data, Camera cameraFocused) -> {
                            // set ML Kit options
                            FirebaseVisionCloudTextRecognizerOptions options =
                                    new FirebaseVisionCloudTextRecognizerOptions.Builder()
                                            // better suits well-formatted dense text (excellent for ingredient labels)
                                            .setModelType(FirebaseVisionCloudTextRecognizerOptions.DENSE_MODEL)
                                            .build();
                            // extract image & get detector instance
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            Bitmap scaledBitmap = scaleDownBitmapQuality(bitmap, 85);
                            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(scaledBitmap);
                            FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance().getCloudTextRecognizer(options);
                            // run detector on extracted image
                            detectInImage(image, textRecognizer, cameraFocused);
                            // set new listener on done FAB
                            doneFab.setOnClickListener(view -> {
                                // save this scan image to SD only if ecodes found
                                if (ECODES_MATCHED) {
                                    // save image to external storage in separate thread
                                    FileSaveTask fileSaveTask = new FileSaveTask(this, new OnAsyncEventListener<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean success) {
                                            ToastUtils.showShort("File successfully saved");
                                            // go back to MainActivity
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            ToastUtils.showShort("Could not save the file");
                                            e.printStackTrace();
                                        }
                                    });
                                    fileSaveTask.execute(data, null, null);
                                } else {
                                    ToastUtils.showShort("No additive codes found. Couldn't save scan");
                                }
                            });
                        });
                    } else {
                        pbScan.setVisibility(View.INVISIBLE);
                        ToastUtils.showShort("Camera not focused. Try again");
                    }
                });
            }
        });
    }

    @SuppressWarnings("SameParameterValue")
    private Bitmap scaleDownBitmapQuality(Bitmap bitmap, int quality) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }

    private void detectInImage(FirebaseVisionImage image, FirebaseVisionTextRecognizer textRecognizer, Camera camera) {
        // return true if detection runs
        textRecognizer.processImage(image)
                .addOnSuccessListener(firebaseVisionText -> {
                    Logger.d("Detection successful");
                    // get OCR words
                    mFoundText = getRecognizedText(firebaseVisionText);
                    if (mFoundText != null) {
                        // match text with ecode list
                        mMatchedEcodesList = HelpUtils.matchEcodes(mFoundText, mEcodeList);
                        Logger.d("Matched ecodes: " + mMatchedEcodesList);
                        if (HelpUtils.checkListNullEmpty(mMatchedEcodesList)) {
                            ECODES_MATCHED = true;
                            // add matched ecodes as chips to sibling fragment
                            populateChipsLayout(mMatchedEcodesList);
                        } else {
                            ECODES_MATCHED = false;
                            ToastUtils.showShort("No additive codes found");
                        }
                    }
                    // restart camera after detection
                    pbScan.setVisibility(View.INVISIBLE);
                    camera.startPreview();
                })
                .addOnFailureListener(e -> {
                    // Task failed with an exception
                    Logger.d("Detection unsuccessful");
                    ToastUtils.showShort("Detection unsuccessful");
                    pbScan.setVisibility(View.INVISIBLE);
                    camera.startPreview();
                    e.printStackTrace();
                });
    }

    private void populateChipsLayout(ArrayList<String> matchedEcodes) {
        // display extracted ecodes in chips layout
        if (mFragmentManager != null) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fl_scan_chips, ScanChipsListFragment.newInstance(matchedEcodes))
                    .commit();
        }
    }

    private String getRecognizedText(FirebaseVisionText firebaseVisionText) {
        if (firebaseVisionText != null) {
            String recognizedText = firebaseVisionText.getText();
            Logger.d("Recognized text: " + recognizedText);
            TEXT_DETECTED = true;
//            for (FirebaseVisionCloudText.Page page : firebaseVisionText.getPages()) {
//                for (FirebaseVisionCloudText.Block block : page.getBlocks()) {
//                    for (FirebaseVisionCloudText.Paragraph paragraph : block.getParagraphs()) {
//                        for (FirebaseVisionCloudText.Word word : paragraph.getWords()) {
//                            StringBuilder wordString = new StringBuilder();
//                            for (FirebaseVisionCloudText.Symbol symbol : word.getSymbols()) {
//                                wordString.append(symbol.getText());
//                            }
//                            mFoundWordsList.add(wordString.toString());
//                        }
//                    }
//                }
//            }
            return recognizedText;
        } else {
            TEXT_DETECTED = false;
            ToastUtils.showShort("No text found");
            return null;
        }
    }


    private void postponeTransitions() {
        supportPostponeEnterTransition();
        cameraFab.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        cameraFab.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                }
        );
    }
}

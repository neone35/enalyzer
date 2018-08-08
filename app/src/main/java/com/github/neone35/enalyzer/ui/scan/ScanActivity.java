package com.github.neone35.enalyzer.ui.scan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.ToastUtils;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.ui.OnAsyncEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudText;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudTextDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class ScanActivity extends AppCompatActivity implements
        ScanCameraFragment.OnScanCameraFragmentListener,
        ScanChipsFragment.OnScanChipsFragmentListener {

    @BindView(R.id.inc_fab_camera)
    FloatingActionButton cameraFab;
    @BindView(R.id.inc_fab_done)
    FloatingActionButton doneFab;
    @BindView(R.id.pb_scan)
    ProgressBar pbScan;

    @BindString(R.string.app_name)
    String mAppName;

    public static boolean TEXT_DETECTED = false;
    private Camera.PictureCallback mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        askForScanShortcutPin();
//        postponeTransitions();
        listenForFABclick(cameraFab, doneFab);
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

    private void listenForFABclick(FloatingActionButton cameraFab, FloatingActionButton doneFab) {
        // take a photo
        cameraFab.setOnClickListener(v -> {
            pbScan.setVisibility(View.VISIBLE);
            // get an image from the camera
            ScanCameraFragment.mCamera.takePicture(null, null, mPicture);
        });
        // initial listener
        doneFab.setOnClickListener(v -> ToastUtils.showShort("Nothing was detected. Try again"));

        // when photo is taken, callback is sent here
        mPicture = (data, camera) -> {
            // set ML Kit options
            FirebaseVisionCloudDetectorOptions options =
                    new FirebaseVisionCloudDetectorOptions.Builder()
                            .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                            .setMaxResults(15)
                            .build();
            // extract image & get detector instance
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            FirebaseVisionCloudTextDetector detector = FirebaseVision.getInstance().getVisionCloudTextDetector(options);
//            FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
            // run detector on extracted image
            detectInImage(image, detector, camera);
            // save this scan instance to DB
            doneFab.setOnClickListener(v -> {
                if (TEXT_DETECTED) {
                    String fileSavingTitle = getResources().getString(R.string.saving_file);
                    String pleaseWaitMessage = getResources().getString(R.string.please_wait);

                    // save image to external storage in separate thread
                    FileSaveTask fileSaveTask = new FileSaveTask(mAppName, fileSavingTitle, pleaseWaitMessage,
                            this, new OnAsyncEventListener<Boolean>() {
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
                    ToastUtils.showShort("Nothing was detected. Try again");
                }
            });
        };
    }

    private void detectInImage(FirebaseVisionImage image, FirebaseVisionCloudTextDetector detector, Camera camera) {
        // return true if detection runs
        detector.detectInImage(image)
                .addOnSuccessListener(firebaseVisionText -> {
                    Logger.d("Detection successful");
                    detectEcodes(firebaseVisionText);
                    // restart camera after detection
                    pbScan.setVisibility(View.INVISIBLE);
                    camera.startPreview();
                })
                .addOnFailureListener(e -> {
                    // Task failed with an exception
                    Logger.d("Detection unsuccessful");
                    pbScan.setVisibility(View.INVISIBLE);
                    camera.startPreview();
                    e.printStackTrace();
                });
    }

    private void detectEcodes(FirebaseVisionCloudText firebaseVisionText) {
        if (firebaseVisionText != null) {
            String recognizedText = firebaseVisionText.getText();
            Logger.d("Recognized text: " + recognizedText);
            TEXT_DETECTED = true;
            for (FirebaseVisionCloudText.Page page : firebaseVisionText.getPages()) {
                for (FirebaseVisionCloudText.Block block : page.getBlocks()) {
                    Rect boundingBox = block.getBoundingBox();
                    Logger.d("Block text: " + block.getTextProperty());
                    for (FirebaseVisionCloudText.Paragraph paragraph : block.getParagraphs()) {
                        Logger.d("Paragraph text: " + paragraph.getTextProperty());
                        for (FirebaseVisionCloudText.Word word : paragraph.getWords()) {
                            Logger.d("Word text: " + word.getTextProperty());
                            for (FirebaseVisionCloudText.Symbol symbol : word.getSymbols()) {
                                Logger.d("Symbol: " + symbol.getText());
                            }
                        }
                    }
                }
            }
        } else {
            TEXT_DETECTED = false;
            ToastUtils.showShort("No text found");
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

    @Override
    public void onScanCameraFragmentInteraction(Uri uri) {
        // TODO: append extracted E codes to ChipsFragment adapter
    }

    @Override
    public void onScanChipsFragmentInteraction(Uri uri) {
        // TODO: save final E code list set (after chips removal) into DB
    }
}

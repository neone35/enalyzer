package com.github.neone35.enalyzer.ui.scan;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.ToastUtils;
import com.github.neone35.enalyzer.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

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

    @BindString(R.string.app_name)
    String mAppName;

    public static boolean mDetectSuccess = false;
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
        cameraFab.setOnClickListener(v -> {
            // get an image from the camera
            ScanCameraFragment.mCamera.takePicture(null, null, mPicture);
        });

        // when picture is taken, callback is sent here
        mPicture = (data, camera) -> {
            // detect text with Firebase ML Kit
            FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                    .setWidth(camera.getParameters().getPreviewSize().width)
                    .setHeight(camera.getParameters().getPreviewSize().height)
                    .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
//                    .setRotation(rotation)
                    .build();
            FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(data, metadata);
            FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
            mDetectSuccess = detectInImage(image, detector, camera);
            // save this scan instance to DB
            doneFab.setOnClickListener(v -> {
                if (mDetectSuccess) {
                    // save image to external storage
                    boolean writeSuccess = writeOutputMediaFileToSD(data);
                    if (writeSuccess) {
                        ToastUtils.showShort("File successfully saved");
                        // go back to MainActivity
                        finish();
                    } else {
                        ToastUtils.showShort("Could not save the file");
                    }
                } else {
                    ToastUtils.showShort("Nothing was detected");
                }
            });
        };
    }

    private boolean detectInImage(FirebaseVisionImage image, FirebaseVisionTextDetector detector, Camera camera) {
        // Task completed successfully
        Task<FirebaseVisionText> result = detector.detectInImage(image)
                .addOnSuccessListener((OnSuccessListener<FirebaseVisionText>) firebaseVisionText -> {
                    ToastUtils.showShort("Detection successful.");
                    Logger.d(firebaseVisionText.getBlocks());
                    detectEcodes(firebaseVisionText);
                    camera.startPreview();
                })
                .addOnFailureListener((OnFailureListener) e -> {
                    // Task failed with an exception
                    ToastUtils.showShort("Detection unsuccessful. Try again.");
                    camera.startPreview();
                    e.printStackTrace();
                });
        return result.isSuccessful();
    }

    private void detectEcodes(FirebaseVisionText firebaseVisionText) {
        for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
            Rect boundingBox = block.getBoundingBox();
            Point[] cornerPoints = block.getCornerPoints();
            String text = block.getText();

            Logger.d("What is found: " + text);

            for (FirebaseVisionText.Line line : block.getLines()) {
                Logger.d(line);
                for (FirebaseVisionText.Element element : line.getElements()) {
                    Logger.d(element);
                }
            }
        }
    }

    private boolean writeOutputMediaFileToSD(byte[] data) {
        File pictureFile = createOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            Logger.d("Error creating media file, check storage permissions. ");
            return false;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            Logger.d("File not found: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Logger.d("Error accessing file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Create a File for saving an image
     */
    private File createOutputMediaFile(int type) {
        // To be safe, check that the SDCard is mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), mAppName);
            // This location works best if you want the created images to be shared
            // between applications and persist after your app has been uninstalled.

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Logger.d(mAppName, "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File mediaFile;
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                        "IMG_" + timeStamp + ".jpg");
            } else {
                return null;
            }

            return mediaFile;
        } else {
            Logger.d("No mounted SD card found");
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

    @Override
    public void onScanCameraFragmentInteraction(Uri uri) {
        // TODO: append extracted E codes to ChipsFragment adapter
    }

    @Override
    public void onScanChipsFragmentInteraction(Uri uri) {
        // TODO: save final E code list set (after chips removal) into DB
    }
}

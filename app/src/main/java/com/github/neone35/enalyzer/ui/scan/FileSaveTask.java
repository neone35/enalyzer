package com.github.neone35.enalyzer.ui.scan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.ui.OnAsyncEventListener;
import com.github.neone35.enalyzer.ui.main.MainActivity;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindString;
import butterknife.ButterKnife;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class FileSaveTask extends AsyncTask<byte[], Void, Boolean> {

    private ProgressDialog dialog;
    private OnAsyncEventListener<Boolean> mCallBack;
    private Exception mException;
    private String mAppName;
    private String mDialogTitle;
    private String mDialogMessage;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    FileSaveTask(String appName, String saveDialogTitle, String saveDialogMessage,
                 Context context, OnAsyncEventListener<Boolean> callback) {
        mAppName = appName;
        mDialogTitle = saveDialogTitle;
        mDialogMessage = saveDialogMessage;
        mCallBack = callback;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(mContext);
        dialog.setTitle(mDialogTitle);
        dialog.setMessage(mDialogMessage);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(byte[]... bytes) {
        try {
            writeOutputMediaFileToSD(bytes[0]);
            return true;
        } catch (Exception e) {
            mException = e;
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean successful) {
        dialog.dismiss();
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(successful);
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }

    private void writeOutputMediaFileToSD(byte[] data) {
        File pictureFile = createOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (pictureFile == null) {
            Logger.d("Error creating media file, check storage permissions");
        } else {
            try {
                // rotate picture before saving
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                // compress and save
                FileOutputStream fos = new FileOutputStream(pictureFile);
                bmp.compress(Bitmap.CompressFormat.JPEG, 85, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                Logger.d("File not found: " + e.getMessage());
            } catch (IOException e) {
                Logger.d("Error accessing file: " + e.getMessage());
            }
        }
    }

    /**
     * Create a File for saving an image
     */
    private File createOutputMediaFile(int type) {
        // To be safe, check that the SDCard is mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File mediaStorageDir = MainActivity.mMediaStorageDir;

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
}

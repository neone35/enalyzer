package com.github.neone35.enalyzer.ui.scan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Environment;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;
import com.github.neone35.enalyzer.ui.main.MainActivity;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class FileSaveTask extends AsyncTask<byte[], Void, ScanPhoto> {

    private ProgressDialog dialog;
    private OnAsyncFileSaveListener<ScanPhoto> mCallBack;
    private Exception mException;
    private String mAppName;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private ScanPhoto mScanPhoto;

    FileSaveTask(Context context, OnAsyncFileSaveListener<ScanPhoto> callback) {
        mCallBack = callback;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        mAppName = mContext.getResources().getString(R.string.app_name);
        String fileSavingTitle = mContext.getResources().getString(R.string.saving_file);
        String pleaseWaitMessage = mContext.getResources().getString(R.string.please_wait);
        dialog = new ProgressDialog(mContext);
        dialog.setTitle(fileSavingTitle);
        dialog.setMessage(pleaseWaitMessage);
        dialog.setIndeterminate(true);
        dialog.show();
    }

    @Override
    protected ScanPhoto doInBackground(byte[]... bytes) {
        try {
            writeOutputMediaFileToSD(bytes[0]);
            return mScanPhoto;
        } catch (Exception e) {
            mException = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(ScanPhoto scanPhotoWithFileAndDate) {
        dialog.dismiss();
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess(scanPhotoWithFileAndDate);
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
                String path = mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg";
                mScanPhoto = new ScanPhoto(path, timeStamp, null);
                mediaFile = new File(path);
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

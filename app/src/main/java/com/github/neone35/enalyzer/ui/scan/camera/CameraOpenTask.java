package com.github.neone35.enalyzer.ui.scan.camera;

import android.hardware.Camera;
import android.os.AsyncTask;

import com.github.neone35.enalyzer.ui.OnAsyncEventListener;

/**
 * A safe way to get an instance of the Camera object.
 */
public class CameraOpenTask extends AsyncTask<Void, Void, Camera> {

    private Camera mCamera;
    private OnAsyncEventListener<Camera> mCallback;
    private Exception mException;

    CameraOpenTask(OnAsyncEventListener<Camera> callback) {
        mCallback = callback;
    }

    @Override
    protected Camera doInBackground(Void... voids) {
        Camera c = null;
        try {
            releaseCameraAndPreview();
            c = Camera.open(0); // attempt to get a back Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            mException = e;
        }
        return c; // returns null if camera is unavailable
    }

    protected void onPostExecute(Camera camera) {
        if (mCallback != null) {
            if (mException == null) {
                mCallback.onSuccess(camera);
            } else {
                mCallback.onFailure(mException);
            }
        }
    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
}

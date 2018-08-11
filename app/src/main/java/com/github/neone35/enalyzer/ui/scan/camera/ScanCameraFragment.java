package com.github.neone35.enalyzer.ui.scan.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.ToastUtils;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.ui.OnAsyncEventListener;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ScanCameraFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.pb_camera_preview)
    ProgressBar pbCameraPreview;
    @BindView(R.id.fl_scan_camera_preview)
    FrameLayout flScanCameraPreview;

    public static Camera mCamera;
    private CameraPreview mPreview;

    public ScanCameraFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan_camera, container, false);
        ButterKnife.bind(this, rootView);

        // Create an instance of Camera and set it on Preview
        startCameraPreview();

        return rootView;
    }

    public void hideStatusBar() {
        Objects.requireNonNull(this.getActivity()).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void startCameraPreview() {
        pbCameraPreview.setVisibility(View.VISIBLE);
        // if phone has back camera, proceed
        if (checkCameraHardware(Objects.requireNonNull(this.getActivity()))) {
            hideStatusBar();
            // Open back camera in separate thread
            CameraOpenTask cameraOpenTask = new CameraOpenTask(new OnAsyncEventListener<Camera>() {
                @Override
                public void onSuccess(Camera camera) {
                    pbCameraPreview.setVisibility(View.INVISIBLE);
                    // Create an instance of Camera
                    mCamera = camera;
                    // Create our Preview view and set it as the content of our activity.
                    mPreview = new CameraPreview(getActivity(), mCamera);
                    flScanCameraPreview.addView(mPreview);
                }

                @Override
                public void onFailure(Exception e) {
                    pbCameraPreview.setVisibility(View.INVISIBLE);
                    ToastUtils.showShort("Camera in use by another app");
                    e.printStackTrace();
                }
            });
            cameraOpenTask.execute(null, null, null);
        } else {
            ToastUtils.showShort("No back facing camera found");
        }
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        // this device has a camera OR
        // no camera on this device
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA) && Camera.getNumberOfCameras() > 0;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCamera != null) {
            mCamera.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCamera != null) {
            mCamera.release();
        }
    }
}

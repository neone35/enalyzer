package com.github.neone35.enalyzer.ui.scan;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
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

    private String mParam1;
    private OnScanCameraFragmentListener mListener;
    public static Camera mCamera;
    private CameraPreview mPreview;

    public ScanCameraFragment() {
    }

    public static ScanCameraFragment newInstance(String param1) {
        ScanCameraFragment fragment = new ScanCameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
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

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onScanCameraFragmentInteraction(uri);
        }
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
        if (context instanceof OnScanCameraFragmentListener) {
            mListener = (OnScanCameraFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScanCameraFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCamera != null) {
            mCamera.release();
        }
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCamera != null) {
            mCamera.release();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnScanCameraFragmentListener {

        void onScanCameraFragmentInteraction(Uri uri);
    }
}

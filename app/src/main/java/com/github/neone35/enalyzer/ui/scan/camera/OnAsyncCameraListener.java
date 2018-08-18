package com.github.neone35.enalyzer.ui.scan.camera;

public interface OnAsyncCameraListener<T> {
    void onSuccess(T object);

    void onFailure(Exception e);
}

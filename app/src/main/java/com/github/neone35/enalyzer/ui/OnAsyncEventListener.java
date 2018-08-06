package com.github.neone35.enalyzer.ui;


public interface OnAsyncEventListener<T> {
    void onSuccess(T object);

    void onFailure(Exception e);
}

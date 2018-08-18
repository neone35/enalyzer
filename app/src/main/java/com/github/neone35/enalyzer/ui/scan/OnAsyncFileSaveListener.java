package com.github.neone35.enalyzer.ui.scan;

import com.github.neone35.enalyzer.data.models.room.ScanPhoto;

public interface OnAsyncFileSaveListener<T> {
    void onSuccess(ScanPhoto object);

    void onFailure(Exception e);
}

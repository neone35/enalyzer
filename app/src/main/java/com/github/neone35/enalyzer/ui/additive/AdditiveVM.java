package com.github.neone35.enalyzer.ui.additive;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.github.neone35.enalyzer.data.MainRepository;
import com.github.neone35.enalyzer.data.models.room.Additive;

public class AdditiveVM extends ViewModel {

    private final LiveData<Additive> mAdditive;

    AdditiveVM(MainRepository repository, String eCode) {
        mAdditive = repository.getAdditiveByEcode(eCode);
    }

    public LiveData<Additive> getOneAdditive() {
        return mAdditive;
    }

}

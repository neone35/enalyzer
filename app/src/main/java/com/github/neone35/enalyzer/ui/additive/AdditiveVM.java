package com.github.neone35.enalyzer.ui.additive;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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

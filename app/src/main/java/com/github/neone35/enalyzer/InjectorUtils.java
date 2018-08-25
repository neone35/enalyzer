package com.github.neone35.enalyzer;

import android.content.Context;

import com.github.neone35.enalyzer.data.MainRepository;
import com.github.neone35.enalyzer.data.database.MainDatabase;
import com.github.neone35.enalyzer.data.network.NetworkRoot;
import com.github.neone35.enalyzer.ui.main.codes.category.CodeCategoryViewModelFactory;
import com.github.neone35.enalyzer.ui.main.scans.detail.ScanDetailViewModelFactory;
import com.github.neone35.enalyzer.ui.main.scans.photos.ScanPhotoViewModelFactory;

public class InjectorUtils {

    // for ViewModels
    private static MainRepository provideRepository(Context context) {
        MainDatabase database = MainDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        NetworkRoot networkDataSource =
                NetworkRoot.getInstance(context.getApplicationContext(), executors);
        return MainRepository.getInstance(
                database.scanPhotoDao(), database.codeCategoryDao(),
                database.additiveDao(), networkDataSource, executors);
    }

    // for services and jobs (external access)
    public static NetworkRoot provideNetworkDataSource(Context context) {
        provideRepository(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return NetworkRoot.getInstance(context.getApplicationContext(), executors);
    }

    public static ScanPhotoViewModelFactory provideScanPhotoViewModelFactory(Context context) {
        MainRepository repository = provideRepository(context.getApplicationContext());
        return new ScanPhotoViewModelFactory(repository);
    }

    public static ScanDetailViewModelFactory provideScanDetailViewModelFactory(Context context, int scanPhotoID) {
        MainRepository repository = provideRepository(context.getApplicationContext());
        return new ScanDetailViewModelFactory(repository, scanPhotoID);
    }

    public static CodeCategoryViewModelFactory provideCodeCategoryViewModelFactory(Context context) {
        MainRepository repository = provideRepository(context.getApplicationContext());
        return new CodeCategoryViewModelFactory(repository);
    }

}

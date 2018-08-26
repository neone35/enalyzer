package com.github.neone35.enalyzer;

import android.content.Context;

import com.github.neone35.enalyzer.data.MainRepository;
import com.github.neone35.enalyzer.data.database.MainDatabase;
import com.github.neone35.enalyzer.data.network.NetworkRoot;
import com.github.neone35.enalyzer.ui.main.codes.detail.CodeAdditivesVMF;
import com.github.neone35.enalyzer.ui.main.scans.detail.ScanAdditivesVMF;
import com.github.neone35.enalyzer.ui.main.codes.category.CodeCategoryVMF;
import com.github.neone35.enalyzer.ui.main.scans.photos.ScanPhotoVMF;

import java.util.List;

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

    public static ScanPhotoVMF provideScanPhotoViewModelFactory(Context context) {
        MainRepository repository = provideRepository(context.getApplicationContext());
        return new ScanPhotoVMF(repository);
    }

    public static ScanAdditivesVMF provideScanAdditivesViewModelFactory(Context context, int scanPhotoID, List<String> ecodes) {
        MainRepository repository = provideRepository(context.getApplicationContext());
        return new ScanAdditivesVMF(repository, scanPhotoID, ecodes);
    }

    public static CodeCategoryVMF provideCodeCategoryViewModelFactory(Context context) {
        MainRepository repository = provideRepository(context.getApplicationContext());
        return new CodeCategoryVMF(repository);
    }

    public static CodeAdditivesVMF provideCodeAdditivesViewModelFactory(Context context, int codeCategoryID, List<String> ecodes) {
        MainRepository repository = provideRepository(context.getApplicationContext());
        return new CodeAdditivesVMF(repository, codeCategoryID, ecodes);
    }

}

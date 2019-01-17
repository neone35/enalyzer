package com.github.neone35.enalyzer.data.network;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.github.neone35.enalyzer.InjectorUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class WikiJobService extends JobIntentService {
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Logger.d("Wiki JobService started");
        NetworkRoot networkRoot = InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        Bundle wikiExtrasBundle = intent.getExtras();
        if (wikiExtrasBundle != null) {
            ArrayList<String> qCodes = wikiExtrasBundle.getStringArrayList(NetworkRoot.KEY_WIKI_QCODES);
            networkRoot.fetchWiki(qCodes);
        }
    }
}

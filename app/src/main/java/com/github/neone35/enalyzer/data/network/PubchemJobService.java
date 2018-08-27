package com.github.neone35.enalyzer.data.network;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.github.neone35.enalyzer.InjectorUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class PubchemJobService extends JobIntentService {
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Logger.d("Pubchem JobService started");
        NetworkRoot networkRoot = InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        Bundle pubchemExtrasBundle = intent.getExtras();
        if (pubchemExtrasBundle != null) {
            ArrayList<String> additiveTitles = pubchemExtrasBundle.getStringArrayList(NetworkRoot.KEY_ADDITIVE_TITLES);
            ArrayList<String> hazardCodes = pubchemExtrasBundle.getStringArrayList(NetworkRoot.KEY_HAZARD_CODES);
            ArrayList<String> hazardStatements = pubchemExtrasBundle.getStringArrayList(NetworkRoot.KEY_HAZARD_STATEMENTS);
            networkRoot.fetchPubchem(additiveTitles, hazardCodes, hazardStatements);
        }
    }
}

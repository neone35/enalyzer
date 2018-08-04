package com.github.neone35.enalyzer.ui.scan;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.neone35.enalyzer.R;

import butterknife.ButterKnife;

public class ScanActivity extends AppCompatActivity implements
        ScanCameraFragment.OnScanCameraFragmentListener,
        ScanChipsFragment.OnScanChipsFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);

        askForScanShortcutPin();
    }

    private void askForScanShortcutPin() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Context ctx = ScanActivity.this;
            AppWidgetManager appWidgetManager = ctx.getSystemService(AppWidgetManager.class);
            ComponentName scanShortcutWidgetProvider = new ComponentName(ctx, ScanShortcutWidgetProvider.class);
            if (appWidgetManager != null && appWidgetManager.isRequestPinAppWidgetSupported()) {
                appWidgetManager.requestPinAppWidget(scanShortcutWidgetProvider, null, null);
            }
        }
    }

    @Override
    public void onScanCameraFragmentInteraction(Uri uri) {
        // TODO: append extracted E codes to ChipsFragment adapter
    }

    @Override
    public void onScanChipsFragmentInteraction(Uri uri) {
        // TODO: save final E code list set (after chips removal) into DB
    }
}

package com.github.neone35.enalyzer.scan;

import android.net.Uri;
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

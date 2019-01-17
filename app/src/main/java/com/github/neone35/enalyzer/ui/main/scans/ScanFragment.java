package com.github.neone35.enalyzer.ui.main.scans;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.ui.main.scans.photos.ScanPhotoListFragment;

// Root Fragment for Scan fragments
public class ScanFragment extends Fragment {

    public ScanFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View scanRootView = inflater.inflate(R.layout.fragment_scan, container, false);

        if (getFragmentManager() != null) {
            // replace root fragment with ScanList first
            // it'll be replaced by ScanDetailList on item click
            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fl_scan_root, ScanPhotoListFragment.newInstance(2))
                        .commit();
            }
        }

        return scanRootView;
    }

}

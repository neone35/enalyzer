package com.github.neone35.enalyzer;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            getFragmentManager().beginTransaction()
                    .add(R.id.fl_scan_root, ScanListFragment.newInstance(2))
                    .commit();
        }

        return scanRootView;
    }

}

package com.github.neone35.enalyzer.scan;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.neone35.enalyzer.R;

import butterknife.ButterKnife;


public class ScanChipsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private OnScanChipsFragmentListener mListener;

    public ScanChipsFragment() {
    }

    public static ScanChipsFragment newInstance(String param1, String param2) {
        ScanChipsFragment fragment = new ScanChipsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan_chips, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onScanChipsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScanChipsFragmentListener) {
            mListener = (OnScanChipsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScanChipsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnScanChipsFragmentListener {
        void onScanChipsFragmentInteraction(Uri uri);
    }
}

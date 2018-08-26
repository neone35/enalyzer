package com.github.neone35.enalyzer.ui.main.scans.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.neone35.enalyzer.InjectorUtils;
import com.github.neone35.enalyzer.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnScanDetailListListener}
 * interface.
 */
public class ScanDetailListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PHOTO_ID = "photo-id";
    private static final String ARG_PHOTO_ECODES = "photo-ecodes";
    public static final int KNOWN_AS_NUM = 3;
    private int mColumnCount = 1;
    private OnScanDetailListListener mListener;
    private int mScanPhotoID;
    private ArrayList<String> mScanPhotoEcodes;
    private ProgressBar mScanLoadingBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScanDetailListFragment() {
    }

    public static ScanDetailListFragment newInstance(int columnCount, int scanPhotoID, ArrayList<String> scanPhotoEcodes) {
        ScanDetailListFragment fragment = new ScanDetailListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_PHOTO_ID, scanPhotoID);
        args.putStringArrayList(ARG_PHOTO_ECODES, scanPhotoEcodes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mScanPhotoID = getArguments().getInt(ARG_PHOTO_ID);
            mScanPhotoEcodes = getArguments().getStringArrayList(ARG_PHOTO_ECODES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_detail_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            // Get repository instance (start observing MutableLiveData trigger)
            ScanAdditivesVMF factory =
                    InjectorUtils.provideScanAdditivesVMF(Objects.requireNonNull(this.getContext()), mScanPhotoID, mScanPhotoEcodes);
            // Tie fragment & ViewModel together
            ScanAdditivesVM viewModel = ViewModelProviders.of(this, factory).get(ScanAdditivesVM.class);
            // Trigger LiveData notification on fragment creation & observe change in DB calling DAO
            viewModel.getAdditives().observe(this, additiveList -> {
                if (additiveList != null) {
                    Logger.d("Setting scanDetail adapter");
                    recyclerView.setAdapter(new ScanDetailListAdapter(mListener, additiveList, mScanPhotoID));
                }
            });
            viewModel.getLoading().observe(this, isLoading -> {
                mScanLoadingBar = view.getRootView().findViewById(R.id.pb_scan_detail);
                if (isLoading != null)
                    if (isLoading) {
                        mScanLoadingBar.setVisibility(View.VISIBLE);
                    } else {
                        mScanLoadingBar.setVisibility(View.GONE);
                    }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScanDetailListListener) {
            mListener = (OnScanDetailListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScanDetailListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mScanLoadingBar.setVisibility(View.GONE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnScanDetailListListener {
        void onScanDetailListInteraction(HashMap<String, View> transitionViews, String eCode, int scanPhotoID);
    }
}

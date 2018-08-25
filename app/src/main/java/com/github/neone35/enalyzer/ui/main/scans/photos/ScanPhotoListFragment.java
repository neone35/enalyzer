package com.github.neone35.enalyzer.ui.main.scans.photos;

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

import com.blankj.utilcode.util.ToastUtils;
import com.github.neone35.enalyzer.InjectorUtils;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.dummy.DummyContent;
import com.github.neone35.enalyzer.ui.main.MainActivity;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnScanPhotoListListener}
 * interface.
 */
public class ScanPhotoListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private OnScanPhotoListListener mListener;
    private File[] mPhotoFilesList = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScanPhotoListFragment() {
    }

    @SuppressWarnings("unused")
    public static ScanPhotoListFragment newInstance(int columnCount) {
        ScanPhotoListFragment fragment = new ScanPhotoListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    public File[] getListOfSavedPhotoFiles() {
        File photosDir = MainActivity.mMediaStorageDir;
        return photosDir.listFiles();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan_list, container, false);

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
            ScanPhotoViewModelFactory factory =
                    InjectorUtils.provideScanPhotoViewModelFactory(Objects.requireNonNull(this.getContext()));
            // Tie fragment & ViewModel together
            ScanPhotoViewModel viewModel = ViewModelProviders.of(this, factory).get(ScanPhotoViewModel.class);
            // Trigger LiveData notification on fragment creation & observe change in DB calling DAO
            viewModel.getScanPhotos().observe(this, scanPhotoList -> {
                if (scanPhotoList != null) {
                    if (!scanPhotoList.isEmpty()) {
                        Logger.d("Setting scanPhotos adapter");
                        // send out recipes, click listener and widget ID (if launched as config activity)
                        recyclerView.setAdapter(new ScanPhotoListAdapter(scanPhotoList, mListener));
                    } else {
                        recyclerView.setAdapter(new ScanPhotoDummyAdapter(DummyContent.ITEMS));
                    }
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnScanPhotoListListener) {
            mListener = (OnScanPhotoListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnScanDetailListListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnScanPhotoListListener {
        void onScanListInteraction(int scanPhotoID);
    }
}

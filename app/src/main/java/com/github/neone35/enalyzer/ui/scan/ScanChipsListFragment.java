package com.github.neone35.enalyzer.ui.scan;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.blankj.utilcode.util.ToastUtils;
import com.github.neone35.enalyzer.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ScanChipsListFragment extends Fragment {

    @BindView(R.id.rv_chips_list)
    RecyclerView rvChips;

    private static final String KEY_SAVED_CHIP_ITEMS = "chip_items";
    private static final String ARG_ECODES_LIST = "param1";
    private ArrayList<String> mEcodesList = new ArrayList<>();
    private RecyclerView.Adapter chipsAdapter;

    public ScanChipsListFragment() {
    }

    public static ScanChipsListFragment newInstance(ArrayList<String> ecodesList) {
        ScanChipsListFragment fragment = new ScanChipsListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_ECODES_LIST, ecodesList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEcodesList = getArguments().getStringArrayList(ARG_ECODES_LIST);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan_chips, container, false);
        ButterKnife.bind(this, rootView);
        Context context = rootView.getContext();

        if (mEcodesList != null) {
            Logger.d("Received ecodes: " + mEcodesList);

//            chipsAdapter = createChipsAdapter(savedInstanceState);
            chipsAdapter = new ScanChipsListAdapter(mEcodesList);

            ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .build();

            rvChips.setLayoutManager(chipsLayoutManager);
            rvChips.setAdapter(chipsAdapter);
            SpacingItemDecoration spacingItemDecoration = new SpacingItemDecoration(20, 20);
            rvChips.addItemDecoration(spacingItemDecoration);
        } else {
            ToastUtils.showShort("No eCodes received");
        }

        return rootView;
    }

    @SuppressWarnings("unchecked")
    private RecyclerView.Adapter createChipsAdapter(Bundle savedInstanceState) {

        List items;

        if (savedInstanceState == null) {
            items = mEcodesList;
        } else {
            items = savedInstanceState.getStringArrayList(KEY_SAVED_CHIP_ITEMS);
        }

        chipsAdapter = new ScanChipsListAdapter(items);
        if (items != null) {
            this.mEcodesList = new ArrayList<String>(items);
        }

        return chipsAdapter;

    }

    @Override
    @SuppressWarnings("unchecked")
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList items = new ArrayList<>(mEcodesList);
        outState.putParcelableArrayList(KEY_SAVED_CHIP_ITEMS, items);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

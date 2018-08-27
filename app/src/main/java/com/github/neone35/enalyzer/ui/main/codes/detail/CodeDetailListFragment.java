package com.github.neone35.enalyzer.ui.main.codes.detail;

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
 * Activities containing this fragment MUST implement the {@link OnCodeDetailListListener}
 * interface.
 */
public class CodeDetailListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_CATEGORY_ID = "category-id";
    private static final String ARG_CATEGORY_ECODES = "category-ecodes";
    private int mColumnCount = 2;
    private OnCodeDetailListListener mListener;
    private int mCodeCategoryID;
    private ArrayList<String> mCodeCategoryEcodes;
    private ProgressBar mCodeLoadingBar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CodeDetailListFragment() {
    }

    public static CodeDetailListFragment newInstance(int columnCount, int codeCategoryID, ArrayList<String> codeCategoryEcodes) {
        CodeDetailListFragment fragment = new CodeDetailListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_CATEGORY_ID, codeCategoryID);
        args.putStringArrayList(ARG_CATEGORY_ECODES, codeCategoryEcodes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mCodeCategoryID = getArguments().getInt(ARG_CATEGORY_ID);
            mCodeCategoryEcodes = getArguments().getStringArrayList(ARG_CATEGORY_ECODES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_detail_list, container, false);

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
            CodeAdditivesVMF factory =
                    InjectorUtils.provideCodeAdditivesVMF(Objects.requireNonNull(this.getContext()), mCodeCategoryID, mCodeCategoryEcodes);
            // Tie fragment & ViewModel together
            CodeAdditivesVM viewModel = ViewModelProviders.of(this, factory).get(CodeAdditivesVM.class);
            // Trigger LiveData notification on fragment creation & observe change in DB calling DAO
            viewModel.getAdditives().observe(this, additiveList -> {
                if (additiveList != null) {
                    Logger.d("Setting codeDetail adapter");
                    RecyclerView.Adapter adapter = recyclerView.getAdapter();
                    if (adapter == null) {
                        recyclerView.setAdapter(new CodeDetailListAdapter(mListener, additiveList, mCodeCategoryID));
                    } else {
                        recyclerView.swapAdapter(new CodeDetailListAdapter(mListener, additiveList, mCodeCategoryID), false);
                    }
                }
            });
            viewModel.getLoading().observe(this, isLoading -> {
                mCodeLoadingBar = view.getRootView().findViewById(R.id.pb_code_detail);
                if (isLoading != null)
                    if (isLoading) {
                        mCodeLoadingBar.setVisibility(View.VISIBLE);
                    } else {
                        mCodeLoadingBar.setVisibility(View.GONE);
                    }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCodeDetailListListener) {
            mListener = (OnCodeDetailListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCodeDetailListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mCodeLoadingBar.setVisibility(View.GONE);
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
    public interface OnCodeDetailListListener {
        void onCodeDetailListInteraction(HashMap<String, View> ivTransitionPhoto, String eCode, int codeCategoryID);
    }
}

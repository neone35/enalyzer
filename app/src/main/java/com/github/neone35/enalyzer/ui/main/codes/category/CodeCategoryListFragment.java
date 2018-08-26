package com.github.neone35.enalyzer.ui.main.codes.category;

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

import com.github.neone35.enalyzer.InjectorUtils;
import com.github.neone35.enalyzer.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnCodeCategoryListListener}
 * interface.
 */
public class CodeCategoryListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 2;
    private OnCodeCategoryListListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CodeCategoryListFragment() {
    }

    public static CodeCategoryListFragment newInstance(int columnCount) {
        CodeCategoryListFragment fragment = new CodeCategoryListFragment();
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_code_category_list, container, false);

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
            CodeCategoryVMF factory =
                    InjectorUtils.provideCodeCategoryVMF(Objects.requireNonNull(this.getContext()));
            // Tie fragment & ViewModel together
            CodeCategoryVM viewModel = ViewModelProviders.of(this, factory).get(CodeCategoryVM.class);
            // Trigger LiveData notification on fragment creation & observe change in DB calling DAO
            viewModel.getCodeCategories().observe(this, codeCategoryList -> {
                if (codeCategoryList != null) {
                    if (!codeCategoryList.isEmpty()) {
                        Logger.d("Setting codeCategory adapter");
                        // send out recipes, click listener and widget ID (if launched as config activity)
                        recyclerView.setAdapter(new CodeCategoryListAdapter(codeCategoryList, mListener));
                    }
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCodeCategoryListListener) {
            mListener = (OnCodeCategoryListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnCodeCategoryListListener");
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
    public interface OnCodeCategoryListListener {
        void onCodeCategoryListInteraction(int codeCategoryID, ArrayList<String> codeCategoryECodes);
    }
}

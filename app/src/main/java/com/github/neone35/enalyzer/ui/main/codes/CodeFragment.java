package com.github.neone35.enalyzer.ui.main.codes;


import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.ui.main.codes.category.CodeCategoryListFragment;

// Root Fragment for Code fragments
public class CodeFragment extends Fragment {

    public CodeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View codeRootView = inflater.inflate(R.layout.fragment_code, container, false);

        if (getFragmentManager() != null) {
            // replace root fragment with CodeCategoryList first
            // it'll be replaced by CodeDetailList on item click
            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fl_code_root, CodeCategoryListFragment.newInstance(2))
                        .commit();
            }
        }

        return codeRootView;
    }

}

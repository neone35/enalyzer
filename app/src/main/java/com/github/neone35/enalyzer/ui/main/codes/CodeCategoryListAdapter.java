package com.github.neone35.enalyzer.ui.main.codes;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.ui.main.codes.CodeCategoryListFragment.OnCodeCategoryListListener;
import com.github.neone35.enalyzer.dummy.DummyContent.DummyItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CodeCategoryListAdapter extends RecyclerView.Adapter<CodeCategoryListAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final CodeCategoryListFragment.OnCodeCategoryListListener mListener;

    CodeCategoryListAdapter(List<DummyItem> items, OnCodeCategoryListListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_code_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onCodeCategoryListInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        DummyItem mItem;
        @BindView(R.id.tv_code_category_range)
        TextView tvCodeCategoryRange;
        @BindView(R.id.tv_code_category)
        TextView tvCodeCategory;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(view, tvCodeCategoryRange);
            ButterKnife.bind(view, tvCodeCategory);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvCodeCategoryRange.getText() + "'";
        }
    }
}

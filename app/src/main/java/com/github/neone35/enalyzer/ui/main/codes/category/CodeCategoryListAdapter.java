package com.github.neone35.enalyzer.ui.main.codes.category;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.data.models.room.CodeCategory;
import com.github.neone35.enalyzer.ui.main.codes.category.CodeCategoryListFragment.OnCodeCategoryListListener;
import com.github.neone35.enalyzer.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CodeCategoryListAdapter extends RecyclerView.Adapter<CodeCategoryListAdapter.ViewHolder> {

    private List<CodeCategory> mCodeCategoryList;
    private final CodeCategoryListFragment.OnCodeCategoryListListener mListener;

    CodeCategoryListAdapter(List<CodeCategory> codeCategoryList, OnCodeCategoryListListener listener) {
        mCodeCategoryList = codeCategoryList;
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

        // bind code categories from viewmodel one by one
        CodeCategory codeCategory = holder.mCodeCategory = mCodeCategoryList.get(position);

        holder.tvCodeCategoryRange.setText(codeCategory.getRange());
        holder.tvCodeCategory.setText(codeCategory.getTitle());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                ArrayList<String> eCodesArrayList = new ArrayList<>(codeCategory.getECodes().size());
                eCodesArrayList.addAll(codeCategory.getECodes());
                mListener.onCodeCategoryListInteraction(
                        holder.mCodeCategory.getId(),
                        eCodesArrayList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCodeCategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        CodeCategory mCodeCategory;
        @BindView(R.id.tv_code_category_range)
        TextView tvCodeCategoryRange;
        @BindView(R.id.tv_code_category)
        TextView tvCodeCategory;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvCodeCategoryRange.getText() + "'";
        }
    }
}

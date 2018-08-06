package com.github.neone35.enalyzer.ui.main.codes;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.dummy.DummyContent;
import com.github.neone35.enalyzer.dummy.DummyContent.DummyItem;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CodeDetailListAdapter extends RecyclerView.Adapter<CodeDetailListAdapter.ViewHolder> {

    private final List<DummyContent.DummyItem> mValues;
    private final CodeDetailListFragment.OnCodeDetailListListener mListener;

    CodeDetailListAdapter(List<DummyContent.DummyItem> items, CodeDetailListFragment.OnCodeDetailListListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_code_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        // assign transitionName to every recyclerView item
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.ivPhoto.setTransitionName(holder.additiveImageTransitionName + position);
        }

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onCodeDetailListInteraction(holder.additiveImageTransitionName + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        public DummyItem mItem;
        @BindView(R.id.iv_code_detail_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_code_detail_title)
        TextView tvPhotoTitle;
        @BindString(R.string.additive_image_transition)
        String additiveImageTransitionName;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvPhotoTitle.getText() + "'";
        }
    }
}

package com.github.neone35.enalyzer.ui.main.scans;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.dummy.DummyContent.DummyItem;
import com.github.neone35.enalyzer.ui.main.MainActivity;

import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanDetailListAdapter extends RecyclerView.Adapter<ScanDetailListAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final ScanDetailListFragment.OnScanDetailListListener mListener;

    ScanDetailListAdapter(List<DummyItem> items, ScanDetailListFragment.OnScanDetailListListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scan_detail_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        // assign unique transition name to recyclerView item
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String photoTransitionName = holder.additiveImageTransitionName + position;
            String ecodeTransitionName = holder.additiveEcodeTransitionName + position;
            holder.ivPhoto.setTransitionName(photoTransitionName);
            holder.tvEcode.setTransitionName(ecodeTransitionName);
        }

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Pass transitionViews to AdditiveActivity through MainActivity callback
                HashMap<String, View> transitionViews = new HashMap<String, View>();
                transitionViews.put(MainActivity.KEY_PHOTO_TRANSITION_VIEW, holder.ivPhoto);
                transitionViews.put(MainActivity.KEY_ECODE_TRANSITION_VIEW, holder.tvEcode);
                mListener.onScanDetailListInteraction(transitionViews);
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
        @BindView(R.id.iv_scan_detail_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_scan_detail_ecode)
        TextView tvEcode;
        @BindView(R.id.tv_scan_detail_category)
        TextView tvCategory;
        @BindView(R.id.tv_scan_detail_names)
        TextView tvNames;
        @BindView(R.id.iv_scan_detail_hazard)
        ImageView ivHazardView;
        @BindString(R.string.additive_image_transition)
        String additiveImageTransitionName;
        @BindString(R.string.additive_ecode_transition)
        String additiveEcodeTransitionName;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvEcode.getText() + "'";
        }
    }
}

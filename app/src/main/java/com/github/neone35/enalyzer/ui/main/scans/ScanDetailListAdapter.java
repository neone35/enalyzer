package com.github.neone35.enalyzer.ui.main.scans;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.dummy.DummyContent.DummyItem;

import java.util.List;

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

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onScanDetailListInteraction(holder.mItem);
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
        ImageView ivPhotoView;
        @BindView(R.id.tv_scan_detail_ecode)
        TextView tvEcode;
        @BindView(R.id.tv_scan_detail_category)
        TextView tvCategory;
        @BindView(R.id.tv_scan_detail_names)
        TextView tvNames;
        @BindView(R.id.iv_scan_detail_hazard)
        ImageView ivHazardView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(view, ivPhotoView);
            ButterKnife.bind(view, tvEcode);
            ButterKnife.bind(view, tvCategory);
            ButterKnife.bind(view, tvNames);
            ButterKnife.bind(view, ivHazardView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvEcode.getText() + "'";
        }
    }
}

package com.github.neone35.enalyzer.ui.main.scans;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.ui.main.scans.ScanPhotoListFragment.OnScanPhotoListListener;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ScanPhotoListAdapter extends RecyclerView.Adapter<ScanPhotoListAdapter.ViewHolder> {

    private File[] mPhotoFileList;
    private Context mContext;
    private final OnScanPhotoListListener mListener;

    ScanPhotoListAdapter(File[] photoFileList, OnScanPhotoListListener listener) {
        mPhotoFileList = photoFileList;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.mSavedPhotoFile = mPhotoFileList[position];
        Uri photoFileUri = Uri.fromFile(holder.mSavedPhotoFile);
        Glide.with(holder.ivPhoto)
                .load(photoFileUri)
                .apply(fitCenterTransform())
                .into(holder.ivPhoto);


        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onScanListInteraction(photoFileUri.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotoFileList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        File mSavedPhotoFile;
        @BindView(R.id.iv_scan_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_scan_photo_date)
        TextView tvPhotoDate;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvPhotoDate.getText() + "'";
        }
    }
}

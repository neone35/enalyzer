package com.github.neone35.enalyzer.ui.main.scans.photos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;
import com.github.neone35.enalyzer.ui.main.scans.photos.ScanPhotoListFragment.OnScanPhotoListListener;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ScanPhotoListAdapter extends RecyclerView.Adapter<ScanPhotoListAdapter.ViewHolder> {

    private List<ScanPhoto> mScanPhotoList;
    private final OnScanPhotoListListener mListener;

    ScanPhotoListAdapter(List<ScanPhoto> scanPhotoList, OnScanPhotoListListener listener) {
        mScanPhotoList = scanPhotoList;
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

        // bind scan photos sorted by time taken from viewmodel one by one
        ScanPhoto scanPhoto = holder.mSavedScanPhoto = mScanPhotoList.get(position);
        Logger.d("Setting scanPhoto file: " + scanPhoto.getFilePath());
        Glide.with(holder.ivPhoto)
                .load(scanPhoto.getFilePath())
                .apply(fitCenterTransform())
                .into(holder.ivPhoto);

        // set date and time of photo on view holder item
        String timeStamp = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm",
                Locale.getDefault()).format(new Date(scanPhoto.getTimeMillis()));
        holder.tvPhotoDate.setText(timeStamp);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onScanListInteraction(scanPhoto.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScanPhotoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        ScanPhoto mSavedScanPhoto;
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

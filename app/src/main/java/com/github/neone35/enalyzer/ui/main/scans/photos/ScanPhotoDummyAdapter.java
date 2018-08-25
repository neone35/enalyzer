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
import com.github.neone35.enalyzer.dummy.DummyContent;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ScanPhotoDummyAdapter extends RecyclerView.Adapter<ScanPhotoDummyAdapter.ViewHolder> {

    private final List<DummyContent.DummyItem> mValues;

    ScanPhotoDummyAdapter(List<DummyContent.DummyItem> items) {
        mValues = items;
    }

    @NonNull
    @Override
    public ScanPhotoDummyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scan_item, parent, false);
        return new ScanPhotoDummyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScanPhotoDummyAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        DummyContent.DummyItem mItem;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
    }
}

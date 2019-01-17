package com.github.neone35.enalyzer.ui.main.scans.photos;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.dummy.DummyContent;

import java.util.List;

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

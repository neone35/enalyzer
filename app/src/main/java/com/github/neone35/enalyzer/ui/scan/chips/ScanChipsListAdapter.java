package com.github.neone35.enalyzer.ui.scan.chips;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.neone35.enalyzer.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanChipsListAdapter extends RecyclerView.Adapter<ScanChipsListAdapter.ViewHolder> {

    public static List<String> mECodeChipsList;

    ScanChipsListAdapter(List<String> eCodeChipsList) {
        mECodeChipsList = eCodeChipsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_scan_chips_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvChip.setText(mECodeChipsList.get(position));
        holder.ibChipClose.setOnClickListener(v ->
                ScanChipsListAdapter.this.removeItem(position));
    }

    @Override
    public int getItemCount() {
        return mECodeChipsList.size();
    }

    private void removeItem(int position) {
        mECodeChipsList.remove(position);
        // animate one item removal
        notifyItemRemoved(position);
        // update all items after removed item
        notifyItemRangeChanged(position, getItemCount());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        @BindView(R.id.tv_chip)
        TextView tvChip;
        @BindView(R.id.ib_chip_close)
        ImageButton ibChipClose;

        ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}

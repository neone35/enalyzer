package com.github.neone35.enalyzer.ui.main.scans.detail;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.data.models.room.ScanPhoto;
import com.github.neone35.enalyzer.ui.main.MainActivity;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ScanDetailListAdapter extends RecyclerView.Adapter<ScanDetailListAdapter.ViewHolder> {

    private final ScanPhoto mScanPhoto;
    private final ScanDetailListFragment.OnScanDetailListListener mListener;
    private List<Additive> mScanPhotoAdditives;
    private final int KNOWN_AS_NUM = 3;

    ScanDetailListAdapter(ScanPhoto scanPhoto, ScanDetailListFragment.OnScanDetailListListener listener,
                          List<Additive> scanPhotoAdditives) {
        mScanPhoto = scanPhoto;
        mListener = listener;
        mScanPhotoAdditives = scanPhotoAdditives;
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
        Additive additive = holder.mAdditive = mScanPhotoAdditives.get(position);

        // bind data to views
        if (additive.getImageURL() != null) {
            Glide.with(holder.ivPhoto)
                    .load(additive.getImageURL())
                    .apply(fitCenterTransform())
                    .into(holder.ivPhoto);
        }
        holder.tvEcode.setText(additive.getEcode());
        holder.tvCategory.setText(additive.getCategory());
        holder.tvScanDetailName.setText(additive.getName());
        if (additive.getKnownAs() != null) {
            ArrayList<String> knownAses = new ArrayList<>();
            for (int i = 0; i < KNOWN_AS_NUM - 1; i++) {
                knownAses.add(additive.getKnownAs().get(i));
            }
            String knownAsJoined = Joiner.on(", ").join(knownAses);
            holder.tvKnownAs.setText(knownAsJoined);
        }

        // assign unique transition name to recyclerView item
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String photoTransitionName = holder.additiveImageTransitionName + position;
            String ecodeTransitionName = holder.additiveEcodeTransitionName + position;
            holder.ivPhoto.setTransitionName(photoTransitionName);
            holder.tvEcode.setTransitionName(ecodeTransitionName);
        }

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                HashMap<String, View> transitionViews = new HashMap<>();
                transitionViews.put(MainActivity.KEY_PHOTO_TRANSITION_VIEW, holder.ivPhoto);
                transitionViews.put(MainActivity.KEY_ECODE_TRANSITION_VIEW, holder.tvEcode);
                // Pass transitionViews to AdditiveActivity through MainActivity callback
                mListener.onScanDetailListInteraction(transitionViews);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScanPhoto.getECodes().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        Additive mAdditive;
        @BindView(R.id.iv_scan_detail_photo)
        ImageView ivPhoto;
        @BindView(R.id.tv_scan_detail_ecode)
        TextView tvEcode;
        @BindView(R.id.tv_scan_detail_category)
        TextView tvCategory;
        @BindView(R.id.tv_scan_detail_known_as)
        TextView tvKnownAs;
        @BindView(R.id.tv_scan_detail_name)
        TextView tvScanDetailName;
        @BindView(R.id.tv_scan_detail_formula)
        TextView tvScanDetailFormula;
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

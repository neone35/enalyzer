package com.github.neone35.enalyzer.ui.main.scans.detail;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.neone35.enalyzer.HelpUtils;
import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.data.models.room.Additive;
import com.github.neone35.enalyzer.ui.main.MainActivity;
import com.google.common.base.Joiner;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class ScanDetailListAdapter extends RecyclerView.Adapter<ScanDetailListAdapter.ViewHolder> {

    private final ScanDetailListFragment.OnScanDetailListListener mListener;
    private List<Additive> mScanPhotoAdditives;
    private int mScanPhotoID;

    ScanDetailListAdapter(ScanDetailListFragment.OnScanDetailListListener listener,
                          List<Additive> scanPhotoAdditives,
                          int scanPhotoID) {
        mListener = listener;
        mScanPhotoAdditives = scanPhotoAdditives;
        mScanPhotoID = scanPhotoID;
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
            String imageUrl = additive.getImageURL();
            if (HelpUtils.imageTypeSupported(imageUrl)) {
                try {
                    Glide.with(holder.ivPhoto)
                            .load(additive.getImageURL())
                            .apply(fitCenterTransform())
                            .into(holder.ivPhoto);
                } catch (Exception e) {
                    Logger.d(e.getMessage());
                }
            }
        }
        holder.tvEcode.setText(additive.getEcode());
        holder.tvCategory.setText(additive.getCategory());
        holder.tvScanDetailName.setText(additive.getName());
        holder.tvScanDetailFormula.setText(additive.getFormula());
        if (additive.getKnownAs() != null) {
            ArrayList<String> knownAses = new ArrayList<>();
            for (int i = 0; i < ScanDetailListFragment.KNOWN_AS_NUM - 1; i++) {
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
                mListener.onScanDetailListInteraction(transitionViews, additive.getEcode(), mScanPhotoID);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScanPhotoAdditives.size();
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

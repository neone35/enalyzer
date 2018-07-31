package com.github.neone35.enalyzer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.neone35.enalyzer.PhotoFragment.OnPhotoListFragmentInteractionListener;
import com.github.neone35.enalyzer.dummy.DummyContent.DummyItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnPhotoListFragmentInteractionListener mListener;

    public PhotoRecyclerViewAdapter(List<DummyItem> items, PhotoFragment.OnPhotoListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onPhotoFragmentInteraction(holder.mItem);
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
        @BindView(R.id.iv_photo)
        ImageView mPhotoView;
        @BindView(R.id.tv_photo_date)
        TextView mPhotoDate;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(view, mPhotoView);
            ButterKnife.bind(view, mPhotoDate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPhotoDate.getText() + "'";
        }
    }
}

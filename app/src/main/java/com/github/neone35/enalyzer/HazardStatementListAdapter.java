package com.github.neone35.enalyzer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.neone35.enalyzer.dummy.DummyContent.DummyItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HazardStatementListAdapter extends RecyclerView.Adapter<HazardStatementListAdapter.ViewHolder> {

    private final List<DummyItem> mValues;

    public HazardStatementListAdapter(List<DummyItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hazard_statement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.tv_hazard_statement_code)
        TextView tvHazardStatementCode;
        @BindView(R.id.tv_hazard_statement)
        TextView tvHazardStatement;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(view, tvHazardStatementCode);
            ButterKnife.bind(view, tvHazardStatement);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvHazardStatementCode.getText() + "'";
        }
    }
}

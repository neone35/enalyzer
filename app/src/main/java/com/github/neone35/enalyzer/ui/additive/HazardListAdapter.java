package com.github.neone35.enalyzer.ui.additive;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.neone35.enalyzer.R;
import com.github.neone35.enalyzer.data.models.room.Hazard;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HazardListAdapter extends RecyclerView.Adapter<HazardListAdapter.ViewHolder> {

    private final List<Hazard> mHazards;

    HazardListAdapter(List<Hazard> hazards) {
        mHazards = hazards;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hazard_statement_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // sort hazard objects by their int code
//        Collections.sort(mHazards, (o1, o2) ->
//                Integer.valueOf(HelpUtils.stripNonDigits(o1.getStatementCode()))
//                        - Integer.valueOf(HelpUtils.stripNonDigits(o2.getStatementCode()))
//        );

        Hazard hazard = holder.mHazard = mHazards.get(position);

        holder.tvHazardStatementCode.setText(hazard.getStatementCode());
        holder.tvHazardStatement.setText(hazard.getStatement());
    }

    @Override
    public int getItemCount() {
        return mHazards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        @BindView(R.id.tv_hazard_statement_code)
        TextView tvHazardStatementCode;
        @BindView(R.id.tv_hazard_statement)
        TextView tvHazardStatement;
        Hazard mHazard;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvHazardStatementCode.getText() + "'";
        }
    }
}

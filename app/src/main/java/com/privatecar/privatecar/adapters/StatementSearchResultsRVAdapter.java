package com.privatecar.privatecar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.StatementsGroup;
import com.privatecar.privatecar.utils.DateUtil;
import com.privatecar.privatecar.utils.Utils;

import java.util.List;

/**
 * Created by basim on 17/2/16.
 */


public class StatementSearchResultsRVAdapter extends RecyclerView.Adapter<StatementSearchResultsRVAdapter.ResultViewHolder> {
    private List<StatementsGroup> results;


    public StatementSearchResultsRVAdapter(List<StatementsGroup> results) {
        this.results = results;
    }

    @Override
    public StatementSearchResultsRVAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_statement_search_result_item, parent, false);
        return new ResultViewHolder(item);
    }

    @Override
    public void onBindViewHolder(StatementSearchResultsRVAdapter.ResultViewHolder holder, int position) {
        StatementsGroup group = results.get(position);

        // set date
        String date = DateUtil.getDayName(group.getDate());
        date += "\n" + DateUtil.convertToString(group.getDate(), "d-M-yyyy");
        holder.tvDate.setText(date);

        // set other data
        holder.tvTrips.setText(String.valueOf(group.getTripCount()));
        holder.tvProfit.setText(Utils.formatDouble(group.getProfit()));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public static class ResultViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvTrips;
        public TextView tvProfit;

        public ResultViewHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvTrips = (TextView) itemView.findViewById(R.id.tv_trips);
            tvProfit = (TextView) itemView.findViewById(R.id.tv_profit);
        }
    }

}
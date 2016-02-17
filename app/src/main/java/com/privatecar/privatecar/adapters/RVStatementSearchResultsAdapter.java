package com.privatecar.privatecar.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.StatementSearchResult;

import java.util.ArrayList;

/**
 * Created by basim on 17/2/16.
 */


public class RVStatementSearchResultsAdapter extends RecyclerView.Adapter<RVStatementSearchResultsAdapter.ResultViewHolder> {

    private ArrayList<StatementSearchResult> results;


    public RVStatementSearchResultsAdapter(ArrayList<StatementSearchResult> results) {
        this.results = results;
    }

    @Override
    public RVStatementSearchResultsAdapter.ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_statement_search_result_item, parent, false);
        return new ResultViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RVStatementSearchResultsAdapter.ResultViewHolder holder, int position) {
        StatementSearchResult result = results.get(position);

        holder.tvDate.setText(String.format("%s\n%s", "Monday", "20-10-2015"));
        holder.tvTrips.setText(String.valueOf(result.getTripCount()));
        holder.tvProfit.setText(String.valueOf(result.getProfit()));
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
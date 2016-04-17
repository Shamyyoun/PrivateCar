package com.privateegy.privatecar.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.adapters.StatementSearchResultsRVAdapter;
import com.privateegy.privatecar.controllers.StatementsGroupsController;
import com.privateegy.privatecar.models.entities.StatementsGroup;
import com.privateegy.privatecar.models.wrappers.SerializableListWrapper;

import java.util.List;

public class DriverStatementSearchResultActivity extends BasicBackActivity {
    private List<StatementsGroup> groups;
    private StatementsGroupsController groupsController;
    private BarChart chart;
    private RecyclerView rvResults;
    private StatementSearchResultsRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_statement_search_result);

        // get groups list and create its controller
        SerializableListWrapper<StatementsGroup> groupsWrapper = (SerializableListWrapper<StatementsGroup>) getIntent().getSerializableExtra(Const.KEY_STATEMENT_GROUPS);
        groups = groupsWrapper.getList();
        groupsController = new StatementsGroupsController(this, groups);

        // customize the chart view
        chart = (BarChart) findViewById(R.id.chart);
        BarData data = new BarData(groupsController.getDates("d/M"), groupsController.getChartDataSets());
        chart.setData(data);
        chart.animateXY(1000, 1000);
        Legend legend = chart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.invalidate();

        // customize the recycler view
        rvResults = (RecyclerView) findViewById(R.id.rv_results);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResults.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvResults.addItemDecoration(itemDecoration);
        rvResults.setHasFixedSize(true);
        adapter = new StatementSearchResultsRVAdapter(groups);
        rvResults.setAdapter(adapter);
    }
}

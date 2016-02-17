package com.privatecar.privatecar.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.RVStatementSearchResultsAdapter;
import com.privatecar.privatecar.models.entities.StatementSearchResult;

import java.util.ArrayList;
import java.util.Date;

public class DriverStatementSearchResultActivity extends BasicBackActivity {

    ArrayList<StatementSearchResult> searchResults = new ArrayList<>();
    RecyclerView rvResults;
    RVStatementSearchResultsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_statement_search_result);

        //http://www.truiton.com/2015/04/android-chart-example-mp-android-chart-library/
        //http://code.tutsplus.com/tutorials/add-charts-to-your-android-app-using-mpandroidchart--cms-23335
        //https://github.com/PhilJay/MPAndroidChart/wiki

        BarChart chart = (BarChart) findViewById(R.id.chart);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.animateXY(1000, 1000);
        Legend legend = chart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.invalidate();

        fillDummyResults();

        rvResults = (RecyclerView) findViewById(R.id.rv_results);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResults.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvResults.addItemDecoration(itemDecoration);
        rvResults.setHasFixedSize(true);
        adapter = new RVStatementSearchResultsAdapter(searchResults);
        rvResults.setAdapter(adapter);

    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        valueSet1.add(new BarEntry(110.000f, 0));
        valueSet1.add(new BarEntry(40.000f, 1));
        valueSet1.add(new BarEntry(60.000f, 2));
        valueSet1.add(new BarEntry(30.000f, 3));
        valueSet1.add(new BarEntry(90.000f, 4));
        valueSet1.add(new BarEntry(100.000f, 5));
        valueSet1.add(new BarEntry(110.000f, 6));
        valueSet1.add(new BarEntry(40.000f, 7));
        valueSet1.add(new BarEntry(60.000f, 8));
        valueSet1.add(new BarEntry(30.000f, 9));
        valueSet1.add(new BarEntry(90.000f, 10));
        valueSet1.add(new BarEntry(100.000f, 11));


        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        valueSet2.add(new BarEntry(150.000f, 0));
        valueSet2.add(new BarEntry(90.000f, 1));
        valueSet2.add(new BarEntry(120.000f, 2));
        valueSet2.add(new BarEntry(60.000f, 3));
        valueSet2.add(new BarEntry(20.000f, 4));
        valueSet2.add(new BarEntry(80.000f, 5));
        valueSet2.add(new BarEntry(150.000f, 6));
        valueSet2.add(new BarEntry(90.000f, 7));
        valueSet2.add(new BarEntry(120.000f, 8));
        valueSet2.add(new BarEntry(60.000f, 9));
        valueSet2.add(new BarEntry(20.000f, 10));
        valueSet2.add(new BarEntry(80.000f, 11));


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, getString(R.string.trips));
        barDataSet1.setColor(Color.BLUE);

        BarDataSet barDataSet2 = new BarDataSet(valueSet2, getString(R.string.profit));
        barDataSet2.setColor(Color.RED);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("2/1");
        xAxis.add("3/1");
        xAxis.add("4/1");
        xAxis.add("5/1");
        xAxis.add("6/1");
        xAxis.add("7/1");
        xAxis.add("2/2");
        xAxis.add("3/2");
        xAxis.add("4/2");
        xAxis.add("5/2");
        xAxis.add("6/2");
        xAxis.add("7/2");

        return xAxis;
    }

    private void fillDummyResults() {
        for (int i = 0; i < 40; i++) {
            StatementSearchResult statementSearchResult = new StatementSearchResult();
            statementSearchResult.setDate(new Date());
            statementSearchResult.setTripCount(i * 5);
            statementSearchResult.setProfit(i * 5 + 2);
            searchResults.add(statementSearchResult);
        }
    }


}

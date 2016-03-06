package com.privatecar.privatecar.controllers;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.StatementsGroup;
import com.privatecar.privatecar.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shamyyoun on 3/5/2016.
 */
public class StatementsGroupsController {
    private Context context;
    private List<StatementsGroup> groups;

    public StatementsGroupsController(Context context, List<StatementsGroup> groups) {
        this.context = context;
        this.groups = groups;
    }

    public List<String> getDates(String dateFormat) {
        List<String> dates = new ArrayList<>(groups.size());
        for (StatementsGroup group : groups) {
            String date = DateUtil.convertToString(group.getDate(), dateFormat);
            dates.add(date);
        }

        return dates;
    }

    public List<IBarDataSet> getChartDataSets() {
        // create chart view lists
        List<IBarDataSet> dataSets;
        List<BarEntry> tripsCountEntries = new ArrayList<>();
        List<BarEntry> profitsEntries = new ArrayList<>();

        // loop to create the entries
        for (int i = 0; i < groups.size(); i++) {
            StatementsGroup group = groups.get(i);
            BarEntry tripsCountEntry = new BarEntry(group.getTripCount(), i);
            BarEntry profitsEntry = new BarEntry(group.getProfit(), i);
            tripsCountEntries.add(tripsCountEntry);
            profitsEntries.add(profitsEntry);
        }

        // create bar data set lists
        BarDataSet barDataSet1 = new BarDataSet(tripsCountEntries, context.getString(R.string.trips));
        barDataSet1.setColor(Color.BLUE);
        BarDataSet barDataSet2 = new BarDataSet(profitsEntries, context.getString(R.string.profit));
        barDataSet2.setColor(Color.RED);

        // add to the main list and return with it
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }
}

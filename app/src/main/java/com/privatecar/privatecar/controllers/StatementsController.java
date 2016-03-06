package com.privatecar.privatecar.controllers;

import com.privatecar.privatecar.models.entities.Statement;
import com.privatecar.privatecar.models.entities.StatementsGroup;
import com.privatecar.privatecar.utils.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Shamyyoun on 3/5/2016.
 */
public class StatementsController {
    private List<Statement> statements;

    public StatementsController(List<Statement> statements) {
        this.statements = statements;
    }

    public List<StatementsGroup> getAsGroups() {
        // create new groups list
        List<StatementsGroup> groups = new ArrayList<StatementsGroup>();

        // loop for statements list to get matched statements as groups
        for (int i = 0; i < statements.size(); i++) {
            // get next statement and check it
            Statement statement1 = statements.get(i);
            if (statement1 == null) {
                continue;
            }

            // create new group with initial data of this statement
            StatementsGroup group = new StatementsGroup();
            group.setDate(DateUtil.convertToCalendar(statement1.getCreatedAt(), "yyyy-MM-dd hh:mm:ss"));
            group.setTripCount(1);
            group.setProfit(statement1.getProfit());

            // loop to get other statements of this day
            for (int j = (i + 1); j < statements.size(); j++) {
                // get next statement and check it
                Statement statement2 = statements.get(j);
                if (statement2 == null) {
                    continue;
                }

                // get the two dates
                Calendar calendar1 = DateUtil.convertToCalendar(statement1.getCreatedAt(), "yyyy-MM-dd hh:mm:ss");
                Calendar calendar2 = DateUtil.convertToCalendar(statement2.getCreatedAt(), "yyyy-MM-dd hh:mm:ss");

                // check dates
                if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)
                        && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
                        && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {

                    // update group data
                    int tripCount = group.getTripCount() + 1;
                    float profit = group.getProfit() + statement2.getProfit();
                    group.setTripCount(tripCount);
                    group.setProfit(profit);

                    // null this statement
                    statements.set(j, null);
                }
            }

            // add this group to groups list
            groups.add(group);
        }

        // sort the groups list with custom comparator
        Comparator<StatementsGroup> comparator = new Comparator<StatementsGroup>() {
            @Override
            public int compare(StatementsGroup lhs, StatementsGroup rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        };
        Collections.sort(groups, comparator);

        return groups;
    }
}

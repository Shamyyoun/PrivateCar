package com.privatecar.privatecar.controllers;

import com.privatecar.privatecar.models.responses.CustomerTrip;

import java.util.List;

/**
 * Created by Shamyyoun on 3/20/2016.
 */
public class CustomerTripsController {
    private List<CustomerTrip> trips;

    public CustomerTripsController(List<CustomerTrip> trips) {
        this.trips = trips;
    }

    public int getTotalMoney() {
        int money = 0;
        for (CustomerTrip trip : trips) {
            money += trip.getFare();
        }

        return money;
    }
}

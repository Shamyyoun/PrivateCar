package com.privatecar.privatecar.fragments;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.models.entities.Ride;
import com.privatecar.privatecar.adapters.RidesRVAdapter;

import java.util.ArrayList;
import java.util.Random;

public class CustomerMyRidesFragment extends BaseFragment {


    ArrayList<Ride> rides = new ArrayList<>();
    RecyclerView rvRides;
    RidesRVAdapter adapter;

    public CustomerMyRidesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_customer_my_rides, container, false);

        fillDumpRides();

        rvRides = (RecyclerView) fragment.findViewById(R.id.rv_rides);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvRides.setLayoutManager(linearLayoutManager);
        rvRides.setHasFixedSize(true);
        adapter = new RidesRVAdapter(rides);
        rvRides.setAdapter(adapter);

        return fragment;
    }

    private void fillDumpRides() {
        for (int i = 0; i < 30; i++) {
            Ride ride = new Ride();

            ride.setRideNumber(new Random().nextInt(3003));
            ride.setReceiptNumber(new Random().nextInt(3003));
            ride.setPickupAddress("Pickup Address" + i);
            ride.setDropOffAddress("DropOff Address" + i);
            ride.setPrice(new Random().nextInt(100));
            ride.setDate("30 Jan 2016");

            rides.add(ride);
        }
    }

}

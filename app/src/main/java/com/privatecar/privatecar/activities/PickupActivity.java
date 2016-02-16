package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.PlacesAdapter;
import com.privatecar.privatecar.models.entities.Place;
import com.privatecar.privatecar.utils.ButtonHighlighterOnTouchListener;
import com.privatecar.privatecar.views.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class PickupActivity extends BasicBackActivity implements View.OnClickListener {
    ImageButton buttonSearch;
    RecyclerView recyclerView;
    PlacesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);


        // customize search button
        buttonSearch = (ImageButton) findViewById(R.id.btn_search);
        buttonSearch.setOnTouchListener(new ButtonHighlighterOnTouchListener(this, R.drawable.search_icon));
        buttonSearch.setOnClickListener(this);

        // customize recycler view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // ===== DUMMY DATA =====
        List<Place> places = new ArrayList<>();
        Place myLocationPlace = new Place();
        myLocationPlace.setTitle("My Location");
        myLocationPlace.setAddress("Abbas Elakkad, Nasr City");
        myLocationPlace.setMyLocation(true);
        places.add(myLocationPlace);

        for (int i = 0; i < 15; i++) {
            Place place = new Place();
            place.setTitle("Title " + i);
            place.setAddress("Address " + i);
            place.setTime(5);
            places.add(place);
        }

        adapter = new PlacesAdapter(this, places, R.layout.item_places);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new PlacesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // open verify trip activity
                startActivity(new Intent(PickupActivity.this, VerifyTripActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                // TODO
                break;
        }
    }
}

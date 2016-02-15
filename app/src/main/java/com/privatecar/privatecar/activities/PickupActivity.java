package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
    Toolbar toolbar;
    ImageButton buttonSearch;
    RecyclerView recyclerView;
    PlacesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        // customize toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.start_white_arrow_icon);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.toolbar_pvt_icon);

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
        List<Place> places = new ArrayList();
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

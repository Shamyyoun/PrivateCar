package com.privateegy.privatecar.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.privateegy.privatecar.R;

/**
 * Created by basim on 23/1/16.
 * A Base activity class that enables the actionbar home button.
 */
public class BasicBackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setIcon(R.drawable.toolbar_pvt_icon);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

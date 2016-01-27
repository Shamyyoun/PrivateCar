package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.view.MenuItem;

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
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

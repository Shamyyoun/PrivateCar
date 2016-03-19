package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.privatecar.privatecar.Const;
import com.privatecar.privatecar.R;
import com.privatecar.privatecar.fragments.DriverAccountFragment;
import com.privatecar.privatecar.fragments.DriverHomeFragment;
import com.privatecar.privatecar.fragments.DriverMessageCenterFragment;
import com.privatecar.privatecar.fragments.DriverRatingsFragment;
import com.privatecar.privatecar.fragments.DriverStatementFragment;
import com.privatecar.privatecar.models.entities.Config;
import com.privatecar.privatecar.models.entities.DriverAccountDetails;
import com.privatecar.privatecar.utils.AppUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

public class DriverHomeActivity extends BaseActivity {

    DrawerLayout dlDrawer;
    NavigationView nvDrawer;
    View nvHeader;
    ImageView ivUserImage;
    TextView tvUserName, tvUserID, tvUserCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setIcon(R.drawable.home_logo);
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();

        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nv_drawer);
        nvDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                //close the drawer after the fragment loads (prevent sluggish behavior)
                nvDrawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        closeDrawer();
                    }
                }, 150);

                if (item.isChecked()) {
                    return false;
                }

                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragmentManager.beginTransaction().replace(R.id.layout_fragment_container, new DriverHomeFragment()).commit();
                        break;
                    case R.id.nav_statement:
                        fragmentManager.beginTransaction().replace(R.id.layout_fragment_container, new DriverStatementFragment()).commit();
                        break;
                    case R.id.nav_ratings:
                        fragmentManager.beginTransaction().replace(R.id.layout_fragment_container, new DriverRatingsFragment()).commit();
                        break;
                    case R.id.nav_account:
                        fragmentManager.beginTransaction().replace(R.id.layout_fragment_container, new DriverAccountFragment()).commit();
                        break;
                    case R.id.nav_message_center:
                        fragmentManager.beginTransaction().replace(R.id.layout_fragment_container, new DriverMessageCenterFragment()).commit();
                        break;
                }

                if (actionBar != null) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            actionBar.setTitle("");
                            actionBar.setIcon(R.drawable.home_logo);
                            break;
                        default:
                            actionBar.setTitle(item.getTitle());
                            actionBar.setIcon(R.drawable.toolbar_pvt_icon);
                    }
                }

                return false;
            }
        });

        nvHeader = nvDrawer.getHeaderView(0);
        ivUserImage = (ImageView) nvHeader.findViewById(R.id.iv_user_image);
        tvUserName = (TextView) nvHeader.findViewById(R.id.tv_user_name);
        tvUserID = (TextView) nvHeader.findViewById(R.id.tv_user_id);
        tvUserCredit = (TextView) nvDrawer.findViewById(R.id.tv_credit);

        if (actionBar != null) {
            actionBar.setTitle("");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_fragment_container, new DriverHomeFragment()).commit();

    }

    @Override
    public void onBackPressed() {
        if (dlDrawer.isDrawerOpen(GravityCompat.END)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void closeDrawer() {
        dlDrawer.closeDrawer(GravityCompat.END);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_toggle:
                dlDrawer.openDrawer(GravityCompat.END);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * method, used to update data in the navigation drawer
     */
    public void updatePersonalInfo(DriverAccountDetails accountDetails) {
        tvUserName.setText(accountDetails.getFullname());
        tvUserID.setText("" + accountDetails.getId());
        tvUserCredit.setText(accountDetails.getCredit() + " " + getString(R.string.currency));

        // load personal image
        String imageUrl = AppUtils.getConfigValue(getApplicationContext(), Config.KEY_BASE_URL) + File.separator + accountDetails.getPersonalPhoto();
        Picasso.with(this).load(imageUrl).error(R.drawable.def_user_photo).placeholder(R.drawable.def_user_photo).into(ivUserImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_COARSE_LOCATION_PERMISSION && resultCode == RESULT_OK) {//this request is sent in DriverHomeFragment
            Log.e(Const.LOG_TAG, "resultCode: " + resultCode);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_fragment_container);
            if (fragment instanceof DriverHomeFragment) {
                DriverHomeFragment homeFragment = (DriverHomeFragment) fragment;
                homeFragment.onConnected(null);
            }
        } else if (requestCode == Const.REQUEST_FINE_LOCATION_PERMISSION && resultCode == RESULT_OK) {//this request is sent in DriverHomeFragment
            Log.e(Const.LOG_TAG, "resultCode: " + resultCode);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.layout_fragment_container);
            if (fragment instanceof DriverHomeFragment) {
                DriverHomeFragment homeFragment = (DriverHomeFragment) fragment;
                homeFragment.beActive(true);
            }
        }
    }


}

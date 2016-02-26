package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.fragments.DriverAccountFragment;
import com.privatecar.privatecar.fragments.DriverHomeFragment;
import com.privatecar.privatecar.fragments.DriverMessageCenterFragment;
import com.privatecar.privatecar.fragments.DriverRatingsFragment;
import com.privatecar.privatecar.fragments.DriverStatementFragment;

public class DriverHomeActivity extends BaseActivity {

    DrawerLayout dlDrawer;
    NavigationView nvDrawer;
    View nvHeader;
    ImageView ivUserImage;
    TextView tvUserName, tvUserID, tvUserBalance;

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
        tvUserBalance = (TextView) nvHeader.findViewById(R.id.tv_user_balance);

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

}

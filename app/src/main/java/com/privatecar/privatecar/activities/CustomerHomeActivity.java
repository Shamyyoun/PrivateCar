package com.privatecar.privatecar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.privatecar.privatecar.fragments.BookALiftFragment;
import com.privatecar.privatecar.fragments.CustomerMyRidesFragment;
import com.privatecar.privatecar.fragments.CustomerPricesFragment;
import com.privatecar.privatecar.fragments.CustomerSettingsFragment;

public class CustomerHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout dlDrawer;
    NavigationView nvDrawer;
    View nvHeader;
    ImageView ivUserImage;
    TextView tvUserName, tvUserID, tvUserBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false); //hide the title
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setLogo(R.drawable.home_logo);
        }

        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nv_drawer);
        nvDrawer.setNavigationItemSelectedListener(this);

        nvHeader = nvDrawer.getHeaderView(0);
        ivUserImage = (ImageView) nvHeader.findViewById(R.id.iv_user_image);
        tvUserName = (TextView) nvHeader.findViewById(R.id.tv_user_name);
        tvUserID = (TextView) nvHeader.findViewById(R.id.tv_user_id);
        tvUserBalance = (TextView) nvHeader.findViewById(R.id.tv_user_balance);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_fragment_container, new BookALiftFragment(), BookALiftFragment.TAG)
                .commit();

    }

    /**
     * overridden method, used to handle click of an item in the navigation menu
     *
     * @param item
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // begin fragment transaction
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // get the suitable fragment
        Fragment fragment = null;
        String tag = null;
        switch (item.getItemId()) {
            case R.id.nav_book_lift:
                tag = BookALiftFragment.TAG;
                fragment = fm.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new BookALiftFragment();
                break;

            case R.id.nav_my_rides:
                tag = CustomerMyRidesFragment.TAG;
                fragment = fm.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new CustomerMyRidesFragment();
                break;

            case R.id.nav_prices:
                tag = CustomerPricesFragment.TAG;
                fragment = fm.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new CustomerPricesFragment();
                break;

            case R.id.nav_settings:
                tag = CustomerSettingsFragment.TAG;
                fragment = fm.findFragmentByTag(tag);
                if (fragment == null)
                    fragment = new CustomerSettingsFragment();
                break;

            case R.id.nav_promo_code:
                // open add promo code activity
                startActivity(new Intent(this, CustomerAddPromoCodeActivity.class));
                break;

            case R.id.nav_tell_friend:
                // open invite friends activity
                startActivity(new Intent(this, CustomerInviteFriendsActivity.class));
                break;

            case R.id.nav_about:
                // open about private activity
                startActivity(new Intent(this, CustomerAboutPrivateActivity.class));
                break;
        }

        // check fragment
        if (fragment != null) {

            // check to add fragment or replace it
            if (fragment.isAdded()) {
                ft.show(fragment);
            } else {
                ft.replace(R.id.layout_fragment_container, fragment, tag);
            }

            // add to back stackk & commit transaction
            ft.addToBackStack(tag);
            ft.commitAllowingStateLoss();

            // check selected item
            item.setChecked(true);
        }

        // close navigation drawer after static time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dlDrawer.closeDrawer(GravityCompat.END);
            }
        }, 150);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (dlDrawer.isDrawerOpen(GravityCompat.END)) {
            dlDrawer.closeDrawer(GravityCompat.END);
        } else {
            finish();
        }
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

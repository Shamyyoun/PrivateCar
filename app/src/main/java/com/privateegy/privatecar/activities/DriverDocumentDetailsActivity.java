package com.privateegy.privatecar.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.privateegy.privatecar.Const;
import com.privateegy.privatecar.R;
import com.privateegy.privatecar.fragments.ImageSlideFragment;
import com.privateegy.privatecar.models.wrappers.SerializableListWrapper;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

public class DriverDocumentDetailsActivity extends BaseActivity {

    private ViewPager viewPager;
    private CirclePageIndicator indicator;
    private ImageSlideViewPagerAdapter adapter;
    private int position;
    List<String> documents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_document_details);

        position = getIntent().getIntExtra("position", 0);
        documents = ((SerializableListWrapper<String>) getIntent().getSerializableExtra("documents")).getList();
        if (documents == null) {
            Log.e(Const.LOG_TAG, "documents = null");
            onBackPressed();
            return;
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setSnap(true);

        viewPager.setCurrentItem(position);
    }

    private void setupViewPager(final ViewPager viewPager) {
        adapter = new ImageSlideViewPagerAdapter(getSupportFragmentManager(), documents);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

    }

    class ImageSlideViewPagerAdapter extends FragmentStatePagerAdapter {
        private List<String> urls;

        public ImageSlideViewPagerAdapter(FragmentManager manager, List<String> urls) {
            super(manager);
            this.urls = urls;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new ImageSlideFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", urls.get(position));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return urls.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}

package com.github.neone35.enalyzer;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.neone35.enalyzer.dummy.DummyContent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        ScanListFragment.OnScanListFragmentInteractionListener,
        ScanDetailListFragment.OnScanDetailListFragmentInteractionListener {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout mTabLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindString(R.string.app_name)
    String mAppName;

    private FirebaseAnalytics mFirebaseAnalytics;
    public static String TAB_POSITION = "tab_position";
    public static String PAGER_INSTRUCT_MOTION = "pager_motion";
    private SharedPreferences mSettings;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.addLogAdapter(new AndroidLogAdapter());
        ButterKnife.bind(this);

        mSettings = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        mFragmentManager = getSupportFragmentManager();

        setSupportActionBar(mToolbar);
        setupFirebaseAnalytics();
        setupTabs(mFragmentManager);

        // show instructive motion (once per lifetime)
        if (!mSettings.getBoolean(PAGER_INSTRUCT_MOTION, false))
            showInstructiveMotion(mViewPager);
    }

    private void showInstructiveMotion(ViewPager pager) {
        final Handler handler = new Handler();
        handler.postDelayed(() ->
                        ObjectAnimator.ofInt(pager, "scrollX", pager.getScrollX() + 100).setDuration(1000).start(),
                2000);
        handler.postDelayed(() ->
                        ObjectAnimator.ofInt(pager, "scrollX", pager.getScrollX() - 100).setDuration(1000).start(),
                3000);
        // save key for lifetime (show motion once)
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(PAGER_INSTRUCT_MOTION, true);
        editor.apply();
    }

    private void setupFirebaseAnalytics() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.START_DATE, Calendar.getInstance().getTime().toString());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
    }

    private void setupTabs(FragmentManager fragmentManager) {
        // Set ViewPager's PagerAdapter so that it can display items
        mViewPager.setAdapter(new MainFragmentPagerAdapter(fragmentManager, MainActivity.this));
        // Give the TabLayout the ViewPager
        mTabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAB_POSITION, mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mViewPager.setCurrentItem(savedInstanceState.getInt(TAB_POSITION));
        }
    }

    @Override
    public void onScanListFragmentInteraction(DummyContent.DummyItem item) {
        Logger.d(item);
        int currentPage = mViewPager.getCurrentItem();
        if (currentPage == 0) {
            // replace ScanList with ScanDetailList
            mFragmentManager.beginTransaction()
                    .replace(R.id.fl_scan_root, ScanDetailListFragment.newInstance(1))
                    // enable switch back to ScanList with addToBackStack
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onScanDetailListFragmentInteraction(DummyContent.DummyItem item) {
        // TODO: Open AdditiveActivity here
    }
}



package com.example.android.constantinenews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.android.constantinenews.data.Article;
import com.example.android.constantinenews.data.ArticleContract.ArticleEntry;

import com.example.android.constantinenews.adapter.ArticleAdapter;
import com.example.android.constantinenews.service.UpdaterService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "MainActivity";

    private static final int DETAIL_ACTIVITY_CODE = 10;

    // the views
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    // the recycler's view adapter
    private ArticleAdapter mAdapter;

    // the layout manager
    private RecyclerView.LayoutManager mLayoutManager;

    // id of the articles loader
    public static final int ID_ARTICLES_LOADER = 2000;

    // layout manager state
    private static final String LAYOUT_MANAGER_STATE = "layout-state";

    private Parcelable mLayoutManagerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        // setting the app's toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // setting the SwipeRefreshLayout and listening for its events
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // recyclerview and layout manager and its adapter
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ArticleAdapter(this, null);
        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(ID_ARTICLES_LOADER, null, this);

        if(savedInstanceState != null){
            mLayoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
        }


        // the ad request
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(LAYOUT_MANAGER_STATE, mLayoutManager.onSaveInstanceState());

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ArticleEntry._ID,
                ArticleEntry.COLUMN_TITLE,
                ArticleEntry.COLUMN_AUTHOR,
                ArticleEntry.COLUMN_GUID,
                ArticleEntry.COLUMN_PUBLISH_DATE,
                ArticleEntry.COLUMN_ARTICLE_BODY,
                ArticleEntry.COLUMN_ARTICLE_LINK,
                ArticleEntry.COLUMN_ARTICLE_PHOTO,
                ArticleEntry.COLUMN_ARTICLE_READ};

        String orderBy = ArticleEntry.COLUMN_PUBLISH_DATE + " DESC";

        return new CursorLoader(MainActivity.this,
                ArticleEntry.CONTENT_URI,
                projection,
                null,
                null,
                orderBy);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        // if there is no data then this first time the app launches
        // so get data from the internet
        if(data.getCount() == 0){
            refresh();
        }

        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    /***
     ****
     ****
     ****  for the SwipeRefreshLayout
     ****
     ****
     *****/

    private boolean mIsRefreshing = false;

    private BroadcastReceiver mRefreshingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (UpdaterService.BROADCAST_ACTION_STATE_CHANGE.equals(intent.getAction())) {
                mIsRefreshing = intent.getBooleanExtra(UpdaterService.EXTRA_REFRESHING, false);
                mSwipeRefreshLayout.setRefreshing(mIsRefreshing);

                if(!mIsRefreshing){
                    getSupportLoaderManager().initLoader(ID_ARTICLES_LOADER, null, MainActivity.this).forceLoad();
                }
            }
        }
    };


    @Override
    public void onRefresh() {
        refresh();
    }

    private void refresh(){
        startService(new Intent(this, UpdaterService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mRefreshingReceiver,
                new IntentFilter(UpdaterService.BROADCAST_ACTION_STATE_CHANGE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mRefreshingReceiver);
    }
}

package com.example.android.constantinenews;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.UserDictionary;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.android.constantinenews.data.ArticleContract;
import com.example.android.constantinenews.data.ArticleContract.ArticleEntry;
import com.example.android.constantinenews.utilities.DateUtils;
import com.example.android.constantinenews.utilities.NetworkUtils;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "DetailActivity";

    // the views
    @BindView(R.id.article_image)
    ImageView mArticleImageView;
    @BindView(R.id.article_title)
    TextView mArticleTitleTextView;
    @BindView(R.id.article_body)
    TextView mArticleBodyTextView;
    @BindView(R.id.article_author)
    TextView mArticleAuthor;
    @BindView(R.id.article_date)
    TextView mArticleDate;
    @BindView(R.id.share_fab)
    FloatingActionButton mShareButton;


    // article identifier in contentProvider
    private Uri mCurrentArticleUri;

    // single article loader identifier
    private static final int ID_SINGLE_ARTICLE_LOADER = 200;

    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DetailTheme);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setElevation(getResources().getDimension(R.dimen.app_bar_elevation));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);



        if(getIntent() != null){
            Intent intent = getIntent();

            if(intent.getData() != null){
                mCurrentArticleUri = intent.getData();
                // start the loader to get the article related data
                getSupportLoaderManager().initLoader(ID_SINGLE_ARTICLE_LOADER, null, this);
            }
        }

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();



        // [START screen_view_hit]
        Log.i(TAG, "Setting screen name: " + TAG);
        mTracker.setScreenName("Image~" + TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        // [END screen_view_hit]

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ArticleEntry._ID,
                ArticleEntry.COLUMN_TITLE,
                ArticleEntry.COLUMN_ARTICLE_PHOTO,
                ArticleEntry.COLUMN_ARTICLE_BODY,
                ArticleEntry.COLUMN_AUTHOR,
                ArticleEntry.COLUMN_PUBLISH_DATE,
                ArticleEntry.COLUMN_ARTICLE_READ};

        return new CursorLoader(DetailActivity.this,
                mCurrentArticleUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()){
            String photoLink = cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_PHOTO));
            String author = cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_AUTHOR));
            long date = cursor.getLong(cursor.getColumnIndex(ArticleEntry.COLUMN_PUBLISH_DATE));
            String title = cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_TITLE));
            final String articleBody = cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_BODY));
            int isArticleRead = cursor.getInt(cursor.getColumnIndex(ArticleEntry.COLUMN_ARTICLE_READ));

            // binding views to their data

            // the article image (if available) && (if internet is available)
            if(!TextUtils.isEmpty(photoLink)){
                if(NetworkUtils.isOnline(this))
                {
                    Picasso.with(this).load(photoLink).into(mArticleImageView);
                    mArticleImageView.setVisibility(View.VISIBLE);
                }
            }

            // the author (if available)
            if(!TextUtils.isEmpty(author)){
                mArticleAuthor.setText(author);
                mArticleAuthor.setVisibility(View.VISIBLE);
            }

            // the date
            mArticleDate.setText(DateUtils.getDateFromLong(date));

            // the title
            mArticleTitleTextView.setText(title);

            // article body
            // add at least five new line so that rest of article
            // won't be obstructed by fab
            mArticleBodyTextView.setText(articleBody + "\n\n\n");

            mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(DetailActivity.this)
                            .setType("text/plain")
                            .setText(articleBody)
                            .getIntent(), getString(R.string.action_share)));
                }
            });
            cursor.close();

            // here we're reading the article for the first time
            if(isArticleRead == 0){
                // consider article as "read"
                updateArticle();
            }
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mArticleImageView.setImageResource(android.R.color.transparent);
        mArticleTitleTextView.setText("");
        mArticleBodyTextView.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            // telling MainActivity if favorites has changed
            //Intent returnIntent = new Intent();
            //returnIntent.putExtra(DETAIL_ACTIVITY_RESULT, mDataHasChanged);
            //setResult(Activity.RESULT_OK, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateArticle(){

        ContentValues mUpdateValues = new ContentValues();

        mUpdateValues.put(ArticleEntry.COLUMN_ARTICLE_READ, 1);


        long mRowsUpdated = getContentResolver().update(mCurrentArticleUri, mUpdateValues,null, null);
    }
}

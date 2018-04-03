package com.example.android.constantinenews.service;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import com.example.android.constantinenews.data.Article;
import com.example.android.constantinenews.data.ArticleContract;
import com.example.android.constantinenews.data.Feed;
import com.example.android.constantinenews.utilities.NetworkUtils;
import com.example.android.constantinenews.utilities.SharedPrefUtils;
import com.example.android.constantinenews.utilities.XmlParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import com.example.android.constantinenews.data.ArticleContract.ArticleEntry;


public class UpdaterService extends IntentService {

    private static final String TAG = "UpdaterService";

    public static final String BROADCAST_ACTION_STATE_CHANGE
            = "com.example.android.constantinenews.intent.action.STATE_CHANGE";
    public static final String EXTRA_REFRESHING
            = "com.example.android.constantinenews.intent.extra.REFRESHING";

    public UpdaterService() {
        super("UpdaterService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(!NetworkUtils.isOnline(getApplicationContext())){
            // here we're not online
            Log.e(TAG, "Not online, not refreshing.");
            return;
        }

        // here we are online we can refresh the data

        // send info back to MainActivity and tell her we're updating
        sendStickyBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, true));

        //Log.e(TAG, "rani hna");

        ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();

        Uri dirUri = ArticleEntry.CONTENT_URI;

        // Delete all articles
        //cpo.add(ContentProviderOperation.newDelete(dirUri).build());


        // first we get the raw XML
        String rawXml = null;
        try {
            rawXml = NetworkUtils.getRawXML();
        } catch (IOException e) {
            Log.e(TAG, "could not get raw xml");
        }


        // then we try to parse the XML
        Feed feed = XmlParser.getFeedFromXml(rawXml);



        long lastUpdateTime = SharedPrefUtils.getLastUpdateTime(this);

        // if true it is the same file we downloaded earlier so nothing new is in it
        if(lastUpdateTime == feed.getFeedDate()){
            sendStickyBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
            Log.e(TAG, "rani hna");
            return;
        }


        boolean anArticleHasBeenAdded = false;
        // insert articles to database
        for(int i = 0; i < feed.getFeedArticles().size(); i++){

            Article currentArticle = feed.getFeedArticles().get(i);

            // article published after the last update are for sure not in DB so we add them
            if(currentArticle.getPublishDate() > lastUpdateTime){

                ContentValues values = new ContentValues();

                values.put(ArticleEntry.COLUMN_TITLE, currentArticle.getTitle());
                values.put(ArticleEntry.COLUMN_AUTHOR, currentArticle.getAuthor());
                values.put(ArticleEntry.COLUMN_GUID, currentArticle.getGuid());
                values.put(ArticleEntry.COLUMN_PUBLISH_DATE, currentArticle.getPublishDate());
                values.put(ArticleEntry.COLUMN_ARTICLE_BODY, currentArticle.getArticleBody());
                values.put(ArticleEntry.COLUMN_ARTICLE_LINK, currentArticle.getArticleLink());
                values.put(ArticleEntry.COLUMN_ARTICLE_PHOTO, currentArticle.getArticlePhoto());
                values.put(ArticleEntry.COLUMN_ARTICLE_READ, currentArticle.isArticleRead());

                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());

                anArticleHasBeenAdded = true;
            }

        }

        try {
            getContentResolver().applyBatch(ArticleContract.CONTENT_AUTHORITY, cpo);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(TAG, "could not update DB");
        }

        // if at least one article has been added update the "update time"
        if(anArticleHasBeenAdded){
            SharedPrefUtils.updateLastUpdateTime(this, feed.getFeedDate());
        }

        sendStickyBroadcast(new Intent(BROADCAST_ACTION_STATE_CHANGE).putExtra(EXTRA_REFRESHING, false));
    }

}

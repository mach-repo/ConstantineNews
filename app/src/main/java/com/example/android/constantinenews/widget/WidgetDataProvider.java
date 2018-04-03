package com.example.android.constantinenews.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Binder;
import android.preference.PreferenceManager;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.constantinenews.MainActivity;
import com.example.android.constantinenews.R;
import com.example.android.constantinenews.data.ArticleContract;
import com.example.android.constantinenews.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by merouane on 16/02/2018.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "WidgetDataProvider";

    private Context mContext;
    private Cursor mCursor;
    private Intent intent;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
        this.intent = intent;
    }

    private void initCursor(){
        if (mCursor != null) {
            mCursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        /**This is done because the widget runs as a separate thread
         when compared to the current app and hence the app's data won't be accessible to it
         because I'm using a content provided **/
        String[] projection = {
                ArticleContract.ArticleEntry._ID,
                ArticleContract.ArticleEntry.COLUMN_TITLE};

        String orderBy = ArticleContract.ArticleEntry.COLUMN_PUBLISH_DATE + " DESC" + " limit 5";

        mCursor = mContext.getContentResolver().query(ArticleContract.ArticleEntry.CONTENT_URI,
                projection,
                null,
                null,
                orderBy);

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onCreate() {
        initCursor();
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
    }

    @Override
    public void onDataSetChanged() {
        /** Listen for data changes and initialize the cursor again **/
        initCursor();
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_list_item);
        mCursor.moveToPosition(position);

        view.setTextViewText(R.id.widget_article_title,
                mCursor.getString(mCursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_TITLE)));

        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

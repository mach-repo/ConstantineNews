package com.example.android.constantinenews.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import com.example.android.constantinenews.data.ArticleContract;
import com.example.android.constantinenews.data.ArticleContract.ArticleEntry;

/**
 * Created by merouane on 15/02/2018.
 */

public class ArticleProvider extends ContentProvider{

    public static final String TAG = ArticleProvider.class.getSimpleName();

    private static final int ARTICLES = 100;

    private static final int ARTICLE_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_ARTICLES, ARTICLES);

        sUriMatcher.addURI(ArticleContract.CONTENT_AUTHORITY, ArticleContract.PATH_ARTICLES + "/#", ARTICLE_ID);
    }

    private ArticleDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new ArticleDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLES:
                cursor = database.query(ArticleEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case ARTICLE_ID:
                selection = ArticleEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(ArticleEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLES:
                return insertArticle(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertArticle(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new movie with the given values
        long id = database.insert(ArticleEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    // don't need it for now
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // we're only gonna need to update a single article
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsUpdated = 0;

        int match = sUriMatcher.match(uri);

        switch (match) {

            case ARTICLE_ID:
                selection = ArticleEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsUpdated = db.update(ArticleEntry.TABLE_NAME,
                        values, selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unrecognised uri: " + uri);
        }

        if(rowsUpdated > 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ARTICLES:
                return ArticleEntry.CONTENT_LIST_TYPE;
            case ARTICLE_ID:
                return ArticleEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}








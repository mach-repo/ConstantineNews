package com.example.android.constantinenews.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.constantinenews.data.ArticleContract.ArticleEntry;

/**
 * Created by merouane on 15/02/2018.
 */

public class ArticleDbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "articles.db";

    private static final int DATABASE_VERSION = 1;

    public ArticleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_ARTICLES_TABLE =  "CREATE TABLE " + ArticleEntry.TABLE_NAME + " ("
                + ArticleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ArticleEntry.COLUMN_TITLE + " TEXT, "
                + ArticleEntry.COLUMN_AUTHOR + " TEXT, "
                + ArticleEntry.COLUMN_GUID + " TEXT NOT NULL, " // this is the unique identifier for an article
                + ArticleEntry.COLUMN_PUBLISH_DATE + " INTEGER, "
                + ArticleEntry.COLUMN_ARTICLE_BODY + " TEXT, "
                + ArticleEntry.COLUMN_ARTICLE_LINK + " TEXT, "
                + ArticleEntry.COLUMN_ARTICLE_PHOTO + " TEXT, "
                + ArticleEntry.COLUMN_ARTICLE_READ  + " INTEGER);";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_ARTICLES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}


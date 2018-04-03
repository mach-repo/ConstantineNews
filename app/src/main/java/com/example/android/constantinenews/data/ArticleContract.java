package com.example.android.constantinenews.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by merouane on 15/02/2018.
 */

public class ArticleContract {

    private ArticleContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.constantinenews";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ARTICLES = "articles";

    public static final class ArticleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ARTICLES);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ARTICLES;


        public final static String TABLE_NAME = "article";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_TITLE ="title";

        public final static String COLUMN_AUTHOR = "author";

        public final static String COLUMN_GUID = "guid";

        public final static String COLUMN_PUBLISH_DATE = "publish_date";

        public final static String COLUMN_ARTICLE_BODY = "article_body";

        public final static String COLUMN_ARTICLE_LINK = "article_link";

        public final static String COLUMN_ARTICLE_PHOTO = "article_photo";

        public final static String COLUMN_ARTICLE_READ = "article_read";
    }
}

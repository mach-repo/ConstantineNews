package com.example.android.constantinenews.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.constantinenews.DetailActivity;
import com.example.android.constantinenews.MainActivity;
import com.example.android.constantinenews.R;
import com.example.android.constantinenews.data.Article;
import com.example.android.constantinenews.data.ArticleContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;
/**
 * Created by merouane on 15/02/2018.
 */

public class ArticleAdapter extends CursorRecyclerViewAdapter<ArticleAdapter.ViewHolder>{

    private final Context mContext;

    private final String TAG = "Adapter";

    public ArticleAdapter(@NonNull Context context, Cursor cursor) {
        super(cursor);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_list_item, parent, false);
        final ViewHolder vh = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri articleUri = ContentUris.withAppendedId(ArticleContract.ArticleEntry.CONTENT_URI,
                        getItemId(vh.getAdapterPosition()));

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.setData(articleUri);
                mContext.startActivity(intent);
            }
        });

        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final Cursor cursor) {

        // article title
        String title = cursor.getString(cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_TITLE));
        holder.mArticleTitle.setText(title);
        // article author
        String author = cursor.getString(cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_AUTHOR));
        if(!TextUtils.isEmpty(author)){
            holder.mArticleAuthor.setText(author);
            holder.mArticleAuthor.setVisibility(View.VISIBLE);
        }
        // article date
        long date = cursor.getLong(cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_PUBLISH_DATE));
        holder.mArticleDate.setText(com.example.android.constantinenews.utilities.
                DateUtils.getDateFromLong(date));

        // set text style bold for non read articles
        int isArticleRead = cursor.getInt(cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_ARTICLE_READ));
        if(isArticleRead == 0){
            holder.mArticleTitle.setTypeface(null, Typeface.BOLD);
        }
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mArticleTitle;
        TextView mArticleAuthor;
        TextView mArticleDate;

        ViewHolder(View itemView) {
            super(itemView);

            mArticleTitle = (TextView) itemView.findViewById(R.id.item_article_title);
            mArticleAuthor = (TextView) itemView.findViewById(R.id.article_author);
            mArticleDate = (TextView) itemView.findViewById(R.id.article_date);

        }
    }
}
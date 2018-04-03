package com.example.android.constantinenews.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by merouane on 16/02/2018.
 */

public class Feed {

    private long feedDate;
    private List<Article> feedArticles;

    public Feed() {
        this.feedDate = 0;
        this.feedArticles = new ArrayList<>();
    }

    public Feed(long feedDate, List<Article> feedArticles) {
        this.feedDate = feedDate;
        this.feedArticles = feedArticles;
    }

    public long getFeedDate() {
        return feedDate;
    }

    public List<Article> getFeedArticles() {
        return feedArticles;
    }

    public void setFeedDate(long feedDate) {
        this.feedDate = feedDate;
    }

    public void setFeedArticles(List<Article> feedArticles) {
        this.feedArticles = feedArticles;
    }
}

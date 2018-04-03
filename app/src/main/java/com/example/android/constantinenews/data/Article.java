package com.example.android.constantinenews.data;

/**
 * Created by merouane on 15/02/2018.
 */

public class Article {

    private long id;
    private String title;
    private String author;
    private String guid;
    private long publishDate;
    private String articleBody;
    private String articleLink;
    private String articlePhoto;
    private boolean articleRead;

    public Article() {
        this.id = 0;
        this.title = "";
        this.author = "";
        this.guid = "";
        this.publishDate = 0;
        this.articleBody = "";
        this.articleLink = "";
        this.articlePhoto = "";
        this.articleRead = false;
    }

    public Article(long id, String title, String author,
                   String guid, long publishDate, String articleBody,
                   String articleLink, String articlePhoto,
                   boolean articleRead) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.guid = guid;
        this.publishDate = publishDate;
        this.articleBody = articleBody;
        this.articleLink = articleLink;
        this.articlePhoto = articlePhoto;
        this.articleRead = articleRead;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGuid() {
        return guid;
    }

    public long getPublishDate() {
        return publishDate;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public String getArticlePhoto() {
        return articlePhoto;
    }

    public boolean isArticleRead() {
        return articleRead;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setPublishDate(long publishDate) {
        this.publishDate = publishDate;
    }

    public void setArticleBody(String articleBody) {
        this.articleBody = articleBody;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public void setArticlePhoto(String articlePhoto) {
        this.articlePhoto = articlePhoto;
    }

    public void setArticleRead(boolean articleRead) {
        this.articleRead = articleRead;
    }

    @Override
    public boolean equals(Object thatArticle){
        if (thatArticle instanceof Article) {
            Article that = (Article) thatArticle;
            return this.guid.equals(that.getGuid());
        }
        return false;
    }
}

package com.example.android.constantinenews.utilities;

import android.util.Log;
import android.util.Xml;

import com.example.android.constantinenews.data.Article;
import com.example.android.constantinenews.data.Feed;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by merouane on 16/02/2018.
 */

public class XmlParser {

    private static final String TAG = XmlParser.class.toString();

    // for getting the date (these two)
    private static final String CHANNEL = "channel";
    private static final String CHANNEL_LAST_BUILD_DATE = "lastBuildDate";

    // for getting a single item (an article)
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String GUID = "guid";
    private static final String PUBLISH_DATE = "pubDate";
    private static final String DESCRIPTION = "description";
    private static final String ENCLOSURE = "enclosure";
    private static final String LINK = "link";


    public static Feed getFeedFromXml(String rawXML) {

        // a feed consists of a date and a list of articles
        Feed feed = new Feed();
        List<Article> articlesList = new ArrayList<Article>();

        XmlPullParser parser = Xml.newPullParser();

        InputStream stream = null;
        try {
            stream = new ByteArrayInputStream(rawXML.getBytes("UTF-8"));
            parser.setInput(stream, null);
            int eventType = parser.getEventType();
            boolean done = false;

            Article newArticle = null;

            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();

                        // we need to grab the date
                        if(name.equalsIgnoreCase(CHANNEL_LAST_BUILD_DATE) && newArticle == null){
                            // is this the lastPubDate
                            long xmlDocumentDate = DateUtils.getTimeStamp(parser.nextText());
                            feed.setFeedDate(xmlDocumentDate);
                            Log.e(TAG, "the feed date is = " + xmlDocumentDate);
                        }

                        if(name.equalsIgnoreCase(ITEM)){
                            newArticle = new Article();
                        }else if (newArticle != null){
                            // here we're sure that we're inside an article (item)
                            if(name.equalsIgnoreCase(TITLE)){
                                newArticle.setTitle(parser.nextText());
                            }else if(name.equalsIgnoreCase(AUTHOR)){
                                newArticle.setAuthor(parser.nextText());
                            }else if(name.equalsIgnoreCase(GUID)){
                                newArticle.setGuid(parser.nextText());
                            }else if(name.equalsIgnoreCase(PUBLISH_DATE)){
                                String dateText = parser.nextText();
                                //Log.e(TAG, "the article date text is = " + dateText);
                                long date = DateUtils.getTimeStamp(dateText);
                                //Log.e(TAG, "the article date is = " + date);
                                newArticle.setPublishDate(date);
                            }else if(name.equalsIgnoreCase(DESCRIPTION)){
                                newArticle.setArticleBody(parser.nextText());
                            }else if(name.equalsIgnoreCase(ENCLOSURE)){
                                newArticle.setArticlePhoto(parser.getAttributeValue(0));
                            }else if(name.equalsIgnoreCase(LINK)){
                                newArticle.setArticleLink(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if(name.equalsIgnoreCase(ITEM) && newArticle != null){
                            articlesList.add(newArticle);
                        }else if(name.equalsIgnoreCase(CHANNEL)){
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }

            // giving feed object its appropriate values
            feed.setFeedArticles(articlesList);
        } catch (Exception e) {
            Log.e(TAG, "could not parse XML");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return feed;
    }
}

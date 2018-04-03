package com.example.android.constantinenews.widget;

/**
 * Created by merouane on 16/02/2018.
 */

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * WidgetService is the that will return our RemoteViewsFactory
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}

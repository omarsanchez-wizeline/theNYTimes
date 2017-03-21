package com.wizeline.omarsanchez.nytimessearch.interfaces;

import android.content.Context;

import com.wizeline.omarsanchez.nytimessearch.models.Filter;

/**
 * Created by omarsanchez on 3/16/17.
 */

public interface DocsRequest {
    void getArticles(Context context, String search, String page, ResultRequest resulRequest, Filter filter);

    void getNewPage(Context context, String search, String page, ResultRequest resultRequest, Filter filter);
}

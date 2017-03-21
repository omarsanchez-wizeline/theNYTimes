package com.wizeline.omarsanchez.nytimessearch.interfaces;

import com.wizeline.omarsanchez.nytimessearch.models.Docs;

import java.util.List;

/**
 * Created by omarsanchez on 3/19/17.
 */

public interface ResultRequest {
    void onRecive(List<Docs> docs);
    void onFailed(Throwable t);
    void onNewPageRecive(List<Docs> docs);
}

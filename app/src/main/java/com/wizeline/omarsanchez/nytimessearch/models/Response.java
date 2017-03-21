package com.wizeline.omarsanchez.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    private List<Docs> docs;
    private Meta meta;

    public List<Docs> getDocs() {
        return docs;
    }

    public boolean isDocs(){
        return docs == null;
    }

    public Meta getMeta() {
        return meta;
    }
}

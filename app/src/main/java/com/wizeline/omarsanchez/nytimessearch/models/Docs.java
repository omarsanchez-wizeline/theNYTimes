package com.wizeline.omarsanchez.nytimessearch.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Docs {

    private String web_url;
    private Headline headline;

    private String lead_paragraph;
    private List<Multimedia> multimedia;

    public String getWebUrl() {
        return web_url;
    }

    public Headline getHeadline() {
        return headline;
    }

    public String getLeadParagraph() {
        return lead_paragraph;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }
}

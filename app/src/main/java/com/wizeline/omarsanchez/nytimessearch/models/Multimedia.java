package com.wizeline.omarsanchez.nytimessearch.models;

import static com.wizeline.omarsanchez.nytimessearch.webService.Config.BASE_URL;

public class Multimedia {

    private String url;
    private String subtype;
    private int width;
    private int height;
    private String type;

    public String getUrl() {
        return BASE_URL + url;
    }

    public String getSubtype() {
        return subtype;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }
}

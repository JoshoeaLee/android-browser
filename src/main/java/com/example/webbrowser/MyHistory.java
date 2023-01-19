package com.example.webbrowser;

import androidx.annotation.NonNull;

public class MyHistory {

    private final String urlTitle;
    private final String urlSite;

    public MyHistory(String urlTitle, String urlSite){
        this.urlTitle = urlTitle;
        this.urlSite = urlSite;
    }

    public String getUrlTitle() {
        return urlTitle;
    }

    public String getUrlSite() {
        return urlSite;
    }

    @NonNull
    @Override
    public String toString(){
        return this.urlTitle + " /$/ " + this.urlSite;
    }


}

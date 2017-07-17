package com.coveros.coverosmobileapp.website;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class URLContent implements Runnable{
    private String htmlStuff;
    private String htmlClassName;
    private static final String TAG = "URLContent";

    URLContent(String saved){ htmlStuff = saved; htmlClassName = "null"; }
    URLContent() { htmlClassName = "null"; }

    String getHtmlStuff() { return htmlStuff; }
    void setHtmlStuff(String newUrl) { htmlStuff = newUrl; }
    String getHtmlClassName(){ return htmlClassName; }

    @Override
    public void run() {
        try{
            Document document = Jsoup.connect(getHtmlStuff()).get();
            htmlClassName = document.body().className();
        }
        catch (IOException i){
            //Catches malformed URL's too!
            Log.i(TAG, "IOException Found in URLContent");
            htmlClassName = "failed with IOException";
        }
    }
}
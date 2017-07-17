package com.coveros.coverosmobileapp.website;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.MalformedURLException;

public class URLContent implements Runnable{
    private String htmlStuff;
    private String htmlClassName;
    private static final String TAG = "URLContent";

    public URLContent(String saved){ htmlStuff = saved; htmlClassName = "null"; }
    public URLContent() { htmlClassName = "null"; }

    public String getHtmlStuff() { return htmlStuff; }
    public void setHtmlStuff(String newUrl) { htmlStuff = newUrl; }
    public String getHtmlClassName(){ return htmlClassName; }

    @Override
    public void run() {
        try{
            Document document = Jsoup.connect(getHtmlStuff()).get();
            htmlClassName = document.body().className();
        }
        catch (IOException i){
            //Catches malformed URL's too!
            Log.i("URLContent", "IOException Found in URLContent");
            htmlClassName = "failed with IOException";
        }
    }
}
package com.coveros.coverosmobileapp.website;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class URLContent implements Runnable{
    private String htmlStuff;
    private String htmlClassName = "null";
    private static final String TAG = "URLContent";

    public URLContent(String saved){ htmlStuff = saved; }

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
            i.printStackTrace();
            htmlClassName = "failed with IOException";
        }
        catch (Exception e){
            e.printStackTrace();
            htmlClassName = "failed with Exception";
        }
    }
}
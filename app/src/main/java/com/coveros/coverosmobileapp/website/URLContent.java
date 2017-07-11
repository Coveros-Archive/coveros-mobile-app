package com.coveros.coverosmobileapp.website;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class URLContent implements Runnable{
    private String htmlStuff;
    private String htmlClassName = "null";
    private String longClassName = "null";

    public URLContent(String saved){ htmlStuff = saved; }

    public String getHtmlStuff() { return htmlStuff; }
    public void setHtmlStuff(String newUrl) { htmlStuff = newUrl; }
    public String getHtmlClassName(){ return htmlClassName; }

    @Override
    public void run() {
        try{
            Document document = Jsoup.connect(getHtmlStuff()).get();
            htmlClassName = document.body().className();
            /*
            Log.d("URLContent", "Document Body Classname 0: " + document.body().className().substring(0,21));
            if(document.body().className().substring(0,21).equals("post-template-default")){
                longClassName = document.body().className();
                htmlClassName = ".post-template-default";
            }
            else{
                //default
                htmlClassName = document.body().className();
            }
            */
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
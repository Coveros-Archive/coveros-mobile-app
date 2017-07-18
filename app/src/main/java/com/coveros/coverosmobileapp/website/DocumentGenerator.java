package com.coveros.coverosmobileapp.website;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class DocumentGenerator implements Runnable {

    private String url;
    private static final String TAG = "DocumentGenerator";
    private Document document;

    public DocumentGenerator(String url){
        this.url = url;
    }

    @Override
    public void run() {
        try {
            Document document = Jsoup.connect(url).get();
        }
        catch (IOException i){
            Log.e(TAG, "Document not created", i);
        }
    }

    public Document getDocument() {
        return document;
    }

}
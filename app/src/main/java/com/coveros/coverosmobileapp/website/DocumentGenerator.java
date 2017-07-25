package com.coveros.coverosmobileapp.website;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocumentGenerator implements Runnable {

    private String url;
    private static final String TAG = "DocumentGenerator";
    private Document document;
    private DocumentGeneratorCallback documentGeneratorCallback;

    public DocumentGenerator(String url, DocumentGeneratorCallback documentGeneratorCallback){
        this.url = url;
        this.documentGeneratorCallback = documentGeneratorCallback;
    }

    @Override
    public void run() {
        try {
            document = Jsoup.connect(url).get();
            this.documentGeneratorCallback.onDocumentReceived(document);
        }
        catch (IOException i){
            Log.e(TAG, "Document not created", i);
        }
    }

    public Document getDocument() {
        return document;
    }

    public Document onDocumentReceived(Document document) {
        return document;
    }

    interface DocumentGeneratorCallback {
        void onDocumentReceived(Document document);
    }

}
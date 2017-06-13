package com.coveros.coverosmobileapp;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by EPainter on 6/12/2017.
 */

public class NoErrorWebViewClient extends WebViewClient{

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
        Log.e(String.valueOf(errorCode), description);

        //ERROR: Host Look Up
        if(errorCode == -2){
            String summary = "<html><body style='background: black;'><p style='color: blue;'>" +
                    "Coveros\nSorry, we cannot currently retrieve the requested information." +
                    "</p></body></html>";
            view.loadData(summary, "text/html", null);
            return;
        }

        //Default Behavior
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}

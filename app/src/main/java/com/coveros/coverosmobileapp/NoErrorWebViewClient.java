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
        Log.d(String.valueOf(errorCode), description);

        //ERROR: Timeout
        if(errorCode == -8){
            String summary = "<html><body style='background: black;'><p style='color: blue;'>" +
                    "Coveros\nSorry, we cannot currently retrieve the requested information." +
                    "Please try again.</p></body></html>";
            view.loadData(summary, "ERROR_TIMEOUT", null);
            return;
        }

        //ERROR: CONNECT (SERVER)
        if(errorCode == -6){
            String summary = "<html><body style='background: black;'><p style='color: blue;'>" +
                    "Coveros\nSorry, we cannot currently retrieve the requested information." +
                    "Please try again.</p></body></html>";
            view.loadData(summary, "ERROR_CONNECT", null);
            return;
        }

        //ERROR: INTERNET DISCONNECTED
        if(errorCode == 106){
            String summary = "<html><body style='background: black;'><p style='color: blue;'>" +
                    "Coveros\nSorry, we cannot currently retrieve the requested information." +
                    "Please try again.</p></body></html>";
            view.loadData(summary, "INTERNET_DISCONNECTED", null);
            return;
        }

        //Default Behavior
        super.onReceivedError(view, errorCode, description, failingUrl);
    }
}
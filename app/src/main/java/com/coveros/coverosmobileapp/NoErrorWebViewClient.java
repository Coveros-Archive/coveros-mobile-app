package com.coveros.coverosmobileapp;

import android.annotation.TargetApi;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by EPainter on 6/12/2017.
 */

public class NoErrorWebViewClient extends WebViewClient{

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
        Log.d(request.getUrl().toString(), error.getDescription().toString());

        //ERROR: Timeout
        if(error.getErrorCode() == -8){
            String summary = "<html><body style='background: black;'><p style='color: blue;'>" +
                    "Coveros\nSorry, we cannot currently retrieve the requested information." +
                    "Please try again.</p></body></html>";
            view.loadData(summary, "ERROR_TIMEOUT", null);
            return;
        }

        //ERROR: CONNECT (SERVER)
        if(error.getErrorCode() == -6){
            String summary = "<html><body style='background: black;'><p style='color: blue;'>" +
                    "Coveros\nSorry, we cannot currently retrieve the requested information." +
                    "Please try again.</p></body></html>";
            view.loadData(summary, "ERROR_CONNECT", null);
            return;
        }

        //ERROR: INTERNET DISCONNECTED
        if(error.getErrorCode() == 106){
            String summary = "<html><body style='background: black;'><p style='color: blue;'>" +
                    "Coveros\nSorry, we cannot currently retrieve the requested information." +
                    "Please try again.</p></body></html>";
            view.loadData(summary, "INTERNET_DISCONNECTED", null);
            return;
        }

        //Default Behavior
        super.onReceivedError(view, request, error);
    }
}
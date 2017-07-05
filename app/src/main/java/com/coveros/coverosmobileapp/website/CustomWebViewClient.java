package com.coveros.coverosmobileapp.website;

import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.content.ContentValues.TAG;

/**
 * Created by EPainter on 6/16/2017.
 * Provides Custom WebView Client that only loads Coveros-related content through WebView.
 * All externally related information is processed in a web browser
 */

public class CustomWebViewClient extends WebViewClient {

    private boolean weAreConnected;
    public boolean getConnection(){
        return weAreConnected;
    }
    public void setConnection(boolean answer){
        weAreConnected = answer;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.e(TAG, "Error occurred while loading the web page at URL: " + request.getUrl().toString());
        //Load Blank Page - Could use html substitute error page here
        view.loadUrl("file:///android_asset/sampleErrorPage.html");
        super.onReceivedError(view, request, error);
    }
}
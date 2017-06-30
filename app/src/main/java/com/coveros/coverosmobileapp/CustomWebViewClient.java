package com.coveros.coverosmobileapp;

import android.content.Intent;
import android.net.Uri;
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

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //If blog website or blog webspage is categorically loaded (hybrid)
        if(url.contains("coveros.com/blog") || url.contains("coveros.com/category/blog")){
            //Switch to Blog Post Format (native)
            onPageFinished(view, url);
            return true;
        }
        else if (url.contains("coveros.com")) {
            view.loadUrl(url);
        } else {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(i);
        }
        return true;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.e(TAG, "Error occurred while loading the web page at URL: " + request.getUrl().toString());
        //Load Blank Page - Could use html substitute error page here
        view.loadUrl("file:///android_asset/sampleErrorPage.html");
        super.onReceivedError(view, request, error);
    }
}
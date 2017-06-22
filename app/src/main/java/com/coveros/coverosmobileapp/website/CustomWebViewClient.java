package com.coveros.coverosmobileapp.website;

import android.content.Intent;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import static android.content.ContentValues.TAG;

/**
 * Custom WebViewClient implementation that differentiates between coveros and non-coveros urls.
 * @author Ethan Painter
 */
class CustomWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        boolean override = !request.getUrl().toString().contains("coveros.com");

        // Handle the override here: This should probably go somewhere else
        if (override) {
            Intent i = new Intent(Intent.ACTION_VIEW, request.getUrl());
            view.getContext().startActivity(i);
        }
        return override;
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.e(TAG, "Error occurred while loading the web page at URL: " + request.getUrl().toString());
        //Load Blank Page - Could use html substitute error page here
        view.loadUrl("file:///android_asset/sampleErrorPage.html");
        super.onReceivedError(view, request, error);
    }
}
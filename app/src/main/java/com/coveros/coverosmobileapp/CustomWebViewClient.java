package com.coveros.coverosmobileapp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by EPainter on 6/16/2017.
 */

public class CustomWebViewClient extends WebViewClient{
    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url){
        if(url.contains("coveros.com")){
            view.loadUrl(url);
        }
        else{
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(i);
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url){
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error){
        Log.e(TAG, "Error occurred while loading the web page at URL: " + request.getUrl().toString());
        view.loadUrl("about:blank");
        super.onReceivedError(view, request, error);
    }
}
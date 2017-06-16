package com.coveros.coverosmobileapp;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    }
}
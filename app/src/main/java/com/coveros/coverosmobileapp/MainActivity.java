package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.Toast;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    private final String webName;

    public MainActivity(){
        webName = "https://www.coveros.com";
    }

    public String getWebName(){
        return webName;
    }

    private WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Link WebView varaible with activity_main_webview for Web View Access
        browser = (WebView) findViewById(R.id.activity_main_webview);

        //Enable Javascript (Plugins)
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //Force Links and redirects to open in WebView instead of a browser
        browser.setWebViewClient(new NoErrorWebViewClient());

        //Testing URL for redirecting
        //browser.loadUrl("http://www.html5rocks.com/");

        //URL for Coveros
        browser.loadUrl("https://coveros.com");
    }

    @Override
    public void onBackPressed(){
        if(browser.canGoBack()){
            browser.goBack();
        }
        else{
            super.onBackPressed();
        }
    }
}
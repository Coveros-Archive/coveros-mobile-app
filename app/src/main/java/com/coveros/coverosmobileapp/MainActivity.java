package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.*;
import android.widget.TableLayout;
import android.widget.Toast;
import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    private String webName;

    public MainActivity(){
        webName = "https://www.coveros.com";
    }

    public String getWebName(){
        return webName;
    }

    public void setWebName(String website){
        webName = website;
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

        //URL for Coveros
        //Can be changed by either using setWebName or changing value in constructor
        browser.loadUrl(webName);

        //Testing AlertDialog Message
        alertView();
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

    //Alert Dialog
    private void alertView(){
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
        dialog.setTitle("Coveros");
        dialog.setMessage("\nSorry, we cannot currently retrieve the requested " +
                "information.\n\nPlease try again.");
        dialog.setButton(dialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
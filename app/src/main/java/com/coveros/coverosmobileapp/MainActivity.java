package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.webkit.*;
import android.widget.TableLayout;
import android.widget.TextView;
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

        //Phone is online
        if(isOnline()){
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
        }
        else{
            try{
                //Link WebView varaible with activity_main_webview for Web View Access
                browser = (WebView) findViewById(R.id.activity_main_webview);

                //Enable Javascript (Plugins)
                WebSettings webSettings = browser.getSettings();
                webSettings.setJavaScriptEnabled(true);

                //Force Links and redirects to open in WebView instead of a browser
                browser.setWebViewClient(new NoErrorWebViewClient());

                alertView();
            }
            catch(Exception e){
                //Save logs if necessary
                //Log.d(Constants.TAG, "Show Dialog: " + e.getMessage());
            }
        }
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

    public boolean isOnline(){
        //Get Connectivity Manager and network info
        ConnectivityManager conMgr = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        //No internet connection
        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            return false;
        }
        //Otherwise, must be connected
        return true;
    }

    //Alert Dialog
    private void alertView(){

        //Initilize Alert Dialog menu
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();

        //Create Strings for Title, messsage, and button
        String title = "Coveros";
        String message = "Sorry, we cannot currently retrieve the requested information.";
        String button = "Please Try Again.";

        //Setters & Toast Message after click "Please Try Again" (onClick)
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(dialog.BUTTON_NEUTRAL, button, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
        //Show dialog and make text changes (font color, size, etc.)
        dialog.show();
    }
}
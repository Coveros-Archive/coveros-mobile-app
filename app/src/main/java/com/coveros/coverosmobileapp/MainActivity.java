package com.coveros.coverosmobileapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.*;
import android.widget.Toast;
//import java.util.function.Function;

public class MainActivity extends AppCompatActivity {

    private String webName;
    private WebView browser;

    public MainActivity(){
        webName = "https://www.coveros.com";
        WebView browser;
    }

    public String getWebName(){
        return webName;
    }

    public void setWebName(String website){
        webName = website;
    }

    public WebView getBrowser(){
        return browser;
    }

    public void setBrowser(WebView br){
        browser = br;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Link WebView varaible with activity_main_webview for Web View Access
        browser = (WebView) findViewById(R.id.activity_main_webview);
        //Force Links and redirects to open in WebView instead of a browser
        browser.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView view, String url, Bitmap fav){
                    super.onPageStarted(view, url, fav);
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    setWebName(url);
                    super.onPageFinished(view, url);
                }
                public String toString(){
                    return "WebViewClient";
                }
        });
    }

    @Override
    protected void onStart(){
        //Phone is online
        if(isOnline()){
            //Enable Javascript (Plugins)
            WebSettings webSettings = browser.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //URL for Coveros
            //Can be changed by either using setWebName or changing value in constructor
            browser.loadUrl(webName);
        }
        else{
            try{
                //Enable Javascript (Plugins)
                WebSettings webSettings = browser.getSettings();
                webSettings.setJavaScriptEnabled(true);
                //AlertDialog
                alertView();
            }
            catch(Exception e){
                //Save logs if necessary
                //Log.d(Constants.TAG, "Show Dialog: " + e.getMessage());
            }
        }
        super.onStart();
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

        //Initilize Alert Dialog menu & Cancel only if pressed on button
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
        dialog.setCanceledOnTouchOutside(false);

        //Create Strings for Title, messsage, and button
        String title = "Alert";
        String message = "Sorry, we cannot currently retrieve the requested information.";
        String button1 = "Exit App";
        String button2 = "Reload App";
        String button3 = "OK";

        //Setters (title, default message, button 1 -> Exit, button2 -> Reload)
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, button1, new DialogInterface.OnClickListener(){
            //Text Message on the bottom after clicking the button
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                finish();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, button2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Loading App", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                recreate();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, button3, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        //Show dialog and make text changes (font color, size, etc.)
        dialog.show();
    }
}
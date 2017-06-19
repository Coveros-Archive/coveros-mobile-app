package com.coveros.coverosmobileapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.*;
import android.widget.Toast;

import java.net.URL;
import java.net.URLConnection;
//import java.util.function.Function;

public class MainActivity extends AppCompatActivity {
    //MainActivity
    private String webName;
    private WebView browser;
    //Alerts
    private AlertDialog dialog;

    public MainActivity(){
        webName = "https://www.coveros.com";
    }

    public String getWebName(){
        return webName;
    }

    public void setWebName(String website){
        webName = website;
    }

    public WebView getWebViewBrowser(){ return browser; }

    public void setWebViewBrowser(WebView br){
        browser = br;
    }

    public AlertDialog getDialog(){
        return dialog;
    }

    /*
     * On Creation/Declaration of App/Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity main = new MainActivity();
        //Link WebView variable with activity_main_webview for Web View Access
        browser = (WebView) findViewById(R.id.activity_main_webview);
        main.setWebViewBrowser(browser);
        //Links open in WebView with Coveros regex check
        browser.setWebViewClient(new CustomWebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap fav){
                super.onPageStarted(view, url, fav);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                setWebName(url);
                super.onPageFinished(view, url);
            }
        });
    }

    /*
     * On App StartUp
     */
    @Override
    protected void onStart(){
        //Phone is online & Connected to a server
        if(isOnline()){
            //Enable Javascript (Plugins)
            WebSettings webSettings = browser.getSettings();
            webSettings.setJavaScriptEnabled(true);
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
                Log.d("Error: ", "Show Dialog: " + e.getMessage());
            }
        }
        super.onStart();
    }

    /*
     * Back button is pressed in the app. Default implementation
     */
    @Override
    public void onBackPressed(){
        if(browser.canGoBack()){
            browser.goBack();
        }
        else{
            super.onBackPressed();
        }
    }

    /*
     * Checks if the user is connected to the Internet
     */
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

    /*
     * Checks if the user is connected to a valid server. Still needs testing
     */
    public boolean isConnectedToServer(String url, int timeout){
        try{
            URL myURL = new URL(url);
            URLConnection connection = myURL.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        }
        catch(Exception e){
            //Handle any exceptions
            return false;
        }
    }

    /*
     * Provides AlertDialog box if an error occurs in internet connectivity
     * Three options:   Reload App (Reload's Activity, NOT THE ENTIRE APP)
     *                  Exit App (Quits the Activity and closes the app)
     *                  OK (Closes the AlertDialog box but keeps the app open)
     *
     * OK option provided to immediately close the dialog box if the user's wifi loads
     *      the Coveros website in the background
     */
    private void alertView(){

        //Init Alert Dialog menu & Cancel only if pressed on button
        dialog = new AlertDialog.Builder(MainActivity.this).create();
        dialog.setCanceledOnTouchOutside(false);

        //Create Strings for Title, messsage, and buttons
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

    /*
     * Purpose: Saving Scrolled position on Web Pages and coming back to for later viewing(onPause)
     * Calculates Scrolling Percentage to return to precisely same location
     * on webpage. Previously onResume after onPause results in user being
     * pushed to the top of a webpage, losing track of where/what they were
     * looking at
     */
    private float calculateProgression(WebView content){
          float contentHeight = content.getContentHeight();
          float currentScrollPosition = content.getScrollY();
          float percentWebview = currentScrollPosition / contentHeight;
          return percentWebview;
    }
}
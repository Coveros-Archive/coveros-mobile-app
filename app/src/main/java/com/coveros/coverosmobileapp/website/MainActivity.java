package com.coveros.coverosmobileapp.website;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.blogpost.BlogPostsListActivity;

import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {
    //MainActivity
    private String webName;
    private WebView browser;
    private AlertDialog dialog;

    private ListView drawerList;
    private static final String[] MENU_TITLES = new String[]{"Home","Blog","Bookmarks"};
    private DrawerLayout menu;

    public MainActivity(){
        webName = "https://www.coveros.com/";
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
        menu = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1 , MENU_TITLES));
        drawerList.setOnItemClickListener(new MainActivity.DrawerItemClickListener());
        menu.addDrawerListener(new DrawerLayout.SimpleDrawerListener(){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset){
                browser.setTranslationX(slideOffset * drawerView.getWidth());
                menu.bringChildToFront(drawerView);
                menu.requestLayout();
            }}
        );
        //URLContent html = new URLContent();
        //Links open in WebView with Coveros regex check
        browser.setWebViewClient(new CustomWebViewClient(){
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //If blog website or blog webspage is categorically loaded (hybrid)
                if(url.contains("coveros.com/blog/") || url.contains("coveros.com/category/blogs/") ||
                        url.contains("dev.secureci.com/blog/") || url.contains("dev.secureci.com/category/blogs/")){
                    //Load new activity
                    Intent startBlogPost = new Intent(getApplicationContext(), BlogPostsListActivity.class);
                    startActivity(startBlogPost);
                    return true;
                }
                if (url.contains("coveros.com") || url.contains("dev.secureci.com")) {
                    view.loadUrl(url);
                    return true;
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(i);
                    return true;
                }
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap fav){
                if(("https://www.coveros.com/blog/").equals(webName)){
                    onBackPressed();
                }
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                setWebName(url);
            }
        });
        if(!isOnline()){
            browser.loadUrl("file:///android_asset/sampleErrorPage.html");
        }
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
            alertView();
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
        return (netInfo != null && netInfo.isConnected() && netInfo.isAvailable());
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
        //Create Strings for Title, messsage, and buttons
        String title = "Alert";
        String message = "Sorry, we cannot currently retrieve the requested information.";
        String button1 = "Exit App";
        String button2 = "Reload App";
        String button3 = "OK";
        //Init Alert Dialog menu & Cancel only if pressed on button
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setNeutralButton(button2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Loading App", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                recreate();
            }
        })
                .setNegativeButton(button3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); }
                })
                .setPositiveButton(button1, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                }).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        //Setters (title, default message, button 1 -> Exit, button2 -> Reload)
        dialog.setTitle(title);
        dialog.setMessage(message);
        //Show dialog and make text changes (font color, size, etc.)
        dialog.show();
    }

    /**
     * Made to navigate through the menu drawer by click
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), BlogPostsListActivity.class));
            }
        }
    }
}
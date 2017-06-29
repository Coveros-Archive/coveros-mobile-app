package com.coveros.coverosmobileapp.website;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.post.PostListActivity;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MainActivity extends AppCompatActivity {
    //MainActivity
    private String webName;
    private WebView browser;

    private String[] menuTitles = new String[]{"Website","Blog","Bookmarks"};
    private DrawerLayout menu;
    private ListView drawerList;
    private WebView postList;

    //Create Strings for Title, messsage, and buttons
    private static final String ALERT_TTLE = "Alert";
    private static final String ALERT_MESSAGE = "Sorry, we cannot currently retrieve the requested information.";
    private static final String ALLERT_BUTTON_1 = "Exit App";
    private static final String ALLERT_BUTTON_2 = "Reload App";
    private static final String ALLERT_BUTTON_3 = "OK";

    public MainActivity() {
        webName = "https://www.coveros.com";
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String website) {
        webName = website;
    }

    public void setWebViewBrowser(WebView br) {
        browser = br;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity main = new MainActivity();
        //Link WebView variable with activity_main_webview for Web View Access
        browser = (WebView) findViewById(R.id.activity_main_webview);
        main.setWebViewBrowser(browser);

        postList = (WebView) findViewById(R.id.activity_main_webview);
        menu = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView)findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1 , menuTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        menu.addDrawerListener(new DrawerLayout.SimpleDrawerListener(){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset){
                postList.setTranslationX(slideOffset * drawerView.getWidth());
                menu.bringChildToFront(drawerView);
                menu.requestLayout();
            }}
        );

        //Links open in WebView with Coveros regex check
        browser.setWebViewClient(new CustomWebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                setWebName(url);
                super.onPageFinished(view, url);
            }
        });
        if (!isOnline()) {
            browser.loadUrl("file:///android_asset/sampleErrorPage.html");
        }
    }

    @Override
    protected void onStart() {
        //Phone is online & Connected to a server
        if (isOnline()) {
            //Enable Javascript (Plugins)
            WebSettings webSettings = browser.getSettings();
            webSettings.setJavaScriptEnabled(true);
            //Can be changed by either using setWebName or changing value in constructor
            browser.loadUrl(webName);
        } else {
            AlertDialog dialog = generateAlertView();
            dialog.show();
        }
        super.onStart();
    }

    /**
     * Back button is pressed in the app. Default implementation
     */
    @Override
    public void onBackPressed() {
        if (browser.canGoBack()) {
            browser.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Checks if the user is connected to the Internet
     */
    public boolean isOnline() {
        //Get Connectivity Manager and network info
        ConnectivityManager conMgr = (ConnectivityManager)
                getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        // NetInfo must be connected and available
        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
    }

    /**
     * Provides AlertDialog box if an error occurs in internet connectivity
     * Three options:   Reload App (Reload's Activity, NOT THE ENTIRE APP)
     *                  Exit App (Quits the Activity and closes the app)
     *                  OK (Closes the AlertDialog box but keeps the app open)
     *
     * OK option provided to immediately close the dialog box if the user's wifi loads
     *      the Coveros website in the background
     */
    private AlertDialog generateAlertView() {
        // Init Alert Dialog menu & Cancel only if pressed on button
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle(ALERT_TTLE)
                .setMessage(ALERT_MESSAGE)
                .setCancelable(false)
                .setNeutralButton(ALLERT_BUTTON_2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Loading App", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        recreate();
                    }
                })
                .setNegativeButton(ALLERT_BUTTON_3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(ALLERT_BUTTON_1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(position) {
                case 0:
                    Intent a = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(a);
                    break;

                case 1:
                    Intent b = new Intent(getApplicationContext(), PostListActivity.class);
                    startActivity(b);
                    break;
            }
        }
    }
}
package com.coveros.coverosmobileapp.website;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.blogpost.BlogPostsListActivity;


@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MainActivity extends AppCompatActivity {
    //MainActivity\
    private boolean isDevSite = true;
    private String webName;
    private WebView browser;
    private AlertDialog dialog;
    private CustomWebViewClient cwvc = new CustomWebViewClient();

    private String[] menuTitles;
    private DrawerLayout menu;
    RequestQueue rQueue;

    //Create Strings for Title, message, and buttons
    private static final String ALERT_TITLE = "Alert";
    private static final String ALERT_MESSAGE = "Sorry, we cannot currently retrieve the requested information.";
    private static final String ALERT_BUTTON_EXIT = "Exit App";
    private static final String ALERT_BUTTON_RELOAD= "Reload App";
    private static final String ALERT_BUTTON_OK = "OK";

    public MainActivity(){ webName = "https://www3.dev.secureci.com"; }
    public MainActivity(String specificUrl) { webName = specificUrl; }

    public String getWebName(){ return webName; }
    public void setWebName(String website){ webName = website; }
    public WebView getWebViewBrowser(){ return browser; }
    public void setWebViewBrowser(WebView br){ browser = br; }
    public AlertDialog getDialog() { return dialog; }
    public CustomWebViewClient getCustomClient() { return cwvc; }
    public void setCustomClient(CustomWebViewClient cc) { cwvc = cc;}

    /*
     * On Creation/Declaration of App/Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Link WebView variable with activity_main_webview for Web View Access
        browser = (WebView) findViewById(R.id.activity_main_webview);
        setWebViewBrowser(browser);
        rQueue = Volley.newRequestQueue(MainActivity.this);

        //constructing the menu navigation drawer
        menu = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList;
        drawerList = (ListView)findViewById(R.id.left_drawer);
        menuTitles = getResources().getStringArray(R.array.menu_Titles);
        drawerList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 , menuTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        menu.addDrawerListener(new DrawerLayout.SimpleDrawerListener(){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset){
                browser.setTranslationX(slideOffset * drawerView.getWidth());
                menu.bringChildToFront(drawerView);
                menu.requestLayout();
            }}
        );
        //Links open in WebView with Coveros regex check
        cwvc.setMainActivity(this);
        browser.setWebViewClient(cwvc);
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
            //JS settings enable/disable hamburger menu, videos, and other media
            browser.getSettings().setJavaScriptEnabled(true);
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
        return netInfo != null && netInfo.isConnected() && netInfo.isAvailable();
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
    protected void alertView(){
        //Init Alert Dialog menu & Cancel only if pressed on button
        dialog = new AlertDialog.Builder(MainActivity.this)
                .setNeutralButton(ALERT_BUTTON_RELOAD, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogs, int which) {
                        Toast.makeText(getApplicationContext(), "Loading App", Toast.LENGTH_SHORT).show();
                        dialogs.dismiss();
                        recreate();
                    }
                })
                .setNegativeButton(ALERT_BUTTON_OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogs, int which) {
                        dialogs.dismiss(); }
                })
                .setPositiveButton(ALERT_BUTTON_EXIT, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogs, int which){
                        Toast.makeText(getApplicationContext(), "Thank You", Toast.LENGTH_SHORT).show();
                        dialogs.dismiss();
                        finish();
                    }
                }).create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        //Setters (title, default message, button 1 -> Exit, button2 -> Reload)
        dialog.setTitle(ALERT_TITLE);
        dialog.setMessage(ALERT_MESSAGE);
        //Show dialog and make text changes (font color, size, etc.)
        dialog.show();
    }

    /**
     * Made to navigate through the menu drawer by click
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        /**
         * This method implements navigating to the corresponding activity when a position is selected on the navigation menu drawer
         * @param parent the current placing of the adapter
         * @param view the current layout shown
         * @param position the int the describes the placing in the list
         * @param id the specified value of the layout
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String website;
            if(isDevSite){
                //Use dev site URL
                website = "https://www3.dev.secureci.com/";
            }
            else{
                //Use Main website
                website = "https://www.coveros.com/";
            }

            switch (position) {
                //Break statements include dev and main websites
                case 0:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website);
                    break;
                case 1:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "#projects");
                    break;
                case 2:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "products/secure-ci/");
                    break;
                case 3:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "products/selenified/");
                    break;
                case 4:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "#services");
                    break;
                case 5:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "services/agile-development/");
                    break;
                case 6:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "services/agile-testing/");
                    break;
                case 7:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "services/agile-transition/");
                    break;
                case 8:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website+  "services/devops/");
                    break;
                case 9:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "services/software-security/");
                    break;
                case 10:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "services/training/");
                    break;
                case 11:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "#latest-news");
                    break;
                case 12:
                    startActivity(new Intent(getApplicationContext(), BlogPostsListActivity.class));
                    break;
                case 13:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "presentations/");
                    break;
                case 14:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "publications/");
                    break;
                case 15:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "webinars-videos/");
                    break;
                case 16:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "#about");
                    break;
                case 17:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "#team");
                    break;
                case 18:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "careers/");
                    break;
                case 19:
                    menu.closeDrawer(Gravity.START);
                    browser.loadUrl(website + "#contact-us");
                    break;
                default:
                    //No Case found (Position further than current list (Load Home Page)
                    browser.loadUrl(website);
            }
        }
    }
}
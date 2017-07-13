package com.coveros.coverosmobileapp.website;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.coveros.coverosmobileapp.blogpost.BlogPostReadActivity;
import com.coveros.coverosmobileapp.blogpost.BlogPostsListActivity;

import static android.content.ContentValues.TAG;

/**
 * Created by EPainter on 6/16/2017.
 * Provides Custom WebView Client that only loads Coveros-related content through WebView.
 * All externally related information is processed in a web browser
 */

public class CustomWebViewClient extends WebViewClient {

    private boolean weAreConnected;
    private MainActivity mainActivity;
    private final String TAG = "CustomWebViewClient";

    public CustomWebViewClient(MainActivity ma) { mainActivity = ma; weAreConnected = true; }
    public CustomWebViewClient() { weAreConnected = true; }

    public MainActivity getMainActivity() { return mainActivity; }
    public void setMainActivity(MainActivity ma) { mainActivity = ma; }
    public boolean getConnection(){ return weAreConnected; }
    public void setConnection(boolean answer){ weAreConnected = answer; }

    @SuppressWarnings("deprecation")
    @Override
    //Logs in this method
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //If blog website or blog webspage is categorically loaded (hybrid)
        boolean isBlogPost = false;
        String value = "";
        URLContent content = new URLContent(url);

        //Create new thread to handle network operations
        Thread th = new Thread(content);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        value = content.getHtmlClassName();

        //Only Blog posts have this body class name listed, Check off that the url is a Blog Post Link
        if(value.substring(0,21).equals("post-template-default")){
            isBlogPost = true;
        }

        //If a Blog Post was clicked on from the home page, redirect to native blog associated with post
        if(isBlogPost){
            //Index at 48 because each blog post has the same index length up until post id numbers
            //Post ID numbers could be n number of digits. Read values until space
            value = value.substring(48);
            String saveID = "";
            //Get only numbers after post id (stops when empty space is present)
            while(value.charAt(0) != ' '){
                saveID += value.charAt(0);
                value = value.substring(1);
            }
            //Start blog
            Intent startBlogPostRead = new Intent(view.getContext(), BlogPostReadActivity.class);
            int sendingPostID = Integer.parseInt(saveID);
            startBlogPostRead.putExtra("blogId", sendingPostID);
            view.getContext().startActivity(startBlogPostRead);
            return true;
        }
        //If blog website or blog web page is categorically loaded (hybrid)
        else if(url.contains("coveros.com/blog/") || url.contains("coveros.com/category/blogs/") ||
                url.contains("dev.secureci.com/blog/") || url.contains("dev.secureci.com/category/blogs/")){
            //Load Blog List
            Intent startBlogPost = new Intent(view.getContext(), BlogPostsListActivity.class);
            view.getContext().startActivity(startBlogPost);
            return true;
        }
        //Default stay in WebView (Recognizes url associated with Coveros content)
        else if (url.contains("coveros.com") || url.contains("dev.secureci.com")){
            view.loadUrl(url);
            return true;
        }
        //Otherwise, resort to WebView for external content
        else {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(i);
            return true;
        }
    }
    @Override
    public void onPageStarted(WebView view, String url, Bitmap fav){
        if(("https://www.coveros.com/blog/").equals(getMainActivity().getWebName())){
            getMainActivity().onBackPressed();
        }
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        getMainActivity().setWebName(url);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.e(TAG, "Error occurred while loading the web page at URL: " + request.getUrl().toString());
        //Load Blank Page - Could use html substitute error page here
        view.loadUrl("file:///android_asset/sampleErrorPage.html");
        super.onReceivedError(view, request, error);
    }
}
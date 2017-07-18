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

/**
 * Created by EPainter on 6/16/2017.
 * Provides Custom WebView Client that only loads Coveros-related content through WebView.
 * All externally related information is processed in a web browser
 */
class CustomWebViewClient extends WebViewClient {

    private boolean weAreConnected;
    private boolean isBlogPost;
    private String savedClassName;
    private int postID;
    private MainActivity mainActivity;
    private static final String TAG = "CustomWebViewClient";
  
    CustomWebViewClient(MainActivity ma) {
        mainActivity = ma;
        weAreConnected = true;
    }

    CustomWebViewClient() {
        weAreConnected = true;
    }

    MainActivity getMainActivity() {
        return mainActivity;
    }

    void setMainActivity(MainActivity ma) {
        mainActivity = ma;
    }

    boolean getConnection() {
        return weAreConnected;
    }

    void setConnection(boolean answer) {
        weAreConnected = answer;
    }

    boolean getIsBlogPost() {
        return isBlogPost;
    }

    void setIsBlogPost(Boolean blogPost) {
        isBlogPost = blogPost;
    }

    int getPostID() {
        return postID;
    }

    void setPostID(int newID) {
        postID = newID;
    }

    String getSavedClassName() {
        return savedClassName;
    }

    void setSavedClassName(String newValue) {
        savedClassName = newValue;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        //If blog website or blog web page is categorically loaded (hybrid)
        isBlogPost = false;
        String value;
        String url = request.getUrl().toString();
        URLContent content = new URLContent(url);

        //Create new thread to handle network operations
        Thread th = new Thread(content);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "Interruption Occurred with thread for URL Content");
            Thread.currentThread().interrupt();
        }
        value = content.getHtmlClassName();

        //Only Blog posts have this body class name listed, Check off that the url is a Blog Post Link
        checkIfBlogPost(value);

        //If a Blog Post was clicked on from the home page, redirect to native blog associated with post
        if (isBlogPost) {
            //Index at 48 because each blog post has the same index length up until post id numbers
            //Post ID numbers could be n number of digits. Read values until space
            value = value.substring(48);
            String saveID;
            //Get only numbers after post id (stops when empty space is present)
            StringBuilder builder = new StringBuilder();
            while(value.charAt(0) != ' '){
                builder.append(value.charAt(0));
                value = value.substring(1);
            }
            saveID = builder.toString();
            //Start individual blog
            Intent startBlogPostRead = new Intent(view.getContext(), BlogPostReadActivity.class);
            postID = Integer.parseInt(saveID);
            startBlogPostRead.putExtra("blogId", postID);
            view.getContext().startActivity(startBlogPostRead);
            return true;
        }
        //If blog website or blog web page is categorically loaded (hybrid)
        else if (url.contains("coveros.com/blog/") || url.contains("coveros.com/category/blogs/") ||
                url.contains("dev.secureci.com/blog/") || url.contains("dev.secureci.com/category/blogs/")) {
            //Load Blog List
            Intent startBlogPost = new Intent(view.getContext(), BlogPostsListActivity.class);
            view.getContext().startActivity(startBlogPost);
            return true;
        }
        //Default stay in WebView (Recognizes url associated with Coveros content)
        else if (url.contains("coveros.com") || url.contains("dev.secureci.com")) {
            view.loadUrl(url);
            return true;
        }
        //Otherwise, resort to Browser for external content
        else {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(i);
            return true;
        }
    }

    public void checkIfBlogPost(String value){
        if (value.length() >= 22 && ("post-template-default").equals(value.substring(0, 21))) {
            setSavedClassName(value.substring(0, 21));
            isBlogPost = true;
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap fav) {
        if (("https://www.coveros.com/blog/").equals(getMainActivity().getWebName())) {
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
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

    private boolean isBlogPost;
    private MainActivity mainActivity;

    private static final String TAG = "CustomWebViewClient";
    private static final String POST_ID_CLASS_PREFIX = "postid-";
  
    CustomWebViewClient(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        //If blog website or blog web page is categorically loaded (hybrid)
        isBlogPost = false;
        String classNames;
        String url = request.getUrl().toString();
        DocumentGenerator urlHtml = new DocumentGenerator(url);

        //Create new thread to handle network operations
        Thread th = new Thread(urlHtml);
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            Log.e(TAG, "Interruption Occurred with thread for URL Content");
            Thread.currentThread().interrupt();
        }
        classNames = urlHtml.getDocument().body().className();

        //Only Blog posts have this body class name listed, Check off that the url is a Blog Post Link
        isBlogPost = checkIsBlogPost(classNames);

        //If a Blog Post was clicked on from the home page, redirect to native blog associated with post
        if (isBlogPost) {
            String postId = parsePostId(classNames);

            //Start individual blog
            Intent startBlogPostRead = new Intent(view.getContext(), BlogPostReadActivity.class);
            startBlogPostRead.putExtra("blogId", Integer.parseInt(postId));
            view.getContext().startActivity(startBlogPostRead);
            return true;
        }
        //If blog website or blog web page is categorically loaded (hybrid)
        else if (checkIsBlog(url)) {
            //Load Blog List
            Intent startBlogPost = new Intent(view.getContext(), BlogPostsListActivity.class);
            view.getContext().startActivity(startBlogPost);
            return true;
        }
        //Default stay in WebView (Recognizes url associated with Coveros content)
        else if (checkIsCoveros(url)) {
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

    public boolean checkIsBlogPost(String value){
        if (value.contains(POST_ID_CLASS_PREFIX)) {
            return true;
        }
        return false;
    }

    public boolean checkIsBlog(String url) {
        final String[] blogUrls = {"coveros.com/blog/", "coveros.com/category/blogs/", "dev.secureci.com/blog/", "dev.secureci.com/category/blogs/"};
        for (String blogUrl : blogUrls) {
            if (url.contains(blogUrl)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIsCoveros(String url) {
        final String[] coverosUrls = {"coveros.com", "dev.secureci.com"};
        for (String coverosUrl : coverosUrls) {
            if (url.contains(coverosUrl)) {
                return true;
            }
        }
        return false;
    }

    public String parsePostId(String classNames) {
        // get index of class name that contains the post id
        int postIdIndex = classNames.indexOf(POST_ID_CLASS_PREFIX);
        // start substring at the post id number
        classNames = classNames.substring(postIdIndex + POST_ID_CLASS_PREFIX.length());

        String postId;
        // stop parsing at space character
        StringBuilder builder = new StringBuilder();
        while(classNames.charAt(0) != ' '){
            builder.append(classNames.charAt(0));
            classNames = classNames.substring(1);
        }
        postId = builder.toString();
        return postId;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap fav) {
        if (("https://www.coveros.com/blog/").equals(mainActivity.getWebName())) {
            mainActivity.onBackPressed();
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        mainActivity.setWebName(url);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.e(TAG, "Error occurred while loading the web page at URL: " + request.getUrl().toString());
        //Load Blank Page - Could use html substitute error page here
        view.loadUrl("file:///android_asset/sampleErrorPage.html");
        super.onReceivedError(view, request, error);
    }

    boolean getIsBlogPost() {
        return isBlogPost;
    }

}
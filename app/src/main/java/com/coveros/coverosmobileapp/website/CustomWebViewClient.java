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

import org.jsoup.nodes.Document;

/**
 * Created by EPainter on 6/16/2017.
 * Provides Custom WebView Client that only loads Coveros-related content through WebView.
 * All externally related information is processed in a web browser
 */
class CustomWebViewClient extends WebViewClient {

    private boolean isBlogPost;
    private MainActivity mainActivity;
    private int postId;
    private String classNames;

    private static final String TAG = "CustomWebViewClient";
    private static final String POST_ID_CLASS_PREFIX = "postid-";
    private static final String[] BLOG_URLS = {"coveros.com/blog/", "coveros.com/category/blogs/", "dev.secureci.com/blog/", "dev.secureci.com/category/blogs/"};
    private static final String[] COVEROS_URLS = {"coveros.com", "dev.secureci.com"};


    CustomWebViewClient(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, WebResourceRequest request) {
        //If blog website or blog web page is categorically loaded (hybrid)
        isBlogPost = false;
        final String url = request.getUrl().toString();

        DocumentGenerator urlHtml = new DocumentGenerator(url, new DocumentGenerator.DocumentGeneratorCallback() {
            @Override
            public void onDocumentReceived(Document document) {
                classNames = document.body().className();
                isBlogPost = checkIsBlogPost(classNames);

                view.post(new RedirectManager(view, url));

            }
        });

        // Create new thread to handle network operations
        Thread getUrlHtmlThread = new Thread(urlHtml);
        getUrlHtmlThread.start();

        return true;

    }

    public boolean checkIsBlogPost(String classNames){
        return classNames.contains(POST_ID_CLASS_PREFIX);
    }

    public boolean checkIsBlog(String url) {
        for (String blogUrl : BLOG_URLS) {
            if (url.contains(blogUrl)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIsCoveros(String url) {
        for (String coverosUrl : COVEROS_URLS) {
            if (url.contains(coverosUrl)) {
                return true;
            }
        }
        return false;
    }

    public String parsePostId(String classNamesToParse) {
        // get index of class name that contains the post id
        int postIdIndex = classNamesToParse.indexOf(POST_ID_CLASS_PREFIX);
        // start substring at the post id number
        String classNamesStartingAtPostId = classNamesToParse.substring(postIdIndex + POST_ID_CLASS_PREFIX.length());

        String parsedPostId;
        // stop parsing at space character
        StringBuilder builder = new StringBuilder();
        while(classNamesStartingAtPostId.charAt(0) != ' '){
            builder.append(classNamesStartingAtPostId.charAt(0));
            classNamesStartingAtPostId = classNamesStartingAtPostId.substring(1);
        }
        parsedPostId = builder.toString();
        return parsedPostId;
    }

    private class RedirectManager implements Runnable {
        WebView webView;
        String redirectUrl;

            private RedirectManager(WebView webView, String redirectUrl) {
                this.webView = webView;
                this.redirectUrl = redirectUrl;
            }
            @Override
            public void run() {
                //If a Blog Post was clicked on from the home page, redirect to native blog associated with post
                if (isBlogPost) {
                    postId = Integer.parseInt(parsePostId(classNames));

                    //Start individual blog
                    Intent startBlogPostRead = new Intent(webView.getContext(), BlogPostReadActivity.class);
                    startBlogPostRead.putExtra("blogId", postId);
                    webView.getContext().startActivity(startBlogPostRead);
                }
                //If blog website or blog web page is categorically loaded (hybrid)
                else if (checkIsBlog(redirectUrl)) {
                    //Load Blog List
                    Intent startBlogPost = new Intent(webView.getContext(), BlogPostsListActivity.class);
                    webView.getContext().startActivity(startBlogPost);
                }
                //Default stay in WebView (Recognizes url associated with Coveros content)
                else if (checkIsCoveros(redirectUrl)) {
                    webView.loadUrl(redirectUrl);
                }
                //Otherwise, resort to Browser for external content
                else {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
                    webView.getContext().startActivity(i);
                }
            }
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

    public int getPostId() {
        return postId;
    }

}
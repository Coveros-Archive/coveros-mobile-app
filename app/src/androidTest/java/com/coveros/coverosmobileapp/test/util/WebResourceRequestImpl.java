package com.coveros.coverosmobileapp.test.util;

import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete implementation of WebResourceRequest, which encompasses parameters to the
 * {@link WebViewClient#shouldInterceptRequest} method.
 */
public class WebResourceRequestImpl implements WebResourceRequest {

    private Uri uri;

    private String method;

    private Map<String, String> requestHeaders;

    public WebResourceRequestImpl(Uri uri) {
        this.uri = uri;
        method = "GET";
        requestHeaders = new HashMap<>();
    }

    @Override
    public Uri getUrl() {
        return uri;
    }

    @Override
    public boolean isForMainFrame() {
        return false;
    }

    @Override
    public boolean isRedirect() {
        return false;
    }

    @Override
    public boolean hasGesture() {
        return false;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }
}

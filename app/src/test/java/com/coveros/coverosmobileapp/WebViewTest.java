package com.coveros.coverosmobileapp;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by EPainter on 6/14/2017.
 */

public class WebViewTest {
    /*
    Check on Getter for WebName()
     */
    @Test
    public void check_GetWebsiteAddressName() throws Exception {
        String savedUrl;
        MainActivity ma = new MainActivity();
        savedUrl = ma.getWebName();
        assertEquals("https://www.coveros.com", savedUrl);
    }

    /*
    Check on Setter for WebName()
     */
    @Test
    public void check_SetWebsiteAddressName() throws Exception {
        String savedUrl = "http://www.html5rocks.com/";
        MainActivity ma = new MainActivity();
        ma.setWebName(savedUrl);
        assertEquals("http://www.html5rocks.com/", ma.getWebName());
    }

    /*
    Check if WebViewClient is used
     */
    @Test
    public void check_WebViewClientUsed() throws Exception {
        MainActivity ma = new MainActivity();
        WebViewClient br = new WebViewClient();
        assertEquals("android.webkit.WebViewClient@6a6824be", br.toString());
    }

    /*
    Check if browser is used
     */
    @Test
    public void check_BrowserUsed() throws Exception {
        MainActivity ma = new MainActivity();
        WebViewClient wv = new WebViewClient();
        assertNotEquals("android.webkit.WebChromeClient@6a6824be", wv.toString());
    }
}
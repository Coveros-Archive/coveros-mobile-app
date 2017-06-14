package com.coveros.coverosmobileapp;

import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by EPainter on 6/14/2017.
 */

public class WebViewTest {

    //MainActivity.java Tests
    @Test
    public void check_GetWebsiteAddressName() throws Exception {
        String savedUrl;
        MainActivity ma = new MainActivity();
        savedUrl = ma.getWebName();
        assertEquals("https://www.coveros.com", savedUrl);
    }

    @Test
    public void check_SetWebsiteAddressName() throws Exception {
        String savedUrl = "http://www.html5rocks.com/";
        MainActivity ma = new MainActivity();
        ma.setWebName(savedUrl);
        assertEquals("http://www.html5rocks.com/", ma.getWebName());
    }

    @Test
    public void check_WebViewClientUsed() throws Exception {
        String answer;
        MainActivity ma = new MainActivity();
        WebViewClient br = new WebViewClient();
        if(br instanceof WebViewClient){
            answer = "WebViewClient";
        }
        else{
            answer = "WebChromeClient";
        }
        assertEquals("WebViewClient", answer);
    }

    @Test
    public void check_BrowserUsed_1() throws Exception {
        String answer;
        MainActivity ma = new MainActivity();
        WebViewClient wv = new WebViewClient();
        if(wv instanceof WebViewClient){
            answer = "WebViewClient";
        }
        else{
            answer = "WebChromeClient";
        }
        assertNotEquals("WebViewClient", answer);
    }

    @Test
    public void check_BrowserUsed_2() throws Exception{
        String answer;
        MainActivity ma = new MainActivity();
        WebChromeClient wcc = new WebChromeClient();
        if(wcc instanceof WebChromeClient){
            answer = "WebChromeClient";
        }
        else{
            answer = "WebViewClient";
        }
        assertEquals("WebChromeClient", answer);
    }
}
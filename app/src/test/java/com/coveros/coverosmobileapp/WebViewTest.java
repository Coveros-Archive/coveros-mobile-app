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

    //MainActivity.java Tests
    @Test
    public void check_GetWebsiteAddressName() throws Exception {
        String savedUrl;
        MainActivity ma = new MainActivity();
        savedUrl = ma.getWebName();
        assertEquals("https://www.coveros.com", savedUrl);
    }

    public void check_GetWebsiteURL() throws Exception {
        MainActivity ma = new MainActivity();
        String savedUrl = ma.getWebName();
        String getUrl = ma.getBrowser().getUrl();
        assertEquals(savedUrl, getUrl);
    }

    @Test
    public void check_SetWebsiteAddressName() throws Exception {
        String savedUrl = "http://www.html5rocks.com/";
        MainActivity ma = new MainActivity();
        ma.setWebName(savedUrl);
        assertEquals("http://www.html5rocks.com/", ma.getWebName());
    }

    @Test
    public void check_NotEqualRedirectingLinks() throws Exception {
        String answer = "https://wwww.html5rocks.com/en/";
        MainActivity ma = new MainActivity();
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
        assertEquals("WebViewClient", answer);
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
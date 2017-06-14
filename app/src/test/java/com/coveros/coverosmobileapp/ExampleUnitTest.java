package com.coveros.coverosmobileapp;

import android.webkit.WebView;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void check_GetWebsiteAddressName() throws Exception {
        String savedUrl = new String();
        MainActivity ma = new MainActivity();
        savedUrl = ma.getWebName();
        assertEquals("https://www.coveros.com", savedUrl);
    }

    @Test
    public void check_SetWebsiteAddressName() throws Exception {
        //Tests redirecting of links (Browser or WebView Access)
        String savedUrl = "http://www.html5rocks.com/";
        MainActivity ma = new MainActivity();
        ma.setWebName(savedUrl);
        assertEquals("http://www.html5rocks.com/", ma.getWebName());
    }

    @Test
    public void check_WebViewClientUsed() throws Exception {
        String answer = new String();
        MainActivity ma = new MainActivity();
        WebView br = ma.getBrowser();
        assertEquals("WebViewClient", answer);
    }

    @Test
    public void check_BrowserUsed() throws Exception {
        String answer = new String();
        MainActivity ma = new MainActivity();
        WebView wv = ma.getBrowser();
    }

    @Test
    public void check_BrowserOrWebViewUsed_1() throws Exception {
        //Browser used

    }

    @Test
    public void check_BrowserOrWebViewUsed_2() throws Exception {
        //WebView Client used

    }

    @Test
    public void check_isOnline() throws Exception {

    }

    @Test
    public void check_NoServerConnection() throws Exception {

        //How can I test this?
    }

    @Test
    public void check_WebViewAfterRedirecting() throws Exception {

    }
}
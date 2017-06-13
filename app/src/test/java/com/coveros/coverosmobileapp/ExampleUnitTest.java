package com.coveros.coverosmobileapp;

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
        //This website automatically redirects
        //Tests redirecting of links (Browser or WebView Access)
        String savedUrl = "http://www.html5rocks.com/";
        MainActivity ma = new MainActivity();
        ma.setWebName(savedUrl);
    }

    @Test
    public void check_WebViewClientUsed() throws Exception {

    }

    @Test
    public void check_NoInternetError() throws Exception {

    }

    @Test
    public void check_NoServerConnection() throws Exception {

        //How can I test this?
    }

    @Test
    public void check_WebViewAfterRedirecting() throws Exception {

    }
}

package com.coveros.coverosmobileapp;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        //Tests redirecting of links (Browser or WebView Access)
        String savedUrl = "http://www.html5rocks.com/";
        MainActivity ma = new MainActivity();
        ma.setWebName(savedUrl);
        assertEquals("http://www.html5rocks.com/", ma.getWebName());
    }

    @Test
    public void check_WebViewClientUsed() throws Exception {
        String answer;
        MainActivity ma = new MainActivity();
        WebViewClient br = new NoErrorWebViewClient();
        if(br instanceof NoErrorWebViewClient){
            answer = "NoErrorWebViewClient";
        }
        else if(br instanceof WebViewClient){
            answer = "WebViewClient";
        }
        else{
            //Default string answer
            answer = "null";
        }
        assertEquals("NoErrorWebViewClient", answer);
    }

    @Test
    public void check_BrowserUsed() throws Exception {
        String answer;
        MainActivity ma = new MainActivity();
        WebViewClient wv = new WebViewClient();
        if(wv instanceof NoErrorWebViewClient){
            answer = "NoErrorWebViewClient";
        }
        else if(wv instanceof WebViewClient){
            answer = "WebViewClient";
        }
        else{
            //Default string answer
            answer = "null";
        }
        assertEquals("WebViewClient", answer);
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

    @Test
    public void check_onBackPressed_1(){

    }

    @Test
    public void check_onBackPressed_2(){


    }

    @Test
    public void check_onBackPressed_3(){


    }

    @Test
    public void check_alertView_1(){


    }

    @Test
    public void check_alertView_2(){


    }

    @Test
    public void check_alertView_3(){

    }

    @Test
    public void check_alertView_4(){


    }

    //NoErrorWebViewClient.java Tests
    public void check_ErrorTesting_1(){


    }

    public void check_ErrorTesting_2(){


    }

    public void check_ErrorTesting_3(){


    }
}
package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by EPainter on 6/14/2017.
 */

public class InstrumentedWebViewTest{

    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void check_ErrorTesting_1(){
        Activity act = mMainActivity.getActivity();
        WebView web = (WebView) act.findViewById(R.id.activity_main_webview);
        mMainActivity.getActivity().setBrowser(web);
        WebViewClient nec = new WebViewClient();
        web.setWebViewClient(nec);

        int errorCode = -8;
        String problem = "This thing doesn't work";
        String failedUrl = "https://This.Is.My.Link.com/";

        nec.onReceivedError(web, errorCode, problem, failedUrl);
        assertEquals(-8, errorCode);
    }

    @Test
    public void check_ErrorTesting_2(){
        Activity act = mMainActivity.getActivity();
        WebView web = (WebView) act.findViewById(R.id.activity_main_webview);
        mMainActivity.getActivity().setBrowser(web);
        WebViewClient nec = new WebViewClient();
        web.setWebViewClient(nec);

        int errorCode = -6;
        String problem = "This thing doesn't work";
        String failedUrl = "https://This.Is.My.Link.com/";

        nec.onReceivedError(web, errorCode, problem, failedUrl);
        assertEquals(-6, errorCode);

    }

    @Test
    public void check_ErrorTesting_3(){
        Activity act = mMainActivity.getActivity();
        WebView web = (WebView) act.findViewById(R.id.activity_main_webview);
        mMainActivity.getActivity().setBrowser(web);
        WebViewClient nec = new WebViewClient();
        web.setWebViewClient(nec);

        int errorCode = 106;
        String problem = "This thing doesn't work";
        String failedUrl = "https://This.Is.My.Link.com/";

        nec.onReceivedError(web, errorCode, problem, failedUrl);
        assertEquals(106, errorCode);

    }

    @Test
    public void check_BrowserOrWebViewUsed_1() throws Exception {
        //Browser used
        Activity act = mMainActivity.getActivity();
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
}

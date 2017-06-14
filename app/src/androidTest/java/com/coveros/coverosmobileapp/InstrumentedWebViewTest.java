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

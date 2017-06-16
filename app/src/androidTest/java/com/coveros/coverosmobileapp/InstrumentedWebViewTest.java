package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

/**
 * Created by EPainter on 6/14/2017.
 */



@RunWith(AndroidJUnit4.class)
public class InstrumentedWebViewTest{

    @BeforeClass
    public static void setUp(){
        Looper.prepare();
    }

    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void check_testRun() throws Exception{
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
    }

    @Test
    public void check_isOnlineTrue() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        boolean theTruth = newMain.isOnline();
        assertEquals(true,theTruth);
    }

    @Test
    public void check_isOnlineFalse() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        //Change Internet connectivity

        newMain.isOnline();
    }

    @Test
    public void check_NoServerConnection() throws Exception {

        //How can I test this?
    }

    @Test
    public void check_alertView_1() throws Exception{
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(MainActivity.class.getName(), null, false);
        MainActivity myAct = (MainActivity) monitor.waitForActivity();
        AlertDialog alert = myAct.getDialog();

        //Assert that alertDialog box can be opened
    }

    @Test
    public void check_alertView_2() throws Exception{


        //Assert that alert button.POSITIVE works (Exit App)
    }

    @Test
    public void check_alertView_3() throws Exception{


        //Assert that alert button.NEUTRAL works (OK)
    }

    @Test
    public void check_alertView_4() throws Exception{


        //Assert that alert button.NEGATIVE works (Reload App)
    }
}

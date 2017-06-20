package com.coveros.coverosmobileapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Looper;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
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

    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    AlertDialog mAlertDialog;

    @Test
    public void check_testRun() throws Exception{
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
    }

    //isOnline tests -> INTERNET IS CURRENTLY CONNECTED
    //MUST MANUALLY SWITCH OFF INTERNET IN EMULATOR FOR CHANGES
    @Test
    public void check_isOnlineTrue() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        boolean theTruth = newMain.isOnline();
        if(newMain.isOnline()){ theTruth = true; }
        else { theTruth = false; }
        assertEquals(true,theTruth);
    }

    @Test
    public void check_AlertShowingWithInternet(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        mAlertDialog = new AlertDialog.Builder(mMainActivity.getActivity()).create();
        boolean workingAsIntended;
        if(mAlertDialog.isShowing()){
            workingAsIntended = false;
            mAlertDialog.dismiss();
        }
        else{
            workingAsIntended = true;
        }
        assertEquals(true,workingAsIntended);
    }

    @Test
    //Check Alert Dialog runs
    public void check_alertView_1() throws Exception{
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        mAlertDialog = new AlertDialog.Builder(mMainActivity.getActivity()).create();
    }

    @Test
    //Check strings
    public void check_alertView_2() throws Exception{
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        mAlertDialog = new AlertDialog.Builder(mMainActivity.getActivity()).create();

    }

    @Test
    public void check_alertView_3() throws Exception{
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        mAlertDialog = new AlertDialog.Builder(mMainActivity.getActivity()).create();
        //Assert that alert button.NEUTRAL works (OK)
    }

    @Test
    public void check_alertView_4() throws Exception{
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        mAlertDialog = new AlertDialog.Builder(mMainActivity.getActivity()).create();
        //Assert that alert button.NEGATIVE works (Reload App)

    }
}

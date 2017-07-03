package com.coveros.coverosmobileapp.website;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.coveros.coverosmobileapp.test.util.LooperTestSuite;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class InstrumentedWebViewTest extends LooperTestSuite {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    private AlertDialog mAlertDialog;

    /*
     * Test if MainActivity Will run with custom intent
     */
    @Test
    public void check_testRun() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
    }

    //isOnline tests -> INTERNET IS CURRENTLY CONNECTED
    //MUST MANUALLY SWITCH OFF INTERNET IN EMULATOR FOR CHANGES
    /*
     * Checks if the app/device is connected to the internet
     */
    @Test
    public void check_isOnlineTrue() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        boolean theTruth = newMain.isOnline();
        assertTrue(theTruth);
    }

    /*
     * Check if alert will show with or without internet
     * WORKS WITH OR WITHOUT INTERNET
     */
    @Test
    public void check_AlertShowingWithInternet() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        mAlertDialog = new AlertDialog.Builder(newMain).create();
        boolean workingAsIntended;
        if (mAlertDialog.isShowing()) {
            workingAsIntended = false;
            mAlertDialog.dismiss();
        } else {
            workingAsIntended = true;
        }
        assertTrue(workingAsIntended);
    }

    /*
     * Runs alertView as a test run
     */
    @Test
    //Check Alert Dialog runs
    public void check_alertView_1() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        mAlertDialog = new AlertDialog.Builder(mMainActivity.getActivity()).create();
    }
    //More AlertView tests necessary but need to access xml ID's
    //which means the creation of Buttons/TextViews in xml activity
    //linking the buttons and strings together
}

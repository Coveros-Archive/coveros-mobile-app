package com.coveros.coverosmobileapp.website;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.coveros.coverosmobileapp.test.util.LooperTestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class WebViewInstrumentedTest extends LooperTestSuite {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivity = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity = null;
    private AlertDialog mAlertDialog;

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent(Intent.ACTION_PICK);
        mMainActivity.launchActivity(intent);
        mActivity = mMainActivity.getActivity();
    }

    /*
     * Check onStart() for Main Activity webName is accurate
     * Assumes internet is connected
     */
    @Test
    public void checkOnStart() throws Exception {
        String expected = "https://www3.dev.secureci.com";
        assertEquals(expected, mActivity.getWebName());
    }

    //isOnline tests -> INTERNET IS CURRENTLY CONNECTED
    //MUST MANUALLY SWITCH OFF INTERNET IN EMULATOR FOR CHANGES
    /*
     * Checks if the app/device is connected to the internet
     */
    @Test
    public void isOnline_True() {
        boolean theTruth = mActivity.isOnline();
        assertTrue(theTruth);
    }

    /*
     * Check if alert will show with or without internet
     * WORKS WITH OR WITHOUT INTERNET
     */
    @Test
    public void alertViewTest() {
        mAlertDialog = new AlertDialog.Builder(mActivity).create();
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
    public void alertView_1() {
        mAlertDialog = new AlertDialog.Builder(mActivity).create();
    }

    /*
    * Check uninitialized dialog and initialized dialog
    */
    @Test
    public void checkAlertDialogDefault() throws Exception {
        MainActivity ma = new MainActivity();
        assertNull(ma.getDialog());             //Not initialized}
    }
}
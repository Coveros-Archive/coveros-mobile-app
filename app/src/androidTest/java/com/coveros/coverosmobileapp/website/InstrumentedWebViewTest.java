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

import static junit.framework.Assert.assertNull;
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
    public void testRun_MainActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
    }

    //isOnline tests -> INTERNET IS CURRENTLY CONNECTED
    //MUST MANUALLY SWITCH OFF INTERNET IN EMULATOR FOR CHANGES
    /*
     * Checks if the app/device is connected to the internet
     */
    @Test
    public void isOnline_True() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        boolean theTruth = newMain.isOnline();
        assertTrue(theTruth);
    }

    /*
     * check on OnBackPressed button (natively a part of the emulator/phone)
     */
    @Test
    public void checkOnBackPressed() throws Exception{
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
    }

    /*
     * Check if alert will show with or without internet
     * WORKS WITH OR WITHOUT INTERNET
     */
    @Test
    public void alertViewTest() {
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
    public void alertView_1() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        MainActivity newMain = mMainActivity.launchActivity(intent);
        mAlertDialog = new AlertDialog.Builder(mMainActivity.getActivity()).create();
    }

    /*
    * Check uninitialized dialog and initialized dialog
    */
    @Test
    public void checkAlertDialogDefault() throws Exception {
        MainActivity ma = new MainActivity();
        assertNull(ma.getDialog());             //Not initialized}
    }

    /*
     * Check on alertView Neutral button press (Reload App)
     */
    @Test
    public void checkAlertDialog_ReloadApp() throws Exception{
        MainActivity mainActivity = new MainActivity();

    }

    /*
     * Check on alertView Negative button press (OK)
     */
    @Test
    public void checkAlertDialog_Ok() throws Exception {
        MainActivity mainActivity = new MainActivity();

    }

    /*
     * Check on alertView Positive Button press (Exit App)
     */
    @Test
    public void checkAlertDialog_Exit() throws Exception {
        MainActivity mainActivity = new MainActivity();

    }

    /*
     * Check Custom Client string for html class name
     */
    @Test
    public void checkCustomClient_HTMLClassName(){


    }

    /*
     * Check Custom Client for Post ID (if Blog Post)
     */
    @Test
    public void checkCustomClient_PostID(){


    }

    /*
     * Check Custom Client if Post ID exists, check blog post from BlogPostReadActivity
     */
    @Test
    public void checkCustomClient_BlogPostReadActivityOpens(){


    }

    /*
     * Check Custom Client loaded URL contains keyword "blog" loads BlogPostListActivity
     */
    @Test
    public void checkCustomClient_LoadsBlogPostListActivity_Blog(){


    }

    /*
     * Check Custom Client loaded URL contains keyword "/category/blogs/"
     */
    @Test
    public void checkCustomClient_LoadsBlogPostListActivity_CategoryBlogs(){


    }

    /*
     * Check Custom Client loaded URL contains coveros related content
     */
    @Test
    public void checkCustomClient_CoverosURL(){


    }

    /*
     * Check Custom Client loaded URL contains dev site url
     */
    @Test
    public void checkCustomClient_SecureCI(){


    }

    /*
     * Check Custom Client loaded external content (Use Browser)
     */
    public void checkCustomClient_ExternalContent(){


    }

    /*
     * Check on Page Started
     * User clicks on new page. Loading as expected
     */
    @Test
    public void checkCustomClient_onPageStarted(){

    }

    /*
     * Check on Page Started
     * User clicks on blogs link. Should load page before clicking on Blogs
     */
    @Test
    public void checkCustomClient_LoadPageBeforeBlogs(){


    }

    /*
     * Check that webName is set on onPageFinished()
     */
    @Test
    public void checkCustomClient_NewWebName(){


    }

    /*
     * Check that onReceivedError loads error page && errorFound
     */
    @Test
    public void checkCustomClient_NewError(){


    }
}

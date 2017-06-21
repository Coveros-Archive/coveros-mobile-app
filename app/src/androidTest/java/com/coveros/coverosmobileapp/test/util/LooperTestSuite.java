package com.coveros.coverosmobileapp.test.util;

import android.os.Looper;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Setup and tear down the main thread for {@link android.os.Looper}. This allows callbacks to be
 * individually tested without requiring a sophisticated mock setup.
 */
public class LooperTestSuite {

    /**
     * Prepare the main thread to be a "looper thread".
     */
    @BeforeClass
    public static void prepareLooper() {
        Looper.prepare();
    }

    /**
     * Get the Looper instance from ThreadLocal and end it, so that other test suites can reinitialize
     * if they need to.
     */
    @AfterClass
    public static void exitLooper() {
        Looper mainLooper = Looper.myLooper();
        if (mainLooper != null) {
            mainLooper.quit();
        }
    }
}

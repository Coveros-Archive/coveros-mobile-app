package com.coveros.coverosmobileapp.test.util;

import android.os.Looper;

import org.junit.BeforeClass;

/**
 * Setup the main thread to be a {@link android.os.Looper} thread. This allows callbacks to be
 * individually tested without requiring a sophisticated mock setup.
 */
public class LooperTestSuite {

    /**
     * Prepare the main thread to be a "looper thread".
     */
    @BeforeClass
    public static void prepareLooper() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
    }
}

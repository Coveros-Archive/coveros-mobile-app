package com.coveros.coverosmobileapp;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.android.volley.Response;

/**
 * Created by Maria on 6/15/2017.
 */

public interface InterfacePostActivity {

    Response.ErrorListener getErrorListener(final Context context);
    AlertDialog getErrorMessage();

}

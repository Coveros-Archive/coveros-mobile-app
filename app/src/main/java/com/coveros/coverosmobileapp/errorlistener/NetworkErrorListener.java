package com.coveros.coverosmobileapp.errorlistener;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class NetworkErrorListener implements Response.ErrorListener {

    private Context context;
    private AlertDialog networkErrorAlertDialog;

    public NetworkErrorListener(Context context, AlertDialog networkErrorAlertDialog) {
        this.context = context;
        this.networkErrorAlertDialog = networkErrorAlertDialog;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {

        if (!((Activity) context).isFinishing()) {
            networkErrorAlertDialog.show();
        }

        NetworkResponse errorNetworkResponse = volleyError.networkResponse;
        String errorData = "";
        if (errorNetworkResponse != null && errorNetworkResponse.data != null) {
            errorData = new String(errorNetworkResponse.data);
        }
        Log.e("NetworkErrorListener", errorData);
    }
}

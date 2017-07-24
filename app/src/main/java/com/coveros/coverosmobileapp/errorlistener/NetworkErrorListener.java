package com.coveros.coverosmobileapp.errorlistener;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.dialog.AlertDialogFactory;

public class NetworkErrorListener implements Response.ErrorListener {

    private Context context;
    private String errorAlertDialogMessage;
    private AlertDialog errorAlertDialog;

    public NetworkErrorListener(Context context, String errorAlertDialogMessage, AlertDialog errorAlertDialog) {
        this.context = context;
        this.errorAlertDialogMessage = errorAlertDialogMessage;
        this.errorAlertDialog = errorAlertDialog;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        errorAlertDialog.show();

        NetworkResponse errorNetworkResponse = volleyError.networkResponse;
        String errorData = "";
        if (errorNetworkResponse != null && errorNetworkResponse.data != null) {
            errorData = new String(errorNetworkResponse.data);
        }
        Log.e("NetworkErrorListener", errorData);
    }
}

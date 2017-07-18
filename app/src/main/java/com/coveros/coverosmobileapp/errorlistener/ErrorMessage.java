package com.coveros.coverosmobileapp.errorlistener;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.R;

public class ErrorMessage implements Response.ErrorListener {

    private AlertDialog errorMessageDialog;
    private Context context;

    public ErrorMessage(Context context) {
        this.context = context;
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        // creates and displays errorMessage
        errorMessageDialog = createErrorMessage();
        errorMessageDialog.show();
        NetworkResponse errorNetworkResponse = volleyError.networkResponse;
        String errorData = "";
        if (errorNetworkResponse != null && errorNetworkResponse.data != null) {
            errorData = new String(errorNetworkResponse.data);
        }
        Log.e("Volley error", errorData);
    }

    private AlertDialog createErrorMessage() {
        AlertDialog errorMessageToCreate = new AlertDialog.Builder(context).create();
        errorMessageToCreate.setTitle(context.getString(R.string.error_message_title));
        errorMessageToCreate.setMessage(context.getString(R.string.error_message_message));
        errorMessageToCreate.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.error_message_dismiss_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return errorMessageToCreate;
    }
}

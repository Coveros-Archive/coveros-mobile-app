package com.coveros.coverosmobileapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * Provides
 * Created by Maria Kim on 6/12/2017.
 */

class PostActivity {

    Response.ErrorListener getErrorListener(Context context) {
        Response.ErrorListener responseListener = new Response.ErrorListener() {
            // logs error
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // creates error message to be displayed to user
                AlertDialog errorMessage = new AlertDialog.Builder(context).create();
                errorMessage.setTitle("Error");
                errorMessage.setMessage("An error occurred.");
                errorMessage.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                errorMessage.show();

                Log.e("Volley error", "" + volleyError.networkResponse.statusCode);
            }
        };
        return responseListener;
    }

    static AlertDialog createErrorMessage(final Context context) {
        AlertDialog errorMessage = new AlertDialog.Builder(context).create();
        errorMessage.setTitle("Error");
        errorMessage.setMessage("An error occured.");
        errorMessage.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        context.getActivity().finish();
                    }
        });
        return errorMessage;
    }


}

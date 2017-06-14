package com.coveros.coverosmobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Response;

/**
 * Provides
 * Created by Maria Kim on 6/12/2017.
 */

class AbstractPostActivity extends AppCompatActivity {

    AlertDialog errorMessage;

    protected Response.ErrorListener getErrorListener(Context context) {

       // logs error
        return volleyError -> {
            // creates error message to be displayed to user
            errorMessage = new AlertDialog.Builder(context).create();
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
            if (errorMessage.isShowing()) {
                Log.d("Error message", "Is showing!!!!!!!!!!!!");
            }

            Log.e("Volley error", ""+ volleyError.networkResponse.statusCode);

        };
    }

    protected AlertDialog getErrorMessage() {
        return errorMessage;
    }

}

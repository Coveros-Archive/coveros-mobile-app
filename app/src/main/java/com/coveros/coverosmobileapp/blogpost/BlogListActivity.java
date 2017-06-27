package com.coveros.coverosmobileapp.blogpost;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * @author Maria Kim
 */

public class BlogListActivity extends ListActivity {

    AlertDialog errorMessage;
    Response.ErrorListener errorListener;

    Response.ErrorListener createErrorListener(final Context context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // creates and displays errorMessage
                errorMessage = createErrorMessage(context);
                errorMessage.show();
                NetworkResponse errorNetworkResponse = volleyError.networkResponse;
                String errorData = "";
                if (errorNetworkResponse != null && errorNetworkResponse.data != null) {
                    errorData = new String(errorNetworkResponse.data);
                }
                Log.e("Volley error", errorData);
            }
        };
    }

    AlertDialog createErrorMessage(Context context){
        AlertDialog errorMessage = new AlertDialog.Builder(context).create();
        errorMessage.setTitle("Error");
        errorMessage.setMessage("An error occurred.");
        errorMessage.setButton(AlertDialog.BUTTON_NEUTRAL, "Okay",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        return errorMessage;
    }

    TextView createTextViewLabel(Context context, String label) {
        TextView textViewLabel = new TextView(context);
        textViewLabel.setText(label);
        textViewLabel.setTextSize(20);
        textViewLabel.setPadding(0,0,0,30);
        return textViewLabel;
    }

}

package com.coveros.coverosmobileapp.blogpost;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.coveros.coverosmobileapp.R;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * @author Maria Kim
 */

public class BlogListActivity extends ListActivity {

    AlertDialog errorMessage;
    Response.ErrorListener errorListener;

    private static final float textViewTextSize = 60;
    private static final int textViewPaddingBottom = 30;

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
        errorMessage.setTitle(context.getString(R.string.error_message_title));
        errorMessage.setMessage(context.getString(R.string.error_message_message));
        errorMessage.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.error_message_dismiss_button),
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
        textViewLabel.setTextSize(COMPLEX_UNIT_PX, textViewTextSize);
        textViewLabel.setPadding(0,0,0,textViewPaddingBottom);
        return textViewLabel;
    }

    static float getTextViewTextSize() { return textViewTextSize; }
    static int getTextViewPaddingBottom() { return textViewPaddingBottom; }
    AlertDialog getErrorMessage() { return errorMessage; }

}

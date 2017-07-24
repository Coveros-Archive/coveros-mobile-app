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
import com.coveros.coverosmobileapp.R;
import com.coveros.coverosmobileapp.dialog.AlertDialogFactory;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * @author Maria Kim
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class BlogListActivity extends ListActivity {

    private static final float TEXT_VIEW_TEXT_SIZE = 60;
    private static final int TEXT_VIEW_PADDING_BOTTOM = 30;

    AlertDialog errorAlertDialog;
    Response.ErrorListener errorListener;

    TextView createTextViewLabel(Context context, String label) {
        TextView textViewLabel = new TextView(context);
        textViewLabel.setText(label);
        textViewLabel.setTextSize(COMPLEX_UNIT_PX, TEXT_VIEW_TEXT_SIZE);
        textViewLabel.setPadding(0, 0, 0, TEXT_VIEW_PADDING_BOTTOM);
        return textViewLabel;
    }

    static float getTextViewTextSize() {
        return TEXT_VIEW_TEXT_SIZE;
    }

    static int getTextViewPaddingBottom() {
        return TEXT_VIEW_PADDING_BOTTOM;
    }

    AlertDialog getErrorAlertDialog() {
        return errorAlertDialog;
    }
}

package com.coveros.coverosmobileapp.dialog;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.coveros.coverosmobileapp.R;

/**
 * @author Maria Kim
 */

public class AlertDialogFactory {


    public static AlertDialog createSuccessAlertDialogDefaultButton(Context context, String message) {
        final String title = context.getString(R.string.success_title);
        final String buttonText = context.getString(R.string.success_button);

        return createAlertDialog(context, title, message, buttonText, false);
    }

    public static AlertDialog createSuccessAlertDialogFinishButton(Context context, String message) {
        final String title = context.getString(R.string.success_title);
        final String buttonText = context.getString(R.string.success_button);

        return createAlertDialog(context, title, message, buttonText, true);
    }

    public static AlertDialog createErrorAlertDialogDefaultButton(Context context, String message) {
        final String title = context.getString(R.string.error_title);
        final String buttonText = context.getString(R.string.error_button);

        return createAlertDialog(context, title, message, buttonText, false);
    }

    public static AlertDialog createNetworkErrorAlertDialogDefaultButton(Context context, String message) {
        final String title = context.getString(R.string.error_title);
        final String buttonText = context.getString(R.string.error_button);
        final String tryAgain = context.getString(R.string.try_again_error_message);

        return createAlertDialog(context, title, message + " " + tryAgain, buttonText, false);
    }

    public static AlertDialog createNetworkErrorAlertDialogFinishButton(final Context context, String message) {
        final String title = context.getString(R.string.error_title);
        final String buttonText = context.getString(R.string.error_button);
        final String tryAgain = context.getString(R.string.try_again_error_message);

        return createAlertDialog(context, title, message + " " + tryAgain, buttonText, true);
    }

    /**
     * Returns AlertDialog with one button that dismisses the AlertDialog and, if flagged, finishes the activity.
     * @param context   context on which button will be displayed
     * @param title    title of AlertDialog
     * @param message    message of AlertDialog
     * @param buttonText    text of button of AlertDialog
     * @return
     */
    private static AlertDialog createAlertDialog(final Context context, String title, String message, String buttonText, final boolean finishOnDismiss) {
        return new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setNeutralButton(buttonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (finishOnDismiss) {
                        ((Activity) context).finish();
                    }
                }
            }).create();
    }

    /**
     * Returns AlertDialog without button set (so that it can be customized).
     * @param context
     * @param title
     * @param message
     * @return
     */
    private static AlertDialog createAlertDialogCustomButton(Context context, String title, String message) {
        return new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .create();
    }

}

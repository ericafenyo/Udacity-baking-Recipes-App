/*
 * Copyright (C) 2017 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eric.bakingrecipes.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.eric.bakingrecipes.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by eric on 08/11/2017.
 * stocks of helper methods and classes
 */

public final class N {
    private static final String DSK = "SKey";

    /**
     * @param context Context
     * @param message text to show as toast
     */
    public static void toast(Context context, Object message) {
        Toast.makeText(context, String.valueOf(message), Toast.LENGTH_SHORT).show();
    }

    /**
     * @param message text to be printed in Logcat
     */
    public static void log(Object message) {
        Log.v("Response from N", String.valueOf(message));
    }

    /**
     * @param view    main text to display
     * @param message parent view example coordinate layout
     */
    public static void makeSnack(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * create snackBar with action button
     *
     * @param view            parent view example coordinate layout
     * @param message         main text to display
     * @param actionMessage   Action button text
     * @param onClickListener View.OnClickListener
     */
    public static void makeSnackAction(View view, String message, String actionMessage, View.OnClickListener onClickListener) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction(actionMessage, onClickListener)
                .setDuration(3000)
                .show();
    }

    /**
     * stores an int value in a form of a key:value data pair
     *
     * @param context Context
     * @param key     the SharedPreference Key that points to a related value
     * @param value   the value to be stored
     */
    public static void storeSLPreferences(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(DSK, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * retrieves a value using a SharedPreference key
     *
     * @param context Context
     * @param key     points to a related value
     * @param deft    a default value to be returned if the returned value is null
     * @return an int value
     */
    public static int getSLPreferences(Context context, String key, int deft) {
        SharedPreferences preferences = context.getSharedPreferences(DSK, MODE_PRIVATE);
        return preferences.getInt(key, deft);
    }


    /**
     * @param url Url
     * @return raw json data
     * @throws IOException
     */
    public static String getHttpResponse(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            connection.disconnect();
        }
    }

    /**
     * returns a boolean based on the internet connectivity of the device
     * @param context Context
     * @return true if device is connected to internet
     */
    public static boolean checkConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * AlertDialog
     */
    public static class NoticeDialogFragment extends DialogFragment {
        /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
        public interface NoticeDialogListener {
            void onDialogPositiveClick(DialogFragment dialog);

            void onDialogNegativeClick(DialogFragment dialog);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Build the dialog and set up the button click handlers
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_message)
                    .setPositiveButton(R.string.dialog_pos_action, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Instantiate the NoticeDialogListener so we can send events to the host
                            NoticeDialogListener mListener = (NoticeDialogListener) getTargetFragment();
                            // Send the positive button event back to the host activity
                            mListener.onDialogPositiveClick(NoticeDialogFragment.this);
                        }
                    })
                    .setNegativeButton(R.string.dialog_neg_action, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Instantiate the NoticeDialogListener so we can send events to the host
                            NoticeDialogListener mListener = (NoticeDialogListener) getTargetFragment();
                            // Send the negative button event back to the host activity
                            mListener.onDialogNegativeClick(NoticeDialogFragment.this);
                        }
                    });
            return builder.create();
        }
    }
}
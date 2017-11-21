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
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.eric.bakingrecipes.R;
import com.example.eric.bakingrecipes.RecipesAppWidget;

/**
 * Created by eric on 08/11/2017.
 * stocks of helper methods and classes
 * TODO: comment on methods
 */

public final class L {



    public static void toast(Context context, Object message) {
        Toast.makeText(context, String.valueOf(message), Toast.LENGTH_SHORT).show();
    }

    public static void log(Object message) {
        Log.v("Response from L", String.valueOf(message));
    }

    public  static void makeSnack(View view, String message){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT).show();
    }

    public static void makeSnackAction(View view, String message, String actionMessage, View.OnClickListener onClickListener){
        Snackbar.make(view,message,Snackbar.LENGTH_SHORT)
                .setAction(actionMessage,onClickListener)
                .setDuration(3000)
                .show();
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
                            // this will send the broadcast to update the appwidget
                            RecipesAppWidget.sendRefreshBroadcast(getActivity());
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
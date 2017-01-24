package com.example.tgzoom.letswatch.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.tgzoom.letswatch.R;
import com.example.tgzoom.letswatch.util.PreferencesUtils;

import javax.inject.Inject;

/**
 * Created by tgzoom on 1/24/17.
 */

public class SortDialogFragment extends DialogFragment {

    private SharedPreferences mSharedPreferences;

    private Context mContext;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String[] values = getResources().getStringArray(R.array.pref_order_entry_values);
        int currentPosition = PreferencesUtils.getSortPosition(mSharedPreferences,mContext);

        builder.setTitle(getActivity().getString(R.string.sort_dialog_title));
        builder.setSingleChoiceItems(R.array.pref_order_entries,currentPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PreferencesUtils.setPreferredOrder(mContext,values[i]);
                SortDialogListener sortDialogListener = (SortDialogListener) getTargetFragment();
                sortDialogListener.onSortChange();
            }
        });

        return builder.create();
    }

    interface SortDialogListener{
        void onSortChange();
    }
}

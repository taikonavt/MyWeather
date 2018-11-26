package com.example.maxim.myweather.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.example.maxim.myweather.R;

public class ExplainPermissionDialogFragment extends DialogFragment
implements DialogInterface.OnClickListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setNeutralButton("OK", this)
                .setMessage(getResources().getString(R.string.permission_explanation));
        return adb.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        OnButtonPermissionDialogListener onButtonPermissionDialogListener;
        try {
            onButtonPermissionDialogListener = (OnButtonPermissionDialogListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()
            + " must implement " + OnButtonPermissionDialogListener.class);
        }
        if (onButtonPermissionDialogListener != null)
            onButtonPermissionDialogListener.onOkButtonPermissionDialogFragmentClick();
    }


    public interface OnButtonPermissionDialogListener{
        void onOkButtonPermissionDialogFragmentClick();
    }
}

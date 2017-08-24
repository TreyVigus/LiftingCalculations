package com.trey.fitnesstools;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;



public class DialogFragmentInexactWeight extends DialogFragment
{
    private double estimatedWeight;
    private TextView dialogText;

    public static final String KEY_DIALOG_WEIGHT = "key_dialog_weight";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        estimatedWeight = getArguments().getDouble(KEY_DIALOG_WEIGHT,0);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_inexact_weight,null);
        dialogText = v.findViewById(R.id.dialogText);

        dialogText.setText("The closest estimate with the available plates is " + LiftingCalculations.desiredFormat(estimatedWeight));


        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.inexact_dialog_title)
                .setView(v)
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }

    public static DialogFragmentInexactWeight newInstance(Double weight)
    {
        //arguments containing the weight that needs to be displayed in the dialog.
        Bundle args = new Bundle();
        args.putDouble(KEY_DIALOG_WEIGHT,weight);

        DialogFragmentInexactWeight dialog = new DialogFragmentInexactWeight();
        dialog.setArguments(args);
        return  dialog;
    }
}

package com.trey.fitnesstools;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class DialogFragmentDeloadPercent extends DialogFragment
{
    private SeekBar seekBarSelectPercent;
    private TextView displayPercent;

    private int percentOfRM;
    private double passedWeight;

    public static String KEY_PASSED_WEIGHT = "key_passed_weight";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_deload_percent,null);

        displayPercent = v.findViewById(R.id.textViewRMPercent);
        seekBarSelectPercent = v.findViewById(R.id.seekBarDialogPercent);
        seekBarSelectPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b)
            {
                percentOfRM = progressValue;
                displayPercent.setText(Integer.toString(percentOfRM) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //intitial values for fields.
        passedWeight = getArguments().getDouble(KEY_PASSED_WEIGHT,0);
        percentOfRM = seekBarSelectPercent.getProgress();
        displayPercent.setText(Integer.toString(percentOfRM) + "%");

        //listener for the OK button on the dialog box.
        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                ActivityDeloadCalculator.startActivityDeloadCalculator(getActivity(),passedWeight,percentOfRM);
            }
        };



        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.dialog_title_deload_percent)
                .setPositiveButton(android.R.string.ok,okListener)
                .create();
    }

    //used to instantiate the dialog fragment and pass it a weight value
    public static DialogFragmentDeloadPercent newInstance(double repMaxWeight)
    {
        Bundle args = new Bundle();
        args.putDouble(KEY_PASSED_WEIGHT,repMaxWeight);

        DialogFragmentDeloadPercent dialog = new DialogFragmentDeloadPercent();
        dialog.setArguments(args);
        return dialog;
    }
}

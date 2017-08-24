package com.trey.fitnesstools;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class DialogFragmentOutput extends DialogFragment
{
    private RecyclerView outputRecyclerView;
    private FragmentPlateCalculator.ResultAdapter adapter;

    public static final String KEY_LIST_DISPLAY = "key_list_display";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_output,null);

        outputRecyclerView = v.findViewById(R.id.recyclerView_output);
        outputRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //use the adapter from FragmentPlateCalculator
        outputRecyclerView.setAdapter(FragmentPlateCalculator.plateAdapter);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.text_result_label)
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }

    //create a dialog fragment to display the plates.
    //the only parameter is the adapter required to configure the recycler view.
    //SET ARGS
    public static DialogFragmentOutput newInstance()
    {
        DialogFragmentOutput dialog = new DialogFragmentOutput();
        return dialog;
    }

    public void setAdapter(FragmentPlateCalculator.ResultAdapter adapter)
    {
        this.adapter = adapter;
    }

}

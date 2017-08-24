package com.trey.fitnesstools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityPlateCalculator extends AppCompatActivity
{
    public static String KEY_WEIGHT = "key_weight";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plate_calculator);

        //add the FragmentPlateCalculator to the fragment manager
        FragmentManager fm = getSupportFragmentManager();
        Fragment plateCalculatorFragment = fm.findFragmentById(R.id.activity_plate_calculator_root);
        if(plateCalculatorFragment == null)
        {
            plateCalculatorFragment = FragmentPlateCalculator.newInstance();
            fm.beginTransaction().add(R.id.activity_plate_calculator_root,plateCalculatorFragment).commit();
        }
    }

    //call this method to start ActivityPlateCalculator if you want to pass it a weight value from another activity.
    public static void startActivityPlateCalculator(Activity context, int weightValue)
    {
        Intent intent = new Intent(context,ActivityPlateCalculator.class);
        intent.putExtra(KEY_WEIGHT,weightValue);
        context.startActivity(intent);
    }
}

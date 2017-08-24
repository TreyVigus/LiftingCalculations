package com.trey.fitnesstools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityRepMaxCalculator extends AppCompatActivity {

    private SeekBar desiredRepMaxBar;
    private TextView txtViewRepMaxTypeInput;
    private TextView txtViewOutput;
    private EditText editTextInputWeight;
    private EditText editTextInputReps;
    private Button btnShowRMPlates;
    private Button btnShowRMPercent;

    private double weightLifted; //amount the user has lifted
    private int repsPerformed;   //number of reps performed when lifting the above amount
    private int desiredRepMax;   //rep max the user wants to calculate (e.g. if 1RM, then desiredRepMax == 1)
    private int repMaxOutput; //the amount of weight the user can lift at the desiredRepMax number of repetitions.


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_max_calculator);

        weightLifted = 0;
        repsPerformed = 0;
        desiredRepMax = 1;
        repMaxOutput = 0;

        desiredRepMaxBar = (SeekBar) findViewById(R.id.seekBar_rep_max);
        desiredRepMaxBar.setProgress(desiredRepMax);
        desiredRepMaxBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean b)
            {
                desiredRepMax = progressValue;
                setTextViewRepMaxTypeInput();

                //if the input from the textboxes is valid, compute the rep max
                if(getInput())
                {
                    getRepMax();
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

        });

        txtViewRepMaxTypeInput = (TextView) findViewById(R.id.inputRepMax);
        setTextViewRepMaxTypeInput();
        txtViewOutput = (TextView) findViewById(R.id.outputRepMax);

        editTextInputWeight = (EditText) findViewById(R.id.editTextEnterWeightLifted);
        editTextInputReps = (EditText) findViewById(R.id.editTextEnterRepsPerformed);
        TextWatcher textListener = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(getInput())
                {
                    getRepMax();
                }
            }
        };
        editTextInputReps.addTextChangedListener(textListener);
        editTextInputWeight.addTextChangedListener(textListener);

        btnShowRMPlates = (Button)findViewById(R.id.btnShowRMPlates);
        //when the button is pressed start an instance of ActivityPlateCalculator, passing it the RM that has just been calculated.
        btnShowRMPlates.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ActivityPlateCalculator.startActivityPlateCalculator(ActivityRepMaxCalculator.this,repMaxOutput);
            }
        });

        btnShowRMPercent = (Button)findViewById(R.id.btnShowRMDeloaded);
        //when the OK button is pressed in the dialog, launch DialogFragmentDeloadPercent, passing it the Rep Max to deload.
        btnShowRMPercent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogFragmentDeloadPercent dialog = DialogFragmentDeloadPercent.newInstance(repMaxOutput);
                dialog.show(getSupportFragmentManager(),"dialog tag");
            }
        });

    }

    private void setTextViewRepMaxTypeInput()
    {
        txtViewRepMaxTypeInput.setText(Integer.toString(desiredRepMax) + " RM");
    }

    //gets the rep max and displays the output
    private void getRepMax()
    {
        repMaxOutput = LiftingCalculations.repMax(desiredRepMax, weightLifted, repsPerformed);
        txtViewOutput.setText(Double.toString(repMaxOutput) + " is your " + Integer.toString(desiredRepMax) + " rep max.");
    }

    //get user input from textboxes; return true if successful, false otherwise.
    private boolean getInput()
    {
        try
        {
            weightLifted = Double.parseDouble(editTextInputWeight.getText().toString());
            repsPerformed = Integer.parseInt(editTextInputReps.getText().toString());
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

}










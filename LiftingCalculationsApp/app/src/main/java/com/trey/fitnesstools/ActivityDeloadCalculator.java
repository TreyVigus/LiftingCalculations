package com.trey.fitnesstools;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityDeloadCalculator extends AppCompatActivity
{
    private EditText editTextEnterDeloadWeight;
    private SeekBar seekBarDeloadPercent;
    private TextView textViewDeloadPercent;
    private TextView textViewOutput;
    private Button btnShowDeloadedPlates;

    private double weightInput;
    private int deloadedWeight;
    private int deloadPercent;

    public static final String KEY_INTENT_WEIGHT = "key_intent_weight";
    public static final String KEY_INTENT_Percent = "key_intent_percent";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deload_calculator);

        editTextEnterDeloadWeight = (EditText)findViewById(R.id.editTextEnterDeloadWeight);
        seekBarDeloadPercent = (SeekBar)findViewById(R.id.seekBarDeloadPercent);
        textViewDeloadPercent = (TextView)findViewById(R.id.textViewDeloadPercent) ;
        textViewOutput = (TextView)findViewById(R.id.txtDeloadedOutput);
        btnShowDeloadedPlates = (Button)findViewById(R.id.btnShowDeloadPlates);

        seekBarDeloadPercent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                //get the deload percent and perform the deload calculation.
                deloadPercent = seekBar.getProgress();
                updatePercentLabel();
                deload();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        editTextEnterDeloadWeight.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable textBox)
            {
                deload();
                if(editTextEnterDeloadWeight.getText().toString().isEmpty())
                    textViewOutput.setText(null);
            }
        });

        //when the button is pressed, start the plate calculator activity.
        btnShowDeloadedPlates.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ActivityPlateCalculator.startActivityPlateCalculator(ActivityDeloadCalculator.this,deloadedWeight);
            }
        });

        textViewDeloadPercent.setText(Integer.toString(seekBarDeloadPercent.getProgress()));

        deloadedWeight = 0;
        weightInput = getIntent().getDoubleExtra(KEY_INTENT_WEIGHT,0);
        if(weightInput > 0)
            editTextEnterDeloadWeight.setText(LiftingCalculations.desiredFormat(weightInput));

        //If deload percent has been passed from another activity, it will be stored in the intent.
        //if not found, the percent will be 0 (second arg in getIntExtra(...)
        //SUBTRACT BY 100 BECAUSE IT IS BEING PASSED THE PERCENT OF THE WEIGHT, NOT THE PERCENT TO DELOAD BY.
        deloadPercent = 100 - getIntent().getIntExtra(KEY_INTENT_Percent,100);
        seekBarDeloadPercent.setProgress(deloadPercent);
        updatePercentLabel();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_question_mark,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.question_item)
        {
            String message = getString(R.string.long_message_deload_calculator);
            DialogFragmentQuestionMark dialog = DialogFragmentQuestionMark.newInstance(message);
            dialog.show(getSupportFragmentManager(),"question_tag");
        }

        return false;
    }

    private void updatePercentLabel()
    {
        textViewDeloadPercent.setText(Integer.toString(deloadPercent) + " %");
    }

    //gets user's weight input and outputs the deloaded amount
    private void deload()
    {
        try
        {
            //make sure the string isn't empty
            if (!editTextEnterDeloadWeight.getText().toString().isEmpty())
            {
                weightInput = Double.parseDouble(editTextEnterDeloadWeight.getText().toString());
                deloadedWeight = LiftingCalculations.deloadWeight(weightInput, deloadPercent);
                textViewOutput.setText(Integer.toString(deloadedWeight) + " is the weight after deloading");
            }
        }
        catch(Exception e)
        {
            Toast.makeText(ActivityDeloadCalculator.this,"Enter valid input", Toast.LENGTH_SHORT);
        }
    }

    //use this method to start ActivityDeloadCalculator from another activity and pass it an initial weight value.
    public static void startActivityDeloadCalculator(Activity context,double weight,int percent)
    {
        Intent intent = new Intent(context,ActivityDeloadCalculator.class);
        intent.putExtra(KEY_INTENT_WEIGHT,weight);
        intent.putExtra(KEY_INTENT_Percent,percent);
        context.startActivity(intent);
    }

}

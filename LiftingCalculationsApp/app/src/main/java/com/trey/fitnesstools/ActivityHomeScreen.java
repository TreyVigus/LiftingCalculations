package com.trey.fitnesstools;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ActivityHomeScreen extends AppCompatActivity
{
    private LinearLayout layoutPlateCalculator;
    private LinearLayout layoutRMCalculator;
    private LinearLayout layoutDeloadCalculator;
    private LinearLayout layoutSetTrackerandTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        layoutPlateCalculator = (LinearLayout)findViewById(R.id.launchPlateCalculator);
        layoutRMCalculator = (LinearLayout)findViewById(R.id.launchRMcalculator);
        layoutDeloadCalculator = (LinearLayout)findViewById(R.id.launchDeloadCalculator);
        layoutSetTrackerandTimer = (LinearLayout)findViewById(R.id.launchSetTracker);


        ClickHandler handler = new ClickHandler();
        layoutPlateCalculator.setOnClickListener(handler);
        layoutRMCalculator.setOnClickListener(handler);
        layoutDeloadCalculator.setOnClickListener(handler);
        layoutSetTrackerandTimer.setOnClickListener(handler);

    }

    private class ClickHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            int clickedId = view.getId();
            //start the activity corresponding to each layout.
            if(clickedId == R.id.launchPlateCalculator)
            {
                startNewActivity(ActivityPlateCalculator.class);
            }
            else if(clickedId == R.id.launchRMcalculator)
            {
                startNewActivity(ActivityRepMaxCalculator.class);
            }
            else if(clickedId == R.id.launchDeloadCalculator)
            {
                startNewActivity(ActivityDeloadCalculator.class);
            }
            else if(clickedId == R.id.launchSetTracker)
            {
                startNewActivity(ActivitySetTracker.class);
            }
        }
    }
    private void startNewActivity(Class<?> cls)
    {
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }
}

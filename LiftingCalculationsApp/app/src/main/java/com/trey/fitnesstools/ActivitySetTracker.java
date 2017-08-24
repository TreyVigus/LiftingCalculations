package com.trey.fitnesstools;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivitySetTracker extends AppCompatActivity
{
    private TextView textViewSetCount;
    private ImageButton btnIncrement;
    private ImageButton btnDecrement;
    private Chronometer stopwatch;
    private Button btnStartStop;
    private Button btnResetTimer;
    private Button btnResetSets;

    private int setCounter;
    private boolean stopwatchRunning;
    private long timeStopped;

    //key for saving the set counter to the instance state bundle.
    public static final String KEY_SET_COUNTER = "key_set_counter";
    public static final String KEY_TIME_STOPPED = "key_time_stopped";
    public static final String KEY_STOPWATCH_RUNNING = "key_stopwatch_running";
    //used to recreate the stopwatch with the correct time after rotation
    public static final String KEY_BASE_ON_ROTATION = "key_base_on_rotation";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_tracker);

        textViewSetCount = (TextView)findViewById(R.id.txtViewSetCount);
        btnDecrement = (ImageButton)findViewById(R.id.btnDecrement);
        btnIncrement = (ImageButton)findViewById(R.id.btnIncrement);
        stopwatch = (Chronometer)findViewById(R.id.stopwatch);
        btnStartStop = (Button)findViewById(R.id.btnToggleStartStop);
        btnResetTimer = (Button)findViewById(R.id.btnResetStopwatch);
        btnResetSets = (Button)findViewById(R.id.zero_button);

        ClickHandler handler = new ClickHandler();
        btnDecrement.setOnClickListener(handler);
        btnIncrement.setOnClickListener(handler);
        btnStartStop.setOnClickListener(handler);
        btnResetTimer.setOnClickListener(handler);
        btnResetSets.setOnClickListener(handler);

        //if there is a saved instance state, retrieve the stored values from the bundle
        if(savedInstanceState != null)
        {
            setCounter = savedInstanceState.getInt(KEY_SET_COUNTER);
            timeStopped = savedInstanceState.getLong(KEY_TIME_STOPPED);
            stopwatchRunning = savedInstanceState.getBoolean(KEY_STOPWATCH_RUNNING);
            stopwatch.setBase(savedInstanceState.getLong(KEY_BASE_ON_ROTATION));
            startStopwatch();
        }
        //else set to default values.
        else
        {
            //no sets counted initially
            setCounter = 0;
            //timeStopped == -1 is a flag to indicate that the timeStopped has not yet been set.
            timeStopped = -1;
            //stopwatch is not running initially
            stopwatchRunning = false;
        }
        updateCountLabel();
    }

    //Event handler for all the buttons on the activity
    private class ClickHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            int id = view.getId();

            if(id == btnDecrement.getId())
            {
                //can't have negative sets
                if(setCounter > 0)
                {
                    setCounter--;
                    updateCountLabel();
                }
            }
            else if(id == btnIncrement.getId())
            {
                setCounter++;
                updateCountLabel();
            }
            else if(id == btnStartStop.getId())
            {
                if(stopwatchRunning)
                    pauseStopwatch();
                else
                    startStopwatch();
            }
            else if(id == btnResetTimer.getId())
            {
                resetStopwatch();
            }
            else if(id == btnResetSets.getId())
            {
                setCounter = 0;
                updateCountLabel();
            }
        }
    }

    private void startStopwatch()
    {
        stopwatchRunning = true;
        btnStartStop.setText(R.string.txt_stop);

        //if the timeStopped has not yet been set(which means that the button is either being pressed for the first time
        //or that it has just been reset), set the base to the current time (elapsedRealTime() is the time that has passed
        //since the activity started) so that the timer starts at 0.
        if(timeStopped == -1)
        {
            stopwatch.setBase(SystemClock.elapsedRealtime());
        }
        //else the stopwatch was paused and now needs to resume again
        else
        {
            long intervalOnPause = SystemClock.elapsedRealtime() - timeStopped;
            stopwatch.setBase(stopwatch.getBase() + intervalOnPause);
        }

        stopwatch.start();
    }

    private void pauseStopwatch()
    {
        stopwatchRunning = false;
        btnStartStop.setText(R.string.txt_start);
        timeStopped = SystemClock.elapsedRealtime();
        stopwatch.stop();
    }

    private void resetStopwatch()
    {
        stopwatchRunning = false;
        btnStartStop.setText(R.string.txt_start);

        //reset the flag, so that if(timeStopped == -1) evaluates to true when the start button is pressed.
        timeStopped = -1;
        //stop the clock from counting
        stopwatch.stop();
        //set the clock's base(value from which it starts ticking up) to the current time
        stopwatch.setBase(SystemClock.elapsedRealtime());
    }

    private void updateCountLabel()
    {
        textViewSetCount.setText(Integer.toString(setCounter));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_STOPWATCH_RUNNING,stopwatchRunning);
        //ensure stopwatch will have the same time after the rotation.
        if(stopwatchRunning)
            pauseStopwatch();
        outState.putLong(KEY_BASE_ON_ROTATION,stopwatch.getBase());
        //save fields
        outState.putInt(KEY_SET_COUNTER,setCounter);
        outState.putLong(KEY_TIME_STOPPED,timeStopped);

    }
}

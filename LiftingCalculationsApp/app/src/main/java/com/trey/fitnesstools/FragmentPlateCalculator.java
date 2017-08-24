package com.trey.fitnesstools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class FragmentPlateCalculator extends Fragment
{
    //weight that has been entered into the EditText
    private double enteredWeight;
    //weight of the bar alone
    private double barWeight;
    //true if the user wants full weight, false if they want one sided
    private boolean fullWeightEntered;

    private EditText txtFullWeight;
    private EditText txtBarWeight;
    private TextView lblBarWeight;
    private Button btnCalculateWeight;
    private RadioGroup radioGroup;
    private RadioButton radioFullWeight;
    private RadioButton radioOneSide;


    //list of Plates used to calculate the user's weight (will be different from the list of ALL types of weights)
    public static PlateList calculationPlates;
    //list of doubles displayed in the RecyclerView
    public static ResultAdapter plateAdapter;

    public static final String KEY_ADAPTER_PLATELIST = "key_adapter_platelist";
    public static final String KEY_SHARED_PREF_PLATES = "key_shared_pref_plates";
    public static final String DialogFragmentID = "settings_button_dialog";
    public static final String KEY_SHARED_PREF_PLATE_LENGTH = "plate_length";

    @Override
    public void onCreate(Bundle savedInstanceState)
     {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //if a weight is passed to the parent activity, retrieve it. Otherwise use the default value of 0.
        enteredWeight = getActivity().getIntent().getIntExtra(ActivityPlateCalculator.KEY_WEIGHT,0);

        //if the screen just rotated, creates a new adapter that had the same list as before rotation.
        if(savedInstanceState != null)
        {
            ArrayList<Double> list = (ArrayList<Double>)savedInstanceState.getSerializable(KEY_ADAPTER_PLATELIST);
            //list would only be null if the user rotates before an adapter is created (i.e. before actually generating any output)
            if(list != null)
            {
                plateAdapter = new ResultAdapter(list);
            }

            //the calculation plates must be loaded in from the shared preferences file on each rotation
            // ****(maybe save in onSaveInstanceState to improve performance?)****
            loadPlatesFromSharedPreferences();
        }
        //if the screen hasn't just rotated (i.e. first time activity is created (before any recreation))
        else
        {
            //try to load the plates from the shared preferences. returns true if the list has elemnts and returns false if the list is empty
            boolean hasPlates = loadPlatesFromSharedPreferences();
            //if the list is empty, the plates have never been saved, so create a new list.
            if(!hasPlates)
            {
                //initialize the calculationPlates with the default plate list.
                calculationPlates = new PlateList();
                calculationPlates.setStandardPlates();
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_plate_calculator,container,false);

        txtFullWeight = v.findViewById(R.id.editTextEnterFullWeight);
        //enteredWeight will only be greater than 0 if the user has passed it from another activity.
        if(enteredWeight > 0)
        {
            txtFullWeight.setText(Double.toString(enteredWeight));
        }


        txtBarWeight = v.findViewById(R.id.editTextEnterBarWeight);
        lblBarWeight = v.findViewById(R.id.lblBarWeight);

        btnCalculateWeight = v.findViewById(R.id.btnCalculateWeight);
        btnCalculateWeight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                configureOutputRecyclerView();
            }
        });




        radioFullWeight = (RadioButton)v.findViewById(R.id.radioFullWeight) ;
        radioOneSide = (RadioButton)v.findViewById(R.id.radioOneSide) ;
        radioGroup = (RadioGroup)v.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId)
            {
                if(checkedId == radioFullWeight.getId())
                {
                    fullWeightEntered = true;
                    txtBarWeight.setVisibility(View.VISIBLE);
                    lblBarWeight.setVisibility(View.VISIBLE);
                }
                else if(checkedId == radioOneSide.getId())
                {
                    fullWeightEntered = false;
                    txtBarWeight.setVisibility(View.INVISIBLE);
                    lblBarWeight.setVisibility(View.INVISIBLE);
                }
            }
        });
        radioFullWeight.setChecked(true);

        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_plate_calculator_menu,menu);
        inflater.inflate(R.menu.menu_question_mark,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.actionItem_plateCalcSettings)
        {
            DialogFragmentPlates dialog = DialogFragmentPlates.newInstance();
            dialog.show(getFragmentManager(),DialogFragmentID);
            return true;
        }
        else if(item.getItemId() == R.id.question_item)
        {
            String message = getString(R.string.long_message_plate_calculator);
            DialogFragmentQuestionMark dialog = DialogFragmentQuestionMark.newInstance(message);
            dialog.show(getFragmentManager(),"question_tag");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if(plateAdapter != null)
        {
            ArrayList<Double> adapterList = (ArrayList<Double>) plateAdapter.getList();
            outState.putSerializable(KEY_ADAPTER_PLATELIST, adapterList);
        }
        else
        {
            outState.putSerializable(KEY_ADAPTER_PLATELIST,null);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        saveCalculationPlatesToSharedPreferences();
    }

    //HACKY way of writing the calculationPlates list to the activity's shared preferences.
    public void saveCalculationPlatesToSharedPreferences()
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();

        //Gson object allows you to convert custom objects (like plate) to string
        Gson gson = new Gson();

        //go through the entire array list, converting each element to a string with the Gson, and storing it in the sharedPreferences.
        int listLength = calculationPlates.size();
        for(int i = 0; i < listLength; i++)
        {
            String stringRepresentation = gson.toJson(calculationPlates.get(i));
            editor.putString(KEY_SHARED_PREF_PLATES + Integer.toString(i),stringRepresentation);
        }
        //the size of the list needs to be saved into the SharedPreferences object so that the plates can be retrieved later.
        editor.putInt(KEY_SHARED_PREF_PLATE_LENGTH,listLength);
        editor.commit();
    }

    //retrieve the calculation plates from the shared preferences file.
    //returns true if the list is not empty
    //returns false if list is empty
    public boolean loadPlatesFromSharedPreferences()
    {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();

        calculationPlates = new PlateList();

        int listLength = sharedPref.getInt(KEY_SHARED_PREF_PLATE_LENGTH,0);
        for(int i = 0; i < listLength; i++)
        {
            String stringRepresentation = sharedPref.getString(KEY_SHARED_PREF_PLATES + Integer.toString(i),"");
            Plate plate = gson.fromJson(stringRepresentation,Plate.class);
            calculationPlates.add(plate);
        }

        return listLength > 0;
    }

    //get input from the text boxes and store in fields
    private void getInput()
    {
        try
        {
            enteredWeight = Double.parseDouble(txtFullWeight.getText().toString());
            //only need to get the bar weight if the user enters their full weight
            if(fullWeightEntered)
                barWeight = Double.parseDouble(txtBarWeight.getText().toString());
        }
        catch(Exception e)
        {
            Toast.makeText(FragmentPlateCalculator.this.getActivity(),"enter a valid number",Toast.LENGTH_SHORT).show();
        }
    }

    //generates the plates for the user's input and passes them to the adapter for the recyclerView to display
    private void configureOutputRecyclerView()
    {
        //get input from text boxes
        getInput();

        //used to store intermediate results from method calls
        MutableDouble ret = new MutableDouble(-1);

        if(fullWeightEntered)
        {
            plateAdapter = new ResultAdapter(findRequiredPlatesFullWeight(enteredWeight,barWeight,ret));
        }
        else
        {
            plateAdapter = new ResultAdapter(findRequiredPlatesOneSideWeight(enteredWeight,ret));
        }

        //show the output plates
        DialogFragmentOutput dialog = DialogFragmentOutput.newInstance();
        dialog.show(getFragmentManager(),"output");

        if(ret.getValue() > -1)
        {
            notifyInexactWeight(ret.getValue());
        }
    }

    //given the amount of weight on one side of the bar, determine which plates are required to represent that weight
    private ArrayList<Double> findRequiredPlatesOneSideWeight(double oneSideWeight, MutableDouble ret)
    {
        ArrayList<Double> resultList = new ArrayList<>();
        //store the leftover amount after the plate list is generated
        double remainingWeight = generateResultPlateList(resultList, oneSideWeight);

        //if the user's weight cannot be exactly represented, notify them.
        determineWeightExactRepresentation(remainingWeight,true, ret);

        return resultList;
    }
    //given the total amount of weight (including the bar), determine which plates are required to represent that weight
    private ArrayList<Double> findRequiredPlatesFullWeight(double fullWeight, double pBarWeight, MutableDouble ret)
    {
        ArrayList<Double> resultList = new ArrayList<>();
        double oneSideWeight = (fullWeight - pBarWeight) / 2;
        double remainingWeight = generateResultPlateList(resultList,oneSideWeight);

        determineWeightExactRepresentation(remainingWeight,false,ret);

        return resultList;
    }

    //tells the user if their weight can be exactly represented with the plates from calculationPlates
    //params: remainingWeight -> weight left over after calculationPlates is iterated through
    //        oneSide -> true if the weight is for one side and false for full weight
    private void determineWeightExactRepresentation(double remainingWeight, boolean oneSide, MutableDouble ret)
    {
        if (remainingWeight > 0)
        {
            //program's approximation of the entered weight
            double approximateWeight;

            if (oneSide)
            {
                approximateWeight = enteredWeight - remainingWeight;
            }
            else
            {
                double totalRemainingWeight = remainingWeight * 2;
                approximateWeight = enteredWeight - totalRemainingWeight;
            }

            //notifyInexactWeight(approximateWeight);
            ret.setValue(approximateWeight);
        }
        else
        {
            //if weight is exact, then the returned double will have a default value of -1
            ret.setValue(-1);
        }
    }

    //tell the user that the weight cannot be exactly represented
    private void notifyInexactWeight(double approximateWeight)
    {
        //create a dialog to notify the user and display the approximate weight.
        DialogFragmentInexactWeight dialog = DialogFragmentInexactWeight.newInstance(approximateWeight);
        dialog.show(getFragmentManager(), "random tag");
    }

    //generates the arrayList of plates from the amount of weight on one side of the bar.
    //make sure the input arrayList is an empty arrayList
    private double generateResultPlateList(ArrayList<Double> resultList, double oneSideWeight)
    {
        //loop through each of the possible plates, starting at the largest
        //subtract the current type of plate, until the remaining size is less than that plate, then go to the next plate type.
        for(int i = 0; i < calculationPlates.size(); i++)
        {
            Plate currentPlate = calculationPlates.get(i);

            //only use the current plate in the calcultion if it is enabled(can be disabled from the checkBoxes in DialogFragmentPlates.
            if(currentPlate.isEnabled())
            {
                double currentPlateValue = currentPlate.getWeight();
                while (oneSideWeight >= currentPlateValue)
                {
                    resultList.add(currentPlateValue);
                    oneSideWeight = oneSideWeight - currentPlateValue;
                }
            }
        }
        //return remaining weight after looping through all possible plates.
        return oneSideWeight;
    }

    //use this to instantiate a new fragment
    public static FragmentPlateCalculator newInstance()
    {
        FragmentPlateCalculator fragment = new FragmentPlateCalculator();
        return fragment;
    }


    //CLASSES FOR CONFIGURING THE RECYCLER-VIEW (ViewHolder and Adapter required)
    // **********************************************************************************************
    private class ResultHolder extends RecyclerView.ViewHolder
    {
        private TextView resultText;

        public ResultHolder(View v)
        {
            super(v);
            resultText = v.findViewById(R.id.weight_text_plate_holder);
        }
        public void bind(double p)
        {
            resultText.setText(Double.toString(p));
        }
    }
    public class ResultAdapter extends RecyclerView.Adapter<ResultHolder>
    {
        private List<Double> results;

        public ResultAdapter(List<Double> results)
        {
            this.results = results;
        }

        @Override
        public ResultHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.view_holder_result,parent,false);
            ResultHolder holder = new ResultHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ResultHolder holder, int position)
        {
            holder.bind(results.get(position));
        }

        @Override
        public int getItemCount()
        {
            return results.size();
        }

        public List<Double> getList()
        {
            return results;
        }

    }
    //**********************************************************************************************

}


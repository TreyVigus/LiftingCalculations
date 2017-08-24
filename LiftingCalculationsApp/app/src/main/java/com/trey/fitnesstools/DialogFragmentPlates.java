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
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class DialogFragmentPlates extends DialogFragment
{
    private RecyclerView plateRecyclerView;
    private EditText txtEnterNewPlate;
    private EditText txtDeletePlate;
    private ImageButton addPlateImageButton;
    private ImageButton deletePlateImageButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_plates,null);

        plateRecyclerView = v.findViewById(R.id.recyclerView_plate);
        plateRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PlateAdapter adapter = new PlateAdapter();
        plateRecyclerView.setAdapter(adapter);

        txtEnterNewPlate = v.findViewById(R.id.editTextEnterNewPlate);
        txtDeletePlate = v.findViewById(R.id.editTextDeleteCustomPlate);

        addPlateImageButton = v.findViewById(R.id.imageButton_addPlates);
        deletePlateImageButton = v.findViewById(R.id.btnDeletePlate);
        View.OnClickListener buttonListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int clickedId = view.getId();
                if(clickedId == addPlateImageButton.getId())
                {
                    addCustomPlate();
                }
                else if(clickedId == deletePlateImageButton.getId())
                {
                    deleteCustomPlate();
                }
            }
        };
        addPlateImageButton.setOnClickListener(buttonListener);
        deletePlateImageButton.setOnClickListener(buttonListener);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.text_plate_type_label)
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }

    //retrieve the user's input from the edit text
    //store this as a plate in FragmentPlateCalculator.calculationPlates
    private void addCustomPlate()
    {
        try
        {
            double newPlateInput = Double.parseDouble(txtEnterNewPlate.getText().toString());
            //new plate with the input weight. second param is true because it will be enabled. third param is true because it's a custom plate.
            Plate newPlate = new Plate(newPlateInput,true,true);

            int insertIndex = FragmentPlateCalculator.calculationPlates.findPlateIndex(newPlate);
            if(insertIndex > -1)
            {
                FragmentPlateCalculator.calculationPlates.add(insertIndex, newPlate);
                plateRecyclerView.getAdapter().notifyItemInserted(insertIndex);
            }
        }
        catch(Exception e)
        {
            Toast.makeText(getActivity(),"Enter a valid plate value",Toast.LENGTH_SHORT);
        }
    }

    //delete a custom plate that the user has added (does nothing if the plate is a default plate).
    private void deleteCustomPlate()
    {
        try
        {
            double deletePlateInput = Double.parseDouble(txtDeletePlate.getText().toString());
            //attempt to remove the plate if it's in the list. (returns boolean indicating if it was found)
            boolean plateFound = FragmentPlateCalculator.calculationPlates.removeCustomPlate(deletePlateInput);
            if(!plateFound)
            {
                Toast.makeText(getActivity(),"A plate with this value could not be found",Toast.LENGTH_SHORT);
            }
            //notify item changed would improve performance
            plateRecyclerView.getAdapter().notifyDataSetChanged();

        }
        catch(Exception e)
        {
            Toast.makeText(getActivity(),"Enter a valid plate value",Toast.LENGTH_SHORT);
        }
    }

    private class PlateHolder extends RecyclerView.ViewHolder
    {
        private TextView weightText;
        private CheckBox checkPlateUsed;

        private Plate plate;

        public PlateHolder(View v)
        {
            super(v);
            weightText = v.findViewById(R.id.weight_text_plate_holder);

            checkPlateUsed = v.findViewById(R.id.check_plate_selected);
            checkPlateUsed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                {
                    //if the box becomes checked, enable the plate so it can be used in calculations.
                    if(isChecked)
                    {
                        plate.setEnabled(true);
                    }
                    //disable it if unchecked
                    else
                    {
                        plate.setEnabled(false);
                    }
                }

            });
        }
        public void bind(Plate p)
        {
            plate = p;
            weightText.setText(Double.toString(plate.getWeight()));
            checkPlateUsed.setChecked(p.isEnabled());
        }
    }
    private class PlateAdapter extends RecyclerView.Adapter<PlateHolder>
    {

        @Override
        public PlateHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.view_holder_plate,parent,false);
            PlateHolder holder = new PlateHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(PlateHolder holder, int position)
        {
            holder.bind(FragmentPlateCalculator.calculationPlates.get(position));
        }

        @Override
        public int getItemCount()
        {
            return FragmentPlateCalculator.calculationPlates.size();
        }
    }


    public static DialogFragmentPlates newInstance()
    {
        return new DialogFragmentPlates();
    }
}

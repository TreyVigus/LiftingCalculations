package com.trey.fitnesstools;


import java.util.ArrayList;
import java.util.List;


//Needs to be sorted from greatest to least for findRequiredPlates(...) in FragmentPlateCalculator
//The weight of an individual plate should be unique. (no duplicates)
//SHOULD BE USING A SET, NOT AN ARRAYLIST
public class PlateList extends ArrayList<Plate>
{


    //add the standard plate set to the plateList
    //second parameter is true if they are enabled by default;
    //third parameter should be false because they are not custom plates, they are default plates.
    public void setStandardPlates()
    {
        add(new Plate(45,Plate.PLATE_ENABLED,Plate.NOT_CUSTOM_PLATE));
        add(new Plate(35,Plate.PLATE_ENABLED,Plate.NOT_CUSTOM_PLATE));
        add(new Plate(25,Plate.PLATE_ENABLED,Plate.NOT_CUSTOM_PLATE));
        add(new Plate(10,Plate.PLATE_ENABLED,Plate.NOT_CUSTOM_PLATE));
        add(new Plate(5,Plate.PLATE_ENABLED,Plate.NOT_CUSTOM_PLATE));
        add(new Plate(2.5,Plate.PLATE_ENABLED,Plate.NOT_CUSTOM_PLATE));
    }

    //return a list that is an exact copy of this one
    public PlateList duplicatePlateList()
    {
        PlateList duplicateList = new PlateList();
        duplicateList.clear();
        for(Plate p: this)
        {
            duplicateList.add(new Plate(p));
        }
        return duplicateList;
    }

    /*@Override
    public boolean add(Plate plate)
    {
        int i = findPlateIndex(p);
        if(p > -1)
          return add(i,p);
        else
          return false;
    }*/

    //finds the index that a given plate should be added to for insertion.
    //returns -1 to indicate failure
    public int findPlateIndex(Plate p)
    {
        if(!checkIfPlateUnique(p)){return -1;} //fails if the plate isn't unique.

        int listLength = size();
        double weight = p.getWeight();
        //ensure that no negative plates are being added.
        if (weight > 0)
        {
            int i = 0;
            //increment i until the correct position is reached.
            while (i < listLength && get(i).getWeight() > weight)
            {
                i++;
            }

            return i;
        }

        else return -1;
    }

    //determine if a given plate is already in the list
    private boolean checkIfPlateUnique(Plate plate)
    {
        double weight = plate.getWeight();
        for(Plate p: this)
        {
            if(p.getWeight() == weight)
            {
                return false;
            }
        }
        return true;
    }
    //disable the plate with the same weight as the parameter if it exists.
    public void disablePlate(Plate plate)
    {
        double weight = plate.getWeight();
        for(int i = 0; i < size(); i++)
        {
            if(get(i).getWeight() == weight)
            {
                get(i).setEnabled(false);
                break;
            }
        }
    }

    //remove the plate with the same weight as the parameter if it exists.
    //returns true if plate was found, false otherwise
    public boolean removePlate(double weight)
    {
        for(int i = 0; i < size(); i++)
        {
            if(get(i).getWeight() == weight)
            {
                remove(i);
                return true;
            }
        }
        return false;

    }

    //remove plate only if it is custom
    //returns true if the plate was found, false otherwise
    public boolean removeCustomPlate(double weight)
    {
        for(int i = 0; i < size(); i++)
        {
            Plate p = get(i);
            if(p.getWeight() == weight && p.isCustom())
            {
                remove(i);
                return true;
            }
        }
        return false;
    }


}

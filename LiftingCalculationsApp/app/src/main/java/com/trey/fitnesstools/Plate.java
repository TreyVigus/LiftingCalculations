package com.trey.fitnesstools;

public class Plate
{
    private double weight;
    private boolean enabled;
    //whether or not this plate is a custom plate added by the user at runtime
    private boolean custom;

    //set the boolean fields to these constants instead of literals
    public static final boolean CUSTOM_PLATE = true;
    public static final boolean NOT_CUSTOM_PLATE = false;
    public static final boolean PLATE_ENABLED = true;
    public static final boolean PLATE_DISABLED = false;

    public Plate(double weight,boolean enabled,boolean custom)
    {
        this.weight = weight;
        this.enabled = enabled;
        this.custom = custom;
    }
    //copy constructor
    public Plate(Plate p)
    {
        weight = p.getWeight();
        enabled = p.isEnabled();
        custom = p.isCustom();
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean b)
    {
        enabled = b;
    }

    public boolean isCustom()
    {
        return custom;
    }

    public void setCustom(boolean b)
    {
        custom = b;
    }
}

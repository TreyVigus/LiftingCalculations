package com.trey.fitnesstools;

import java.text.DecimalFormat;

public class LiftingCalculations
{
    //estimate the 1RM from any lift that uses a given weight and number of reps.
    //Epley Formula
    public static int oneRepMax(double weightUsed,int reps)
    {
        double oneRM = weightUsed*(1 + (double)reps/30);
        return (int)Math.round(oneRM);
    }

    //estimate any RM from a given weight and number of reps.
    //formula derived w/ some algebra using the 1RM formula
    public static int repMax(int desiredReps, double weightUsed, int reps)
    {
        if(desiredReps == 1)
        {
            return oneRepMax(weightUsed,reps);
        }

        double oneRM = oneRepMax(weightUsed,reps);
        return (int)Math.round(oneRM / (1 + (double)desiredReps/30));
    }

    //given a percent to deload by, outputs the new weight.
    public static int deloadWeight(double weight,int percent)
    {
        double deloadedValue = weight - weight*((double)percent/100);
        return (int)Math.round(deloadedValue);
    }

    //omit unecessary trailing zeros when displaying decimals.
    public static String desiredFormat(double d)
    {
        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);
        return format.format(d);
    }

}

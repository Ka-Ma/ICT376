package au.edu.murdoch.ict376.a2.wheresmycar;

/**
 * Created by Kat on 28/10/2017.
 */

public class Vehicle {
    private String mRego;
    private String mDisplayName;
    private String mDescription;

    Vehicle(String rego, String displayName, String description){
        mRego = rego;
        mDisplayName = displayName;
        mDescription = description;
    }

    public String getRego(){
        return mRego;
    }

    public String getDisplayName(){
        return mDisplayName;
    }

    public String getDescription(){
        return mDescription;
    }
}

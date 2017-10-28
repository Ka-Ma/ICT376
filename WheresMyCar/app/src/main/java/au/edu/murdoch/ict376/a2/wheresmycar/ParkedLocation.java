package au.edu.murdoch.ict376.a2.wheresmycar;


/**
 * Created by Kat on 28/10/2017.
 */

public class ParkedLocation {
    private double mLongitude;
    private double mLatitude;
    private String mParkedTime; // eg 10:30am
    private String mDate; //eg 23/10/2017
    private int mDurationHr;
    private int mDurationMin;
    private int mCostPerHour; //in cents

    ParkedLocation(double longitude, double latitude, String parkedTime, String date, int durationHr, int durationMin, int costPerHr){
        mLongitude = longitude;
        mLatitude = latitude;
        mParkedTime = parkedTime;
        mDate = date;
        mDurationHr = durationHr;
        mDurationMin = durationMin;
        mCostPerHour = costPerHr;
    }

    public double getLongitude(){
        return mLongitude;
    }

    public double getLatitude(){
        return mLatitude;
    }

    public String getParkedTime(){
        return mParkedTime;
    }

    public String getDate(){
        return mDate;
    }

    public int getDurationHr() { return mDurationHr; }

    public int getDurationMin() { return mDurationMin; }

    public int getCostPerHour() { return mCostPerHour; }
}

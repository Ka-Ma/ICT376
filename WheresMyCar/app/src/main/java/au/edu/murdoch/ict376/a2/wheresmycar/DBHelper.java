package au.edu.murdoch.ict376.a2.wheresmycar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.InputMismatchException;


/**
 * Created by Kat on 28/10/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    //Database name
    public static final String DATABASE_NAME = "ParkingDatabase.db";

    //table "vehicle"
    public static final String VEHICLE_TABLE_NAME = "vehicle";
    public static final String VEHICLE_COLUMN_REGO = "rego";
    public static final String VEHICLE_COLUMN_DISPLAY_NAME = "displayName";
    public static final String VEHICLE_COLUMN_DESCRIPTION = "description";
    //default column replaced by query to find last used vehicle

    //table "parking"
    public static final String PARKING_TABLE_NAME = "parking";
    public static final String PARKING_COLUMN_ID = "id";
    public static final String PARKING_COLUMN_DATE = "date";
    public static final String PARKING_COLUMN_TIME = "time";
    public static final String PARKING_COLUMN_REGO = "rego";
    public static final String PARKING_COLUMN_DURATION_HR = "durationHr";
    public static final String PARKING_COLUMN_DURATION_MIN = "durationMin";
    public static final String PARKING_COLUMN_CENTS_PER_HOUR = "centsPerHour";
    public static final String PARKING_COLUMN_PARK_LOCATION_LAT = "parkLocationLat";
    public static final String PARKING_COLUMN_PARK_LOCATION_LONG = "parkLocationLong";

    //version to change if tables need to be recreated

    static int ver = 3;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, ver);
    }

    //only run if database does not exist
    @Override
    public void onCreate(SQLiteDatabase db){
        //create table "vehicle"
        db.execSQL(
                "create table " + VEHICLE_TABLE_NAME + "("+
                        VEHICLE_COLUMN_REGO + " text primary key, " +
                        VEHICLE_COLUMN_DISPLAY_NAME + " text, " +
                        VEHICLE_COLUMN_DESCRIPTION + " text)"
        );

        //create table "parking"
        db.execSQL(
                "create table " + PARKING_TABLE_NAME + "("+
                        PARKING_COLUMN_ID + " integer primary key autoincrement, " +
                        PARKING_COLUMN_DATE + " text, " +
                        PARKING_COLUMN_TIME + " text, " +
                        PARKING_COLUMN_REGO + " text, " +
                        PARKING_COLUMN_DURATION_HR + " integer, " +
                        PARKING_COLUMN_DURATION_MIN + " integer, " +
                        PARKING_COLUMN_CENTS_PER_HOUR + " integer, " +
                        PARKING_COLUMN_PARK_LOCATION_LAT + " double, " +
                        PARKING_COLUMN_PARK_LOCATION_LONG + " double, " +
                        "FOREIGN KEY (" + PARKING_COLUMN_REGO + ") REFERENCES " + VEHICLE_TABLE_NAME + "(" + VEHICLE_COLUMN_REGO + "))"
        );

    }

    //only called when version number is lower than requested in constructor to delete existing and call onCreate
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + VEHICLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PARKING_TABLE_NAME);
        onCreate(db);
    }

    //new vehicle
    public boolean insertVehicle(Vehicle v){
        SQLiteDatabase db = this.getWritableDatabase();

        //prepare row to insert
        ContentValues contentValues = new ContentValues();

        contentValues.put(VEHICLE_COLUMN_REGO, v.getRego());
        contentValues.put(VEHICLE_COLUMN_DISPLAY_NAME, v.getDisplayName());
        contentValues.put(VEHICLE_COLUMN_DESCRIPTION, v.getDescription());

        //insert row
        db.insert(VEHICLE_TABLE_NAME, null, contentValues);

        return true; //TODO need to verify that rego is unique
    }

    //get vehicle from rego given
    public Vehicle getVehicle(String rego){
        Vehicle v = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + VEHICLE_TABLE_NAME + " where " + VEHICLE_COLUMN_REGO + " is '" + rego + "'", null);

        if(res.moveToFirst()){
            v = new Vehicle(res.getString(res.getColumnIndex(VEHICLE_COLUMN_REGO)), res.getString(res.getColumnIndex(VEHICLE_COLUMN_DISPLAY_NAME)), res.getString(res.getColumnIndex(VEHICLE_COLUMN_DESCRIPTION)));
        }

        res.close();
        db.close();

        return v;
    }

    //update vehicle
    public boolean updateVehicle(Vehicle v){
        SQLiteDatabase db = this.getWritableDatabase();

        //prep new values
        ContentValues contentValues = new ContentValues();

        contentValues.put(VEHICLE_COLUMN_REGO, v.getRego());
        contentValues.put(VEHICLE_COLUMN_DISPLAY_NAME, v.getDisplayName());
        contentValues.put(VEHICLE_COLUMN_DESCRIPTION, v.getDescription());

        // run the update query
        db.update(VEHICLE_TABLE_NAME, contentValues, VEHICLE_COLUMN_REGO+ " = ? ",  new String[]{ v.getRego() } );

        return true;
    }

    //delete vehicle
    public Integer deleteVehicle (String rego) {

        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(PARKING_TABLE_NAME,
                PARKING_COLUMN_REGO + " = ?" ,  new String[] { rego });


        // delete contact
        return db.delete(VEHICLE_TABLE_NAME,
                VEHICLE_COLUMN_REGO + " = ? ", new String[] { rego });
    }

    //new parking instance
    public long insertParking(String rego, ParkedLocation pl) {
        long id;

        SQLiteDatabase db = this.getWritableDatabase();

        //prepare row to insert
        ContentValues contentValues = new ContentValues();

        contentValues.put(PARKING_COLUMN_REGO, rego);
        contentValues.put(PARKING_COLUMN_DATE, pl.getDate());
        contentValues.put(PARKING_COLUMN_TIME, pl.getParkedTime());
        contentValues.put(PARKING_COLUMN_DURATION_HR, pl.getDurationHr());
        contentValues.put(PARKING_COLUMN_DURATION_MIN, pl.getDurationMin());
        contentValues.put(PARKING_COLUMN_CENTS_PER_HOUR, pl.getCostPerHour());
        contentValues.put(PARKING_COLUMN_PARK_LOCATION_LAT, pl.getLatitude());
        contentValues.put(PARKING_COLUMN_PARK_LOCATION_LONG, pl.getLongitude());

        //insert row
        id = db.insert(PARKING_TABLE_NAME, null, contentValues);

        return id;
    }

    //update parking duration
    public boolean updateParking(long id, String rego, ParkedLocation pl){
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("myapp", "updating for id "+id);

        //prep new values
        ContentValues contentValues = new ContentValues();

        //commenting out those lines that shouldn't be updated
        //contentValues.put(PARKING_COLUMN_REGO, rego);
        //contentValues.put(PARKING_COLUMN_DATE, pl.getDate());
        //contentValues.put(PARKING_COLUMN_TIME, pl.getParkedTime());
        contentValues.put(PARKING_COLUMN_DURATION_HR, pl.getDurationHr());
        contentValues.put(PARKING_COLUMN_DURATION_MIN, pl.getDurationMin());
        contentValues.put(PARKING_COLUMN_CENTS_PER_HOUR, pl.getCostPerHour());
        //contentValues.put(PARKING_COLUMN_PARK_LOCATION_LAT, pl.getLatitude());
        //contentValues.put(PARKING_COLUMN_PARK_LOCATION_LONG, pl.getLongitude());

        // run the update query
        db.update(PARKING_TABLE_NAME, contentValues, PARKING_COLUMN_ID + " = ? ", new String[] { Long.toString(id) } );

        return true;
    }

    //delete parking, needed?

    //count vehicles
    public int numVehicles(){
        SQLiteDatabase db = this.getReadableDatabase();

        return (int) DatabaseUtils.queryNumEntries(db, VEHICLE_TABLE_NAME);
    }

    //get list of all vehicles
    public ArrayList<String> getVehicleList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + VEHICLE_TABLE_NAME, null );

        ArrayList<String> a = new ArrayList<>();
        if(res.moveToFirst()){
            do{
                a.add(res.getString(res.getColumnIndex(VEHICLE_COLUMN_REGO)));
            }while (res.moveToNext());
        }
        res.close();
        db.close();

        return a;
    }

    //get last vehicle used
    public String getLastVehicle(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PARKING_TABLE_NAME + " order by " + PARKING_COLUMN_ID + " desc limit 1", null);

        String rego = null;

        if(res.moveToFirst()){
            rego = res.getString(res.getColumnIndex(PARKING_COLUMN_REGO));
        }

        res.close();
        db.close();

        return rego;
    }

    //get all parking instances
    public ArrayList<String> getAllParking(){
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PARKING_TABLE_NAME, null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            //date, time, rego, duration, cost
            Integer costPH = res.getInt(res.getColumnIndex(PARKING_COLUMN_CENTS_PER_HOUR));
            Integer costDH = res.getInt(res.getColumnIndex(PARKING_COLUMN_DURATION_HR));
            Integer costDM = res.getInt(res.getColumnIndex(PARKING_COLUMN_DURATION_MIN));
            float calc = (((float)costPH * (float)costDH) + ((float)costPH/60 * (float)costDM))/100; //cast all integers to float to calc
            String cost = String.format("%.2f", calc); //converts calculate float to #.## format
            list.add(res.getString(res.getColumnIndex(PARKING_COLUMN_DATE))+" "+
                    res.getString(res.getColumnIndex(PARKING_COLUMN_TIME))+" "+
                    res.getString(res.getColumnIndex(PARKING_COLUMN_REGO))+" "+
                    res.getString(res.getColumnIndex(PARKING_COLUMN_DURATION_HR))+":"+
                    res.getString(res.getColumnIndex(PARKING_COLUMN_DURATION_MIN))+ " $" +
                    cost);

            res.moveToNext();
        }

        res.close();
        db.close();

        return list;
    }


    //get all parking instances for a vehicle
    public ArrayList<String> getParking(String rego){
        ArrayList<String> list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PARKING_TABLE_NAME + " where " + PARKING_COLUMN_REGO + " is '" + rego + "'", null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            //date, time, rego, duration, cost
            Integer costPH = res.getInt(res.getColumnIndex(PARKING_COLUMN_CENTS_PER_HOUR));
            Integer costDH = res.getInt(res.getColumnIndex(PARKING_COLUMN_DURATION_HR));
            Integer costDM = res.getInt(res.getColumnIndex(PARKING_COLUMN_DURATION_MIN));
            float calc = (((float)costPH * (float)costDH) + ((float)costPH/60 * (float)costDM))/100; //cast all integers to float to calc
            String cost = String.format("%.2f", calc); //converts calculate float to #.## format
            list.add(res.getString(res.getColumnIndex(PARKING_COLUMN_DATE))+" "+
                    res.getString(res.getColumnIndex(PARKING_COLUMN_TIME))+" "+
                    res.getString(res.getColumnIndex(PARKING_COLUMN_REGO))+" "+
                    res.getString(res.getColumnIndex(PARKING_COLUMN_DURATION_HR))+":"+
                    res.getString(res.getColumnIndex(PARKING_COLUMN_DURATION_MIN))+ " $" +
                    cost);

            res.moveToNext();
        }

        res.close();
        db.close();

        return list;
    }

    //get total cost for all vehicles
    public String getTotalCostAllVehicles(){
        float sum = 0;
        String total;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PARKING_TABLE_NAME, null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            Integer costPH = res.getInt(res.getColumnIndex(PARKING_COLUMN_CENTS_PER_HOUR));
            Integer costDH = res.getInt(res.getColumnIndex(PARKING_COLUMN_DURATION_HR));
            Integer costDM = res.getInt(res.getColumnIndex(PARKING_COLUMN_DURATION_MIN));
            float calc = (((float)costPH * (float)costDH) + ((float)costPH/60 * (float)costDM))/100;
            Log.d("myapp", "doing sums this trips was"+ calc);
            sum = sum + calc;
            Log.d("myapp", "doing sums total so far"+ sum);
            res.moveToNext();
        }

        res.close();
        db.close();

        total = String.format("%.2f", sum);
        Log.d("myapp", "doing sums strung out total "+total);

        return total;
    }

    //get total cost for this vehicle
    public String getTotalCostThisVehicle(String rego){
        float sum = 0;
        String total;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + PARKING_TABLE_NAME + " where " + PARKING_COLUMN_REGO + " is " + rego, null);
        res.moveToFirst();
        while(!res.isAfterLast()){
            Integer costPH = res.getInt(res.getColumnIndex(PARKING_COLUMN_CENTS_PER_HOUR));
            Integer costDH = res.getInt(res.getColumnIndex(PARKING_COLUMN_DURATION_HR));
            Integer costDM = res.getInt(res.getColumnIndex(PARKING_COLUMN_DURATION_MIN));
            float calc = (((float)costPH * (float)costDH) + ((float)costPH/60 * (float)costDM))/100;
            Log.d("myapp", "doing sums this trips was"+ calc);
            sum = sum + calc;
            Log.d("myapp", "doing sums total so far"+ sum);
            res.moveToNext();
        }

        res.close();
        db.close();

        total = String.format("%.2f", sum);
        Log.d("myapp", "doing sums strung out total "+total);

        return total;
    }
}

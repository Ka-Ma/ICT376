package au.edu.murdoch.ict376.a2.wheresmycar;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final String PARKING_COLUMN_DOLLARS_PER_HOUR = "dollarsPerHour";
    public static final String PARKING_COLUMN_PARK_LOCATION_LAT = "parkLocationLat";
    public static final String PARKING_COLUMN_PARK_LOCATION_LONG = "parkLocationLong";

    //version to change if tables need to be recreated
    static int ver = 1;

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
                        PARKING_COLUMN_DOLLARS_PER_HOUR + " decimal(10,2), " +
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

        return true;
    }

    //update vehicle

    //delete vehicle

    //new parking instance
    public boolean insertParking(String rego, ParkedLocation pl) {
        SQLiteDatabase db = this.getWritableDatabase();

        //prepare row to insert
        ContentValues contentValues = new ContentValues();

        contentValues.put(PARKING_COLUMN_REGO, rego);
        contentValues.put(PARKING_COLUMN_DATE, pl.getDate());
        contentValues.put(PARKING_COLUMN_TIME, pl.getParkedTime());
        contentValues.put(PARKING_COLUMN_DURATION_HR, pl.getDurationHr());
        contentValues.put(PARKING_COLUMN_DURATION_MIN, pl.getDurationMin());
        contentValues.put(PARKING_COLUMN_DOLLARS_PER_HOUR, pl.getCostPerHour());
        contentValues.put(PARKING_COLUMN_PARK_LOCATION_LAT, pl.getLatitude());
        contentValues.put(PARKING_COLUMN_PARK_LOCATION_LONG, pl.getLongitude());

        //insert row
        db.insert(PARKING_TABLE_NAME, null, contentValues);

        return true;
    }

    //update parking duration

    //delete parking, needed?

    //get list of all vehicles

    //get last vehicle used

    //get all parking instances

    //get all parking instances for a vehicle

    //get total cost for all vehicles

    //get total cost for this vehicle
}

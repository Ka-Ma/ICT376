package au.edu.murdoch.ict376.a2.wheresmycar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    AddVehicleFragment vehicleFragment;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHelper(this);

        if (savedInstanceState == null) {
            // if NOT first ever run (eg, no vehicles in db)
            if(mydb.numVehicles()>0) {
                homeFragment = homeFragment.newInstance();
                getFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();
            }else{
                vehicleFragment = vehicleFragment.newInstance("");
                getFragmentManager().beginTransaction().add(R.id.fragment_container, vehicleFragment).commit();
            }
        }else{
            Log.d(TAG, "savedInstanceState is not null");
            //homeFragment = (HomeFragment)getFragmentManager().findFragmentById(R.id.fragment_container);
        }

        BluetoothHelper bluetoothHelper = new BluetoothHelper(this);
        bluetoothHelper.enableBluetooth();
    }
}

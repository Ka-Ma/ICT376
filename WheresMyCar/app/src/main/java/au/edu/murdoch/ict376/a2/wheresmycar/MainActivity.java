package au.edu.murdoch.ict376.a2.wheresmycar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            // if not first ever run (eg, no vehicles in db)
            homeFragment = homeFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, homeFragment).commit();
            //else
            //vehicleFragment = vehicleFragment.newInstance();
            //getFragmentManager().beginTransaction().add(R.id.fragment_container, vehicleFragment).commit();
        }else{
            homeFragment = (HomeFragment)getFragmentManager().findFragmentById(R.id.fragment_container);
        }

        BluetoothHelper bluetoothHelper = new BluetoothHelper(this);
        bluetoothHelper.enableBluetooth();
    }
}

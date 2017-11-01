package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Cameron on 28/10/2017.
 */

public class BluetoothActivity extends Activity {
    BluetoothFragment bluetoothFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            //populate variables with extras
        }

        if (savedInstanceState == null) {
            bluetoothFragment = BluetoothFragment.newInstance();
            getFragmentManager().beginTransaction().replace(android.R.id.content, bluetoothFragment).commit();
        }else{
            bluetoothFragment = (BluetoothFragment)getFragmentManager().findFragmentById(android.R.id.content);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}

package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by Cameron on 28/10/2017.
 */

public class BluetoothHelper extends View{
    BluetoothAdapter bluetoothAdapter;

    public BluetoothHelper(Context context) {
        super(context);
    }

    // Enable bluetooth
    public void enableBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if device supports bluetooth
        if (bluetoothAdapter == null) {
            Log.d(TAG, "Bluetooth unavailable");
        }
        // Enable bluetooth if it is not enabled
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            getContext().startActivity(enableBluetoothIntent);
        }
    }
}
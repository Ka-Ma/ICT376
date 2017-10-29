package au.edu.murdoch.ict376.a2.wheresmycar;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by Cameron on 28/10/2017.
 */

public class BluetoothHelper extends View{
    BluetoothAdapter bluetoothAdapter;

    public BluetoothHelper(Context context) {
        super(context);
    }

    // Check if Bluetooth is on
    public boolean isOn() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return true;
    }

    // Enable Bluetooth
    public void enableBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if device supports bluetooth
        if (bluetoothAdapter == null) {
            Log.d(TAG, "Bluetooth unavailable");
            //Toast.makeText(getContext(), "Bluetooth is not supported on this device.", Toast.LENGTH_LONG).show();
        }
        // Enable bluetooth if it is not enabled
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            getContext().startActivity(enableBluetoothIntent);
        }
    }

    // Discover Bluetooth
    public void discoverBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery(); // Cancel any current discovery
            bluetoothAdapter.startDiscovery();
        }
        if (!bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.startDiscovery();
        }
    }
}
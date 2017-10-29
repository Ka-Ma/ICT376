package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Kat on 24/10/2017.
 */

public class BluetoothFragment extends Fragment {

    //members
    boolean mDualPane;
    private BluetoothHelper bluetoothHelper;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    private Button buttonBluetooth;
    private ListView listviewBluetoothDevices;
    public ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();

    public static BluetoothFragment newInstance(){
        BluetoothFragment f = new BluetoothFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bluetooth_layout, container, false);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Register a broadcast receiver for connection change
        IntentFilter connectionChangeFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        getActivity().registerReceiver(connectionBroadcastReceiver, connectionChangeFilter);

        // Register a broadcast receiver for Bluetooth turning on
        IntentFilter bluetoothOnFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(bluetoothOnReceiver, bluetoothOnFilter);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothHelper = new BluetoothHelper(getActivity());
        if (bluetoothAdapter.isEnabled()) {
            showDiscoveredDevices();
            // Register a broadcast receiver for UUID service discovery protocol
            IntentFilter uuidSdpFilter = new IntentFilter(BluetoothDevice.ACTION_UUID);
            getActivity().registerReceiver(uuidBroadcastReceiver, uuidSdpFilter);
        } else {
            bluetoothHelper.enableBluetooth();
        }
    }

    public void showDiscoveredDevices() {
        // Create an intent filter for discovered devices
        IntentFilter discoverIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // Register the broadcast receiver for discovered devices
        getActivity().registerReceiver(discoverBroadcastReceiver, discoverIntent);

        bluetoothHelper = new BluetoothHelper(getActivity());
        bluetoothHelper.discoverBluetooth();
    }
    // Broadcast receiver for discovered devices
    private BroadcastReceiver discoverBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            ArrayList btDeviceNameList = new ArrayList();

            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "ACTION_FOUND: device name: " + device.getName());
                Log.d(TAG, "ACTION_FOUND: device address: " + device.getAddress());
                bluetoothDevices.add(device);
                btDeviceNameList.add(device.getName());
            }

            // Put discovered devices into a list view
            ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, btDeviceNameList);
            listviewBluetoothDevices = (ListView) getActivity().findViewById(R.id.listviewBluetoothDevices);
            listviewBluetoothDevices.setAdapter(arrayAdapter);
            // Set on-click listeners for devices list
            listviewBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                    bluetoothAdapter.cancelDiscovery(); // Make sure discovery is cancelled when item selected

                    String deviceName = bluetoothDevices.get(i).getName();
                    String deviceAddress = bluetoothDevices.get(i).getAddress();

                    Log.d(TAG, "AdapterView: item clicked: " + deviceName);
                    Log.d(TAG, "AdapterView: item clicked: " + deviceAddress);

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        bluetoothDevices.get(i).fetchUuidsWithSdp(); // Get UUID's of the selected device using service discovery protocol
                        bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress); // Get the MAC address of the selected device
                    }
                }
            });
        }
    };

    private BroadcastReceiver bluetoothOnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    showDiscoveredDevices();
                    // Register a broadcast receiver for UUID service discovery protocol
                    IntentFilter uuidSdpFilter = new IntentFilter(BluetoothDevice.ACTION_UUID);
                    getActivity().registerReceiver(uuidBroadcastReceiver, uuidSdpFilter);
                }
            }
        }
    };

    private BroadcastReceiver uuidBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_UUID)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                // Loop through UUID's to make a connection
                for (int i = 0; i < uuidExtra.length; i++) {
                    UUID deviceUUID = UUID.fromString(uuidExtra[i].toString());
                    Log.d(TAG, "UUID: " + uuidExtra[i].toString());
                    try {
                        bluetoothDevice.createRfcommSocketToServiceRecord(deviceUUID).connect();
                        Log.d(TAG, "***Connection successful***" + uuidExtra[i].toString());
                        return;
                    } catch (IOException connectException) {
                        Log.d(TAG, "Connection failed: " + connectException);
                        try {
                            bluetoothDevice.createRfcommSocketToServiceRecord(deviceUUID).close();
                        } catch (IOException closeException) {
                            Log.e(TAG, "Could not close the client socket", closeException);
                        }
                    }
                }
            }
        }
    };

    // Broadcast receiver for connection state
    private BroadcastReceiver connectionBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                Log.d(TAG, "Bluetooth connection lost.");
                // Record parking location
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(bluetoothOnReceiver);
        getActivity().unregisterReceiver(uuidBroadcastReceiver);
        getActivity().unregisterReceiver(connectionBroadcastReceiver);
    }
}

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

        // Show currently available devices in a list on fragment creation
        showDiscoveredDevices();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Register a broadcast receiver for connection change
        IntentFilter connectionChangeFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        getActivity().registerReceiver(connectionBroadcastReceiver, connectionChangeFilter);
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
                    bluetoothAdapter.cancelDiscovery();

                    String deviceName = bluetoothDevices.get(i).getName();
                    String deviceAddress = bluetoothDevices.get(i).getAddress();


                    Log.d(TAG, "AdapterView: item clicked: " + deviceName);
                    Log.d(TAG, "AdapterView: item clicked: " + deviceAddress);

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        //Log.d(TAG, "Trying to pair with: " + deviceName);
                        //bluetoothDevices.get(i).createBond();
                        UUID deviceUUID = bluetoothDevices.get(i).getUuids()[1].getUuid();
                        //UUID deviceUUID = UUID.fromString("7bdcf245-85d0-4d73-8a41-8f519fb28e6d");
                        bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress);
                        try {
                            bluetoothDevice.createRfcommSocketToServiceRecord(deviceUUID).connect();
                            Log.d(TAG, "Connection successful: " + bluetoothDevice);
                        } catch (IOException connectException) {
                            Log.d(TAG, "Connection failed: " + bluetoothDevice + " " + connectException);
                            try {
                                bluetoothDevice.createRfcommSocketToServiceRecord(deviceUUID).close();
                            } catch (IOException closeException) {
                                Log.e(TAG, "Could not close the client socket", closeException);
                            }
                        }
                    }
                }
            });
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
}

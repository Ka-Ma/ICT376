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
import android.widget.TextView;
import android.widget.Toast;
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
    private TextView textViewDeviceStatus;
    ListView listViewBluetoothDevices;
    public ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    ArrayList<String> btDeviceList = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    static final String DEVICES_STATUS = "devicesStatus";
    static final String DEVICE_NAME_LIST = "listViewDevices";
    static final String DEVICE_LIST = "bluetoothArrayList";

    public static BluetoothFragment newInstance(){
        BluetoothFragment f = new BluetoothFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.bluetooth_layout, container, false);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            textViewDeviceStatus = (TextView) getActivity().findViewById(R.id.textViewDeviceStatus);
            textViewDeviceStatus.setText(savedInstanceState.getString(DEVICES_STATUS));
            btDeviceList = savedInstanceState.getStringArrayList(DEVICE_NAME_LIST);
            bluetoothDevices = savedInstanceState.getParcelableArrayList(DEVICE_LIST);
            populateListView();
        } else {
            textViewDeviceStatus = (TextView) getActivity().findViewById(R.id.textViewDeviceStatus);
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothHelper = new BluetoothHelper(getActivity());
            if (bluetoothAdapter.isEnabled()) {
                startBluetoothDiscovery();
                /*// Register a broadcast receiver for UUID service discovery protocol
                IntentFilter uuidSdpFilter = new IntentFilter(BluetoothDevice.ACTION_UUID);
                getActivity().registerReceiver(uuidBroadcastReceiver, uuidSdpFilter);*/
            } else {
                bluetoothHelper.enableBluetooth();
            }
        }

        // Register a broadcast receiver for connection change
        IntentFilter connectionChangeFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        connectionChangeFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        getActivity().registerReceiver(connectionBroadcastReceiver, connectionChangeFilter);

        // Register a broadcast receiver for Bluetooth turning on
        IntentFilter bluetoothOnFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(bluetoothOnBroadcastReceiver, bluetoothOnFilter);

        // Create an intent filter for discovered devices
        IntentFilter discoverIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // Register the broadcast receiver for discovered devices
        getActivity().registerReceiver(discoverBroadcastReceiver, discoverIntent);

        // Register a broadcast receiver for finish of Bluetooth discovery
        IntentFilter discoveryFinishedFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(discoveryFinishedBroadcastReceiver, discoveryFinishedFilter);

        /*// Register a broadcast receiver for UUID service discovery protocol
        IntentFilter uuidSdpFilter = new IntentFilter(BluetoothDevice.ACTION_UUID);
        getActivity().registerReceiver(uuidBroadcastReceiver, uuidSdpFilter);*/

        // Button to refresh the discovered devices list
        Button btnRefresh = (Button) getActivity().findViewById(R.id.btn_refresh_bluetooth);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the ArrayLists and ListView before rediscovery
                bluetoothDevices.clear();
                btDeviceList.clear();
                if (arrayAdapter != null) arrayAdapter.notifyDataSetChanged(); // Clear the devices list before re-discovery

                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                bluetoothHelper = new BluetoothHelper(getActivity());
                if (bluetoothAdapter.isEnabled()) {
                    startBluetoothDiscovery();
                    /*// Register a broadcast receiver for UUID service discovery protocol
                    IntentFilter uuidSdpFilter = new IntentFilter(BluetoothDevice.ACTION_UUID);
                    getActivity().registerReceiver(uuidBroadcastReceiver, uuidSdpFilter);*/
                } else {
                    bluetoothHelper.enableBluetooth();
                }
            }
        });
    }

    public void startBluetoothDiscovery() {
        /*// Create an intent filter for discovered devices
        IntentFilter discoverIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        // Register the broadcast receiver for discovered devices
        getActivity().registerReceiver(discoverBroadcastReceiver, discoverIntent);*/

        bluetoothHelper = new BluetoothHelper(getActivity());
        bluetoothHelper.discoverBluetooth();

        textViewDeviceStatus = (TextView) getActivity().findViewById(R.id.textViewDeviceStatus);
        textViewDeviceStatus.setText("Discovering devices...");
        Log.d(TAG, "Bluetooth discovery started...");
    }

    // Broadcast receiver for discovered devices
    private BroadcastReceiver discoverBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            // If devices are found put their names and addresses into an array
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG, "Bluetooth device found: name: " + device.getName());
                Log.d(TAG, "Bluetooth device found: address: " + device.getAddress());

                // Devices can be "discovered" more than once which results in multiple entries in the ListView,
                // so check if it is already in the ArrayList before adding it
                boolean found = bluetoothDevices.contains(device);

                if (found == true) {
                    Log.d(TAG, "Device already found.");
                } else {
                    Log.d(TAG, "Device added to bluetoothDevices ArrayList.");
                    bluetoothDevices.add(device);
                    // If the device name is not null use it, otherwise use the MAC address
                    if (device.getName() != null)
                        btDeviceList.add(device.getName());
                    else
                        btDeviceList.add(device.getAddress());

                    updateListView();
                }
            }
        }
    };

    public void updateListView() {
        // Put discovered devices into a ListView
        if (btDeviceList.size() == 1) {
            arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, btDeviceList);
            listViewBluetoothDevices = (ListView) getActivity().findViewById(R.id.listviewBluetoothDevices);
            listViewBluetoothDevices.setAdapter(arrayAdapter);
        } else {
            arrayAdapter.notifyDataSetChanged();
        }

        // Set on-click listeners for devices list
        listViewBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                bluetoothAdapter.cancelDiscovery(); // Make sure discovery is cancelled when item selected

                String deviceName = bluetoothDevices.get(i).getName();
                String deviceAddress = bluetoothDevices.get(i).getAddress();
                Log.d(TAG, "ListView item clicked: " + i + ": " + deviceName + ", " + deviceAddress);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    bluetoothDevices.get(i).fetchUuidsWithSdp(); // Get UUID's of the selected device using service discovery protocol
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress); // Get the MAC address of the selected device
                }
            }
        });
    }

    // Broadcast receiver for switching Bluetooth on
    private BroadcastReceiver bluetoothOnBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    // Register a broadcast receiver for UUID service discovery protocol
                    IntentFilter uuidSdpFilter = new IntentFilter(BluetoothDevice.ACTION_UUID);
                    getActivity().registerReceiver(uuidBroadcastReceiver, uuidSdpFilter);
                    // Begin discovery
                    startBluetoothDiscovery();
                }
            }
        }
    };

    // Broadcast receiver for when bluetooth has finished the discovery process
    private BroadcastReceiver discoveryFinishedBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Bluetooth discovery finished.");
            final String action = intent.getAction();

            if (btDeviceList.isEmpty()) {
                Log.d(TAG, "No devices found.");
                textViewDeviceStatus.setText(R.string.no_bt_devices);
            } else {
                Log.d(TAG, "Devices found.");
                textViewDeviceStatus.setText(R.string.bt_tap_device);
                //populateListView();
            }
        }
    };

    private void populateListView() {
        // Put discovered devices into a ListView
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, btDeviceList);
        ListView listViewBluetoothDevices = (ListView) getActivity().findViewById(R.id.listviewBluetoothDevices);
        listViewBluetoothDevices.setAdapter(arrayAdapter);
        //arrayAdapter.notifyDataSetChanged();
        // Set on-click listeners for devices list
        listViewBluetoothDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                bluetoothAdapter.cancelDiscovery(); // Make sure discovery is cancelled when item selected

                String deviceName = bluetoothDevices.get(i).getName();
                String deviceAddress = bluetoothDevices.get(i).getAddress();
                Log.d(TAG, "ListView item clicked: " + i + ": " + deviceName + ", " + deviceAddress);

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    bluetoothDevices.get(i).fetchUuidsWithSdp(); // Get UUID's of the selected device using service discovery protocol
                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceAddress); // Get the MAC address of the selected device
                }
            }
        });
    }

    // Broadcast receiver for remote Bluetooth UUID's
    private BroadcastReceiver uuidBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_UUID)) {
                //BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Parcelable[] uuidExtra = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID);
                // Loop through devices UUID's to make a connection
                for (int i = 0; i < uuidExtra.length; i++) {
                    UUID deviceUUID = UUID.fromString(uuidExtra[i].toString());
                    Log.d(TAG, "UUID: " + uuidExtra[i].toString());
                    try {
                        bluetoothDevice.createRfcommSocketToServiceRecord(deviceUUID).connect();
                        Log.d(TAG, "***Connection successful*** " + uuidExtra[i].toString());
                        Toast.makeText(getActivity(), "Connected", Toast.LENGTH_LONG).show();
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
    // For this application if a connection is lost the users (parking) location is recorded
    private BroadcastReceiver connectionBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_ACL_CONNECTED)) Log.d(TAG, "Bluetooth connection made.");
            if(action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED)) {
                Log.d(TAG, "Bluetooth connection lost.");
                Toast.makeText(getActivity(), R.string.bt_connection_lost, Toast.LENGTH_LONG).show();
                // Do some stuff
                // Record parking location
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String devicesStatus = textViewDeviceStatus.getText().toString();
        outState.putString(DEVICES_STATUS, devicesStatus);

        outState.putStringArrayList(DEVICE_NAME_LIST, btDeviceList);

        outState.putParcelableArrayList(DEVICE_LIST, bluetoothDevices);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bluetoothAdapter.cancelDiscovery();
        getActivity().unregisterReceiver(bluetoothOnBroadcastReceiver);
        getActivity().unregisterReceiver(uuidBroadcastReceiver);
        getActivity().unregisterReceiver(connectionBroadcastReceiver);
        getActivity().unregisterReceiver(discoverBroadcastReceiver);
        getActivity().unregisterReceiver(discoveryFinishedBroadcastReceiver);
    }
}

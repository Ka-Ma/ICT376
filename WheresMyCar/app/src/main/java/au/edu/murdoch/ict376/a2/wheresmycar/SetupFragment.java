package au.edu.murdoch.ict376.a2.wheresmycar;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static android.content.ContentValues.TAG;

/**
 * Created by Cameron on 28/10/2017.
 */

public class SetupFragment extends Fragment {
    Button btnAddCar;
    Button btnBluetooth;

    public static SetupFragment newInstance(){
        SetupFragment f = new SetupFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_layout, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnAddCar = getActivity().findViewById(R.id.btn_add_car);
        btnBluetooth = getActivity().findViewById(R.id.btn_bluetooth);

        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Setup -> Bluetooth: clicked");
                Bundle dataBundle = new Bundle();
                //add stuff to bundle

                /*BluetoothFragment bluetoothFragment = BluetoothFragment.newInstance();
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout, bluetoothFragment).commit();*/

                Intent intent = new Intent(getActivity().getApplicationContext(), BluetoothActivity.class);
                //intent.putExtras(dataBundle);

                startActivity(intent);
            }
        });
    }
}

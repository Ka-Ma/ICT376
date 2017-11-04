package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kat on 24/10/2017.
 */

public class HomeFragment extends Fragment{

    //view
    boolean mDualPane;
    View    mLayoutView;

    //members
    Button mBtnRecordPark;
    Spinner mSprVehicle;
    Button mBtnHistory;
    Button mBtnSetup;
	
    DBHelper mydb;
    String rego;

    public static HomeFragment newInstance(){
        HomeFragment f = new HomeFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.home_layout, container, false);

        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());

        //link ui
        mBtnRecordPark = getActivity().findViewById(R.id.btn_record_park);
        mBtnSetup = getActivity().findViewById(R.id.btn_setup);
        mSprVehicle = getActivity().findViewById(R.id.vehicles_spinner);
        mBtnHistory = getActivity().findViewById(R.id.btn_history);


        mBtnRecordPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id;

                //TODO get location!
                double latitude = -32.066567;
                double longitude = 115.831996;

                //setup date & time
                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                String time = new SimpleDateFormat("hh:mm a").format(new Date());


                ParkedLocation pl = new ParkedLocation(longitude, latitude, time, date, 0, 0, 0);

                id = mydb.insertParking(rego, pl);
                Log.d("myapp", "the id returned was "+id);


                Bundle dataBundle = new Bundle();
                //add stuff to bundle
                dataBundle.putString("rego", rego);
                dataBundle.putLong("id", id);

                Intent intent = new Intent(getActivity().getApplicationContext(), FeeOrTimeLimitActivity.class);
                intent.putExtras(dataBundle);

                startActivity(intent);

                /*View detailsFrame = getActivity().findViewById(R.id.right_fragment_container);
                mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                if (mDualPane) {
                    // display on the same Activity
                    //if dialog reply is yes
                    FeeOrTimeLimitFragment checkTimeOrFee = FeeOrTimeLimitFragment.newInstance();


                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.right_fragment_container, checkTimeOrFee);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }else {
                    Bundle dataBundle = new Bundle();
                    //add stuff to bundle
                    dataBundle.putString("rego", rego); //where second rego is from currently chosen value on spinner


                    Intent intent = new Intent(getActivity().getApplicationContext(), FeeOrTimeLimitActivity.class);
                    //intent.putExtras(dataBundle);

                    startActivity(intent);
                }*/
            }
        });


        //setup spinner //TODO need to add something to refresh the list after adding a car, maybe in onResume()?
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mydb.getVehicleList());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSprVehicle.setAdapter(adapter);
        String lastVeh;
        if((lastVeh = mydb.getLastVehicle()) != null)
            mSprVehicle.setSelection(adapter.getPosition(lastVeh));

        mSprVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rego = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
		
		mBtnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View detailsFrame = getActivity().findViewById(R.id.right_fragment_container);
                mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                if (mDualPane) {

                    // display on the same Activity

                    HistoryFragment history = HistoryFragment.newInstance();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.right_fragment_container, history);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                }else {

                    Bundle dataBundle = new Bundle();
                    //add stuff to bundle


                    Intent intent = new Intent(getActivity().getApplicationContext(), HistoryActivity.class);
                    intent.putExtras(dataBundle);

                    startActivity(intent);
                }

            }
        });
		
        mBtnSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle dataBundle = new Bundle();
                //add stuff to bundle

                Intent intent = new Intent(getActivity().getApplicationContext(), SetupActivity.class);
                //intent.putExtras(dataBundle);

                startActivity(intent);

                /*View detailsFrame = getActivity().findViewById(R.id.right_fragment_container);
                mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                if (mDualPane) {
                    // display on the same Activity
                    //if dialog reply is yes
                    SetupFragment setupFragment = SetupFragment.newInstance();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.right_fragment_container, setupFragment);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }else {
                    Bundle dataBundle = new Bundle();
                    //add stuff to bundle

                    Intent intent = new Intent(getActivity().getApplicationContext(), SetupActivity.class);

                    //intent.putExtras(dataBundle);

                    startActivity(intent);

                }*/
            }
        });
    }
}

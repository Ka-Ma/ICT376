package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kat on 24/10/2017.
 */

public class VehicleListFragment extends Fragment {

    //members
    boolean mDualPane;
    View mLayoutView;
    ListView obj;

    DBHelper mydb;
    ArrayList<String> list;
    ArrayAdapter adapter;


    public static VehicleListFragment newInstance(){

        VehicleListFragment f = new VehicleListFragment();
        return f;
    }

    @Override
    public void onResume(){
        super.onResume();

        list = mydb.getVehicleList();
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        obj.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.vehicle_list_layout, container, false);

        return mLayoutView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());


        list = mydb.getVehicleList();
        obj = (ListView)mLayoutView.findViewById(R.id.list_vehicle);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        obj.setAdapter(adapter);

        // At the click on an item, start a new activity that will display the content of the database
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                // The index of the vehicle that will be shown in the new activity DislayActivity.java
                String rego=(String)arg0.getItemAtPosition(position);
                Log.d("myapp", "hopefull the rego "+rego);

                //
                Bundle dataBundle = new Bundle();
                dataBundle.putString("rego", rego);
                Intent intent = new Intent(getActivity().getApplicationContext(),AddVehicleActivity.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });

    }
}

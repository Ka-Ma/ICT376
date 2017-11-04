package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Kat on 24/10/2017.
 */

public class AddVehicleFragment extends Fragment {

    boolean mDualPane;
    View mLayoutView;
    EditText mRego;
    EditText mName;
    EditText mDescription;
    Button mSubmit;
    Button mDelete;

    DBHelper mydb;
    String rego = null;

    public static AddVehicleFragment newInstance(String rego){

        AddVehicleFragment f = new AddVehicleFragment();

        Bundle args = new Bundle();
        args.putString("rego", rego);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.vehicle_layout, container, false);

        return mLayoutView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());
        rego = getArguments().getString("rego");

        //link ui
        mRego = getActivity().findViewById(R.id.edit_rego);
        mName = getActivity().findViewById(R.id.edit_vehicle_name);
        mDescription = getActivity().findViewById(R.id.edit_vehicle_description);
        mSubmit = getActivity().findViewById(R.id.button_submit_vehicle);

        if(rego != null){ //editing existing vehicle
            mDelete = getActivity().findViewById(R.id.button_delete_vehicle);
            mDelete.setVisibility(View.VISIBLE);

            Vehicle oV = mydb.getVehicle(rego);

            mRego.setText(oV.getRego());
            mRego.setEnabled(false);
            mRego.setFocusable(false);
            mRego.setKeyListener(null);
            mName.setText(oV.getDisplayName());
            mDescription.setText(oV.getDescription());

            mSubmit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    View detailsFrame = getActivity().findViewById(R.id.right_fragment_container);
                    mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;



                    Vehicle v = new Vehicle(mRego.getText().toString(), mName.getText().toString(), mDescription.getText().toString());
                    mydb.updateVehicle(v);
                    //return to previous activity after update.
                    getActivity().onBackPressed();

                }
            });

            mDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    View detailsFrame = getActivity().findViewById(R.id.right_fragment_container);
                    mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                    mydb.deleteVehicle(mRego.getText().toString());

                    //return to previous activity after
                    getActivity().onBackPressed();
                }
            });

        }else { //adding new vehicle

            mSubmit.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    View detailsFrame = getActivity().findViewById(R.id.right_fragment_container);
                    mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                    if(TextUtils.isEmpty(mRego.getText())){
                        Toast.makeText(getActivity(), getString(R.string.regoWarning), Toast.LENGTH_SHORT).show();
                    } else {
                        Vehicle v = new Vehicle(mRego.getText().toString(), mName.getText().toString(), mDescription.getText().toString());
                        mydb.insertVehicle(v);
                        //return to previous activity after new vehicle added.
                        getActivity().onBackPressed();

                    }
                }
            });
        }



    }
}

package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kat on 24/10/2017.
 */

public class AddVehicleFragment extends Fragment {

    //members
    boolean mDualPane;
    View mLayoutView;


    public static AddVehicleFragment newInstance(){

        AddVehicleFragment f = new AddVehicleFragment();
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




    }
}

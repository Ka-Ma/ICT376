package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kat on 24/10/2017.
 */

public class DurationCostFragment extends Fragment {

    //members
    boolean mDualPane;
    View mLayoutView;
    EditText mDurationHr;
    EditText mDurationMin;
    EditText mCost; //NOTE entered in as decimal dollars and cents, stored in database as integer cents
    Button mBtnNext;

    DBHelper mydb;

    public static DurationCostFragment newInstance(String rego, Long id){

        DurationCostFragment f = new DurationCostFragment();

        Bundle args = new Bundle();
        args.putString("rego", rego);
        args.putLong("id", id);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.duration_cost_layout, container, false);

        return mLayoutView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());

        //link ui
        mDurationHr = getActivity().findViewById(R.id.edit_dur_hour);
        mDurationMin = getActivity().findViewById(R.id.edit_dur_min);
        mCost = getActivity().findViewById(R.id.edit_cost);
        mBtnNext = getActivity().findViewById(R.id.button_dur_cost_Next);

        mBtnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                View detailsFrame = getActivity().findViewById(R.id.right_fragment_container);
                mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;


                //check for empty or invalid entry
                int hr, min, cost = 0;
                float costF;
                try{
                    hr = Integer.parseInt(mDurationHr.getText().toString());
                }catch(NumberFormatException nfe){
                    hr = 0;
                }
                try{
                    min = Integer.parseInt(mDurationMin.getText().toString());
                }catch(NumberFormatException nfe){
                    min = 0;
                }
                try{
                    costF = Float.parseFloat(mCost.getText().toString());
                }catch(NumberFormatException nfe){
                    costF = 0;
                    cost = 0;
                }
                if(costF>0){
                    //convert dollars to cents and integer
                    cost = Math.round(costF*100);
                }

                ParkedLocation pl = new ParkedLocation(0, 0, "", "", hr, min, cost);

                mydb.updateParking(getArguments().getLong("id"), getArguments().getString("rego"), pl);

                if(mDualPane){
                    //display on same activity
                    WaitingFragment wait = WaitingFragment.newInstance();
                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("durHr", Integer.parseInt(mDurationHr.getText().toString()));
                    dataBundle.putInt("durMin", Integer.parseInt(mDurationMin.getText().toString()));
                    wait.setArguments(dataBundle);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.right_fragment_container, wait);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                }else {

                    Bundle dataBundle = new Bundle();
                    //dataBundle.putString("rego", getArguments().getString("rego"));
                    //dataBundle.putInt("durHr", Integer.parseInt(mDurationHr.getText().toString()));
                    //dataBundle.putInt("durMin", Integer.parseInt(mDurationMin.getText().toString()));
                    //dataBundle.putInt("cost", Integer.parseInt(mCost.getText().toString()));

                    Intent intent = new Intent(getActivity().getApplicationContext(), WaitingActivity.class);
                    intent.putExtras(dataBundle);

                    startActivity(intent);
                }

            }
        });
    }
}

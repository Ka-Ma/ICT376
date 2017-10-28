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

    public static DurationCostFragment newInstance(){

        DurationCostFragment f = new DurationCostFragment();
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

                if(mDualPane){
                    //display on same activity
                    WaitingFragment wait = WaitingFragment.newInstance();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.right_fragment_container, wait);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();

                }else {

                    Bundle dataBundle = new Bundle();
                    //dataBundle.putInt("id", 0);

                    Intent intent = new Intent(getActivity().getApplicationContext(), WaitingActivity.class);
                    intent.putExtras(dataBundle);   //

                    startActivity(intent);          //
                }

            }
        });
    }
}

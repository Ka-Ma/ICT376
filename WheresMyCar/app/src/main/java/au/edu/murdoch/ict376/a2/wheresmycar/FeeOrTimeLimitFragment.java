package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Cameron on 29/10/2017.
 */

public class FeeOrTimeLimitFragment extends Fragment {
    Button btn_yes;
    Button btn_no;
    boolean mDualPane;

    public static FeeOrTimeLimitFragment newInstance(String rego, Long id){
        FeeOrTimeLimitFragment f = new FeeOrTimeLimitFragment();

        Bundle args = new Bundle();
        args.putString("rego", rego);
        args.putLong("id", id);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.check_fee_time_layout, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_no = (Button) getActivity().findViewById(R.id.btn_no);

        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View detailsFrame = getActivity().findViewById(R.id.right_fragment_container);
                mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                // Pass -1 to WaitingFragment as a flag indicating there is no time limit
                long totalTime = -1;

                if (mDualPane) {
                    // Display on the same Activity

                    WaitingFragment waitingFragment = WaitingFragment.newInstance(totalTime);

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.right_fragment_container, waitingFragment);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }else {
                    Bundle dataBundle = new Bundle();

                    dataBundle.putLong("DURATION", totalTime);
                    Intent intent = new Intent(getActivity().getApplicationContext(), WaitingActivity.class);
                    intent.putExtras(dataBundle);

                    startActivity(intent);
                }
            }
        });

        btn_yes = (Button) getActivity().findViewById(R.id.btn_yes);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View detailsFrame = getActivity().findViewById(R.id.right_fragment_container);
                mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

                if (mDualPane) {
                    // display on the same Activity
                    // if dialog reply is yes
                    DurationCostFragment durationCostFragment = DurationCostFragment.newInstance(getArguments().getString("rego"), getArguments().getLong("id"));

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.right_fragment_container, durationCostFragment);

                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }else {
                    Bundle dataBundle = new Bundle();
                    //add stuff to bundle
                    dataBundle.putString("rego", getArguments().getString("rego"));
                    dataBundle.putLong("id", getArguments().getLong("id"));

                    Intent intent = new Intent(getActivity().getApplicationContext(), DurationCostActivity.class);
                    intent.putExtras(dataBundle);

                    startActivity(intent);
                }
            }
        });
    }
}

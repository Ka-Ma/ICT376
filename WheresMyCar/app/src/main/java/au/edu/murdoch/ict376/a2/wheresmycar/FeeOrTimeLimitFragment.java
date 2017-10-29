package au.edu.murdoch.ict376.a2.wheresmycar;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Cameron on 29/10/2017.
 */

public class FeeOrTimeLimitFragment extends Fragment {
    Button btnYes;
    Button btnNo;

    public static FeeOrTimeLimitFragment newInstance(){
        FeeOrTimeLimitFragment f = new FeeOrTimeLimitFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.check_fee_time_layout, container, false);

        return v;
    }
}

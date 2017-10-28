package au.edu.murdoch.ict376.a2.wheresmycar;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Cameron on 28/10/2017.
 */

public class SetupFragment extends Fragment {
    public static SetupFragment newInstance(){
        SetupFragment f = new SetupFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.setup_layout, container, false);

        return v;
    }
}

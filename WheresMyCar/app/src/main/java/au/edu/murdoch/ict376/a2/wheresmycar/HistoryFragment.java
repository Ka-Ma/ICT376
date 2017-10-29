package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kat on 24/10/2017.
 */

public class HistoryFragment extends Fragment {

    //members
    boolean mDualPane;
    View mLayoutView;
    ListView obj;
    TextView mHistoryTotal;

    DBHelper mydb;


    public static HistoryFragment newInstance(){

        HistoryFragment f = new HistoryFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.history_layout, container, false);

        return mLayoutView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        mydb = new DBHelper(getActivity());
        mHistoryTotal = getActivity().findViewById(R.id.history_total);


        ArrayList<String> list = mydb.getAllParking();
        obj = (ListView)mLayoutView.findViewById(R.id.list_history);
        obj.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list));

        mHistoryTotal.setText(getString(R.string.lbl_history_total).concat(mydb.getTotalCostAllVehicles()));

    }
}

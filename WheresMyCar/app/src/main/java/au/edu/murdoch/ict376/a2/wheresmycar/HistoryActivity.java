package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Kat on 24/10/2017.
 */

public class HistoryActivity extends Activity {
    HistoryFragment historyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            //populate variables with extras
        }

        if (savedInstanceState == null) {
            historyFragment = HistoryFragment.newInstance();
            getFragmentManager().beginTransaction().replace(android.R.id.content, historyFragment).commit();
        }else{
            historyFragment = (HistoryFragment)getFragmentManager().findFragmentById(android.R.id.content);
        }


    }
}

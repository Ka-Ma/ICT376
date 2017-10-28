package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Kat on 24/10/2017.
 */

public class WaitingActivity extends Activity {
    WaitingFragment waitingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            //populate variables with extras
        }

        if (savedInstanceState == null) {
            waitingFragment = WaitingFragment.newInstance();
            getFragmentManager().beginTransaction().replace(android.R.id.content, waitingFragment).commit();
        }else{
            waitingFragment = (WaitingFragment)getFragmentManager().findFragmentById(android.R.id.content);
        }


    }
}

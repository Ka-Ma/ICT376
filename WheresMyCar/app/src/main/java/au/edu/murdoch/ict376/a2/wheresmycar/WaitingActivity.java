package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Kat on 24/10/2017.
 * Created by Cameron on 29/10/2017.
 * Fight you for it!
 */

public class WaitingActivity extends Activity {
    WaitingFragment mWaitingFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            //populate variables with extras
        }

        if (savedInstanceState == null) {
            mWaitingFragment = WaitingFragment.newInstance();
            getFragmentManager().beginTransaction().replace(android.R.id.content, mWaitingFragment).commit();
        }else{
            mWaitingFragment = (WaitingFragment)getFragmentManager().findFragmentById(android.R.id.content);
        }
    }
}

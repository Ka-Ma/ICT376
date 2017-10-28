package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Cameron on 28/10/2017.
 */

public class SetupActivity extends Activity{
    SetupFragment setupFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            //populate variables with extras
        }

        if (savedInstanceState == null) {
            setupFragment = SetupFragment.newInstance();
            getFragmentManager().beginTransaction().replace(android.R.id.content, setupFragment).commit();
        }else{
            setupFragment = (SetupFragment)getFragmentManager().findFragmentById(android.R.id.content);
        }
    }
}

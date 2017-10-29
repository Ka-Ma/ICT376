package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Cameron on 29/10/2017.
 */

public class FeeOrTimeLimitActivity extends Activity {
    FeeOrTimeLimitFragment feeOrTimeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            //populate variables with extras
        }

        if (savedInstanceState == null) {
            feeOrTimeFragment = FeeOrTimeLimitFragment.newInstance();
            getFragmentManager().beginTransaction().replace(android.R.id.content, feeOrTimeFragment).commit();
        }else{
            feeOrTimeFragment = (FeeOrTimeLimitFragment)getFragmentManager().findFragmentById(android.R.id.content);
        }
    }
}

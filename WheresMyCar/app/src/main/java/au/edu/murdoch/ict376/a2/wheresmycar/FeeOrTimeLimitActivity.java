package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Cameron on 29/10/2017.
 */

public class FeeOrTimeLimitActivity extends Activity {
    FeeOrTimeLimitFragment feeOrTimeFragment;
    String rego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            //populate variables with extras
            rego = extras.getString("rego");
        }

        if (savedInstanceState == null) {
            feeOrTimeFragment = FeeOrTimeLimitFragment.newInstance(rego);
            getFragmentManager().beginTransaction().replace(android.R.id.content, feeOrTimeFragment).commit();
        }else{
            feeOrTimeFragment = (FeeOrTimeLimitFragment)getFragmentManager().findFragmentById(android.R.id.content);
        }
    }
}

package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Kat on 24/10/2017.
 */

public class DurationCostActivity extends Activity {
    DurationCostFragment durationCostFragment;
    String rego;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        if(extras !=null){
            //populate variables with extras
            rego = extras.getString("rego");
            id = extras.getLong("id");
        }

        if (savedInstanceState == null) {
            durationCostFragment = DurationCostFragment.newInstance(rego, id);
            getFragmentManager().beginTransaction().replace(android.R.id.content, durationCostFragment).commit();
        }else{
            durationCostFragment = (DurationCostFragment)getFragmentManager().findFragmentById(android.R.id.content);
        }


    }
}

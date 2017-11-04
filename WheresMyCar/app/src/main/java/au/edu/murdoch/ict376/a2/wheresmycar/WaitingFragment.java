package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by Kat on 24/10/2017.
 */

public class WaitingFragment extends Fragment {

    //members
    boolean mDualPane;
    View mLayoutView;
    TextView textViewCountdownLbl;
    TextView textViewCountdownHr;
    TextView textViewCountdownMin;
    TextView textViewCountdownSec;
    TextView textViewTimeSeparator1;
    TextView textViewTimeSeparator2;
    long parkingTimeLimit;


    public static WaitingFragment newInstance(long milliSecs){
        WaitingFragment f = new WaitingFragment();

        Bundle args = new Bundle();
        args.putLong("DURATION", milliSecs);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLayoutView = inflater.inflate(R.layout.waiting_layout, container, false);

        return mLayoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        parkingTimeLimit = getArguments().getLong("DURATION");

        if (parkingTimeLimit > 0) {
            textViewCountdownLbl = (TextView) getActivity().findViewById(R.id.textViewCountdownLbl);
            textViewCountdownHr = (TextView) getActivity().findViewById(R.id.textViewCountdownHr);
            textViewCountdownMin = (TextView) getActivity().findViewById(R.id.textViewCountdownMin);
            textViewCountdownSec = (TextView) getActivity().findViewById(R.id.textViewCountdownSec);
            textViewTimeSeparator1 = (TextView) getActivity().findViewById(R.id.textViewCountdownSeparator1);
            textViewTimeSeparator2 = (TextView) getActivity().findViewById(R.id.textViewCountdownSeparator2);
            textViewTimeSeparator1.setText(":");
            textViewTimeSeparator2.setText(":");
            textViewCountdownLbl.setText(R.string.lbl_time_remaining);

            if (savedInstanceState == null) {
                getActivity().registerReceiver(timerBroadcastReceiver, new IntentFilter("TimerUpdates"));

                Intent intent = new Intent(getActivity(), TimerService.class);
                intent.putExtra("TIMER_LENGTH", parkingTimeLimit);
                long t = intent.getExtras().getLong("TIMER_LENGTH");
                Log.d(TAG, "Timer length = " + t);
                getActivity().startService(intent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(timerBroadcastReceiver, new IntentFilter("TimerUpdates"));
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private BroadcastReceiver timerBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String hoursUntilFinished = intent.getStringExtra("hoursUntilFinished");
            String minutesUntilFinished = intent.getStringExtra("minutesUntilFinished");
            String secondsUntilFinished = intent.getStringExtra("secondsUntilFinished");
            String millisUntilFinished = intent.getStringExtra("millisUntilFinished");
            if (millisUntilFinished.equals("0")) {
                textViewCountdownLbl.setText(R.string.lbl_times_up);
                textViewCountdownHr.setText(hoursUntilFinished);
                textViewCountdownMin.setText(minutesUntilFinished);
                textViewCountdownSec.setText(secondsUntilFinished);
                Toast.makeText(getActivity(), "Parking time limit reached.", Toast.LENGTH_LONG).show();
                getActivity().unregisterReceiver(timerBroadcastReceiver);
                getActivity().stopService(new Intent(getActivity(), TimerService.class));
            } else {
                textViewCountdownHr.setText(hoursUntilFinished);
                textViewCountdownMin.setText(minutesUntilFinished);
                textViewCountdownSec.setText(secondsUntilFinished);
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("Timer", 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(timerBroadcastReceiver);
        } catch(IllegalArgumentException e) {
            Log.d(TAG, "Receiver previously unregistered." + e);
        }
    }
}

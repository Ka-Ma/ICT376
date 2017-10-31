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
    TextView textviewCountdownLbl;
    TextView textViewCountdown;


    public static WaitingFragment newInstance(){
        WaitingFragment f = new WaitingFragment();
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

        textviewCountdownLbl = (TextView) getActivity().findViewById(R.id.textViewCountdownLbl);
        textviewCountdownLbl.setText(R.string.lbl_time_remaining);
        textViewCountdown = (TextView) getActivity().findViewById(R.id.textViewCountdown);

        getActivity().registerReceiver(timerBroadcastReceiver, new IntentFilter("TimerUpdates"));

        Intent intent = new Intent(getActivity(), TimerService.class);
        getActivity().startService(intent);
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
            String secondsUntilFinished = intent.getStringExtra("secondsUntilFinished");
            if (secondsUntilFinished.equals("0")) {
                textviewCountdownLbl.setText(R.string.lbl_times_up);
                textViewCountdown.setText(secondsUntilFinished);
                Toast.makeText(getActivity(), "Parking time limit reached.", Toast.LENGTH_LONG).show();
                getActivity().unregisterReceiver(timerBroadcastReceiver);
                getActivity().stopService(new Intent(getActivity(), TimerService.class));
            } else {
                textViewCountdown.setText(secondsUntilFinished);
            }
        }
    };

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

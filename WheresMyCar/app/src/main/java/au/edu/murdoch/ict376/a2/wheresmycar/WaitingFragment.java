package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                textViewCountdown.setText(String.valueOf(millisUntilFinished / 1000));
            }
            public void onFinish() {
                textviewCountdownLbl.setText(R.string.lbl_times_up);
                textViewCountdown.setText("00");
                MediaPlayer mediaPlayer = MediaPlayer.create(getActivity(), R.raw.ping);
                mediaPlayer.start();
                Toast.makeText(getActivity(), "Parking time limit reached.", Toast.LENGTH_LONG).show();
            }
        }.start();
    }
}

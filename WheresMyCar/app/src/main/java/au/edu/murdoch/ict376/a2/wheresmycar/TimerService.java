package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by Cameron on 30/10/2017.
 */

public class TimerService extends Service {
    Intent intent = new Intent("TimerUpdates");
    CountDownTimer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Starting timer...");
        startTimer();
    }

    private void startTimer() {
        timer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                String secondsUntilFinished = String.valueOf(millisUntilFinished / 1000);
                intent.putExtra("secondsUntilFinished", secondsUntilFinished);
                sendBroadcast(intent);
            }
            public void onFinish() {
                MediaPlayer mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.ping);
                mediaPlayer.start();
                intent.putExtra("secondsUntilFinished", "0");
                sendBroadcast(intent);
            }
        };
        timer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Log.d(TAG, "Timer cancelled");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

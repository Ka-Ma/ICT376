package au.edu.murdoch.ict376.a2.wheresmycar;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
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
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long millisTotal = intent.getExtras().getLong("TIMER_LENGTH");
        Log.d(TAG, "Timer length = " + millisTotal);

        Log.d(TAG, "Starting timer...");
        startTimer(millisTotal);

        return super.onStartCommand(intent, flags, startId);
    }

    private void startTimer(long millisTotal) {
        timer = new CountDownTimer(millisTotal, 1000) {
            public void onTick(long millisUntilFinished) {
                long hours = millisUntilFinished / 3600000;
                long minutes = millisUntilFinished / 60000;
                long seconds = millisUntilFinished / 1000;
                String hoursUntilFinished = String.valueOf(seconds / 3600);
                String minutesUntilFinished = String.valueOf((seconds / 60) % 60);
                String secondsUntilFinished = String.valueOf(seconds % 60);
                intent.putExtra("hoursUntilFinished", hoursUntilFinished);
                intent.putExtra("minutesUntilFinished", minutesUntilFinished);
                intent.putExtra("secondsUntilFinished", secondsUntilFinished);
                intent.putExtra("millisUntilFinished", String.valueOf(millisUntilFinished));
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

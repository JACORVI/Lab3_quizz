package com.example.lab_quizz;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Service to handle the countdown timer that continues running even when the app is in the background
 * This ensures the timer keeps running even if the device is rotated or the activity is recreated
 */
public class CountdownTimerService extends Service {

    private final IBinder binder = new TimerBinder();
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isTimerRunning;

    // Interface for timer updates
    public interface TimerListener {
        void onTimerTick(long millisUntilFinished);
        void onTimerFinish();
    }

    private TimerListener listener;

    public class TimerBinder extends Binder {
        CountdownTimerService getService() {
            return CountdownTimerService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra("TOTAL_SECONDS")) {
                int totalSeconds = intent.getIntExtra("TOTAL_SECONDS", 0);
                timeLeftInMillis = totalSeconds * 1000;

                // Only start if not already running
                if (!isTimerRunning) {
                    startTimer();
                }
            }
        }

        // If killed, restart with the same Intent
        return START_REDELIVER_INTENT;
    }

    public void startTimer() {
        if (timeLeftInMillis > 0) {
            countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timeLeftInMillis = millisUntilFinished;
                    if (listener != null) {
                        listener.onTimerTick(millisUntilFinished);
                    }
                }

                @Override
                public void onFinish() {
                    isTimerRunning = false;
                    timeLeftInMillis = 0;
                    if (listener != null) {
                        listener.onTimerFinish();
                    }
                }
            }.start();

            isTimerRunning = true;
        }
    }

    public void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }

    public void setTimerListener(TimerListener listener) {
        this.listener = listener;
    }

    public long getTimeLeftInMillis() {
        return timeLeftInMillis;
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
    }
}
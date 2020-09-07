package com.porterdustin.zombienuke;

import android.os.Handler;

public class GameClock {
    public static final int CLOCK_TICK_MILLIS = 4;
    private Handler handler;
    private boolean isPaused;

    public GameClock() {
        isPaused = false;
    }

    private void run() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (isPaused) {
                    // do nothing
                } else {
                    // do everything
                }
                handler.postDelayed(this, CLOCK_TICK_MILLIS);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    private void play() {
        isPaused = false;
    }

    private void pause() {
        isPaused = true;
    }
}

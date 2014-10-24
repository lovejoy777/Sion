package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by lovejoy777 on 02/01/14.
 */
public class Thanks extends Activity {

    MediaPlayer thanksMusic;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thanks);

        thanksMusic = MediaPlayer.create(Thanks.this, R.raw.thanks_sound);
        thanksMusic.start();

        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(5000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }

            }

        };

        logoTimer.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        thanksMusic.release();
    }

}

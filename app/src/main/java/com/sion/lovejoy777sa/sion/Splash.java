package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    sleep(2500);
                    Intent mainActivityIntent = new Intent("com.sion.lovejoy777sa.sion.MENU");
                    startActivity(mainActivityIntent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}


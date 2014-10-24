package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by lovejoy on 25/05/14.
 */
public class menu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttoninitd = (Button) findViewById(R.id.buttoninitd);
        Button buttonroms = (Button) findViewById(R.id.buttonroms);

        buttoninitd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.sion.lovejoy777sa.sion.INITD"));
            }
        });


        buttonroms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.sion.lovejoy777sa.sion.ROMS"));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the menu/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent intent1 = new Intent();
                intent1.setClass(this, Settings.class);
                startActivity(intent1);
                break;

            case R.id.action_instructions:
                Intent intent2 = new Intent();
                intent2.setClass(this, Instructions.class);
                startActivity(intent2);
                break;

            case R.id.action_about:
                Intent intent3 = new Intent();
                intent3.setClass(this, About.class);
                startActivity(intent3);
                break;

            case R.id.action_changelog:
                Intent intent4 = new Intent();
                intent4.setClass(this, ChangeLog.class);
                startActivity(intent4);
                break;

            case R.id.action_donation:
                Intent intent5 = new Intent();
                intent5.setClass(this, InAppBillingCoffee.class);
                startActivity(intent5);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
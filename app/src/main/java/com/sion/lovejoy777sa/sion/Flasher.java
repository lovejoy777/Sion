package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import util.SimpleUtils;

/**
 * Created by lovejoy on 01/10/14.
 */
public class Flasher extends Activity {

    static final String TAG = "sion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flasher);
        Button buttonfilechooser = (Button) findViewById(R.id.buttonfilechooser);
        Button idinstallbutton = (Button) findViewById(R.id.idinstallbutton);
        Button deletechooserbutton = (Button) findViewById(R.id.deletechooserbutton);
        Button deletebutton = (Button) findViewById(R.id.deletebutton);

        final Intent extras = getIntent();
        // SZP gets the string of SourceZipPath for text veiw
        String SZP = null;
        if (extras != null) {
            SZP = extras.getStringExtra("key1");

        }
        TextView tv = (TextView) findViewById(R.id.tvidpath);

        tv.setText(SZP);

        // FILE CHOOSER BUTTON
        buttonfilechooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.sion.lovejoy777sa.sion.FLASHERCHOOSER"));
            }
        });

        //  Install BUTTON
        idinstallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent extras = getIntent();

                if (extras != null) {
                    String SZN = extras.getStringExtra("key3");
                    String iszip = ".zip";

                    if (SZN != null && !SZN.isEmpty()) {

                    if (SZN.endsWith(iszip)) {

                        oninstallClick();

                    }else {
                        Toast.makeText(Flasher.this, "Invalid File", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    }else {
                        Toast.makeText(Flasher.this, "Please Choose a File", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // DELETE FILE CHOOSER BUTTON
        deletechooserbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.sion.lovejoy777sa.sion.DELETEFLASHERCHOOSER"));
            }
        });

        // DELETE FILE CHOOSER BUTTON BUTTON

        if (SZP != null)

        {
            deletebutton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    command3(); // delete chosen file

                }

            });
        }
    }

    private void oninstallClick() {
    Intent extras = getIntent();
    String SZN = extras.getStringExtra("key3");
    String SZP = extras.getStringExtra("key1");

            Intent iIntent;
            iIntent = new Intent(this, WaitFlasher.class);
            iIntent.putExtra("key1", SZP);
            iIntent.putExtra("key3", SZN);
            startActivity(iIntent);
            finish();
    }

    // COMMAND 3 DELETE CHOSEN FILE
    public void command3() {

        CheckBox deleteflashercb = (CheckBox) findViewById(R.id.deleteflashercb);

        Intent extras = getIntent();
        String SZP = extras.getStringExtra("key1");
        if (SZP.length() >= 1)
            if (deleteflashercb.isChecked()) {
                RootTools.deleteFileOrDirectory(SZP, true);
                Toast.makeText(Flasher.this, "Delete Successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Flasher.this, "Delete Failed", Toast.LENGTH_LONG).show();
                //finish();
            }
        finish();
    }

    private void LoadPrefs() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX", false);
        if(cbValue){
            setTheme(R.style.DarkTheme);

        }else{
            setTheme(R.style.LightTheme);

        }
    }
}
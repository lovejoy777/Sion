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
        final Button deletebutton = (Button) findViewById(R.id.deletebutton);
        final Button deletechooserbutton = (Button) findViewById(R.id.deletechooserbutton);

        final Intent extras = getIntent();
        // gets the string of source path for text veiw
        String sourcezippath = null;
        if (extras != null) {
            sourcezippath = extras.getStringExtra("key1");

        }
        TextView tv = (TextView) findViewById(R.id.tvidpath);

        tv.setText(sourcezippath);

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
                    String sourcezipname = extras.getStringExtra("key3");
                    String iszip = ".zip";

                    if (sourcezipname != null && !sourcezipname.isEmpty()) {

                    if (sourcezipname.endsWith(iszip)) {

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

        if (sourcezippath != null)

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
    String sourcezipname = extras.getStringExtra("key3");
    String sourcezippath = extras.getStringExtra("key1");

            Intent iIntent;
            iIntent = new Intent(this, WaitFlasher.class);
            iIntent.putExtra("key1", sourcezippath);
            iIntent.putExtra("key3", sourcezipname);
            startActivity(iIntent);

            finish();

    }

    // COMMAND 3 DELETE CHOSEN FILE
    public void command3() {

        CheckBox deleteflashercb = (CheckBox) findViewById(R.id.deleteflashercb);

        Intent extras = getIntent();
        String sourcezippath = extras.getStringExtra("key1");
        if (sourcezippath.length() >= 1)
            if (deleteflashercb.isChecked()) {
                RootTools.deleteFileOrDirectory(sourcezippath, true);
                Toast.makeText(Flasher.this, "Delete Successful", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Flasher.this, "Delete Failed", Toast.LENGTH_LONG).show();
                //finish();
            }
        finish();
    }

    private void LoadPrefs() {
        //cb = (CheckBox) findViewById(R.id.checkBoxDark);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX", false);
        if(cbValue){
            setTheme(R.style.DarkTheme);

        }else{
            setTheme(R.style.LightTheme);

        }


    }
}
package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.stericson.RootTools.RootTools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import util.Basename;
import util.SimpleUtils;

/**
 * Created by lovejoy on 01/10/14.
 */
public class InitD extends Activity {

    static final String TAG = "sion";
    CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initd);
        Button buttoninitdfilechooser = (Button) findViewById(R.id.buttoninitdfilechooser);
        final Button idinstallbutton = (Button) findViewById(R.id.idinstallbutton);
        idinstallbutton.setTag(1);
        idinstallbutton.setText("Install Chosen Init.d");
        Button deletebutton = (Button) findViewById(R.id.deletebutton);
        Button rebootbutton = (Button) findViewById(R.id.rebootbutton);
        Button deletechooserbutton = (Button) findViewById(R.id.deletechooserbutton);


        Intent extras = getIntent();
        // gets the string of source path for text veiw
        String sourcezippath = null;
        if (extras != null) {
            sourcezippath = extras.getStringExtra("key1");
        }
        TextView tv = (TextView) findViewById(R.id.tvidpath);
        tv.setText(sourcezippath);

        // FILE CHOOSER BUTTON
        buttoninitdfilechooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.sion.lovejoy777sa.sion.FILECHOOSER"));
            }
        });


        // INSTALL BUTTON
        if (sourcezippath != null)
            //idinstallbutton.setVisibility(View.VISIBLE);
        idinstallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //command1();
                Intent extras = getIntent();

                if (extras != null) {
                    String sourcezipname = extras.getStringExtra("key3");
                    String iszip = ".zip";

                    if (sourcezipname != null && !sourcezipname.isEmpty()) {
                        if (sourcezipname.endsWith(iszip)) {

                            oninstallClick();

                        }else {
                            Toast.makeText(InitD.this, "Invalid File", Toast.LENGTH_LONG).show();
                            finish();
                        }

                    }else {
                        Toast.makeText(InitD.this, "Please Choose a File", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        // REBOOT BUTTON
        rebootbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                command4(); // reboot device
            }
        });

        // DELETE FILE CHOOSER BUTTON
        deletechooserbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent("com.sion.lovejoy777sa.sion.DELETEFILECHOOSER"));
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
        //String iszip = ".zip";

        Intent iIntent;
        iIntent = new Intent(this, WaitInitD.class);
        iIntent.putExtra("key1", sourcezippath);
        iIntent.putExtra("key3", sourcezipname);
        startActivity(iIntent);

        finish();
    }

    // COMMAND 3 DELETE CHOSEN FILE
    public void command3() {
        CheckBox deletecb = (CheckBox) findViewById(R.id.deletecb);

        Intent extras = getIntent();
        String sourcezippath = extras.getStringExtra("key1");

        if (sourcezippath.length() >= 1)
            if (deletecb.isChecked()) {
                RootTools.deleteFileOrDirectory(sourcezippath, true);
                Toast.makeText(InitD.this, "Delete Successful", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(InitD.this, "Confirm with checkbox", Toast.LENGTH_LONG).show();
                //finish();
            }
        finish();
    }

    // COMMAND 4, REBOOT DEVICE

    public void command4(){

        try {
            Toast.makeText(InitD.this, "Rebooting", Toast.LENGTH_LONG).show();
            Process proc = Runtime.getRuntime().exec("su");
            DataOutputStream std2in = new DataOutputStream(proc.getOutputStream());
            //from here all commands are executed with su permissions
            std2in.writeBytes("-c\n");
            std2in.flush();
            std2in.writeBytes("reboot\n");
            std2in.flush();
            std2in.close();
            proc.waitFor();

            finish();
        } catch (InterruptedException e) {
            Log.e(TAG, "reboot waitfor", e);
        } catch (IOException e) {
            Log.e(TAG, "reboot runtime", e);
        }
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

package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.stericson.RootTools.RootTools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import util.SimpleUtils;

/**
 * Created by lovejoy on 01/10/14.
 */
public class Flasher extends Activity {


    static final String TAG = "sion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.flasher);
        Button buttonfilechooser = (Button) findViewById(R.id.buttonfilechooser);
        final Button idinstallbutton = (Button) findViewById(R.id.idinstallbutton);


        Button deletebutton = (Button) findViewById(R.id.deletebutton);
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
        buttonfilechooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent("com.sion.lovejoy777sa.sion.FLASHERCHOOSER"));
            }
        });

        // INSTALL BUTTON
        if (sourcezippath != null)
        idinstallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idinstallbutton.setText("Setting Up Environment");


                         command1();                                          //RUN COMMAND ONE
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
        deletebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                command3(); //COMMAND 3 DELETE CHOSEN FILE

            }
        });

    }                                                                         // COMMAND 1 make temp dirs copy & move & unzip & mount rw
    public void command1() {


        Intent extras = getIntent();

        if (extras != null) {
            String sourcezipname = extras.getStringExtra("key3");
            String sourcezippath = extras.getStringExtra("key1");
            String sionfolderpath = "/storage/emulated/legacy/";
            String sionfoldername = "Sion";
            String sionromfolderpath = "/storage/emulated/legacy/Sion/";
            String sionromfoldername = "rom";
            String sionromcompletefilepath = "/storage/emulated/legacy/Sion/rom/" + sourcezipname;
            String sionrommvtopath = "/storage/emulated/legacy/Sion/rom/";
            String sionromupdatename = "romupdate.zip";
            String sionupdaterscriptpath = "/storage/emulated/legacy/Sion/rom/romupdatescript";
            String delsddir = "/storage/emulated/legacy/Sion";

            File dir = new File(delsddir);
            if (dir.exists() && dir.isDirectory()) {
                // do something here
                RootTools.deleteFileOrDirectory(delsddir, true);
            }

            boolean success = false;
            if (sourcezipname.length() >= 1) {
      success = SimpleUtils.createDir(sionfolderpath, sionfoldername);                   // MKDIR /storage/emulated/legacy/Sion
                SimpleUtils.createDir(sionromfolderpath, sionromfoldername);                       // MKDIR /storage/emulated/legacy/Sion/rom
                SimpleUtils.copyToDirectory(sourcezippath, sionrommvtopath);               // COPY SOURCE FILE TO SDCARD FOLDER
                SimpleUtils.renameTarget(sionromcompletefilepath, sionromupdatename);              // RENAME SOURCE FILE TO romupdate.zip

                if (success)
                    Toast.makeText(Flasher.this, "Installing Script", Toast.LENGTH_LONG).show();   // TOAST MESSAGE INSTALLING SCRIPT

            }
            InputStream in = getResources().openRawResource(R.raw.romupdatescript);                // COPY SCRIPT FROM APP TO SDCARD
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(sionupdaterscriptpath);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "fileoutputstream is null", e);
            }
            byte[] buff = new byte[1024];
            int read = 0;

            try {
                try {
                    while ((read = in.read(buff)) > 0) {
                        if (out != null) {
                            out.write(buff, 0, read);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "out write is null", e);
                }
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "in is null", e);
                }

                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "out is null", e);
                }
            }

        }
        command2();
                                                                       // RUN COMMAND "
    }

    public void command2() {              // COMMAND 2


        String rename1 = "/storage/emulated/legacy/Sion/rom/romupdatescript";
        String romupdaterscript = "romupdatescript.sh";
        String newnamefullpath = "/storage/emulated/legacy/Sion/rom/romupdatescript.sh";
        String sysfolder = "/system/";
        String sysandname = "/system/Sion/";
        String sysandnamefile = "/system/Sion/romupdatescript.sh";
        String sysandnamefileplus = "/system/Sion/romupdatescript.sh/";
        String syssion = "Sion";

        SimpleUtils.renameTarget(rename1, romupdaterscript);                                       // Rename Script romupdatescript.sh
        SimpleUtils.createDir(sysfolder, syssion);
        SimpleUtils.copyToDirectory(newnamefullpath, sysandname);                                  // COPY /storage/emulated/legacy/Sion/rom/romupdatescript.sh /system/Sion/

        RootTools.remount(sysandname, "RW");                                                       // FOLDER PERM RW
        RootTools.remount(sysandnamefileplus, "RW");                                               // FILE PERM RW

        Process proc1 = null;
        try {

            proc1 = Runtime.getRuntime().exec("su");
            DataOutputStream stdin = new DataOutputStream(proc1.getOutputStream());
            //from here all commands are executed with su permissions
            stdin.writeBytes("-c\n");
            stdin.flush();
            stdin.writeBytes("chmod 0666 " + sysandname + "\n");                               //
            stdin.flush();
            stdin.writeBytes("chmod 0777 " + sysandnamefile + "\n");                           // PERM MAKE FILE EXECUTABLE
            stdin.flush();
            stdin.close();
            proc1.waitFor();

        } catch (InterruptedException e) {
            Log.e(TAG, "permission wait", e);
        } catch (IOException e) {
            Log.e(TAG, "permission runtime", e);
        }

        Toast.makeText(Flasher.this, "Booting Recovery", Toast.LENGTH_LONG).show();                // TOAST MESSAGE BOOTING RECOVERY

        RootTools.remount(sysandnamefileplus, "RW");                                       // DELETE SYSTEM FOLDER

        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec("su");
            DataOutputStream stdin = new DataOutputStream(proc.getOutputStream());
            //from here all commands are executed with su permissions
            stdin.writeBytes("-c\n");
            stdin.flush();
            stdin.writeBytes("." + sysandnamefile + "\n");                                    // RUN SCRIPT
            stdin.flush();
            stdin.close();
            proc.waitFor();
        } catch (InterruptedException e) {
            Log.e(TAG, "permission wait", e);
        } catch (IOException e) {
            Log.e(TAG, "permission runtime", e);

        } finally {
            finish();
        }
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
}
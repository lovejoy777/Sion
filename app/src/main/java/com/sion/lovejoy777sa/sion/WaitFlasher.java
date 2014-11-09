package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
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
 * Created by lovejoy on 07/11/14.
 */
public class WaitFlasher extends Activity {
    private Handler mHandler = new Handler();
    static final String TAG = "sion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitflasher);

        Intent extras = getIntent();

        if (extras != null) {
            String sourcezipname = extras.getStringExtra("key3");
            String iszip = ".zip";

            if (sourcezipname.endsWith(iszip)) {
                mHandler.postDelayed(new Runnable() {
                    public void run() {

                        command1 ();

                    }
                }, 1000);
            } else {
                Toast.makeText(WaitFlasher.this, "Invalid File", Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

    public void command1() {

        Intent extras = getIntent();
        // gets the string of source path for text veiw

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


            if (sourcezipname.length() >= 1) {
                SimpleUtils.createDir(sionfolderpath, sionfoldername);                   // MKDIR /storage/emulated/legacy/Sion
                SimpleUtils.createDir(sionromfolderpath, sionromfoldername);                       // MKDIR /storage/emulated/legacy/Sion/rom
                SimpleUtils.copyToDirectory(sourcezippath, sionrommvtopath);               // COPY SOURCE FILE TO SDCARD FOLDER
                SimpleUtils.renameTarget(sionromcompletefilepath, sionromupdatename);              // RENAME SOURCE FILE TO romupdate.zip

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
        command2();                                                                                // RUN COMMAND "
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

        Toast.makeText(WaitFlasher.this, "Booting Recovery", Toast.LENGTH_LONG).show();                // TOAST MESSAGE BOOTING RECOVERY

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sion.lovejoy777sa.sion.commands.RootCommands;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeoutException;

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
            String SZN = extras.getStringExtra("key3");
            String iszip = ".zip";

            if (SZN.endsWith(iszip)) {
                mHandler.postDelayed(new Runnable() {
                    public void run() {

                        try {
                            // STARTS COMMAND 1
                            command1();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }, 2000);
            } else {
                Toast.makeText(WaitFlasher.this, "Invalid File", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void command1() throws InterruptedException {

        Intent extras = getIntent();

        if (extras != null) {
            String SZN = extras.getStringExtra("key3");
            String SZP = extras.getStringExtra("key1");
            String sionromdata = getApplicationInfo().dataDir + "/otaroms";
            //getApplicationInfo().dataDir
            //String sionromdata = "/data/data/com.sion.lovejoy777sa.sion/otaroms";
            String sionromcompletefilepath = getApplicationInfo().dataDir + "/otaroms/" + SZN;
            String sionupdaterscriptpath = getApplicationInfo().dataDir + "/otaroms/romupdatescript";
            String sionautodeleterscriptpath = getApplicationInfo().dataDir + "/otaroms/autodeleterscript";
            String newname = getApplicationInfo().dataDir + "/otaroms/romupdatescript.sh";
            String copyotaout = getApplicationInfo().dataDir + "/otaroms/otarom.zip";


            if (SZN.length() >= 1) {

                File dir = new File(getApplicationInfo().dataDir + "/otaroms/otarom.zip");

                // DELETES PREVIOUS FLASHABLE.ZIP
                if (dir.exists() && dir.isDirectory()) {
                    SimpleUtils.deleteTarget(getApplicationInfo().dataDir + "/otaroms/otarom.zip");
                    //RootTools.deleteFileOrDirectory(sionromdata, true);
                }

                //CREATES DIR /data/data/com.sion.lovejoy777sa.sion/otaroms
                SimpleUtils.createDir(getApplicationInfo().dataDir, "otaroms");
                // CHANGES PERMISSION FOR /data/data/com.sion.lovejoy777sa.sion/otaroms FOLDER
                //CommandCapture command = new CommandCapture(0, "chmod 0777 " + sionromdata);

                try {
                    CommandCapture command = new CommandCapture(0, "chmod 0777 " + sionromdata);
                    RootTools.getShell(true).add(command);
                    while (!command.isFinished()) {
                        Thread.sleep(1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (RootDeniedException e) {
                    e.printStackTrace();
                }

                // COPIES AUTODELETERSCRIPT FROM RAW FOLDER
                InputStream in = getResources().openRawResource(R.raw.autodeleterscript);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(sionautodeleterscriptpath);
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

                //COPIES SZP to /data/data/com.sion.lovejoy777sa.sion/otaroms
                InputStream input = null;
                OutputStream output = null;
                try {
                    input = new FileInputStream(SZP);
                    output = new FileOutputStream(copyotaout);
                    byte[] buf = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buf)) > 0) {
                        output.write(buf, 0, bytesRead);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        assert input != null;
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert output != null;
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // RENAMES FLASHABLE.ZIP FILE
                SimpleUtils.renameTarget(sionromcompletefilepath, "otarom.zip");

            }

            // COPIES ROMUPDATER SCRIPT FROM RAW FOLDER
            InputStream in = getResources().openRawResource(R.raw.romupdatescript);
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

            try {

                // RENAMES ROMUPDATER SCRIPT
                SimpleUtils.renameTarget(sionupdaterscriptpath, "romupdatescript.sh");
                // CHANGES PERMISSIONS FOR UPDATER SCRIPT
                CommandCapture command1 = new CommandCapture(0, "chmod 0777 " + getApplicationInfo().dataDir + "/otaroms/romupdatescript.sh", "chmod 0755 " + getApplicationInfo().dataDir + "/otaroms/autodeleterscript");
                RootTools.getShell(true).add(command1);
                while (!command1.isFinished()) {
                    Thread.sleep(2);
                }

                RootTools.remount("/system", "RW");
                // COPY NEW FILE TO INIT.D FOLDER
                RootTools.copyFile(sionautodeleterscriptpath, "/system/etc/init.d", false, true);

                   // SimpleUtils.renameTarget(sionupdaterscriptpath, newname);

                // RUNS UPDATER SCRIPT
                    CommandCapture command2 = new CommandCapture(0, "." + getApplicationInfo().dataDir + "/otaroms/romupdatescript.sh");
                    RootTools.getShell(true).add(command2);
                    while (!command2.isFinished()) {
                        Thread.sleep(2);
                    }
                // CLOSES ALL ROOT SHELLS INCASE FILE DOESNT FLASH
                RootTools.closeAllShells();

            } catch (RootDeniedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



        @Override
        protected void onDestroy() {
            super.onDestroy();
        }

    // LOADS THEME
    private void LoadPrefs() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX", false);
         if( cbValue){
            setTheme(R.style.DarkTheme);

         }else{
             setTheme(R.style.LightTheme);
         }
    }
}
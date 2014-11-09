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
 * Created by lovejoy on 08/11/14.
 */
public class WaitInitD extends Activity {
    private Handler mHandler = new Handler();
    static final String TAG = "sion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitinitd);

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
                Toast.makeText(WaitInitD.this, "Invalid File", Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }

    // COMMAND 1 make temp dirs copy & move & unzip & mount rw
    public void command1() {

        Intent extras = getIntent();

        if (extras != null) {
            String sourcezipname = extras.getStringExtra("key3");
            String sourcezippath = extras.getStringExtra("key1");
            String sdinstalldirpath = "/storage/emulated/legacy/";
            String sdinstalldirname = "sdsion";
            String systeminstalldirpath = "/system/";
            String systeminstalldirname = "syssion";

            Basename newbasename = new Basename(sourcezipname, '/', '.');
            String newname = "" + newbasename.basename();
            String systeminstall = "/system/syssion";
            String sdinstall = "/storage/emulated/legacy/sdsion";
            String sdmovedsdinstall = sdinstall + "/" + sourcezipname;
            String systeminstallnewname = "" + systeminstall + "/" + newname;
            String sdinstallnewname = "" + sdinstall + "/" + newname;
            String sdinstallmvtopath = "" + sdinstalldirpath + sdinstalldirname;
            String permdir = "" + systeminstalldirpath + systeminstalldirname + "/";
            String permfile = "" + systeminstalldirpath + systeminstalldirname + "/" + newname + "/";

            boolean success = false;

            if (sourcezipname.length() >= 1) {
                success = SimpleUtils.createDir(
                        sdinstalldirpath,
                        sdinstalldirname);
                SimpleUtils.createDir(
                        systeminstalldirpath,
                        systeminstalldirname);


                SimpleUtils.copyToDirectory(sourcezippath, sdinstallmvtopath);
                try {
                    unzip(sdmovedsdinstall, sdinstall);

                    RootTools.remount(permdir, "RW");
                    Process proc1 = null;
                    try {
                        proc1 = Runtime.getRuntime().exec("su");
                        DataOutputStream stdin = new DataOutputStream(proc1.getOutputStream());
                        //from here all commands are executed with su permissions
                        stdin.writeBytes("-c\n");
                        stdin.flush();
                        stdin.writeBytes("chmod 0777 " + permdir + "\n");
                        stdin.flush();
                        stdin.writeBytes("cp -fr " + sdinstallnewname + " " + permdir + "\n");
                        stdin.flush();
                        stdin.close();
                        proc1.waitFor();

                    } catch (InterruptedException e) {
                        Log.e(TAG, "permission wait", e);
                    } catch (IOException e) {
                        Log.e(TAG, "permission runtime", e);
                    }

                    RootTools.remount(permfile, "RW");

                    Process proc = null;
                    try {
                        proc = Runtime.getRuntime().exec("su");
                        DataOutputStream stdin = new DataOutputStream(proc.getOutputStream());
                        //from here all commands are executed with su permissions
                        stdin.writeBytes("-c\n");
                        stdin.flush();
                        stdin.writeBytes("chmod 0777 " + systeminstallnewname + "\n");
                        stdin.flush();
                        stdin.close();
                        proc.waitFor();

                    } catch (InterruptedException e) {
                        Log.e(TAG, "permission wait", e);
                    } catch (IOException e) {
                        Log.e(TAG, "permission runtime", e);

                        if (success)
                            Toast.makeText(WaitInitD.this, "Unzipping", Toast.LENGTH_LONG).show();

                    } finally {
                        finish();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            command2();
        }
        else {
            Toast.makeText(WaitInitD.this, "Choose a file", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // COMMAND 2 change file perms and copy to destination folder
    public void command2() {

        Intent extras = getIntent();

        String sourcezipname = extras.getStringExtra("key3");

        Basename newbasename = new Basename(sourcezipname, '/', '.');
        String newname = "" + newbasename.basename();
        String systeminstall = "/system/syssion";
        String systeminstallnewname = "" + systeminstall + "/" + newname;
        String destdir = "/system/etc/init.d/";
        String delsysdir = "/system/syssion";
        String delsddir = "/storage/emulated/legacy/sdsion";

        RootTools.copyFile(systeminstallnewname, destdir, true, true);
        RootTools.deleteFileOrDirectory(delsysdir, true);
        RootTools.deleteFileOrDirectory(delsddir, true);

        Toast.makeText(WaitInitD.this, "Install Finished", Toast.LENGTH_LONG).show();
        finish();

    }

    /**
     * Unzip a zip file.  Will overwrite existing files.
     *
     * @param zipFile Full path of the zip file you'd like to unzip.
     * @param location Full path of the directory you'd like to unzip to (will be created if it doesn't exist).
     * @throws java.io.IOException
     */
    public void unzip(String zipFile, String location) throws IOException {

        int size;
        byte[] buffer = new byte[1024];

        try {
            if ( !location.endsWith("/") ) {
                location += "/";
            }
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), 1024));
            try {
                ZipEntry ze;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if ( null != parentDir ) {
                            if ( !parentDir.isDirectory() ) {
                                parentDir.mkdirs();
                            }
                        }

                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, 1024);
                        try {
                            while ( (size = zin.read(buffer, 0, 1024)) != -1 ) {
                                fout.write(buffer, 0, size);
                            }

                            zin.closeEntry();
                        }
                        finally {
                            fout.flush();
                            fout.close();
                        }

                    }
                }

            }
            finally {
                zin.close();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Unzip exception", e);
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

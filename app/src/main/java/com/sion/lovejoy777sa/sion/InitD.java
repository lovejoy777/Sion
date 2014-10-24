package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

    static final String TAG = "777";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.initd);
        Button buttoninitdfilechooser = (Button) findViewById(R.id.buttoninitdfilechooser);
        Button idinstallbutton = (Button) findViewById(R.id.idinstallbutton);
        idinstallbutton.setVisibility(View.GONE);
        Button deletebutton = (Button) findViewById(R.id.deletebutton);
        deletebutton.setVisibility(View.GONE);
        Button rebootbutton = (Button) findViewById(R.id.rebootbutton);


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

        if (sourcezippath != null)
            idinstallbutton.setVisibility(View.VISIBLE);
        idinstallbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                command1();
            }

        });

        // REBOOT BUTTON
        rebootbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                command4(); // reboot device
            }
        });

        // DELETE FILE CHOOSER BUTTON BUTTON

        if (sourcezippath != null)
            deletebutton.setVisibility(View.VISIBLE);
        deletebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                command3(); // reboot device


            }
        });
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
                        stdin.writeBytes("chmod 0755 " + permdir + "\n");
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
                        stdin.writeBytes("chmod 0755 " + systeminstallnewname + "\n");
                        stdin.flush();
                        stdin.close();
                        proc.waitFor();

                    } catch (InterruptedException e) {
                        Log.e(TAG, "permission wait", e);
                    } catch (IOException e) {
                        Log.e(TAG, "permission runtime", e);

                        if (success)
                            Toast.makeText(InitD.this, "Unzipping", Toast.LENGTH_LONG).show();

                    } finally {
                        finish();
                    }

                    command2();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        else {
            Toast.makeText(InitD.this, "Choose a file", Toast.LENGTH_LONG).show();
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

        Toast.makeText(InitD.this, "Install Finished", Toast.LENGTH_LONG).show();
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
            } else {
                Toast.makeText(InitD.this, "Delete Failed", Toast.LENGTH_LONG).show();
                //finish();
            }
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

}

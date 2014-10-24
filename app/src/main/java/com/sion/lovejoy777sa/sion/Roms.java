package com.sion.lovejoy777sa.sion;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lovejoy on 01/10/14.
 */
public class Roms extends Activity {

    static final String TAG = "777";

    // needed for .getString
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roms);

        // gets the string of source path for text veiw
        Bundle extras = getIntent().getExtras();


        String romsPath = null;
        String gappsPath = null;
        if (extras != null) {
            romsPath = extras.getString("string_key4", "no src path");
            gappsPath = extras.getString("string_key5", "no src path");
        }

        // declare the text view to hold the source path
        TextView tv1 = (TextView) findViewById(R.id.tvromspath);
        tv1.setText(romsPath);

        TextView tv2 = (TextView) findViewById(R.id.tvgappspath);
        tv2.setText(gappsPath);


        Button buttonromsfilechooser = (Button) findViewById(R.id.buttonromsfilechooser);
        Button buttongappsfilechooser = (Button) findViewById(R.id.buttongappsfilechooser);
        Button romsinstallbutton = (Button) findViewById(R.id.idromsinstallbutton);

        // File Browser Button
        buttonromsfilechooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.sion.lovejoy777sa.sion.FILECHOOSER"));
            }
        });

        buttongappsfilechooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent("com.sion.lovejoy777sa.sion.FILECHOOSER"));
            }
        });

        romsinstallbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // declaring sourcePath & destinationPath strings from FileChooser.class
                Bundle extras = getIntent().getExtras();

                String sourcePath = null;
                if (extras != null) {
                    sourcePath = extras.getString("string_key1");
                }
                String destPath = null;
                if (extras != null) {
                    destPath = extras.getString("string_key2");
                }
                String sourcename = null;
                if (extras != null) {
                    sourcename = extras.getString("string_key3");
                }

                // installing file

                try {
                    Environment.getExternalStorageDirectory();

                    if (sourcename != null) {
                        // If it ends in .zip
                        if(sourcename.endsWith(".zip")){
                            // gets rid of the file extension of the source name
                         //   String basePath = sourcename;
                         //   Basename newbasename = new Basename(basePath, '/', '.');
                         //   String newname = "" + newbasename.basename();
                          //  String zipdestPath="/system/etc/init.d/" + newname;
                            // make a new directory called tempunzipfolder and copy chosen file to it
                         //   Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "mkdir " + tempunzipfolder + ";mount -o rw,remount /system" + ";cp " + sourcePath + " " + tempunzipfolder + "/" + sourcename + ";mount -o ro,remount /system"  });
                         //   proc.waitFor();

                            // unzip folder);
                          //  unzip(zippedmovedpath, tempunzipfolder);

                           // Toast.makeText(Roms.this, sourcename + " Unzipped", Toast.LENGTH_LONG).show();
                            // install unzipped file and change permissions to 0175
                           // Process proc1 = Runtime.getRuntime().exec(new String[] { "su", "-c", "mount -o rw,remount /system" + ";cp " + tempunzipfolder + "/" + newname + " " + zipdestPath + ";chmod " + zipdestPath + ";chmod 0755 " + zipdestPath + ";mount -o ro,remount /system" });
                           // proc1.waitFor();
                            Toast.makeText(Roms.this, "testing in progress", Toast.LENGTH_LONG).show();
                        }
                        else{
                            // None zipped file
                            // install chosen file and change permissions to 0775
                           // Process proc2 = Runtime.getRuntime().exec(new String[] { "su", "-c", "mount -o rw,remount /system" + ";cp " + sourcePath + " " + destPath + ";chmod " + destPath + ";chmod 0755 " + destPath + ";mount -o ro,remount /system" });
                           // proc2.waitFor();
                            Toast.makeText(Roms.this, "testing in progress", Toast.LENGTH_LONG).show();
                        }
                    }
              //  } catch (InterruptedException e) {
               //     e.printStackTrace();
              //  } catch (IOException e) {
                //    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        });




    }

}

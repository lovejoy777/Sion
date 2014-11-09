package com.sion.lovejoy777sa.sion;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.sion.lovejoy777sa.sion.commands.Permissions;
import com.stericson.RootTools.RootTools;

import java.io.File;

/**
 * Created by lovejoy on 21/10/14.
 */
public class Settings  extends Activity implements View.OnClickListener {

    CheckBox cb;
    ImageButton b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoadTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        cb = (CheckBox) findViewById(R.id.checkBoxDark);
        b = (ImageButton) findViewById(R.id.saveButton);
        b.setOnClickListener(this);

        LoadPrefs();

    }

    private void LoadPrefs() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX", false);
        if(cbValue){
            cb.setChecked(true);


            }else{
            cb.setChecked(false);

            }


    }

    private void LoadTheme() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean cbValue = sp.getBoolean("CHECKBOX", false);
        if(cbValue){
            setTheme(R.style.DarkTheme);

        }else{
            setTheme(R.style.LightTheme);

        }


    }

    private void savePrefs(String key, boolean value) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }



    @Override
    public void onClick(View view) {

        savePrefs("CHECKBOX", cb.isChecked());
        Toast.makeText(Settings.this, "Theme choice saved\nPlease restart Sion", Toast.LENGTH_LONG).show();

    }





    private static boolean mShowHiddenFiles;
    private static boolean mRootAccess;
    public static String defaultdir;

    public static void updatePreferences(Context context) {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(context);

        mShowHiddenFiles = p.getBoolean("displayhiddenfiles", true);
        mRootAccess = p.getBoolean("enablerootaccess", true);
        defaultdir = p.getString("defaultdir", Environment
                .getExternalStorageDirectory().getPath());

        rootAccess();
    }

    public static boolean showHiddenFiles() {
        return mShowHiddenFiles;
    }

    public static boolean rootAccess() {
        return mRootAccess && RootTools.isAccessGiven();
    }


}

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
import android.widget.Toast;

import com.sion.lovejoy777sa.sion.commands.Permissions;
import com.stericson.RootTools.RootTools;

import java.io.File;

/**
 * Created by lovejoy on 21/10/14.
 */
public class Settings  extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

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

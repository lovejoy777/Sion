package com.sion.lovejoy777sa.sion;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lovejoy on 24/10/14.
 */
public class DeleteFileChooser extends ListActivity {

    static final String TAG = "sion";

    private File currentDir;
    private FileArrayAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        LoadPrefs();
        super.onCreate(savedInstanceState);

        String systdest = "/system/etc/init.d/";

        currentDir = new File(systdest);
        fill(currentDir);
    }
    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: " + f.getName());
        List<Option> dir = new ArrayList<Option>();
        List<Option> fls = new ArrayList<Option>();
        try{
            if (dirs != null) {
                for(File ff: dirs)
                {
                    if(ff.isDirectory())
                        dir.add(new Option(ff.getName(),"Folder",ff.getAbsolutePath()));
                    else
                    {
                        fls.add(new Option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
                    }
                }
            }
        }catch(Exception e) {
            Log.e(TAG, "getting File f", e);

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new Option("..","Parent Directory",f.getParent()));
        adapter = new FileArrayAdapter(DeleteFileChooser.this,R.layout.file_view,dir);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        super.onListItemClick(l, v, position, id);
        Option o = adapter.getItem(position);
        if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
        else
        {
            onFileClick(o);
        }
    }
    private void onFileClick(Option o)
    {
        String sourcezippath = "" + o.getPath();
        String systdest = "/system/etc/init.d/";
        String sourcezipname = "" + o.getName();

        Intent iIntent = new Intent(this, InitD.class);
        iIntent.putExtra("key1", sourcezippath);
        iIntent.putExtra("key2", systdest);
        iIntent.putExtra("key3", sourcezipname);
        startActivity(iIntent);

        finish();
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

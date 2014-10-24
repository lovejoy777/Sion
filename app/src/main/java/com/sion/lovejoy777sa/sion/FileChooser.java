package com.sion.lovejoy777sa.sion;

/**
 * Created by lovejoy on 05/10/14.
 */

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileChooser extends ListActivity {

    private File currentDir;
    private FileArrayAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
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
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new Option("..","Parent Directory",f.getParent()));
        adapter = new FileArrayAdapter(FileChooser.this,R.layout.file_view,dir);
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

}
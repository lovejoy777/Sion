/*
 * Copyright (C) 2014 Simple Explorer
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.widget.Toast;

import com.sion.lovejoy777sa.sion.menu;
import com.sion.lovejoy777sa.sion.R;
import com.sion.lovejoy777sa.sion.commands.RootCommands;
import com.sion.lovejoy777sa.sion.Settings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;

public class SimpleUtils {

    private static final int BUFFER = 4096;

    // scan file after move/copy
    public static void requestMediaScanner(final Context context,
                                           final File... files) {
        final String[] paths = new String[files.length];
        int i = 0;
        for (final File file : files) {
            paths[i] = file.getPath();
            i++;
        }
        MediaScannerConnection.scanFile(context, paths, null, null);
    }

    // TODO: fix search with root
    private static void search_file(String dir, String fileName,
                                    ArrayList<String> n) {
        File root_dir = new File(dir);
        String[] list = root_dir.list();

        if (list != null && root_dir.canRead()) {
            for (String aList : list) {
                File check = new File(dir + "/" + aList);
                String name = check.getName();

                if (check.isFile()
                        && name.toLowerCase().contains(fileName.toLowerCase())) {
                    n.add(check.getPath());
                } else if (check.isDirectory()) {
                    if (name.toLowerCase().contains(fileName.toLowerCase())) {
                        n.add(check.getPath());

                    // change this!
                    } else if (check.canRead() && !dir.equals("/"))
                        search_file(check.getAbsolutePath(), fileName, n);
                }
            }
        } else {
            n.addAll(RootCommands.findFiles(dir, fileName));
        }
    }


    public static void moveToDirectory(String old, String newDir) {
        String file_name = old.substring(old.lastIndexOf("/"), old.length());
        File old_file = new File(old);
        File cp_file = new File(newDir + file_name);

        if (!old_file.renameTo(cp_file)) {
            copyToDirectory(old, newDir);
            deleteTarget(old, newDir);
        }
    }

    public static void copyToDirectory(String old, String newDir) {
        File old_file = new File(old);
        File temp_dir = new File(newDir);
        byte[] data = new byte[BUFFER];
        int read;

        if (old_file.canWrite() && temp_dir.isDirectory()
                && temp_dir.canWrite()) {
            if (old_file.isFile()) {
                String file_name = old.substring(old.lastIndexOf("/"),
                        old.length());
                File cp_file = new File(newDir + file_name);

                try {
                    BufferedOutputStream o_stream = new BufferedOutputStream(
                            new FileOutputStream(cp_file));
                    BufferedInputStream i_stream = new BufferedInputStream(
                            new FileInputStream(old_file));

                    while ((read = i_stream.read(data, 0, BUFFER)) != -1)
                        o_stream.write(data, 0, read);

                    o_stream.flush();
                    i_stream.close();
                    o_stream.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (old_file.isDirectory()) {
                String files[] = old_file.list();
                String dir = newDir
                        + old.substring(old.lastIndexOf("/"), old.length());

                if (!new File(dir).mkdir())
                    return;

                for (String file : files) copyToDirectory(old + "/" + file, dir);
            }

        } else {
            //if (Settings.rootAccess())
                RootCommands.moveCopyRoot(old, newDir);
        }
    }

    // filePath = currentDir + "/" + item
    // newName = new name
    public static boolean renameTarget(String filePath, String newName) {
        File src = new File(filePath);

        String temp = filePath.substring(0, filePath.lastIndexOf("/"));
        File dest = new File(temp + "/" + newName);

        return src.renameTo(dest);
    }

    // path = currentDir
    // name = new name
    public static boolean createDir(String path, String name) {
        File folder = new File(path, name);
boolean success = false;

        if (folder.exists())
            success = false;

        if (folder.mkdir())
            success = true;
        else  {
            success = RootCommands.createRootdir(folder, path);
        }

        return success;
    }

    public static void deleteTarget(String path, String dir) {
        File target = new File(path);

        if (target.isFile() && target.canWrite()) {
            target.delete();
        } else {
            if (target.isDirectory() && target.canRead()) {
                String[] file_list = target.list();

                if (file_list != null && file_list.length == 0) {
                    target.delete();
                    return;
                } else if (file_list != null && file_list.length > 0) {

                    for (String aFile_list : file_list) {
                        File temp_f = new File(target.getAbsolutePath() + "/"
                                + aFile_list);

                        if (temp_f.isDirectory())
                            deleteTarget(temp_f.getAbsolutePath(), dir);
                        else if (temp_f.isFile()) {
                            temp_f.delete();
                        }
                    }
                }

                if (target.exists())
                    target.delete();
            } else if (target.exists() && !target.delete()) {
               // if (Settings.rootAccess())
                    RootCommands.DeleteFileRoot(path);
            }
        }
    }

    public static ArrayList<String> searchInDirectory(String dir,
                                                      String fileName) {
        ArrayList<String> names = new ArrayList<String>();
        search_file(dir, fileName, names);
        return names;
    }

    // save current string in ClipBoard
    public static void savetoClipBoard(final Context co, String dir1) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) co
                .getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(
                "Copied Text", dir1);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(co,
                "'" + dir1 + "' " + co.getString(R.string.copiedtoclipboard),
                Toast.LENGTH_SHORT).show();
    }






}
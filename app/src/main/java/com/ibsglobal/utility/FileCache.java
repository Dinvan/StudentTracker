package com.ibsglobal.utility;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ravi on 5/11/2017.
 */

public class FileCache {
    public static final String MAIN_DIRECOTRY = "HiringAlerts";
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private File file, tmpFile, exportFile;
    private static Context context;

    public FileCache(Context context) {
        this.context = context;
        file = new File((context.getFilesDir() + File.separator + MAIN_DIRECOTRY));
        if (!file.exists()) {
            file.mkdir();
        }
        tmpFile = new File((context.getFilesDir() + File.separator + MAIN_DIRECOTRY + File.separator + "tmp"));
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        exportFile = new File((context.getFilesDir() + File.separator + MAIN_DIRECOTRY + File.separator + "export"));
        if (!exportFile.exists()) {
            exportFile.mkdir();
        }
    }

    public File getParent() {
        return file;
    }

    public File getFile(String name) {
        if (null != name) {
            return new File(file, name);
        }
        return null;
    }

    public File getExportFile(String name) {
        if (null != name) {
            return new File(exportFile, name);
        }
        return null;
    }

    public static boolean deleteAllFiles(File directory) {
        if (null != directory && directory.isDirectory()) {
            String[] children = directory.list();
            int count = 0;
            for (int i = 0; i < children.length; i++) {
                File file = new File(directory, children[i]);
                if (file.exists()) {
                    file.delete();
                    count++;
                }
            }
            return children.length == count;
        }

        return false;
    }

    public boolean clearTmpDir() {
        return deleteAllFiles(tmpFile);
    }

    private TmpName tmpName;

    public File getNextTmpFile(String extension) {
        if (null == tmpName) {
            tmpName = new TmpName();
        }

        return new File(tmpFile, tmpName.getNextName() + "." + extension);
    }

    public File getCurrTmpFile(String extension) {
        if (null == tmpName) {
            tmpName = new TmpName();
        }
        String name = tmpName.getCurrName();
        return null == name ? null : new File(tmpFile, name + "." + extension);
    }

    public File getPrevTmpFile(String extension) {
        if (null == tmpName) {
            tmpName = new TmpName();
        }
        String name = tmpName.getPrevName();
        return null == name ? null : new File(tmpFile, name + "." + extension);
    }

    public File moveCurrTmpFileToMain() {
        File source = getCurrTmpFile("png");
        if (null != source) {
            File dest = new File(file, source.getName());
            try {
                copyFile(source, dest);
                return dest;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private class TmpName {
        private List<String> names;
        private final long ID = System.currentTimeMillis();
        private int index;

        public TmpName() {
            names = new ArrayList<String>();
            index = 0;
        }

        public String getNextName() {
            String name = "tmp_" + ID + "_" + index;
            names.add(name);
            index++;
            return name;
        }

        public String getCurrName() {
            if (index > 0) {
                return names.get(index - 1);
            }
            return null;
        }

        public String getPrevName() {
            index--;
            if (index > 0) {
                return names.get(index - 1);
            }
            return null;
        }
    }

    public static void copyFile(File sourceLocation, File targetLocation) throws IOException {
        InputStream in = new FileInputStream(sourceLocation);
        OutputStream out = new FileOutputStream(targetLocation);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    public static void writeFile(Context context, String name, byte[] buffer) {
        if (null != name && null != buffer) {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
                fileOutputStream.write(buffer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean appendToFile(File file, String appendContents) {
        boolean result = false;
        try {
            if (file != null) {
                file.createNewFile();
                Writer writer = new BufferedWriter(new FileWriter(file, true), 1024);
                writer.write(appendContents);
                writer.close();
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String readFile(Context context, String file) {
        if (TextUtils.isEmpty(file)) {
            return "";
        }
        FileInputStream fin = null;
        String temp = "";
        try {

            fin = context.openFileInput(file);
            int c;

            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return temp.toString();
//
////string temp contains all the data of the file.
//
//
//
//        if (null != file && file.exists()) {
//            try {
//                BufferedReader br = new BufferedReader(new FileReader(file));
//                String line;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line);
//                    sb.append('\n');
//                }
//                br.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
    }

    public static void log(String data, boolean trimTo1k) {
        if (null != data) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + MAIN_DIRECOTRY);
            if (!file.exists()) {
                file.mkdir();
            }
            File logFile = new File(file, "Log.txt");
            data = trimTo1k ? data.substring(0, data.length() > 1024 ? 1024 : data.length()) : data;
            FileCache.appendToFile(logFile, SIMPLE_DATE_FORMAT.format(new Date()) + "=> " + data);
        }
    }
}

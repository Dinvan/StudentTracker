package com.npsindore.utility;

import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper {


    private static final String TAG = FileHelper.class.getName();
    /**
     * Method for converting Uri into File path
     * @param context
     * @param uri Selected file Uri
     * @return
     */
    public static String getPath(final Context context, final Uri uri) {
        if (uri == null || context == null) {
            return null;
        }

        //final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        }
        // MediaStore (and general)
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Nullable
    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        String fileName = null;
        final String _display_name_column = "_display_name";
        final String[] projection = {
                column, _display_name_column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                final int column_name_index = cursor.getColumnIndexOrThrow(_display_name_column);
                fileName = cursor.getString(column_name_index);
                String filePath = cursor.getString(column_index);
                if (!StringHelper.isEmpty(filePath)) {
                    return filePath;
                }
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        if (fileName != null) {
            InputStream input;
            try {
                input = context.getContentResolver().openInputStream(uri);

                File file = FileHelper.createNewFile(context, fileName);//new File(Environment.getExternalStorageDirectory() + "/" + fileName);
                try {
                    OutputStream output = new FileOutputStream(file);
                    try {
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;
                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        return file.getAbsolutePath();
                    } finally {
                        output.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (input != null) {
                        input.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    static int  SIZE_LIMIT = 1;
    /**
     * Get compress files based on sample size and quality
     * @param file to be compressed
     * @param sampleSize to be used for compression
     * @param quality  to bused in compression
     * @return Compressed File
     */
    public static File compressBitmap(File file, int sampleSize, int quality,Context context) {
        File file1 = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            FileInputStream inputStream = new FileInputStream(file);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();
            file1 = getPath(context);
            FileOutputStream outputStream = new FileOutputStream(file1);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.close();
            long lengthInKb = (file1.length() / 1024) / 1024; //in mb
            if (lengthInKb > SIZE_LIMIT) {
                compressBitmap(file1, (sampleSize * 2), (quality / 4),context);
            }
            selectedBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file1;
    }

    /**
     * Method for getting temporary files location
     * @return
     */
    public static File getPath(Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("QAInspecta", Context.MODE_PRIVATE);
        // Create imageDir
        File file = new File(directory, "doc.jpg");
        Log.v("getPath", String.format("getPath() :: file = %s", file));
        return file;
    }

    public static File createNewFile(Context context, String fileName) throws IOException {
        File storageDir = context.getExternalFilesDir("QAInspecta");
        File file = new File(storageDir, fileName);
        Log.e("newFile", file.getAbsolutePath());
        return file;
    }

}

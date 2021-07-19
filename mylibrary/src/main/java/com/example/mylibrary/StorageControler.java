package com.example.mylibrary;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StorageControler {

    private String TAG = "";
    private Context context;
    private static int targetHeight = 0;
    private static int targetWidth = 0;
    private static int quality = 0;

    public StorageControler(Context context, String tag) {
        this.context = context;
        this.TAG = tag;

    }
    public StorageControler(Context context, String tag,int targetWidth, int targetHeight,int quality) {
        this.context = context;
        this.TAG = tag;
        this.targetWidth=targetWidth;
        this.targetHeight=targetHeight;
        this.quality=quality;

    }

    public void createTempDir() {
        File sd = context.getCacheDir();
        File folder = new File(sd, "/shravan/");

        if (!folder.exists()) {
            if (!folder.mkdir()) {
                Log.e("ERROR", "Cannot create a directory!");
            } else {
                folder.mkdirs();

            }
        }
    }

    public void deleteDir() {
        Log.e(TAG, "deleteDir: dfd");
        File dir = context.getCacheDir();
        if (dir != null) {
            String[] children = dir.list();
            if (children.length != 0) {
                for (int i = 0; i < children.length; i++) {
                    if (children[i].startsWith("cropped")) {
                        File file = new File(dir, children[i]);
                        file.delete();
                    }
                    Log.e(TAG, "deleteDir: " + children[i]);
                }
            }
        }
        dir = new File(dir, "/shravan/");
        if (dir != null) {
            String[] children1 = dir.list();
            if (children1 != null) {
                for (int i = 0; i < children1.length; i++) {
                    File file = new File(dir, children1[i]);
                    file.delete();
                    Log.e(TAG, "deleteDir: " + children1[i]);
                }
            }
        }

        // The directory is now empty so delete it
        return;
    }


    public String saveImageToStorage(Uri uri) {


    String filename=null;
    try {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (targetWidth != 0 || targetHeight != 0) {
            width = targetWidth > 0 ? targetWidth : bitmap.getWidth();
            height = targetHeight > 0 ? targetHeight : bitmap.getHeight();
        } else if (quality > 0) {
            width = bitmap.getWidth() * quality / 100;
            height = bitmap.getHeight() * quality / 100;

        }
        Log.e(TAG, "saveImageToStorage: "+width+" "+height );
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        String fileName = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        OutputStream fos;
        File image = null;
        Uri imageUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Doc" + fileName + ".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Pictures");
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
            filename=imageUri.toString();

        } else {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Pictures");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("PhotoEditorSDK", "Failed to create directory");
                } else {
                    return null;
                }
            }

            image = new File(mediaStorageDir.toString(), "Doc" + fileName + ".jpg");
            fos = new FileOutputStream(image);
            filename=image.toURI().toString();
        }


        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);


        bitmap.recycle();
        fos.close();

    } catch (Exception e) {
        filename=null;
    }

    return filename;

    }

    public String saveImageToStorage(Bitmap bitmap) {

        String filename=null;
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (targetWidth != 0 || targetHeight != 0) {
                width = targetWidth > 0 ? targetWidth : bitmap.getWidth();
                height = targetHeight > 0 ? targetHeight : bitmap.getHeight();
            } else if (quality > 0) {
                width = bitmap.getWidth() * quality / 100;
                height = bitmap.getHeight() * quality / 100;

            }
            Log.e(TAG, "saveImageToStorage: "+width+" "+height );
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            String fileName = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
            OutputStream fos;
            File image = null;
            Uri imageUri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Doc" + fileName + ".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Pictures");
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(imageUri);
                filename=imageUri.toString();

            } else {
                File mediaStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Pictures");
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.e("PhotoEditorSDK", "Failed to create directory");
                    } else {
                        return null;
                    }
                }

                image = new File(mediaStorageDir.toString(), "Doc" + fileName + ".jpg");
                fos = new FileOutputStream(image);
                filename=image.toURI().toString();
            }


                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);


                bitmap.recycle();
                fos.close();

        } catch (Exception e) {
            filename=null;
        }

return filename;
    }


    public String storeInTemp(byte[] bytes){


        File file = new File(context.getCacheDir(), "/shravan/temp.jpg");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.toURI().toString();
    }


    public static String getFileToByte(Context context,Uri uri)  {
        Mat mat = new Mat();
        String encodeString = null;
        try {
            Utils.bitmapToMat(MediaStore.Images.Media.getBitmap(
                    context.getContentResolver(),
                    uri), mat);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2RGB);

        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
            matOfByte.release();

        try{

            encodeString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        encodeString = encodeString.replaceAll("(\\r|\\n|\\t)", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mat.release();
        return encodeString;
    }

}
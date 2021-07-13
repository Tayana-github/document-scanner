package com.example.mylibrary;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static boolean CROP_ENABLE = false;
    private static int sourceType = 1;
    private static String filepath = null;
    private static int targetHeight = 0;
    private static int targetWidth = 0;
    private static boolean edgeDetection = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sourceType = extras.getInt("sourceType");
            CROP_ENABLE = extras.getBoolean("CROP_ENABLE");
            targetHeight = extras.getInt("targetHeight");
            targetWidth = extras.getInt("targetWidth");

            edgeDetection = extras.getBoolean("edgeDetection");
        }
        if (sourceType == 1) {
            Intent intent = new Intent(MainActivity.this, com.example.mylibrary.Camera2APIActivity.class);
            intent.putExtra("edgeDetection", edgeDetection);
            startActivityForResult(intent, 100);
        } else {

               Intent intent = new Intent(MainActivity.this, com.example.mylibrary.PickFromGalleryActivity.class);
               intent.putExtra("edgeDetection", edgeDetection);
               startActivityForResult(intent, 200);


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 101) {


            Bundle extras = data.getExtras();
            filepath = extras.getString("filename");
            edgeDetection=extras.getBoolean("edgeDetection");
            if (CROP_ENABLE) {
                Intent intent = new Intent(this, CropImageActivity.class);
                intent.putExtra("edgeDetection", edgeDetection);
                intent.putExtra("filename", filepath);
                if(edgeDetection) {

                    intent.putExtra("x", extras.getInt("x"));
                    intent.putExtra("y", extras.getInt("y"));
                    intent.putExtra("w", extras.getInt("w"));
                    intent.putExtra("h", extras.getInt("h"));
                }
                startActivityForResult(intent, 300);
            }
            else {
                Intent intent = new Intent();
                try {
                    filepath= saveImageToStorage(Uri.parse(filepath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intent.putExtra("filepath", filepath);
                setResult(401, intent);
                finish();
            }

        }else if (resultCode == 201) {


            Bundle extras = data.getExtras();
            filepath = extras.getString("filename");
            edgeDetection=extras.getBoolean("edgeDetection");
            if (CROP_ENABLE) {
                Intent intent = new Intent(this, CropImageActivity.class);
                intent.putExtra("edgeDetection", edgeDetection);
                intent.putExtra("filename", Uri.fromFile(new File(filepath)).toString());
                if(edgeDetection) {

                    intent.putExtra("x", extras.getInt("x"));
                    intent.putExtra("y", extras.getInt("y"));
                    intent.putExtra("w", extras.getInt("w"));
                    intent.putExtra("h", extras.getInt("h"));
                }
                startActivityForResult(intent, 300);
            }
            else {

                Intent intent = new Intent();
                intent.putExtra("filepath",filepath);
                setResult(401, intent);
                finish();

            }
        }else
        if (resultCode == 301) {


            Bundle extras = data.getExtras();

            filepath= extras.getString("filepath");

            try {
                filepath= saveImageToStorage(Uri.parse(filepath));
                Intent intent = new Intent();
                intent.putExtra("filepath", filepath);
                setResult(401, intent);
                finish();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        else{
            finish();
        }

    }



    private String saveImageToStorage(Uri uri) throws IOException {

        Bitmap bitmap ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            bitmap  =   ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), uri));
        } else {
             bitmap =  MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        }


//
//             float degrees = 90; //rotation degree
//             Matrix matrix = new Matrix();
//             matrix.setRotate(degrees);
//             bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//

        int width=targetWidth>0?targetWidth:bitmap.getWidth();
        int height=targetHeight>0?targetHeight:bitmap.getHeight();
        bitmap=Bitmap.createScaledBitmap(bitmap, width, height, true);
        String fileName = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        OutputStream imageOutStream;
        OutputStream fos;
        File image=null;
        Uri imageUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentResolver resolver = getApplicationContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Doc"+fileName+".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Pictures");
            imageUri  = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
            // Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();


        } else {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Pictures");
            // Create a storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("PhotoEditorSDK", "Failed to create directory");
                }
                else{
                    finish();
                }
            }
            //String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+"Pictures";
            image = new File(mediaStorageDir.toString(), "Doc"+fileName+".jpg");
            fos = new FileOutputStream(image);
        }

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } finally {
            fos.close();
        }


        if(imageUri!=null)
        {
            return getPath(getApplicationContext(),imageUri);
        }
        else {
            Log.e(TAG, "saveImageToStorage: "+image.getAbsolutePath());

            return image.getAbsolutePath();
        }


    }

    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Log.i("URI",uri+"");
        String result = uri+"";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {
            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length-1];
            final String[] dat = imgary.split("%3A");
            final String docId = dat[1];
            final String type = dat[0];
            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
            } else if ("audio".equals(type)) {
            }
            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {
                    dat[1]
            };
            return getDataColumn(context, contentUri, selection, selectionArgs);
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


}
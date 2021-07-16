package com.example.mylibrary;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;


import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;


import java.io.ByteArrayOutputStream;
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
                try {
                    intent.putExtra("filename", storeIntemp(getApplicationContext(),Uri.parse(filepath)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            return imageUri.toString();
        }
        else {
            Log.e(TAG, "saveImageToStorage: "+image.getAbsolutePath());

            return image.toURI().toString();
        }


    }
   private String storeIntemp(Context context,Uri  uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        File file = new File(getApplicationContext().getCacheDir(), "temp.jpg");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(byteArray);

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
        return  file.toURI().toString();

    }


}
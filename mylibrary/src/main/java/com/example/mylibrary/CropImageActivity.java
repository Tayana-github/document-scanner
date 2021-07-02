package com.example.mylibrary;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CropImageActivity extends AppCompatActivity {
    private static final String TAG = "CropImageActivity";
    String value;

    RectData rectData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_image);

        Log.e(TAG, "onActivityResult: "+ "sss" );

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             value = extras.getString("filename");
             rectData=new com.example.mylibrary.RectData(extras.getInt("w"),extras.getInt("h"),extras.getInt("x"),extras.getInt("y"));
            Log.e(TAG, "onActivityResult: "+ rectData );
            //The key argument here must match that used in the other activity
        }

        if(isStoragePermissionGranted()) {
            CropImage.activity(Uri.parse(value)).setAutoZoomEnabled(true).setInitialCropWindowRectangle(new android.graphics.Rect(rectData.x, rectData.y, rectData.x + rectData.w, rectData.y + rectData.h)).start(CropImageActivity.this);
            //CropImage.activity().setAutoZoomEnabled(true).setInitialCropWindowRectangle(new android.graphics.Rect(rectData.x, rectData.y, rectData.x + rectData.w, rectData.y + rectData.h)).start(CropImageActivity.this);

        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: "+resultCode);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri( );

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);

                    saveImageToStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 Exception error = result.getError();
                Log.e(TAG, "onActivityResult: "+ error);

            }
        }

     finish();
    }



    private void saveImageToStorage(Bitmap bitmap) throws IOException {
        String fileName = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        OutputStream imageOutStream;
        OutputStream fos;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentResolver resolver = getApplicationContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Doc"+fileName+".jpg");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Pictures");
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
           // Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();


        } else {
            String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File image = new File(imagePath, "Doc"+fileName+".jpg");
            fos = new FileOutputStream(image);
        }

        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } finally {
            fos.close();
        }

    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");

                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.e(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            CropImage.activity(Uri.parse(value)).setAutoZoomEnabled(true).setInitialCropWindowRectangle(new android.graphics.Rect(rectData.x, rectData.y, rectData.x + rectData.w, rectData.y + rectData.h)).start(CropImageActivity.this);

            //resume tasks needing this permission
        }
        else {
           finish();
        }
        }



}

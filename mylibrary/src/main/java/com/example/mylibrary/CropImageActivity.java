package com.example.mylibrary;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;



public class CropImageActivity extends AppCompatActivity {
    private static final String TAG = "CropImageActivity";
    String value;

    RectData rectData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             value = extras.getString("filename");
             rectData=new com.example.mylibrary.RectData(extras.getInt("w"),extras.getInt("h"),extras.getInt("x"),extras.getInt("y"));

            //The key argument here must match that used in the other activity
        }



        if(isStoragePermissionGranted()) {

            CropImage.activity(Uri.parse(value)).setAutoZoomEnabled(true).setInitialCropWindowRectangle(new android.graphics.Rect(rectData.x, rectData.y, rectData.x + rectData.w, rectData.y + rectData.h)).start(CropImageActivity.this);
       //CropImage.activity(Uri.parse(value)).start(CropImageActivity.this);

            //cropImageView.rotateImage(0);
        }
        else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }



    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri( );
                Log.e(TAG, "onActivityResult: "+resultUri );
                String path=resultUri.toString();
                Intent intent=new Intent();
                intent.putExtra("filepath",path);
                setResult(301, intent);
finish();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 Exception error = result.getError();
                Log.e(TAG, "onActivityResult: "+ error);

            }
        }

     finish();
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

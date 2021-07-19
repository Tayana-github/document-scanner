package com.example.mylibrary;


import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static boolean CROP_ENABLE = false;
    private static int sourceType = 0;
    private static String filepath = null;
    private static int targetHeight = 0;
    private static int targetWidth = 0;
    private static int quality = 0;
    private static boolean edgeDetection = true;
    private StorageControler storageControler;


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
            quality = extras.getInt("quality");
            edgeDetection = extras.getBoolean("edgeDetection");
            Log.e(TAG, "onCreate: "+edgeDetection );
        }
        storageControler = new StorageControler(getApplicationContext(), "MainActivity",targetWidth,targetHeight,quality);
        storageControler.createTempDir();
        if (sourceType == 1) {
            Intent intent = new Intent(MainActivity.this, com.example.mylibrary.Camera2APIActivity.class);
            intent.putExtra("edgeDetection", edgeDetection);
            intent.putExtra("CROP_ENABLE", CROP_ENABLE);
            startActivityForResult(intent, 100);
        } else {

            Intent intent = new Intent(MainActivity.this, com.example.mylibrary.PickFromGalleryActivity.class);
            intent.putExtra("edgeDetection", edgeDetection);
            intent.putExtra("CROP_ENABLE", CROP_ENABLE);
            startActivityForResult(intent, 200);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 101) {


            Bundle extras = data.getExtras();
            filepath = extras.getString("filename");
            edgeDetection = extras.getBoolean("edgeDetection");
            if (CROP_ENABLE) {
                Intent intent = new Intent(this, CropImageActivity.class);
                intent.putExtra("edgeDetection", edgeDetection);
                intent.putExtra("filename", filepath);
                if (edgeDetection) {

                    intent.putExtra("x", extras.getInt("x"));
                    intent.putExtra("y", extras.getInt("y"));
                    intent.putExtra("w", extras.getInt("w"));
                    intent.putExtra("h", extras.getInt("h"));
                }
                startActivityForResult(intent, 300);
            } else {
                Intent intent = new Intent();


                    intent.putExtra("filepath", filepath);

                    storageControler.deleteDir();
                setResult(401, intent);
                finish();


            }

        } else if (resultCode == 201) {


            Bundle extras = data.getExtras();
            filepath = extras.getString("filename");
            edgeDetection = extras.getBoolean("edgeDetection");
            if (CROP_ENABLE) {
                Intent intent = new Intent(this, CropImageActivity.class);
                intent.putExtra("edgeDetection", edgeDetection);
                intent.putExtra("filename", filepath);
                if (edgeDetection) {

                    intent.putExtra("x", extras.getInt("x"));
                    intent.putExtra("y", extras.getInt("y"));
                    intent.putExtra("w", extras.getInt("w"));
                    intent.putExtra("h", extras.getInt("h"));
                }
                startActivityForResult(intent, 300);
            } else {
                storageControler.deleteDir();
                Intent intent = new Intent();
                intent.putExtra("filepath", filepath);
                setResult(401, intent);

                finish();


            }
        } else if (resultCode == 301) {


            Bundle extras = data.getExtras();

            filepath = extras.getString("filepath");
            Intent intent = new Intent();
            filepath = storageControler.saveImageToStorage(Uri.parse(filepath));


            intent.putExtra("filepath", filepath);

            storageControler.deleteDir();
            setResult(401, intent);
            finish();
        } else {
            storageControler.deleteDir();
            Intent intent = new Intent();
            setResult(401, intent);
            finish();

        }

    }
}
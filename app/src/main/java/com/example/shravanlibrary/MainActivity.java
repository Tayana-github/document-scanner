package com.example.shravanlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;

import com.example.mylibrary.StorageControler;




public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, com.example.mylibrary.MainActivity.class);

        intent.putExtra("sourceType", 1);
        intent.putExtra("CROP_ENABLE", false);
        intent.putExtra("targetHeight", 720);
        intent.putExtra("targetWidth", 1080);
        intent.putExtra("quality", 50);
        intent.putExtra("edgeDetection", true);
        startActivityForResult(intent, 2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 401) {




            if(data.hasExtra("filepath")) {
                Bundle extras = data.getExtras();



                    Log.e(TAG, "onActivityResult: decode" );

                    Log.e(TAG, "onActivityResult: " + StorageControler.getFileToByte(getApplicationContext(), Uri.parse(extras.getString("filepath")),0,0,50,0,false));


            }

        }else{
           finish();
        }

    }

}
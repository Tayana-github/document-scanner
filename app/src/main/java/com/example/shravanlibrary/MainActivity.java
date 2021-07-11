package com.example.shravanlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mylibrary.CropImageActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, com.example.mylibrary.MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("sourceType", 1);
        intent.putExtra("CROP_ENABLE", true);
       intent.putExtra("targetHeight", 720);
       intent.putExtra("targetWidth", 1080);

      intent.putExtra("edgeDetection", true);
        startActivityForResult(intent, 2);
        //finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 401) {


            Bundle extras = data.getExtras();
            Log.e(TAG, "onActivityResult: " + extras.getString("filepath"));


        }

    }
}
package com.example.shravanlibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, com.example.mylibrary.MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("sourceType", 0);
        intent.putExtra("CROP_ENABLE", true);
        intent.putExtra("targetHeight", 720);
        intent.putExtra("targetWidth", 1080);

        intent.putExtra("edgeDetection", false);
        startActivityForResult(intent, 2);
        //finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == 401) {


            Bundle extras = data.getExtras();
            Log.e(TAG, "onActivityResult: 12 " + extras.getString("filepath"));

            try {


                Log.e(TAG, "onActivityResult: "+getFileToByte(getApplicationContext(),Uri.parse(extras.getString("filepath"))) );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "onActivityResult: "+e );
            }



        }else{
           // finish();
        }

    }
    public static String getFileToByte(Context context,Uri uri) throws FileNotFoundException {
        final InputStream imageStream;
        imageStream = context.getContentResolver().openInputStream(uri);
        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try{

            bos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        encodeString = encodeString.replaceAll("(\\r|\\n|\\t)", "");
        return encodeString;
    }


}
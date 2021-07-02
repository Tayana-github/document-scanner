package com.example.mylibrary;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(MainActivity.this,MainActivity3.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("start","camera");
        startActivity(intent);
        finish();
       // getActionBar().hide();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, Camera2BasicFragment.newInstance()).addToBackStack(null)
//                .commit();

    }

}
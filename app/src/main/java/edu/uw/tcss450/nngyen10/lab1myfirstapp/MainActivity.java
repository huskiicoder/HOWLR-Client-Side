package edu.uw.tcss450.nngyen10.lab1myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("onCreate", "Activities in onCreate() method");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart", "Activities in onStart() method");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "Activities in onResume() method");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause", "Activities in onPause() method");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop", "Activities in onStop() method");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy", "Activities in onDestroy() method");
    }
}
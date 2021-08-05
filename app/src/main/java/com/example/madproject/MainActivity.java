package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toLoginPage(View view) {
        Intent tologinpage = new Intent(this, Login.class);
        startActivity(tologinpage);
    }

    public void toRegister(View view) {
        Intent toregisterpage = new Intent(this, Sign_Up.class);
        startActivity(toregisterpage);
    }
}
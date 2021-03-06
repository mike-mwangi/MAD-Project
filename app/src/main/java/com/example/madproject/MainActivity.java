package com.example.madproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=findViewById(R.id.button);
        register=findViewById(R.id.button2);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser !=null && currentUser.isEmailVerified()){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
        }

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
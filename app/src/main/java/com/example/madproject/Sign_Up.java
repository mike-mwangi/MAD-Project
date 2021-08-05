package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_Up extends AppCompatActivity {
    TextInputLayout fName,Email,pno,passw,Uname;
    Button register,toLogin;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fName = findViewById(R.id.fName);
        Uname = findViewById(R.id.uName);
        Email = findViewById(R.id.email);
        pno = findViewById(R.id.pno);
        passw = findViewById(R.id.passw);
        register = findViewById(R.id.reg);
        toLogin =findViewById(R.id.toLogin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fName.getEditText().getText().toString();
                String username = Uname.getEditText().getText().toString();
                String email = Email.getEditText().getText().toString();
                String number = pno.getEditText().getText().toString();
                String pass = passw.getEditText().getText().toString();

                //Text Validation
                if(name.length()==0){
                    fName.setError("Field must not be empty");
                }
                if(username.length()==0){
                    Uname.setError("Field must not be empty");
                }
                if(email.length()==0){
                    Email.setError("Field must not be empty");
                }
                if (number.length()==0){
                    pno.setError("Field must not be empty");
                }
                if (pass.length()==0){
                    passw.setError("Field must not be empty");
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("Invalid Email Address");
                }
                if (!Patterns.PHONE.matcher(number).matches()){
                    pno.setError("Invalid phone number");
                }else{
                    rootNode = FirebaseDatabase.getInstance();
                    reference = rootNode.getReference("users");


                    UserHelper helper = new UserHelper(name,username,email,number,pass);

                    reference.child(username).setValue(helper);

                    Intent toLanding = new Intent( Sign_Up.this, Landing_Page.class);
                    startActivity(toLanding);

                    Toast.makeText(Sign_Up.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                }




            }
        });


    }

    public void toLogin(View view) {
        Intent tologin = new Intent(this, Login.class);
        startActivity(tologin);
    }
}
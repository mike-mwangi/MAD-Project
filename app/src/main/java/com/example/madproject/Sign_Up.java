package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_Up extends AppCompatActivity {
    TextInputLayout fName,Email,pno,passw,Uname;
    Button register,toLogin;

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference authReference;
    FirebaseAuth mAuth;

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

        mAuth= FirebaseAuth.getInstance();

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
                   // authReference=rootNode.getReference();
                    mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String userId=mAuth.getCurrentUser().getUid();

                            UserHelper helper = new UserHelper(name,username,email,number,pass);

                            reference.child(userId).setValue(helper);

                            Toast.makeText(Sign_Up.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                            Intent toLanding = new Intent( Sign_Up.this, HomeActivity.class);
                            startActivity(toLanding);


                        }
                    });


                }




            }
        });


    }

    public void toLogin(View view) {
        Intent tologin = new Intent(this, Login.class);
        startActivity(tologin);
    }
}
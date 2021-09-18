package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
private FirebaseAuth mAuth;
private DatabaseReference dbRef;
private TextInputLayout email, password;
private Button forgotPassword, login, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.emailAddress);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login_button);
        signUp=findViewById(R.id.toRegister);
        forgotPassword=findViewById(R.id.forget_password);
        mAuth=FirebaseAuth.getInstance();
        dbRef=FirebaseDatabase.getInstance().getReference().child("users");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkLogin();
            }
        });

    }
    private void checkLogin(){
        String login_email=email.getEditText().getText().toString();
        String login_password=password.getEditText().getText().toString();

        if(!TextUtils.isEmpty(login_email)&&!TextUtils.isEmpty(login_password)){
            mAuth.signInWithEmailAndPassword(login_email,login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserExist();

                    }else{
                        Toast.makeText(Login.this,"User Not Found",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            Toast.makeText(Login.this,"Fill in all fields",Toast.LENGTH_LONG).show();
        }
    }

    private void checkUserExist(){
        String user_id=mAuth.getCurrentUser().getUid();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(user_id)){
                    Intent toHomepage=new Intent(Login.this,HomeActivity.class);
                    startActivity(toHomepage);
                }else{
                    Toast.makeText(Login.this,"User not registered",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void recoverPassword(View view) {

    }

    public void signUp(View view) {

        startActivity(new Intent(Login.this, Sign_Up.class));
    }
}
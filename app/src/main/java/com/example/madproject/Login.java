package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;


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
    public String TAG= "LOGIN ACTIVITY";

    TextInputLayout email;
    TextInputLayout password;
    TextView notVerifiedText;
    ExtendedFloatingActionButton resendVerificationButton;
    MaterialButton loginBtn;
    MaterialButton forgotPassBtn;


    public FirebaseAuth mAuth;
private DatabaseReference dbRef;
    private FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.passw);
        loginBtn=findViewById(R.id.login_btn);
        notVerifiedText=findViewById(R.id.not_verified_text);
        resendVerificationButton=findViewById(R.id.resend_verification_btn);
        forgotPassBtn=findViewById(R.id.forgot_password_btn);


    resendVerificationButton.setVisibility(View.INVISIBLE);
    notVerifiedText.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getEditText().getText().toString().contentEquals("")) {


                    email.setError("Email cannot be empty");


                } else if (password.getEditText().getText().toString().contentEquals("")) {

                    password.setError("Password cant be empty");

                } else {


                    mAuth.signInWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {



                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");

                                        user = mAuth.getCurrentUser();

                                        if (user != null) {
                                            if (user.isEmailVerified()) {


                                                System.out.println("Email Verified : " + user.isEmailVerified());
                                                Intent HomeActivity = new Intent(Login.this, MainActivity.class);
                                                setResult(RESULT_OK, null);
                                                startActivity(HomeActivity);
                                                Login.this.finish();


                                            } else {

                                                resendVerificationButton.setVisibility(View.VISIBLE);

                                                notVerifiedText.setText("Please Verify your EmailID and SignIn");
                                                notVerifiedText.setVisibility(View.VISIBLE);


                                            }
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        if (task.getException() != null) {
                                            notVerifiedText.setText(task.getException().getMessage());
                                        }

                                    }

                                }
                            });


                }


            }
        });

        resendVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendEmailVerification(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent forgotPasswordActivity = new Intent(Login.this, Sign_Up.class);
                startActivity(forgotPasswordActivity);
                Login.this.finish();

            }
        });


    }


    public void toLogin(View view) {
        String emailString=email.getEditText().getText().toString();
        String passwordString=password.getEditText().getText().toString();
        //verify(emailString,passwordString);
    }
  /*  public void verify(String email,String password){

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

   */
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
    public void sendEmailVerification(FirebaseUser user) throws Exception{
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    Login.this);

                            // set title
                            alertDialogBuilder.setTitle("Please Verify Your EmailID");

                            // set dialog message
                            alertDialogBuilder
                                    .setMessage("A verification Email Is Sent To Your Registered EmailID, please click on the link and Sign in again!")
                                    .setCancelable(false)
                                    .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            Intent signInIntent = new Intent(Login.this, Login.class);
                                            Login.this.finish();
                                        }
                                    });

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // show it
                            alertDialog.show();


                        }
                    }
                });
    }
}
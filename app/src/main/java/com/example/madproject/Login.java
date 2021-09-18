package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {
    public String TAG= "LOGIN ACTIVITY";

    TextInputLayout email;
    TextInputLayout password;
    TextView notVerifiedText;
    ExtendedFloatingActionButton resendVerificationButton;
    MaterialButton loginBtn;
    MaterialButton forgotPassBtn;

    public FirebaseAuth mAuth;

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

                                        FirebaseUser user = mAuth.getCurrentUser();

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
        verify(emailString,passwordString);
    }
    public void verify(String email,String password){

    }
}
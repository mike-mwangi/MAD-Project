package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_Up extends AppCompatActivity {
    TextInputLayout fName,Email,pno,passw,Uname;
    Button register,toLogin;
    FirebaseAuth mAuth;
    public String TAG=this.getPackageName();

    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference authReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
mAuth=FirebaseAuth.getInstance();
        fName = findViewById(R.id.fName);
        Uname = findViewById(R.id.uName);
        Email = findViewById(R.id.email);
        pno = findViewById(R.id.pno);
        passw = findViewById(R.id.passw);
        register = findViewById(R.id.reg);
        toLogin =findViewById(R.id.login_btn);

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
                if (name.length() == 0) {
                    fName.setError("Field must not be empty");
                }
                if (username.length() == 0) {
                    Uname.setError("Field must not be empty");
                }
                if (email.length() == 0) {
                    Email.setError("Field must not be empty");
                }
                if (number.length() == 0) {
                    pno.setError("Field must not be empty");
                }
                if (pass.length() == 0) {
                    passw.setError("Field must not be empty");
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Email.setError("Invalid Email Address");
                }
                if (!Patterns.PHONE.matcher(number).matches()) {
                    pno.setError("Invalid phone number");
                } else {
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


                    UserHelper helper = new UserHelper(name, username, email, number, pass);


                        }
                    });



                    Toast.makeText(Sign_Up.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                }



                    mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(Sign_Up.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                try {
                                    if (user != null)
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "Email sent.");

                                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                    Sign_Up.this);

                                                            // set title
                                                            alertDialogBuilder.setTitle("Please Verify Your EmailID");

                                                            // set dialog message
                                                            alertDialogBuilder
                                                                    .setMessage("A verification Email Is Sent To Your Registered EmailID, please click on the link and Sign in again!")
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {

                                                                            Intent signInIntent = new Intent(Sign_Up.this, Login.class);
                                                                            Sign_Up.this.finish();
                                                                        }
                                                                    });

                                                            // create alert dialog
                                                            AlertDialog alertDialog = alertDialogBuilder.create();

                                                            // show it
                                                            alertDialog.show();


                                                        }
                                                    }
                                                });
                                } catch (Exception e) {

                                }
                            }
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
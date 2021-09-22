package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

public class ProfileActivity extends AppCompatActivity {

    private TextInputLayout fname,email,number;
    private TextView username;
    private Button updateUserButton;
    private ImageView profileImg;

    //private FirebaseUser user;
    private DatabaseReference databaseReference;
    //private FirebaseAuth firebaseAuth;
    //private StorageReference storageReference;

    private String usernumber = "";
    private String userFName = "";
    private String userName = "";
    private String userImage;

    static int reqCode=1;
    private final int IMAGE_REQUEST = 1;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fname = findViewById(R.id.fname_prof);
        email = findViewById(R.id.email_Profile);
        number = findViewById(R.id.pNumberProf);
        username = findViewById(R.id.username_prof);
        profileImg = findViewById(R.id.profile_image);
        updateUserButton = findViewById(R.id.updateUser);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >=22){
                    checkAndRequestPermission();
                }else{
                    openGallery();
                }
            }
        });

        updateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateUserDetais();
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        showUserData();

    }

    private void updateUserDetais() {
        //String newName = fname.getEditText().getText().toString();
        //String newNumber = number.getEditText().getText().toString();
        //String newuserName = fname.getEditText().getText().toString();
        //String newProfImage =

    }

    private void openGallery() {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("profile_image/*");
        startActivityForResult(imageIntent,IMAGE_REQUEST);

    }

    private void checkAndRequestPermission() {
        if(ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)){

                Toast.makeText(ProfileActivity.this,"Please accept required permission",Toast.LENGTH_SHORT).show();
            }else{
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},reqCode);
            }
        }else
            openGallery();

    }

    private void showUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String Temail = user.getEmail();

            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();


            databaseReference = FirebaseDatabase.getInstance().getReference();
            Query query = databaseReference.child("users");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    usernumber = snapshot.child(uid).child("number").getValue(String.class);
                    userFName = snapshot.child(uid).child("fname").getValue(String.class);
                    userName = snapshot.child(uid).child("username").getValue(String.class);
                    userImage=snapshot.child(uid).child("userImage").getValue(String.class);
                    Glide.with(ProfileActivity.this)
                            .load(userImage)
                            .into(profileImg);

                    fname.getEditText().setText(userFName);
                    number.getEditText().setText(usernumber);
                    username.setText(userName);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
            email.getEditText().setText(Temail);


        } else {
            // No user is signed in
            Toast.makeText(ProfileActivity.this,"Please sign in to view details",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null){
            uri = data.getData();
            profileImg.setImageURI(uri);


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_screen_options, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
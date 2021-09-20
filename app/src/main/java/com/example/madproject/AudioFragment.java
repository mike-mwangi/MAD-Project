package com.example.madproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class AudioFragment extends Fragment {
    //Audio variables
    private EditText audio_title;
    private ImageButton audio_image;
    private Button audio_upload,attach_audio;
    private DatabaseReference dbAudioRef;
    private StorageReference storageRef;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    static int reqCode=1;
    private final int IMAGE_REQUEST = 1;
    private final int AUDIO_REQUEST = 2;
    private Uri uri,audioUri;

    public AudioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_audio, container, false);

        //initialization
        audio_title=rootView.findViewById(R.id.audio_title);
        audio_image=rootView.findViewById(R.id.audio_image);
        audio_upload=rootView.findViewById(R.id.audio_upload);
       attach_audio=rootView.findViewById(R.id.attach_audio);
        dbAudioRef =FirebaseDatabase.getInstance().getReference().child("audio");
        storageRef = FirebaseStorage.getInstance().getReference();
        db=FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        audio_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){

                        Toast.makeText(getActivity(),"Please accept required permission",Toast.LENGTH_SHORT).show();
                    }else{
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},reqCode);
                    }
                }else {
                    Intent audioImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    audioImageIntent.setType("image/*");
                    startActivityForResult(audioImageIntent, IMAGE_REQUEST);
                }
            }
        });
        //Fetch audio from phone storage
        attach_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Fetching audio...",Toast.LENGTH_SHORT).show();
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){

                        Toast.makeText(getActivity(),"Please accept required permission",Toast.LENGTH_SHORT).show();
                    }else{
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},reqCode);
                    }
                }else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("audio/*"); // specify "audio/mp3" to filter only mp3 files
                    startActivityForResult(intent,AUDIO_REQUEST);
                }
            }
        });
        //Publishing audio post to firebase
        audio_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               save();
            }
        });

        return rootView;
    }

    private void save() {
        Toast.makeText(getActivity(), "Uploading Audio....", Toast.LENGTH_SHORT).show();
        //Fetch input values
        String audioTitle = audio_title.getText().toString().trim();

        //fetch current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat cDate = new SimpleDateFormat("MM/dd/yyyy");
        final String currentDate = cDate.format(calendar.getTime());

        Calendar calendar2 = Calendar.getInstance();
        SimpleDateFormat cTime = new SimpleDateFormat("HH:mm");
        final String currentTime = cTime.format(calendar2.getTime());


        if ((!TextUtils.isEmpty(audioTitle))) {
            StorageReference path = storageRef.child("audio_blogs").child(uri.getLastPathSegment());
            path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            //get download url
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String imageUrl = uri.toString();
                                    Toast.makeText(getActivity(), "AudioImage uploaded successfully", Toast.LENGTH_SHORT).show();
                                    //final DatabaseReference audio = dbAudioRef.push();

                                    //Firestore implementation
                                    // Create an audio Firestore map
                                    Map<String, Object> audioPost = new HashMap<>();
                                    audioPost.put("audioImage", imageUrl);
                                    audioPost.put("audioTitle",audioTitle);
                                    audioPost.put("UserId", currentUser.getUid());
                                    audioPost.put("uploadDate",currentDate);
                                    audioPost.put("uploadTime",currentTime);


                                    //store in Firestore
                                    db.collection("audioPost")
                                            .add(audioPost)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    Toast.makeText(getActivity(), "Audio posted successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                    Toast.makeText(getActivity(), "Audio posted failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                }
                            });

                        }
                    }
                }
            });

        }

    }

    //Used by both audio_image and attach_audio
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AudioFragment.super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null) {
            //set Image
            uri = data.getData();
            audio_image.setImageURI(uri);
            }
        if(requestCode == AUDIO_REQUEST && resultCode == RESULT_OK && data!=null) {
            //audio from files
            audioUri = data.getData();
            Toast.makeText(getActivity(), "Audio ready for upload", Toast.LENGTH_SHORT).show();
            }
    }



}

package com.example.madproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class AudioFragment extends Fragment {
    //Audio variables
    private EditText audio_title;
    private ImageButton audio_image;
    private Button audio_upload,attach_audio;
    private DatabaseReference dbAudioRef;

    static int reqCode=1;
    private final int REQUEST = 1;
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
                    audioImageIntent.setType("audio/images/*");
                    startActivityForResult(audioImageIntent, REQUEST);
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
                    startActivityForResult(intent,1);
                }
            }
        });
        //Publishing audio post to firebase
        audio_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                //

            }
        });

        return rootView;
    }

    //Used by both audio_image and attach_audio: sort this
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AudioFragment.super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST && resultCode == RESULT_OK && data!=null){
            //set Image
            uri=data.getData();
            audio_image.setImageURI(uri);

            //audio from files
            audioUri = data.getData();
            Toast.makeText(getActivity(),"Audio ready for upload",Toast.LENGTH_SHORT).show();


        }
    }


}
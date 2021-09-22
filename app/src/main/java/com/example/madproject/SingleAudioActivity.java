package com.example.madproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.madproject.Models.Audio;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.IOException;

public class SingleAudioActivity extends AppCompatActivity {
    Button backwardBtn,forwardBtn,pauseBtn;
    ShapeableImageView audioImage;
    ImageView speakerImage;
    MaterialTextView speakerName;
    MaterialTextView songDuration;
    TextView title;
    MediaPlayer mediaPlayer;
    SeekBar seekbar;
    Audio myAudio;
    Toolbar toolbar;
    private int length;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_audio);
        backwardBtn=findViewById(R.id.backward);
        forwardBtn=findViewById(R.id.forward);
        pauseBtn=findViewById(R.id.pause_play);
        audioImage = (ShapeableImageView) findViewById(R.id.audio_image);
        title = findViewById(R.id.audio_title);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        speakerImage=findViewById(R.id.speaker_image);
        speakerName=findViewById(R.id.speaker_name);
        songDuration=findViewById(R.id.duration);
        toolbar=findViewById(R.id.home_toolbar);

        Intent intent = getIntent();
        String audioID = intent.getStringExtra("audioID");
        fetchAudioFromDatabase(audioID);

        pauseBtn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    pauseBtn.setText("PLAY");
                    length = mediaPlayer.getCurrentPosition();
                }
                else{
                    mediaPlayer.start();
                    mediaPlayer.seekTo(length);
                    pauseBtn.setText("| |");
                }

            }
        });
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

    private void fetchAudioFromDatabase(String audioID) {
        DocumentReference audiPost = FirebaseFirestore.getInstance().collection("audioPost").document(audioID);
        audiPost.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                myAudio=value.toObject(Audio.class);
                Glide.with(SingleAudioActivity.this)
                        .load(myAudio.getAudioImage())
                        .into(audioImage);
                title.setText(myAudio.getAudioTitle());
                FirebaseDatabase.getInstance().getReference("users").child(myAudio.getUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserHelper value1 = snapshot.getValue(UserHelper.class);
                        Glide.with(SingleAudioActivity.this)
                                .load(value1.getUserImage())
                                .into(speakerImage);
                        speakerName.setText(value1.getFname());


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                playAudio();
            }
        });
    }

    public void playAudio(){
        String audioUrl = myAudio.getAudio();

        // initializing media player
        mediaPlayer = new MediaPlayer();

        // below line is use to set the audio
        // stream type for our media player.
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // below line is use to set our
        // url to our media player.
        try {
            mediaPlayer.setDataSource(audioUrl);
            // below line is use to prepare
            // and start our media player.
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        // below line is use to display a toast message.
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
        SingleAudioActivity.this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                long totalDuration = mediaPlayer.getDuration();
                long currentDuration = mediaPlayer.getCurrentPosition();

                long totalMinutes = (totalDuration/ 1000) / 60;
                long totalSeconds = (totalDuration/ 1000) % 60;
                long currentMinutes = (currentDuration/ 1000) / 60;
                long currentSeconds = (currentDuration/ 1000) % 60;

                songDuration.setText(String.valueOf(totalMinutes)+" : "+String.valueOf(totalSeconds)+" / "+String.valueOf(currentMinutes)+" : "+String.valueOf(currentSeconds));


                double percentageUsed = (long)((float)currentDuration/totalDuration*100);

                seekbar.setProgress((int) percentageUsed);

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 1000);
            }
        });
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress * 1000);
                }
            }
        });
    }
    private Handler mHandler = new Handler();
//Make sure you update Seekbar on UI thread

}
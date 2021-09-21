package com.example.madproject;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SingleAudioActivity extends AppCompatActivity {
    Button b1,b2,b3,b4;
    ImageView audioImage;
    TextView title;
    MediaPlayer mediaPlayer;
    SeekBar seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_audio);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        audioImage = (ImageView) findViewById(R.id.audio_image);
        title = findViewById(R.id.audio_title);


        mediaPlayer = MediaPlayer.create(this, Uri.parse("nthng"));
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setClickable(false);
        b2.setEnabled(false);

    }
}
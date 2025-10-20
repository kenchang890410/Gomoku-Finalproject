package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button PVEesay, PVP, PVEhard;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PVP = findViewById(R.id.pvpbt);
        PVEesay = findViewById(R.id.esaybt);
        PVEhard = findViewById(R.id.hardbt);

        //music
        mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusictitle);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //game mode
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int variableValue = 0;

                switch (v.getId()) {
                    case R.id.pvpbt:
                        variableValue = 0;
                        break;
                    case R.id.esaybt:
                        variableValue = 2;
                        break;
                    case R.id.hardbt:
                        variableValue = 3;
                        break;
                }

                //to page 2
                Intent intent = new Intent(MainActivity.this, startplay.class);
                //Sending variable
                intent.putExtra("myVariable", variableValue);
                startActivity(intent);
            }
        };

        PVP.setOnClickListener(buttonClickListener);
        PVEesay.setOnClickListener(buttonClickListener);
        PVEhard.setOnClickListener(buttonClickListener);

        //Setting button color
        PVP.setBackgroundColor(Color.parseColor("#DEB878"));
        PVEesay.setBackgroundColor(Color.parseColor("#DEB878"));
        PVEhard.setBackgroundColor(Color.parseColor("#DEB878"));

        //Disable Screen Rotation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaPlayer.start();
    }

}
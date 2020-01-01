package com.example.hw1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class Setting extends AppCompatActivity {
    private Button about, exit;
    public static Switch sensorSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();
        about = findViewById(R.id.AboutBTN1);
        exit = findViewById(R.id.backBTN1);
        sensorSwitch = findViewById(R.id.SwichBTN);
        sensorSwitch.setChecked(true);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAbout();
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }


    public void goToAbout(){
        Intent aboutIntent = new Intent(this, About.class);
        startActivity(aboutIntent);
    }

}

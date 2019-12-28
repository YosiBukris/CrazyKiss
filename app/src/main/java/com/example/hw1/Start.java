package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Start extends AppCompatActivity {
    private Button newGame, scoreList, settings, exit;
    private ImageView logo;
    private Animation logoAnim;
    private String basicName = "     Enter Your Name";
    public static EditText name;
    public static MediaPlayer ring;
    public static String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        logo = findViewById(R.id.logo);
        newGame = findViewById(R.id.newGameBTN);
        scoreList = findViewById(R.id.scoreBTN);
        settings = findViewById(R.id.settingsBTN);
        exit = findViewById(R.id.exitBTN);
        name = findViewById(R.id.editName);
        ring= MediaPlayer.create(this,R.raw.ring);
        start();
    }

    private void start() {
        ring.start();
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        logo.startAnimation(logoAnim);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startNewGame();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                System.exit(1);
            }
        });

        scoreList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToScoreList();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettings();
            }
        });
    }

    public void startNewGame(){
        if(name.getText().toString().isEmpty())
            name.setBackgroundColor(Color.RED);
        else {
            ring.pause();
            playerName = name.getText().toString();
            Intent newGameIntent = new Intent(Start.this, MainGame.class);
            startActivity(newGameIntent);
        }
    }

    public void goToScoreList(){
        ring.pause();
        Intent scoreIntent = new Intent(this, Score.class);
        startActivity(scoreIntent);
    }

    public void goToSettings(){
        ring.pause();
        Intent settingIntent = new Intent(this, Setting.class);
        startActivity(settingIntent);
    }
}

package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
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
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Start extends AppCompatActivity {
    private Button newGame, scoreList, about, exit;
    private ImageView logo;
    private Animation logoAnim;
    public static ArrayList<Integer> scoresArr = new ArrayList<Integer>();
    public static MediaPlayer ring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        logo = findViewById(R.id.logo);
        newGame = findViewById(R.id.newGameBTN);
        scoreList = findViewById(R.id.scoreBTN);
        about = findViewById(R.id.aboutBTN);
        exit = findViewById(R.id.exitBTN);
        ring= MediaPlayer.create(this,R.raw.ring);
        ring.start();
        start();
    }

    private void start() {
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

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAbout();
            }
        });
    }

    public void startNewGame(){
        ring.pause();
        Intent newGameIntent = new Intent(Start.this, MainGame.class);
        startActivity(newGameIntent);
    }

    public void goToScoreList(){
        ring.pause();
        Collections.sort(scoresArr);
        Intent scoreIntent = new Intent(this, Score.class);
        startActivity(scoreIntent);
    }

    public void goToAbout(){
        ring.pause();
        Intent aboutIntent = new Intent(this, About.class);
        startActivity(aboutIntent);
    }
}

package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainGame extends AppCompatActivity {
    private ImageView[] enemy;

    private ValueAnimator[] animations;
    private int animationIndex;

    private ImageView player;
    private ImageView[] hearts;
    private int playerPlace = 1, heartsCount = 3;

    private final int maxLeft = 0;
    private final int maxRight = 2;

    private Button right,left,pause;
    private ImageView resumeIMG, pauseIMG;

    private boolean toResume = false;
    private MediaPlayer kissSound;

    private TextView scoreLBL;
    private int score = 0;

    int [] enemyPlace = new int[2];
    int [] playerPlaceNow = new int[2];

    int time = 350;

    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        enemy = new ImageView[]{
                findViewById(R.id.enemy1), findViewById(R.id.enemy2), findViewById(R.id.enemy3)
        };

        player = findViewById(R.id.player);

        hearts = new ImageView[]{
                findViewById(R.id.heart1), findViewById(R.id.heart2), findViewById(R.id.heart3)
        };
        right = findViewById(R.id.rightBTN);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveRight(right);
            }
        });
        left = findViewById(R.id.leftBTN);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveLeft(left);
            }
        });

        kissSound= MediaPlayer.create(this,R.raw.kiss);

        resumeIMG = findViewById(R.id.resumeIMG);
        resumeIMG.setVisibility(View.INVISIBLE);
        pauseIMG = findViewById(R.id.pauseIMG);
        pauseIMG.setVisibility(View.VISIBLE);

        scoreLBL = findViewById(R.id.score);

        pause = findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(toResume == true)
                    resumeGame();
                else
                    pauseGame();
            }
        });
        start();
    }

    public void start(){
        WindowManager wm=getWindowManager();
        Display disp= wm.getDefaultDisplay();
        Point size=new Point();
        disp.getSize(size);
        screenHeight=size.y;
        scoreLBL.setText("SCORE: " + score);
        Start.ring.start();
        setUpAnimations();
    }

    public void setUpAnimations(){
        animations = new ValueAnimator[enemy.length];

        for (animationIndex = 0 ; animationIndex < enemy.length ; animationIndex++){
            SetAnimationParameters();
            animations[animationIndex].start();
            animations[animationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int position = animationIndex;
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                    int animatedValue =(int) updatedAnimation.getAnimatedValue();
                    if (toResume == true)
                        enemy[position].setTranslationY(enemy[position].getTranslationY());
                    else enemy[position].setTranslationY(animatedValue);
                        if(isCollision(enemy[position],player)) {
                            enemy[position].setY(-130);
                            checkHit();
                            updatedAnimation.start();
                        }
                        else updateScore(enemy[position],updatedAnimation);
                }
            });
            }
    }

    private boolean isCollision(ImageView enemyCol, ImageView playerCol) {
        int[] enemy_locate = new int[2];
        int[] player_locate = new int[2];

        enemyCol.getLocationOnScreen(enemy_locate);
        playerCol.getLocationOnScreen(player_locate);

        Rect rect1=new Rect(enemy_locate[0],enemy_locate[1],(int)(enemy_locate[0]+ enemyCol.getWidth()),(int)(enemy_locate[1]+enemyCol.getHeight()));
        Rect rect2=new Rect(player_locate[0],player_locate[1],(int)(player_locate[0]+ playerCol.getWidth()),(int)(player_locate[1]+playerCol.getHeight()));

        return Rect.intersects(rect1,rect2);
    }

    private void checkHit() {
        kissSound.start();
        if(heartsCount == 1) {
            hearts[heartsCount - 1].setVisibility(View.INVISIBLE);
            pauseGame();
            gameOver();
        }
        else {
            hearts[heartsCount - 1].setVisibility(View.INVISIBLE);
            heartsCount--;
            }
    }

    private void gameOver() {
        Start.scoresArr.add(score);
        Start.ring.pause();
        Intent gameOverIntent = new Intent(MainGame.this, GameOver.class);
        gameOverIntent.putExtra("score", score);
        startActivity(gameOverIntent);
    }

    private void SetAnimationParameters() {
        final int initialHeight = -200;
        animations[animationIndex] = ValueAnimator.ofInt(initialHeight,screenHeight +400);
        animations[animationIndex].setDuration(3000 + (long) (Math.random() * 7000 - 1));
        animations[animationIndex].setStartDelay((long) (Math.random() * 1));
        animations[animationIndex].setRepeatCount(Animation.INFINITE);
    }

    private void pauseGame(){
        resumeIMG.setVisibility(View.VISIBLE);
        pauseIMG.setVisibility(View.INVISIBLE);
        for (int i = 0; i < enemy.length; i++) {
            animations[i].pause();
        }
        toResume = true;
    }

    private void resumeGame(){
        resumeIMG.setVisibility(View.INVISIBLE);
        pauseIMG.setVisibility(View.VISIBLE);
        for (int i = 0; i < enemy.length; i++) {
            animations[i].resume();
        }
        toResume = false;
    }

    public void moveRight(View view) {
        if (player.getX() < (getResources().getDisplayMetrics().widthPixels * 2 / enemy.length))
            player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / enemy.length);
    }

    public void moveLeft(View view) {
        if (player.getX() >= (getResources().getDisplayMetrics().widthPixels * 1 / enemy.length))
            player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / enemy.length);
    }

    private synchronized void updateScore(View enemy,ValueAnimator updatedAnimation){
        if(enemy.getY()>player.getY()+player.getHeight()){
            score += 10;
            if(score<9999) {
                scoreLBL.setText("SCORE: " + score);
            }else{
                scoreLBL.setText("SCORE: " +score);
                scoreLBL.setTextSize(20);
            }
            updatedAnimation.start();
        }
    }
}
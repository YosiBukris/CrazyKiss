package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class MainGame extends AppCompatActivity implements SensorEventListener {
    public static String scoreName = "score";

    private ValueAnimator[] animations, moneyAnim;

    private ImageView player, resumeIMG, pauseIMG;
    private ImageView[] hearts, money, enemy;

    private Button right, left, pause;

    private boolean toResume = false;
    private MediaPlayer kissSound;

    private TextView scoreLBL;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    public static boolean sensorsOn;

    private int screenHeight, score = 0, heartsCount = 3, animationIndex, moneyAnimIndex;
    private float screenWidth, x;
    private final int enemyMoney = 10, walletMoney = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        enemy = new ImageView[]{
                findViewById(R.id.enemy1), findViewById(R.id.enemy2), findViewById(R.id.enemy3), findViewById(R.id.enemy4), findViewById(R.id.enemy5)
        };

        player = findViewById(R.id.player);

        hearts = new ImageView[]{
                findViewById(R.id.heart1), findViewById(R.id.heart2), findViewById(R.id.heart3)
        };

        money = new ImageView[]{
                findViewById(R.id.money1), findViewById(R.id.money2), findViewById(R.id.money3), findViewById(R.id.money4), findViewById(R.id.money5)};

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

        kissSound = MediaPlayer.create(this, R.raw.kiss);

        resumeIMG = findViewById(R.id.resumeIMG);
        resumeIMG.setVisibility(View.INVISIBLE);
        pauseIMG = findViewById(R.id.pauseIMG);
        pauseIMG.setVisibility(View.VISIBLE);

        scoreLBL = findViewById(R.id.score);

        pause = findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toResume == true)
                    resumeGame();
                else
                    pauseGame();
            }
        });
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        start();
    }

    private void turnOffAroows() {
        right.setVisibility(View.INVISIBLE);
        left.setVisibility(View.INVISIBLE);
    }

    public void start() {
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        if (Setting.sensorSwitch != null)
        {
            if(Setting.sensorSwitch.isChecked())
                sensorsOn =true;
            else
                sensorsOn =false;
        }
        if (sensorsOn)
            turnOffAroows();
        screenHeight=size.y;
        screenWidth= (float) (getResources().getDisplayMetrics().widthPixels * 2 / enemy.length);
        scoreLBL.setText("SCORE: " + score);
        Start.ring.start();
        setUpMoneyAnimations();
        setUpEnemyAnimations();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(sensorsOn) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                x -= event.values[0];
                if (x < 0) x = 0;
                else if (x > screenWidth) x = screenWidth;
                player.setX(x * 2);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setUpMoneyAnimations() {
        moneyAnim = new ValueAnimator[money.length];

        for (moneyAnimIndex = 0 ; moneyAnimIndex < money.length ; moneyAnimIndex++){
            SetMoneyAnimParameters();
            moneyAnim[moneyAnimIndex].start();
            moneyAnim[moneyAnimIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                private int position = moneyAnimIndex;
                @Override
                public void onAnimationUpdate(ValueAnimator moneyUpdatedAnimation) {
                    int animatedValue =(int) moneyUpdatedAnimation.getAnimatedValue();
                    if (toResume == true)
                        money[position].setTranslationY(money[position].getTranslationY());
                    else money[position].setTranslationY(animatedValue);
                    if(isCollision(money[position],player)) {
                        updateScore(money[position],moneyUpdatedAnimation,walletMoney);
                        money[position].setY(-1000);
                    }
                    //else moneyUpdatedAnimation.start();
                }
            });
        }
    }

    public void setUpEnemyAnimations(){
        animations = new ValueAnimator[enemy.length];

        for (animationIndex = 0 ; animationIndex < enemy.length ; animationIndex++){
            SetEnemyAnimParameters();
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
                    else updateScore(enemy[position],updatedAnimation,enemyMoney);
                }
            });
        }
    }

    private void SetMoneyAnimParameters() {
        final int initialHeight = -(4000 + (int) (Math.random() * 1000));
        moneyAnim[moneyAnimIndex] = ValueAnimator.ofInt(initialHeight ,screenHeight +400);
        moneyAnim[moneyAnimIndex].setDuration(7000 + (long) (Math.random() * 7000 - 1));
        moneyAnim[moneyAnimIndex].setStartDelay((long) (Math.random() * 1));
        moneyAnim[moneyAnimIndex].setRepeatCount(Animation.INFINITE);
    }

    private void SetEnemyAnimParameters() {
        final int initialHeight = -(500 + (int) (Math.random() * 1000));
        animations[animationIndex] = ValueAnimator.ofInt(initialHeight ,screenHeight +400);
        animations[animationIndex].setDuration(7000 + (long) (Math.random() * 7000 - 1));
        animations[animationIndex].setStartDelay((long) (Math.random() * 1));
        animations[animationIndex].setRepeatCount(Animation.INFINITE);
    }

    private boolean isCollision(ImageView objectCol, ImageView playerCol) {
        int[] object_locate = new int[2];
        int[] player_locate = new int[2];

        objectCol.getLocationOnScreen(object_locate);
        playerCol.getLocationOnScreen(player_locate);

        Rect rect1=new Rect(object_locate[0],object_locate[1],(int)(object_locate[0]+ objectCol.getWidth()),(int)(object_locate[1]+objectCol.getHeight()));
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
        Start.ring.pause();
        Intent gameOverIntent = new Intent(MainGame.this, GameOver.class);
        gameOverIntent.putExtra(scoreName, score);
        startActivity(gameOverIntent);
        finish();
    }

    private void pauseGame(){
        resumeIMG.setVisibility(View.VISIBLE);
        pauseIMG.setVisibility(View.INVISIBLE);
        for (int i = 0; i < enemy.length; i++) {
            animations[i].pause();
            moneyAnim[i].pause();
        }
        toResume = true;
    }

    private void resumeGame(){
        resumeIMG.setVisibility(View.INVISIBLE);
        pauseIMG.setVisibility(View.VISIBLE);
        for (int i = 0; i < enemy.length; i++) {
            animations[i].resume();
            moneyAnim[i].resume();
        }
        toResume = false;
    }

    public void moveRight(View view) {
        if (player.getX() < (getResources().getDisplayMetrics().widthPixels * 3.5 / enemy.length))
            player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / enemy.length);
    }

    public void moveLeft(View view) {
        if (player.getX() >= (getResources().getDisplayMetrics().widthPixels * 0.5 / enemy.length))
            player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / enemy.length);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private synchronized void updateScore(View object,ValueAnimator updatedAnimation, int value){
        if(object.getY()>player.getY()+player.getHeight()){
            score += value;
            if(score<9999)
                scoreLBL.setText("SCORE: " + score);
            else{
                scoreLBL.setText("SCORE: " +score);
                scoreLBL.setTextSize(20);
            }
            if (value == enemyMoney)
                updatedAnimation.start();
        }
    }

}
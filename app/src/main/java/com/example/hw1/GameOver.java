package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    Button newGame, mainMenu, exit;
    ImageView logo;
    Animation logoAnim;
    TextView scoreLBLGameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        getSupportActionBar().hide();
        logo = findViewById(R.id.kissLogo);
        newGame = findViewById(R.id.newGameBTN1);
        mainMenu = findViewById(R.id.mainMenuBTN1);
        exit = findViewById(R.id.exitBTN1);
        scoreLBLGameOver = findViewById(R.id.scoreLBL);
        Intent intent = getIntent();
        scoreLBLGameOver.setText("Your Score : " + intent.getIntExtra("score",0));
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

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu();
            }
        });
    }

    public void startNewGame(){
        Intent newGameIntent = new Intent(GameOver.this, MainGame.class);
        startActivity(newGameIntent);
    }

    public void goToMenu(){
        finish();
    }

}

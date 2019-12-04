package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class Score extends AppCompatActivity {
    Button back;
    TextView [] scoresValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        back = findViewById(R.id.backBTN);
        scoresValues = new TextView[]{
                findViewById(R.id.score_02),findViewById(R.id.score_03),findViewById(R.id.score_04),findViewById(R.id.score_05)};
        int sizeList = Start.scoresArr.size();
        for (int i = 0; i < sizeList && i < scoresValues.length; i++) {
            scoresValues[i].setText(Start.scoresArr.get(i) + "\n");
        }
        start();
    }

    private void start() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu();
            }
        });
    }

    public void goToMenu(){
        Intent menuIntent = new Intent(this, Start.class);
        startActivity(menuIntent);
    }
}

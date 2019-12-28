package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Score extends AppCompatActivity {
    private Button back;
    private final int namePlace = 0, scorePlace = 1, locationPlace = 2, picPlace = 3;
    private final String Json_Key = "ScoreHistory";
    private MySharedReference msr;
    private ScoresHistory history;
    private final int MaxScores = 5;
    private Button ScoreTable_BTN_back,allScore_BTN_tableRowScore1_1,allScore_BTN_tableRowScore2_1,
            allScore_BTN_tableRowScore3_1,allScore_BTN_tableRowScore4_1,allScore_BTN_tableRowScore5_1;
    private TextView[][] allScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        back = findViewById(R.id.backBTN2);
        msr = new MySharedReference(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMenu();
            }
        });

        allScore_BTN_tableRowScore1_1 = findViewById(R.id.allScore_BTN_tableRowScore1_1);
        allScore_BTN_tableRowScore1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapLocation(1);
            }
        });
        allScore_BTN_tableRowScore2_1 = findViewById(R.id.allScore_BTN_tableRowScore2_1);
        allScore_BTN_tableRowScore2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapLocation(2);
            }
        });
        allScore_BTN_tableRowScore3_1 = findViewById(R.id.allScore_BTN_tableRowScore3_1);
        allScore_BTN_tableRowScore3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapLocation(3);
            }
        });
        allScore_BTN_tableRowScore4_1 = findViewById(R.id.allScore_BTN_tableRowScore4_1);
        allScore_BTN_tableRowScore4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapLocation(4);
            }
        });
        allScore_BTN_tableRowScore5_1 = findViewById(R.id.allScore_BTN_tableRowScore5_1);
        allScore_BTN_tableRowScore5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapLocation(5);
            }
        });

        allScores=new TextView[][] {
                {findViewById(R.id.allScore_TBL_tableRowScore1),findViewById(R.id.allScore_TBL_tableRowScore1_1), findViewById(R.id.allScore_TBL_tableRowScore1_2),allScore_BTN_tableRowScore1_1},
                {findViewById(R.id.allScore_TBL_tableRowScore2),findViewById(R.id.allScore_TBL_tableRowScore2_1), findViewById(R.id.allScore_TBL_tableRowScore2_2),allScore_BTN_tableRowScore2_1},
                {findViewById(R.id.allScore_TBL_tableRowScore3),findViewById(R.id.allScore_TBL_tableRowScore3_1), findViewById(R.id.allScore_TBL_tableRowScore3_2),allScore_BTN_tableRowScore3_1},
                {findViewById(R.id.allScore_TBL_tableRowScore4),findViewById(R.id.allScore_TBL_tableRowScore4_1), findViewById(R.id.allScore_TBL_tableRowScore4_2),allScore_BTN_tableRowScore4_1},
                {findViewById(R.id.allScore_TBL_tableRowScore5),findViewById(R.id.allScore_TBL_tableRowScore5_1), findViewById(R.id.allScore_TBL_tableRowScore5_2),allScore_BTN_tableRowScore5_1}};

        start();
    }

    private void start() {
        fillTBL();
    }

    public void fillTBL(){
        String result = msr.getString(Json_Key,"NULL");
        history = new ScoresHistory(result);
        for(int i = 0 ; i < allScores.length ; i++){
            allScores[i][3].setVisibility(View.INVISIBLE);
        }
        if (result != "NULL")
        {
            Type type = new TypeToken<ArrayList<scoreClass>>(){}.getType();
            ArrayList<scoreClass> myObject = new Gson().fromJson(result,type);
            history.setAllScores(myObject);
            history.sortScores(myObject);
            for (int i = 0; i < myObject.size() && i < MaxScores; i++) {
                allScores[i][namePlace].setText(myObject.get(i).getName());
                allScores[i][scorePlace].setText(myObject.get(i).getScore() + "");
                allScores[i][locationPlace].setText(myObject.get(i).getLocation());
                allScores[i][picPlace].setVisibility(View.VISIBLE);
            }
        }
    }

    private void openMapLocation(int index) {
        String result = msr.getString(Json_Key, "NULL");
        Type typeMyType = new TypeToken<ArrayList<scoreClass>>() {
        }.getType();
        ArrayList<scoreClass> myObject = new Gson().fromJson(result, typeMyType);
        double[] x = myObject.get(index - 1).getVecLocation();
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("Values", x);
        finish();
        startActivity(intent);
    }

    public void goToMenu(){
        finish();
    }
}

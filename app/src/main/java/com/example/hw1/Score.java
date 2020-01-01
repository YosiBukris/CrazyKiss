package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Score extends AppCompatActivity implements OnMapReadyCallback {
    private Button back;
    private double x,y;
    private final int namePlace = 0, scorePlace = 1, locationPlace = 2, picPlace = 3;
    private final String Json_Key = "History2";
    private MySharedReference msr;
    private ScoresHistory history;
    private final int MaxScores = 5;
    private GoogleMap mMap;
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        double[] loc = myObject.get(index - 1).getVecLocation();
        x = loc[1];
        y = loc[0];
        onMapReady(mMap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(x, y);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13.5f));
    }

    public void goToMenu(){
        finish();
    }
}

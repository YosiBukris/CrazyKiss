package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.Settings;

import com.google.gson.Gson;

public class GameOver extends AppCompatActivity {
    private Button mainMenu, exit;
    private ImageView logo;
    private Animation logoAnim;
    private int thisScore;
    private MySharedReference msr;
    private final String Json_Key = "History2";
    private ScoresHistory history;
    private LocationManager locationManager;
    private LocationListener listener;
    private TextView scoreLBLGameOver;
    private double[] vecLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msr = new MySharedReference(this);
        setContentView(R.layout.activity_game_over);
        getSupportActionBar().hide();
        logo = findViewById(R.id.kissLogo);
        mainMenu = findViewById(R.id.mainMenuBTN1);
        exit = findViewById(R.id.exitBTN1);
        scoreLBLGameOver = findViewById(R.id.scoreLBL);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                vecLocation = new double[]{location.getLongitude(),location.getLatitude()};
                history = new ScoresHistory(msr.getString(Json_Key, "NULL"));
                history.addScore(new scoreClass(thisScore, Start.playerName,"",vecLocation));
                String json = new Gson().toJson(history.getAllScores());
                msr.putString(Json_Key, json);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();

        Intent intent = getIntent();
        thisScore = intent.getIntExtra(MainGame.scoreName, 0);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                System.exit(1);
            }
        });
        start();
    }

    private void start() {
        logoAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        logo.startAnimation(logoAnim);
        if (thisScore < 9999)
            scoreLBLGameOver.setText("Your Score : " + thisScore);
        else {
            scoreLBLGameOver.setText("Your Score : " + thisScore);
            scoreLBLGameOver.setTextSize(20);
        }
    }

    public void goToMenu() {
        Intent menuIntent = new Intent(GameOver.this, Start.class);
        startActivity(menuIntent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                goToMenu();
            }
        });
    }
}

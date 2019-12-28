package com.example.hw1;

import android.os.Build;

import com.google.gson.Gson;

import java.util.ArrayList;

public class ScoresHistory implements Comparable{
    private ArrayList<scoreClass> scoresArr;

    public ScoresHistory(String str){
        if(str.equals("NULL"))
            scoresArr = new ArrayList<scoreClass>();
        else
            scoresArr = new Gson().fromJson(str,ArrayList.class);
    }

    public void addScore (scoreClass score) { scoresArr.add(score);}

    public ArrayList<scoreClass> getAllScores(){return scoresArr;}

    public void sortScores(ArrayList<scoreClass> scoreList){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            scoreList.sort(null);
    }

    public void setAllScores(ArrayList<scoreClass> scoreList){
        this.scoresArr = scoreList;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}

package com.example.hw1;

import android.location.Location;

public class scoreClass implements Comparable{
    private int score;
    private String name;
    private String location;
    private double[] vecLocation;

    public scoreClass (int score, String name, String location, double[] vec){
        this.score = score;
        this.name = name;
        this.location = location;
        this.vecLocation = vec;
    }

    public double[] getVecLocation() {
        return vecLocation;
    }

    public void setVecLocation(double[] vecLocation) {
        this.vecLocation = vecLocation;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Object o) {
        scoreClass s = (scoreClass) o;
        if(this.score == s.score)
            return 0;
        else if (this.score > s.score)
            return -1;
        else
            return 1;
    }
}

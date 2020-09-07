package com.porterdustin.zombienuke;

import android.util.DisplayMetrics;
import android.app.Activity;

public class RoundInfo {
    Bounds bounds;
    Zombie[] zombieList;
    Human[] humanList;
    double scale;

    public RoundInfo (Activity activity, Zombie[] zombieList, Human[] humanList, double scale) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        bounds = new Bounds(displayMetrics.widthPixels, displayMetrics.heightPixels);
        this.zombieList = zombieList;
        this.humanList = humanList;
        this.scale = scale;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public Zombie[] getZombieList() {
        return zombieList;
    }

    public void setZombieList(Zombie[] zombieList) {
        this.zombieList = zombieList;
    }

    public Human[] getHumanList() {
        return humanList;
    }

    public void setHumanList(Human[] humanList) {
        this.humanList = humanList;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}

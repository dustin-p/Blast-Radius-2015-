package com.porterdustin.zombienuke;

import android.content.Context;
import android.view.ViewGroup;

public class Pistol extends RangedWeapon {

    public Pistol(Human parent, RoundInfo roundInfo) {
        setParent(parent);
        setRoundInfo(roundInfo);
        setImageId(R.drawable.pistol_animation);
        setFireRate(90);
        setAccuracy(0.75f);
        setxOffset(28);
        setyOffset(14);
        setRecoil(3);
    }

    public void shoot(double direction) {
        setSteps((int) (Math.random() * 500));
        createProjectile(new Bullet(getParent().getContext(), 36), direction);
    }
}
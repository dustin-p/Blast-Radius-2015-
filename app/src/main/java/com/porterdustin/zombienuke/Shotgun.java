package com.porterdustin.zombienuke;

public class Shotgun extends RangedWeapon {
    final int projectileCount = 4;

    public Shotgun(Human parent, RoundInfo roundInfo) {
        setParent(parent);
        setRoundInfo(roundInfo);
        setImageId(R.drawable.shotgun_animation);
        setFireRate(50);
        setAccuracy(0.80f);
        setxOffset(28);
        setyOffset(21);
        setRecoil(3);
    }

    public void shoot(double direction) {
        setSteps((int) (Math.random() * 300));
        for (int i = 0; i < projectileCount; i++){
            createProjectile(new Bullet(getParent().getContext(), 24 + Math.random()*32), direction);
        }
    }
}
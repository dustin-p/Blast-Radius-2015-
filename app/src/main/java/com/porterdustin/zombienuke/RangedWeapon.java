package com.porterdustin.zombienuke;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.ViewGroup;


public abstract class RangedWeapon extends Weapon{
    private static final int MILLIS_IN_SECOND = 1000;
    private static final int SECONDS_IN_MINUTE = 60;
    private int fireRate;
    private int xOffset;
    private int yOffset;
    private int recoil;
    private float accuracy;
    private Human parent;
    private RoundInfo roundInfo;
    private int steps = 0;

    public void step (double direction) {
        steps += 100;

        if (steps >= SECONDS_IN_MINUTE * MILLIS_IN_SECOND / fireRate) {
            steps = 0;
            shoot(direction);
        }
    }

    public void createProjectile (Projectile projectile, double direction) {
        Position projectileStart = new Position(parent.getPosition());
        double scale = roundInfo.getScale();
        parent.setImage((int) direction);
        parent.setImageResource(getImageId());
        ((AnimationDrawable) (parent.getDrawable())).start();

        if (direction > 90 && direction < 270) {
            projectileStart.setX(-xOffset * scale, true);
        } else {
            projectileStart.setX(xOffset * scale, true);
        }
        parent.getPosition().setX((direction > 90 && direction < 270? scale*recoil : -scale*recoil), true);
        projectileStart.setY(yOffset * scale, true);
        projectile.setDirection(direction + 180*(Math.random() - 0.5)*(1 - accuracy));
        projectile.setPosition(projectileStart);
        projectile.setBounds(roundInfo.getBounds());
        projectile.setScale(scale);
        projectile.setZombieList(roundInfo.getZombieList());

        ((ViewGroup) getParent().getParent()).addView(projectile);
    }

    abstract void shoot (double direction);

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public Human getParent() {
        return parent;
    }

    public void setParent(Human parent) {
        this.parent = parent;
    }

    public RangedWeapon() {
        setType(Type.RANGED);
    }

    public RoundInfo getRoundInfo() {
        return roundInfo;
    }

    public void setRoundInfo(RoundInfo roundInfo) {
        this.roundInfo = roundInfo;
    }

    public int getxOffset() {
        return xOffset;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getyOffset() {
        return yOffset;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getRecoil() {
        return recoil;
    }

    public void setRecoil(int recoil) {
        this.recoil = recoil;
    }
}

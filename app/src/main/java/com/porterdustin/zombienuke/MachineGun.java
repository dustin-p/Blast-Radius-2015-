package com.porterdustin.zombienuke;

public class MachineGun extends RangedWeapon {
    double lastDirection;

    public MachineGun(Human parent, RoundInfo roundInfo) {
        setParent(parent);
        setRoundInfo(roundInfo);
        setImageId(R.drawable.machinegun_animation);
        setFireRate(500);
        setAccuracy(0.67f);
        setxOffset(28);
        setyOffset(24);
        setRecoil(3);
        lastDirection = 0;
    }

    public void shoot(double direction) {
        if (direction > 90 && direction < 270 && !(lastDirection > 90 && lastDirection < 270)) {
            setSteps(-350);
        } else if ((direction <= 90 || direction >= 270) && !(lastDirection <= 90 || lastDirection >= 270)) {
            setSteps(-350);
        } else {
            createProjectile(new Bullet(getParent().getContext(), 24 + Math.random()*24), direction);
        }
        if (Math.random() < 0.2 && getAccuracy() < 0.6) {
            setSteps(-500 + (int) (Math.random() * 100));
        }
        lastDirection = direction;
    }
}
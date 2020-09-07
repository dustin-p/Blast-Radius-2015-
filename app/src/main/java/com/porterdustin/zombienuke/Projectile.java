package com.porterdustin.zombienuke;

import android.os.Handler;
import android.widget.ImageView;
import android.content.Context;
import android.view.ViewManager;

public abstract class Projectile extends ImageView {
    private Position position;
    private Bounds bounds;
    private Zombie[] zombieList;
    private double direction;
    private double speed;
    private double scale;
    private int size;

    public Projectile (Context context) {
        super(context);
        setX(-32);
        setY(-32);
        travel();
    }

    private void travel() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                position.move(speed, direction);
                if (bounds.outOfBounds(position)) {
                    destroy();
                } else {
                    if (!((GlobalVariables) getContext().getApplicationContext()).getGamePaused()) {
                        setX((float) (position.getX() - (size * scale / 2)));
                        setY((float) (position.getY() - (size * scale / 2)));
                        for (int k = 0; k < zombieList.length; k++) {
                            if (position.distanceTo(zombieList[k].getPosition()) <
                                            zombieList[k].getSize() * scale / 3) {
                                zombieList[k].hit(scale);
                                destroy();
                                return;
                            }
                        }
                    }
                    handler.postDelayed(this, 4);
                }
            }
        };
        handler.postDelayed(runnable, 1);
    }

    public void destroy() {
        ((ViewManager)this.getParent()).removeView(this);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

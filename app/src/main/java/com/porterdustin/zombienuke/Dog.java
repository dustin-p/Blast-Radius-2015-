package com.porterdustin.zombienuke;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class Dog extends ImageView{
	GlobalVariables global;
	Human owner;
	Zombie[] zombieList;
	int screenSize;
	int hPad;
	int vPad;
	float x;
	float y;
	float direction;
	double startSpeed;
	double speed;
	private double hSpeed;
	private double vSpeed;
	private int size;
	private int zombieCount;
	private int minDistance;
	private int distance1;
	private int distance2;
	private int biteTime;
	private Zombie zombieToAttack;
	
	
	public Dog(Context context, int screenWidth, int horzPadding, int vertPadding, final Human owner) {
		super(context);
		global = ((GlobalVariables) getContext().getApplicationContext());
		screenSize = screenWidth;
		hPad = horzPadding;
		vPad = vertPadding;
		direction = (float) (Math.random()*360);
		this.owner = owner;
		speed = 0;
		zombieList = global.getZombieList();
		minDistance = screenSize;
		setBackgroundResource(R.drawable.dog_animation);
		((AnimationDrawable) getBackground()).start();
		setScaleY(0.85f);
		x = hPad + screenSize/4 + (int)(Math.random()*screenSize/2);
		y = vPad + screenSize/4 + (int)(Math.random()*screenSize/2);
		setVisibility(GONE);
		
	}
	
	public void addInPlay(double speed) {
		startSpeed = speed + Math.random()/2;
		this.speed = startSpeed;
		setAlpha(0.55f);
		setVisibility(VISIBLE);
		global.setDogCount(global.getDogCount() + 1);
		zombieList = global.getZombieList();
		zombieToAttack = zombieList[(int)(global.getZombieCount()*Math.random())];
	}
	
	public void move(double scale) {
		setX((float) (x += hSpeed*scale) - size/2);
		setY((float) (y += vSpeed*scale) - size/2);
	}
	
	public void setDirection() {
		zombieCount = global.getZombieCount();
		
		while (global.getHumanCount() > 0 && owner.isDead()) {
			owner = global.getHumanList()[(int) (Math.random()*global.getHumanList().length)];
		}
		biteTime--;
		if (Math.random() < 0.1)
			minDistance = screenSize;
		if (biteTime == 0) {
			setBackgroundResource(R.drawable.dog_animation);
			((AnimationDrawable) getBackground()).start();
			biteTime = -1;
			speed = startSpeed;
		}
		if (biteTime != 1) {
			for (int k = 0; k < zombieCount; k++) {
				distance1 = zombieList[k].distanceFrom(x, y);
				distance2 = zombieList[k].distanceFrom(owner.getXval(), owner.getYval()) + distance1;
				if (distance1 < size/2) {
					bite(zombieList[k]);
					k = zombieCount - 1;
				} else if (distance2 < minDistance && !zombieList[k].outOfPlay()) {
					minDistance = distance2;
					zombieToAttack = zombieList[k];
				}
				
			}	
			direction = (float) directionTo((float)zombieToAttack.getXpos(), (float)zombieToAttack.getYpos());
		}
		
		while (direction > 360)
			direction -= 360;
		while (direction < 0)
			direction += 360;
		
		if (direction > 90 && direction < 270)
			setScaleX(-0.85f);
		else
			setScaleX(0.85f);
		
		hSpeed = (float) (speed*Math.cos(Math.toRadians(direction)));
		vSpeed = (float) (speed*Math.sin(Math.toRadians(direction)));
	}

	private void bite(Zombie zombieToKill) {
		speed = startSpeed/2;
		direction = (float) directionTo((float)zombieToKill.getXpos(), (float)zombieToKill.getYpos());
		zombieToKill.kill(size/96);	
		biteTime = 1;
		setBackgroundResource(R.drawable.bite_animation);
		((AnimationDrawable) getBackground()).start();
		minDistance = screenSize;
		
	}
	
	public double directionTo(float x2, float y2) {
		return Math.atan2(y2-y, x2-x) * (180 / Math.PI);
	}
	
	public void setXval(int x) {
		this.x = x;
	}
	
	public void setYval(int y) {
		this.y = y;
	}
	
	public double getXval() {
		return x;
	}
	
	public double getYval() {
		return y;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

}

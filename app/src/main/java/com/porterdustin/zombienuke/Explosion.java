package com.porterdustin.zombienuke;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.porterdustin.effects.SplatterEffect;


public class Explosion extends ImageView{
	Zombie[] zombieList;
	Human[] humanList;
	SplatterEffect[] splatterList;
	private double scale;
	private double gameScale;
	private int size;
	private int xVal;
	private int yVal;
	private float boostSpeed;
	private boolean fingerDown;
	private int sCycle = 0;
	private int maxZBar;
	private GlobalVariables global;
	private int rotate = 45;

	public Explosion(Context context) {
		super(context);
		setVisibility(GONE);
		global = ((GlobalVariables) getContext().getApplicationContext());
		setLayoutParams(new LayoutParams(2, 2));
	}
	
	public void update(double scaleMult, Zombie[] zombies, Human[] survivors, SplatterEffect[] guts) {
		scale = scaleMult;
		zombieList = zombies;
		humanList = survivors;
		splatterList = guts;
		size = 0;
		sCycle = 0;
		
		
		setImageResource(R.drawable.explosion);
		this.setLayoutParams(new RelativeLayout.LayoutParams(8, 8));
		setVisibility(GONE);
	}
	

	
	
	public void tapScreen(int x, int y, double scaleMult, float zombieBoost) {
		if (global.getExpWaitTime() < 0) {
			xVal = x;
			yVal = y;
			setX(x);
			setY(y);
			size = (int) (scale*112);
			setScaleX(size/4);
			setScaleY(size/4);
			boostSpeed = zombieBoost;
			
			gameScale = scaleMult;
			fingerDown = true;
			setVisibility(VISIBLE);
		}
	}

	public void releaseScreen() {
		if (size < scale*112)
			size = (int) (scale*112);
		if (fingerDown && size > 1)
			explode();
		fingerDown = false;
	}
	
	public void expand(float expandX, float expandY) {
		if (global.getExpWaitTime() < 1) {
			if (fingerDown && size <= global.getScreenWidth()/1.75) {
				int temp = (int) (Math.sqrt((expandX-getX())*(expandX-getX()) + (expandY-getY())*(expandY-getY()))*1.1);
				if (temp - size > scale*12)
					size += (temp - size)/4;
				else if (temp > scale*112)
					size = temp;
				setScaleX(size/4);
				setScaleY(size/4);
				
			} else if (fingerDown) {
				releaseScreen();
			}
			
			//setRotation((float)(Math.atan2(expandY-yVal, expandX-xVal) * (180 / Math.PI)));
			
		}
	}
	
	public void explode() {
		global.getBoomList()[global.getExpCycle()].create(size, xVal, yVal);
		global.getPaperRipList()[global.getExpCycle()].create(size, xVal, yVal);
		global.setExpCycle();
		global.setExpWaitTime(9);

		killCharacters(true);
		
		/*
		if (tempKills > 9 && tempDeaths == 0) {
			PowerUp extraLife = new PowerUp(game);
			extraLife.setInPlay(xVal, yVal, 0);
			((ViewGroup) game.findViewById(R.id.main)).addView(extraLife);
		}
		
		if (global.getPowerUpCount() > 0 && tempKills >= global.getPowerUpThresh() - global.getPowerUpCount()
				&& tempDeaths == 0) {
			global.setPowerUpCount(global.getPowerUpCount() - 1);
			PowerUp levelPowerUp = new PowerUp(game);
			levelPowerUp.setInPlay(xVal, yVal, 1);
			((ViewGroup) game.findViewById(R.id.main)).addView(levelPowerUp);
		}*/
		
		size = 0;
		setVisibility(GONE);
	}
	

	public void killCharacters(boolean killHumans) {
		for (int k = 0; k < humanList.length && killHumans; k++) {
			if (humanList[k].distanceFrom(xVal, yVal) < size*0.95
					&& !humanList[k].isDead() && !humanList[k].outOfBounds(gameScale)) {
				if (humanList[k].hasHelmet()) {
					humanList[k].setHelmet(false);
				} else if (!(humanList[k].hasBlastShield() && humanList[k].distanceFrom(xVal, yVal) > (size*0.667))){
					humanList[k].kill(gameScale, splatterList[sCycle]);
					sCycle++;
					if (sCycle == splatterList.length)
						sCycle = 0;
				}
			}
		}
		int zCount = global.getZombieCount();
		for (int k = 0; k < zCount; k++) {
			if(zombieList[k].distanceFrom(xVal, yVal) < size && !zombieList[k].outOfBounds()) {
				zombieList[k].changeSpeed(boostSpeed/2);
				if (zombieList[k].isCurable())
					zombieList[k].cure();
				else
					killZombie(k);
			}
		}
	}

	public void setScale(double scale) {
		this.scale = scale;
	}
	
	public int getZBarSize() {
		return maxZBar;
	}
	
	public void killZombie(int k){
		
		zombieList[k].changeSpeed(boostSpeed/2);
		zombieList[k].kill(gameScale);
	}
	
	public void paintZombie(int k, int color){
		zombieList[k].paint(color);
	}
	
	public int getSize() {
		return (int) (size/scale);
	}

}

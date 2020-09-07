package com.porterdustin.zombienuke;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

public class BearTrap extends View{

	private int x;
	private int y;
	private int size;
	private AnimationDrawable frameAnimation;
	private Zombie[] zombieList;
	private GlobalVariables global;
	private boolean canSnap;
	private boolean ready;
	private int hPad;
	private int vPad;
	private int identification;
	private int totalTraps;
	private double scale;

	public BearTrap(Context context, int hPad, int vPad) {
		super(context);
		
		global = ((GlobalVariables) getContext().getApplicationContext());
		this.hPad = hPad;
		this.vPad = vPad;
		setVisibility(GONE);
	}
	
	public void ready(int k, int total, boolean midGame) {
		int direction = (int) (360*(double)(k)/total);
		int distanceFromCenter = (int)(6*total + Math.random()*160 + (midGame? 96 : 40));
		identification = k;
		totalTraps = total;
		scale = global.getScale();
		x = (int) (hPad + global.getScreenWidth()/2 + (distanceFromCenter*scale*Math.cos(Math.toRadians(direction))));
		y = (int) (vPad + global.getScreenWidth()/2 + (distanceFromCenter*scale*Math.sin(Math.toRadians(direction))));
		size = (int) (scale*144);
		setLayoutParams(new RelativeLayout.LayoutParams(size, size));
		setX(x - size/2);
		setY(y - size/2);
		if (Math.random() < 0.5)
			setScaleX(-1);
		else
			setScaleX(1);
		setVisibility(VISIBLE);
		setBackgroundResource(R.drawable.beartrap_animation);
		canSnap = true;
		ready = true;
	}

	public void snap() {
		if (ready) {
			zombieList = global.getZombieList();
			if (zombieList != null && zombieList[0] != null)
			for (int k = 0; canSnap && k < global.getZombieCount() ; k++) {
				if (!isShown()) {
					setVisibility(GONE);
					ready = false;
				} else if (zombieList[k].distanceFrom(x,y) < size/4) {
					zombieList[k].setHealth(0);
					zombieList[k].kill((double)(size)/128);
					frameAnimation = (AnimationDrawable) getBackground();
					frameAnimation.start();
					canSnap = false;
					final Handler handler = new Handler();
			        Runnable runnable = new Runnable() {
						@Override
			        	   public void run() {
							if (!global.getGamePaused()) {
								setBackgroundResource(0);
								setBackgroundResource(R.drawable.beartrap_animation);
								canSnap = true;
							} else {
								handler.postDelayed(this, 270);
							}
						}
			        };
			        handler.postDelayed(runnable, 270);
				}
			}
		}
	}
	
	public void setSize(double scale) {
		size = (int) (scale*144);
		this.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
		setX(x - size/2);
		setY(y - size/2);
	}
	
	public int getXval() {
		return x;
	}
	public void setXval(int x) {
		this.x = x;
	}
	
	public int getYval() {
		return y;
	}
	
	public void setYval(int y) {
		this.y = y;
	}
	
	public boolean isReady() {
		return ready;
	}

	public int getIdentification() {
		return identification;
	}

	public void setIdentification(int identification) {
		this.identification = identification;
	}

	public int getTotalTraps() {
		return totalTraps;
	}

	public void setTotalTraps(int totalTraps) {
		this.totalTraps = totalTraps;
	}

}

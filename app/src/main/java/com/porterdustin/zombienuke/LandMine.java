package com.porterdustin.zombienuke;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

public class LandMine extends View{

	private int x;
	private int y;
	private int size;
	private Zombie[] zombieList;
	private GlobalVariables global;
	private boolean ready;
	private int hPad;
	private int vPad;
	private int identification;
	private int totalMines;
	private double scale;

	public LandMine(Context context, int hPad, int vPad) {
		super(context);
		
		global = ((GlobalVariables) getContext().getApplicationContext());
		this.hPad = hPad;
		this.vPad = vPad;
		setVisibility(GONE);
	}
	
	public void drop (int x, int y) {
		this.x = x;
		this.y = y;
		ready(0, 1, true);
	}
	
	public void ready(int k, int total, boolean midGame) {
		global.setLandMineCount(global.getLandMineCount() + 1);
		identification = k;
		totalMines = total;
		scale = global.getScale();
		if (!midGame) {
			int direction = (int) (360*(double)(k)/total);
			int distanceFromCenter = (int)(6*total + Math.random()*160 + 96);
			x = (int) (hPad + global.getScreenWidth()/2 + (distanceFromCenter*scale*Math.cos(Math.toRadians(direction))));
			y = (int) (vPad + global.getScreenWidth()/2 + (distanceFromCenter*scale*Math.sin(Math.toRadians(direction))));
		}
		
		
		setSize(scale);
		
		if (Math.random() < 0.5)
			setScaleX(-1);
		else
			setScaleX(1);
		setVisibility(VISIBLE);
		setBackgroundResource(R.drawable.landmine);
		ready = true;
		
		final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
			@Override
        	   public void run() {
				checkToBlowUp();
				if (ready)
					handler.postDelayed(this, 150);
			}
        };
        handler.postDelayed(runnable, 1);
	}

	public void checkToBlowUp() {
		if (ready) {
			zombieList = global.getZombieList();
			if (zombieList != null && zombieList[0] != null)
			for (int k = 0; k < global.getZombieCount() ; k++) {
				if (!isShown()) {
					setVisibility(GONE);
					ready = false;
				} else if (zombieList[k].distanceFrom(x,y) < size/1.55) {
					global.setLandMineCount(global.getLandMineCount() - 1);
					new Bomb(getContext(), scale, (int)(scale*308), (int)x, (int)y);
					setVisibility(GONE);
					ready = false;
				}
			}
		}
	}
	
	public void setSize(double scale) {
		size = (int) (scale*56);
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

	public int getTotalMines() {
		return totalMines;
	}

	public void setTotalTraps(int totalMines) {
		this.totalMines = totalMines;
	}

}

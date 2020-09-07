package com.porterdustin.zombienuke;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class Bomb {
	Zombie[] zombieList;
	private GlobalVariables global;

	public Bomb(Context context, double scale, int size, int xVal, int yVal) {
		global = ((GlobalVariables) context.getApplicationContext());
		global.getBoomList()[global.getExpCycle()].create(size/2, xVal, yVal);
		global.getPaperRipList()[global.getExpCycle()].create(size/2, xVal, yVal);
		global.setExpCycle();
		
		zombieList = global.getZombieList();
		
		for (int k = 0; k < global.getZombieCount(); k++) {
			if(zombieList[k].distanceFrom(xVal, yVal) < size/2 && !zombieList[k].outOfBounds()) {
				zombieList[k].kill(scale);
			}
		}
	}

}

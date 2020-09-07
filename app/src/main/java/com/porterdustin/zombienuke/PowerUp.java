package com.porterdustin.zombienuke;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PowerUp extends ImageView{
	private int teir;
	private int x;
	private int y;
	private int maxX;
	private int size;
	private int steps;
	private Human[] humanList;
	private Dog[] dogList;
	private BearTrap[] trapList;
	private LandMine[] mineList;
	private GlobalVariables global;
	private boolean destroyed;
	private Activity game;
	private int type;
	private int selection;
	private RoundInfo roundInfo;

	public PowerUp(Context context, RoundInfo roundInfo) {
		super(context);
		setVisibility(GONE);
		this.roundInfo = roundInfo;
		game = (Activity) context;
		global = ((GlobalVariables) getContext().getApplicationContext());
		setBackgroundResource(R.drawable.gift);
	}
	
	
	
	public void useGenerator(int[] list) {
		int humanCount = global.getHumanList().length;
		int credits = (1 + humanCount) * (list[0] == 2? 4 : list[0] == 3? 8 : 2);
		int startingCredits = credits;
		int powerUpCount = 1 + (list[2] == 0? 0 : 1) + (list[3] == 0? 0 : 1);
		
		humanList = global.getHumanList();
		
		//random power up
		if (list[1] == -1) {
			if (Math.random() < 0.11) {
				credits = 0;
				for (int k = 0; k < humanList.length; k++) {
					givePaintGun();
				}
			} else {
				//apparel
				if (Math.random() < 0.66) {
					credits -= spendCredits(7 + (int)(Math.random()*5), credits);
				}
				//weapon
				int temp = (int)(Math.random()*4) + list[0];
				for (int k = 0; k < humanList.length && credits > 0; k++) {
					if (Math.random() < 0.33)
						temp = (int)(Math.random()*7);
					credits -= spendCredits(temp, credits);
				}
				//equipment
				while (credits > 0) {
					credits -= spendCredits(12 + (int)(Math.random()*2.9), credits);
				}
			}
		}
		
		//first power up slot
		if (list[1] > 5 && list[1] < 12) {		//first power up apparel or cure
			credits -= spendCredits(list[1], credits);
		} else if (list[1] > 0) {		//first power up weapon or equipment
			if (list[1] < 6) {	//give weapon
				for (int k = 0; k < humanList.length && startingCredits - credits < startingCredits/powerUpCount; k++) {
					credits -= spendCredits(list[1], credits);
				}
			} else while (startingCredits - credits < startingCredits/powerUpCount) {	//give equipment
				credits -= spendCredits(list[1], credits);
			}
		}
		
		//second power up slot
		startingCredits = credits;
		if (list[2] > 0) {
			if (list[2] > 5 && list[2] < 12) {		//second power up apparel or cure
				credits -= spendCredits(list[2], credits);
			} else if (list[2] < 6) {	//give weapon
				for (int k = 0; k < humanList.length && credits > (powerUpCount == 2? 0 : startingCredits/2); k++) {
					credits -= spendCredits(list[2], credits);
				}
			} else while (credits > (powerUpCount == 2? 1 : startingCredits/2)) {	//give equipment
				credits -= spendCredits(list[2], credits);
			}
		}
		
		//third power up slot
		startingCredits = credits;
		if (list[3] > 0) {
			if (list[3] > 5 && list[3] < 12) {		//third power up apparel or cure
				credits -= spendCredits(list[2], credits);
			} else if (list[3] < 6) {	//give weapon
				for (int k = 0; k < humanList.length && credits > 0; k++) {
					credits -= spendCredits(list[3], credits);
				}
			} else while (credits > 0) {	//give equipment
				credits -= spendCredits(list[3], credits);
			}
		}
	}
	
	
	
	
	public int spendCredits(int powerUpNumber, int credits) {
		switch (powerUpNumber) {
			case 1:
				if (credits > 2)
					givePistol();
				return 4;
			case 2:
				if (credits > 2)
					giveAxe();
				return 3;
			case 3:
				if (credits >= 6)
					giveShotgun();
				return 8;
			case 4:
				if (credits >= 8)
					giveMachineGun();
				return 10;
			case 5:

				return 0;
			case 6:
				if (credits >= 2.5*global.getHumanList().length) {
					giveCure();
					return (int) (2.5*global.getHumanList().length);
				}
				return 0;
			case 7:
				if (credits >= 2*global.getHumanList().length) {
					giveHelmets();
					return 2*global.getHumanList().length;
				}
				return 0;
			case 8:
				if (credits >= global.getHumanList().length) {
					giveExpBelts();
					return (int)(0.8*global.getHumanList().length);
				}
				return 0;
			case 9:
				if (credits >= 2.75*global.getHumanList().length) {
					giveBlastShields();
					return (int)(2.75*global.getHumanList().length);
				}
				return 0;
			case 10:
				giveSpeed();
				return global.getHumanList().length/2;
			case 11:
				if (credits >= 1.5*global.getHumanList().length) {
					giveCamo();
					return (int)(1.5*global.getHumanList().length);
				}
			case 12:
				giveBearTraps(3);
				return 2;
			case 13:
				if (credits > 2)
					giveDogs(1);
				return 5;
			case 14:
				giveLandMines(3);
				return 5;
		}
		return 0;
	}
	

	private void giveCure() {
		global.setCurable(true);
	}



	public void giveBearTraps(int count) {
		trapList = global.getTrapList();
		count += trapList[0].getTotalTraps();
		if (count > trapList.length)
			count = trapList.length;
		for (int k = 0; k < count; k++) {
			trapList[k].ready(k, count, true);
		}
	}
	
	public void giveLandMines(int count) {
		
		mineList = global.getMineList();
		count += mineList[0].getTotalMines();
		global.setMaxLandMines(count);
		if (count > mineList.length)
			count = mineList.length;
		for (int k = 0; k < count; k++) {
			mineList[k].ready(k, count, false);
		}
	}
	
	public void giveDogs(int count) {
		dogList = global.getDogList();
		while(global.getDogCount() < dogList.length && count > 0) {
			dogList[global.getDogCount()].addInPlay(3 + Math.random());
			count--;
		}
	}
	
	public void giveHelmets() {
		for (int k = 0; k < humanList.length; k++) {
			humanList[k].setHelmet(true);
		}
	}

	public void giveExpBelts() {
		for (int k = 0; k < humanList.length; k++) {
			humanList[k].giveExpBelt();
		}
	}
	
	public void giveBlastShields() {
		global.getExplosion().setImageResource(R.drawable.explosion2);
		for (int k = 0; k < humanList.length; k++) {
			humanList[k].giveBlastShield();
		}
	}
	
	public void giveCamo() {
		for (int k = 0; k < global.getZombieList().length; k++) {
			global.getZombieList()[k].setSmart(false);
			global.getZombieList()[k].setSmartChance(0);
		}
		for (int k = 0; k < humanList.length; k++) {
			humanList[k].giveCamo();
		}
	}
	
	public void giveSpeed() {
		for (int k = 0; k < humanList.length; k++) {
			humanList[k].giveSpeed();
		}
	}
	
	public void givePaintGun() {
		global.setHasGuns(true);
		int i = (int) (Math.random()*humanList.length);
		while (humanList[i].isDead())
			i = (int) (Math.random()*humanList.length);
		humanList[i].giveWeapon(new Pistol(humanList[i], roundInfo));
	}
	
	
	public void givePistol() {
		boolean gunGiven = false;
		global.setHasGuns(true);
		
		for (int k = 0; k < humanList.length && !gunGiven; k++) {
			if (!humanList[k].hasWeapon()) {
				humanList[k].giveWeapon(new Pistol(humanList[k], roundInfo));
				gunGiven = true;
			}
		}
	}
	
	public void giveShotgun() {
		boolean gunGiven = false;
		global.setHasGuns(true);
		
		for (int k = 0; k < humanList.length && !gunGiven; k++) {
			if (!humanList[k].hasWeapon()) {
				humanList[k].giveWeapon(new MachineGun(humanList[k], roundInfo));
				gunGiven = true;
			}
		} if (!gunGiven) {
			int i = (int) (Math.random()*humanList.length);
			while (humanList[i].isDead())
				i = (int) (Math.random()*humanList.length);
			humanList[i].giveWeapon(new MachineGun(humanList[i], roundInfo));
		}
	}
	
	public void giveMachineGun() {
		boolean gunGiven = false;
		global.setHasGuns(true);
		
		for (int k = 0; k < humanList.length && !gunGiven; k++) {
			if (!humanList[k].hasWeapon()) {
				humanList[k].giveWeapon(new MachineGun(humanList[k], roundInfo));;
				gunGiven = true;
			}
		} if (!gunGiven) {
			int i = (int) (Math.random()*humanList.length);
			while (humanList[i].isDead())
				i = (int) (Math.random()*humanList.length);
			humanList[i].giveWeapon(new MachineGun(humanList[i], roundInfo));
		}
	}
	
	public void giveAxe() {
		boolean axeGiven = false;
		for (int k = 0; k < humanList.length && !axeGiven; k++) {
			if (!humanList[k].hasWeapon()) {
				humanList[k].giveWeapon(new Axe());
				axeGiven = true;
			}
		}
	}
	
	public void giveLife() {
		setVisibility(VISIBLE);
		setBackgroundResource(R.drawable.extralife_animation);
		((AnimationDrawable)getBackground()).start();
		//global.setLives(global.getLives() + 1);
		//((TextView)game.findViewById(R.id.lives)).setText("Lives: " + String.valueOf(global.getLives()));
	}
	
	

	
	public void giveGift() {
		humanList = global.getHumanList();
		if (humansLeft() && !global.isGameLost() && !global.isLevelComplete()) {
			if (teir != 0) {
				//giveTeir1(1, 2);
				//giveLife();
			} else {
				//give weapons
				if (Math.random() < 0.5) {
					if (!anyWeapons() && Math.random() < 0.75) {
						for (int k = 0; k < humanList.length; k++) {
							givePistol();
						}
					} else {
						if (Math.random() < 0.2)
							giveMachineGun();
						else if (Math.random() < 0.4)
							giveShotgun();
						else
							giveAxe();
						if (Math.random() < 0.05)
							givePaintGun();
					}
				//give equipment
				} else {
					if (Math.random() < 0.5)
						giveHelmets();
					else
						giveBearTraps(4 + (int)(Math.random()*4));
				}
			}
		}
	}
	
	public void destroy() {
		if (!destroyed) {
			game = null;
			global = null;
			humanList = null;
			trapList = null;
			destroyed = true;
			((ViewManager)this.getParent()).removeView(this);
		}
	}
	
	public boolean humansLeft() {
		if (humanList == null)
			return false;
		for (int k = 0; k < humanList.length; k ++)
			if (!humanList[k].isDead())
				return true;
		return false;
	}
	
	public boolean anyWeapons() {
		if (humanList == null)
			return false;
		for (int k = 0; k < humanList.length; k ++)
			if (!humanList[k].isDead() && humanList[k].hasWeapon())
				return true;
		return false;
	}
	
}

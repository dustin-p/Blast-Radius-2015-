package com.porterdustin.zombienuke;

import android.graphics.Color;

public class Level {
	private int humanCount;
	private int zombieCount;
	private int startingZombies;
	private int zombiesToKill;
	private int zoomType;
	private int levelType;
	private int powerUpTeir;
	private int powerUpCount;
	private int powerUpThresh;
	private int background;
	private int backgroundColor;
	private float zombieSpeed;
	private float smartChance;
	private float fastChance;
	private float strongChance;
	private float invisibleChance;
	private float newZombieChance;
	private float zombieBoostSpeed;
	
	
	
	public Level(int levelNumber) {
		
		//background
		generateBackground((int)((7166.333*levelNumber/7.0) % 95) % 9, levelNumber);
		
		//human count
		setHumanCount(3);
		if (levelNumber % 3 == 0)
			setHumanCount(4);
		if (levelNumber % 3 == 1)
			setHumanCount(5);
		if ((3 + levelNumber) % 9 == 0)
			setHumanCount(1);
		if ((4 + levelNumber) % 13 == 0)
			setHumanCount(9);
		
		//power up
		if (humanCount <= 5) {
			switch (generateRandom(levelNumber, 4) % 4) {
				case 0: setPowerUpTeir(0);
						if (levelNumber < 50 || levelNumber % 5 == 0)
							break;
				case 2: if (levelNumber > 2)
							setPowerUpTeir(1);
						if (levelNumber < 20 || levelNumber % 3 == 0)
							break;
				case 1: if (levelNumber > 5)
							setPowerUpTeir(2);
						else if (levelNumber > 3)
							setPowerUpTeir(1);
						if (levelNumber < 30 || levelNumber % 2 == 0)
							break;
				case 3 : if (levelNumber > 14)
						setPowerUpTeir(2);
						
			}
		}
		else
			setPowerUpTeir(0);
		
		
		//zoom type
		setZoomType(2);
		if ((levelNumber - 3) % 6 == 0)
			setZoomType(1);
		if ((levelNumber - 2) % 9 == 0)
			setZoomType(3);
		if ((levelNumber + 2) % 7 == 0)
			setZoomType(0);
		if ((levelNumber + 1) % 5 == 0 && humanCount <= 3)
			setZoomType(3);
		if (humanCount > 6) {
			setLevelType(1);
			if (levelNumber % 2 == 0)
				setZoomType(0);
			else
				setZoomType(1);
		}
		if (humanCount == 1) {
			if (levelNumber % 2 == 0)
				setZoomType(3);
			else
				setZoomType(4);
		}
		
		
		
		//zombies
		setZombieCount(40 - 2*humanCount);
		setZombiesToKill(150);
		
		if (powerUpTeir == 1)
			zombiesToKill *= 1 + (humanCount/16.0);
		
		if (powerUpTeir == 2)
			zombiesToKill *= 1.75 + (humanCount/12.0);
		
		
		setStartingZombies(3);
		setZombieSpeed(1.5f);
		setSmartChance(0.8f);
		setFastChance(0.1f);
		setStrongChance(0.1f);
		setInvisibleChance(0.05f);
		setNewZombieChance(0.3f);
		setZombieBoostSpeed(0.02f);
		
		
		if (764 % levelNumber < (801 % levelNumber)/levelNumber) {
			setStartingZombies(humanCount);
			zombieSpeed *= 1.5;
			smartChance = 1;
			fastChance = 0.5f;
			strongChance = 0.5f;
			invisibleChance = 0;
			newZombieChance = 0;
			zombieBoostSpeed *= 2;
			zombiesToKill *= 0.33;
		}
		
		if (levelType == 1) {
			zombieSpeed *= 0.88f;
			zombiesToKill *= 0.5f;
		}
		
		if (zoomType == 0)
			zombieSpeed *= 1.5;
		else if (zoomType == 1)
			zombieSpeed *= 1.3;
		else if (zoomType == 2)
			zombieSpeed *= 1;
		else if (zoomType == 3)
			zombieSpeed *= 0.8;
		else if (zoomType == 4)
			zombieSpeed *= 0.6;
		
		
		
	}


	
	public void generateBackground(int backgroundNumber, int levelNumber) {
		switch(backgroundNumber) {
			case 0:	
				setBackground(R.drawable.background0);	//paper
				setBackgroundColor(Color.WHITE);
				break;
			case 1:
				setBackground(R.drawable.background3);	//desert
				if (levelNumber % 3 == 0)
					setBackgroundColor(Color.HSVToColor( new float[]{ 8, 0.1f, 0.5f} ));
				else if (levelNumber % 3 == 1)
					setBackgroundColor(Color.HSVToColor( new float[]{ 270, 0.1f, 0.4f} ));
				else
					setBackgroundColor(Color.HSVToColor( new float[]{ 180, 0.2f, 0.75f} ));
				break;
			case 2:
				setBackground(R.drawable.background1);	//grass
				if (levelNumber % 5 == 0) 
					setBackgroundColor(Color.HSVToColor( new float[]{ 162, 0.18f, 0.35f} ));
				else if (levelNumber % 5 == 1) 
					setBackgroundColor(Color.HSVToColor( new float[]{ 60, 0.3f, 0.9f} ));
				else if (levelNumber % 5 == 2) 
					setBackgroundColor(Color.HSVToColor( new float[]{ 32, 0.2f, 0.5f} ));
				else if (levelNumber % 5 == 3) 
					setBackgroundColor(Color.HSVToColor( new float[]{ 28, 0.5f, 0.7f} ));
				else
					setBackgroundColor(Color.HSVToColor( new float[]{ 270, 0.1f, 0.35f} ));
				break;
			case 3:
				setBackground(R.drawable.background2);	//cross roads
				if (levelNumber % 4 == 0) 
					setBackgroundColor(Color.HSVToColor( new float[]{ 180, 0.18f, 0.4f} ));
				else if (levelNumber % 4 == 1) 
					setBackgroundColor(Color.HSVToColor( new float[]{ 60, 0.3f, 0.9f} ));
				else if (levelNumber % 4 == 2) 
					setBackgroundColor(Color.HSVToColor( new float[]{ 90, 0.2f, 0.9f} ));
				else
					setBackgroundColor(Color.HSVToColor( new float[]{ 240, 0.15f, 0.4f} ));
				break;
			case 4:
				setBackground(R.drawable.background4);	//baseball field
				if (levelNumber % 3 == 0)
					setBackgroundColor(Color.HSVToColor( new float[]{ 96, 0.2f, 0.4f} ));
				else if (levelNumber % 3 == 1)
					setBackgroundColor(Color.HSVToColor( new float[]{ 60, 0.25f, 0.8f} ));
				else
					setBackgroundColor(Color.HSVToColor( new float[]{ 150, 0.18f, 0.35f} ));
				break;
				
			case 5:
				setBackground(R.drawable.background5);	//forest
				if (levelNumber % 5 == 0 || levelNumber % 5 == 1)
					setBackgroundColor(Color.HSVToColor( new float[]{ 96, 0.18f, 0.5f} ));
				else if (levelNumber % 5 == 2)
					setBackgroundColor(Color.HSVToColor( new float[]{ 0, 0.2f, 0.5f} ));
				else if (levelNumber % 5 == 3)
					setBackgroundColor(Color.HSVToColor( new float[]{ 180, 0.18f, 0.35f} ));
				else
					setBackgroundColor(Color.HSVToColor( new float[]{ 60, 0.25f, 0.8f} ));
				break;
			case 6:
				setBackground(R.drawable.background6);	//bear skin rug
				if (levelNumber % 3 == 0)
					setBackgroundColor(Color.HSVToColor( new float[]{ 20, 0.18f, 0.5f} ));
				else if (levelNumber % 3 == 1)
					setBackgroundColor(Color.HSVToColor( new float[]{ 0, 0.18f, 0.5f} ));
				else
					setBackgroundColor(Color.HSVToColor( new float[]{ 40, 0.18f, 0.5f} ));
				break;
			case 7:
				setBackground(R.drawable.background7);	//moon
				if (levelNumber % 6 == 2)
					setBackgroundColor(Color.HSVToColor( new float[]{ 200, 0.10f, 0.4f} ));
				else
					setBackgroundColor(Color.HSVToColor( new float[]{ 188, 0.20f, 1f} ));
				
				break;
			case 8:
				setBackground(R.drawable.background0);	//unfinished
				setBackgroundColor(Color.HSVToColor( new float[]{ 180, 0.18f, 0.4f} ));
				break;
		}
	}
	
	
	public int generateRandom (int value, int range){
		int returnVal = (value * 214013 + 2531011) % range;
		return returnVal;
	}

	public int getHumanCount() {
		return humanCount;
	}

	public void setHumanCount(int humanCount) {
		this.humanCount = humanCount;
	}



	public int getZombieCount() {
		return zombieCount;
	}



	public void setZombieCount(int zombieCount) {
		this.zombieCount = zombieCount;
	}



	public int getZombiesToKill() {
		return zombiesToKill;
	}



	public void setZombiesToKill(int zombiesToKill) {
		this.zombiesToKill = zombiesToKill;
	}



	public int getZoomType() {
		return zoomType;
	}



	public void setZoomType(int zoomType) {
		this.zoomType = zoomType;
	}



	public float getSmartChance() {
		return smartChance;
	}



	public void setSmartChance(float smartChance) {
		this.smartChance = smartChance;
	}



	public float getFastChance() {
		return fastChance;
	}



	public void setFastChance(float fastChance) {
		this.fastChance = fastChance;
	}



	public float getZombieSpeed() {
		return zombieSpeed;
	}



	public void setZombieSpeed(float zombieSpeed) {
		this.zombieSpeed = zombieSpeed;
	}



	public int getStartingZombies() {
		return startingZombies;
	}



	public void setStartingZombies(int startingZombies) {
		this.startingZombies = startingZombies;
	}



	public float getNewZombieChance() {
		return newZombieChance;
	}



	public void setNewZombieChance(float newZombieChance) {
		this.newZombieChance = newZombieChance;
	}



	public float getZombieBoostSpeed() {
		return zombieBoostSpeed;
	}



	public void setZombieBoostSpeed(float zombieBoostSpeed) {
		this.zombieBoostSpeed = zombieBoostSpeed;
	}



	public int getLevelType() {
		return levelType;
	}



	public void setLevelType(int levelType) {
		this.levelType = levelType;
	}



	public float getStrongChance() {
		return strongChance;
	}



	public void setStrongChance(float strongChance) {
		this.strongChance = strongChance;
	}



	public float getInvisibleChance() {
		return invisibleChance;
	}



	public void setInvisibleChance(float invisibleChance) {
		this.invisibleChance = invisibleChance;
	}



	public int getPowerUpCount() {
		return powerUpCount;
	}



	public void setPowerUpCount(int powerUpCount) {
		this.powerUpCount = powerUpCount;
	}



	public int getPowerUpThresh() {
		return powerUpThresh;
	}



	public void setPowerUpThresh(int powerUpThresh) {
		this.powerUpThresh = powerUpThresh;
	}



	public int getPowerUpTeir() {
		return powerUpTeir;
	}



	public void setPowerUpTeir(int powerUpTeir) {
		this.powerUpTeir = powerUpTeir;
	}



	public int getBackground() {
		return background;
	}



	public void setBackground(int background) {
		this.background = background;
	}



	public int getBackgroundColor() {
		return backgroundColor;
	}



	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}

package com.porterdustin.zombienuke;

import android.app.Application;
import android.content.SharedPreferences;

import com.porterdustin.effects.BoomEffect;
import com.porterdustin.effects.InfectEffect;
import com.porterdustin.effects.PaperRipEffect;
import com.porterdustin.effects.ZombiePopEffect;

public class GlobalVariables extends Application {
	private Human[] humanList;
	private Zombie[] zombieList;
	private BearTrap[] trapList;
	private LandMine[] mineList;
	private Dog[] dogList;
	private BoomEffect[] boomList;
	private PaperRipEffect[] paperRipList;
	private ZombiePopEffect[] zombiePopList;
	private InfectEffect[] infectFxList;
	private Level level;
	private Explosion explosion;
    private boolean gamePaused;
    private boolean gameLost;
    private boolean levelComplete;
    private boolean readyForGift;
    private boolean hasGuns;
    private boolean hasGift;
    private boolean curable;
    private boolean reset;
    private double scale;
    private int bgColor;
    private int zombieCount;
    private int dogCount;
    private int humanCount;
    private int infections;
    private int kills;
    private int killsNeeded;
    private int deaths;
    private int giftX;
    private int giftY;
    private int giftTier;
    private int powerUpCount;
    private int powerUpThresh;
    private int maxLandMines;
    private int landMineCount;
    private int currentGift;
    private int screenWidth;
    private int horzPadding;
    private int vertPadding;
    private int expWaitTime;
    private int popCycle;
    private int infectCycle;
    private int expCycle;
	//do not reset
    //private int highestLevel;
    private int lives;
    
    public void reset() {
    	humanList = null;
    	zombieList = null;
    	explosion = null;
    	trapList = null;
    	mineList = null;
    	boomList = null;
    	paperRipList = null;
    	zombiePopList = null;
    	setInfectFxList(null);
    	level = null;
    	gamePaused = false;
    	gameLost = false;
    	levelComplete = false;
    	readyForGift = false;
    	hasGuns = false;
    	hasGift = false;
    	curable = false;
    	scale = 0;
    	bgColor = 0;
    	zombieCount = 0;
    	humanCount = 0;
    	infections = 0;
    	kills = 0;
    	killsNeeded = 0;
    	deaths = 0;
    	giftX = 0;
    	giftY = 0;
    	giftTier = 0;
    	powerUpCount = 0;
    	powerUpThresh = 0;
    	maxLandMines = 0;
    	landMineCount = 0;
    	currentGift = 0;
    	screenWidth = 0;
    	horzPadding = 0;
    	vertPadding = 0;
    	expWaitTime = 0;
    	popCycle = 0;
    	infectCycle = 0;
    	expCycle = 0;
    	reset = true;
    }	
    
    public boolean getGamePaused() {
        return gamePaused;
    }

    public void setGamePaused(Boolean gamePaused) {
        this.gamePaused = gamePaused;
    }

	public boolean isReadyForGift() {
		return readyForGift;
	}

	public void setReadyForGift(boolean readyForGift) {
		this.readyForGift = readyForGift;
	}

	public int getCurrentGift() {
		return currentGift;
	}

	public void setCurrentGift(int currentGift) {
		this.currentGift = currentGift;
	}

	public Human[] getHumanList() {
		return humanList;
	}

	public void setHumanList(Human[] humanList) {
		this.humanList = humanList;
	}

	public Zombie[] getZombieList() {
		return zombieList;
	}

	public void setZombieList(Zombie[] zombieList) {
		this.zombieList = zombieList;
	}

	public boolean hasGuns() {
		return hasGuns;
	}

	public void setHasGuns(boolean hasGuns) {
		this.hasGuns = hasGuns;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public void setLevel(Level thisLevel) {
		level = thisLevel;
	}
	
	public Level getLevel() {
		return level;
	}

	public boolean getGift() {
		return hasGift;
	}

	public void giveGift(boolean hasGift, int x, int y, int hitCount) {
		this.hasGift = hasGift;
		setGiftX(x);
		setGiftY(y);
		setGiftTier(hitCount);
	}

	public int getGiftY() {
		return giftY;
	}

	public void setGiftY(int giftY) {
		this.giftY = giftY;
	}

	public int getGiftX() {
		return giftX;
	}

	public void setGiftX(int giftX) {
		this.giftX = giftX;
	}

	public int getZombieCount() {
		return zombieCount;
	}

	public void setZombieCount(int zombieCount) {
		this.zombieCount = zombieCount;
	}

	public ZombiePopEffect[] getZombiePopList() {
		return zombiePopList;
	}

	public void setZombiePopList(ZombiePopEffect[] zombiePopList) {
		this.zombiePopList = zombiePopList;
	}

	public int getPopCycle() {
		return popCycle;
	}
	
	public int getInfectCycle() {
		return infectCycle;
	}
	
	public int getExpCycle() {
		return expCycle;
	}

	public void setPopCycle() {
		if (popCycle < zombiePopList.length - 1)
			popCycle++;
		else
			popCycle = 0;
	}
	
	public void resetPopCycle() {
		popCycle = 0;
	}
	
	public void setInfectCycle() {
		if (infectCycle < infectFxList.length - 1)
			infectCycle++;
		else
			infectCycle = 0;
	}

	public void resetInfectCylce() {
		infectCycle = 0;
	}
	
	public void setExpCycle() {
		if (expCycle < boomList.length - 1)
			expCycle++;
		else
			expCycle = 0;
	}
	
	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public BearTrap[] getTrapList() {
		return trapList;
	}

	public void setTrapList(BearTrap[] trapList) {
		this.trapList = trapList;
	}

	public int getGiftTier() {
		return giftTier;
	}

	public void setGiftTier(int giftTier) {
		this.giftTier = giftTier;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
		
        SharedPreferences.Editor editor = getSharedPreferences("progress", 0).edit();
		editor.putInt("lives", lives);
		editor.commit();
	}

	public boolean isGameLost() {
		return gameLost;
	}

	public void setGameLost(boolean gameLost) {
		this.gameLost = gameLost;
	}

	public boolean isLevelComplete() {
		return levelComplete;
	}

	public void setLevelComplete(boolean levelComplete) {
		this.levelComplete = levelComplete;
	}

	public int getHumanCount() {
		return humanCount;
	}

	public void setHumanCount(int humanCount) {
		this.humanCount = humanCount;
	}

	public int getInfections() {
		return infections;
	}

	public void setInfections(int infections) {
		this.infections = infections;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public int getKillsNeeded() {
		return killsNeeded;
	}

	public void setKillsNeeded(int killsNeeded) {
		this.killsNeeded = killsNeeded;
	}

	public BoomEffect[] getBoomList() {
		return boomList;
	}

	public void setBoomList(BoomEffect[] boomList) {
		this.boomList = boomList;
	}
	
	public PaperRipEffect[] getPaperRipList() {
		return paperRipList;
	}

	public void setPaperRipList(PaperRipEffect[] paperRipList) {
		this.paperRipList = paperRipList;
	}

	public int getHorzPadding() {
		return horzPadding;
	}

	public void setHorzPadding(int horzPadding) {
		this.horzPadding = horzPadding;
	}

	public int getVertPadding() {
		return vertPadding;
	}

	public void setVertPadding(int vertPadding) {
		this.vertPadding = vertPadding;
	}

	public int getExpWaitTime() {
		return expWaitTime;
	}

	public void setExpWaitTime(int expWaitTime) {
		this.expWaitTime = expWaitTime;
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

	public int getBgColor() {
		return bgColor;
	}

	public void setBgColor(int bgColor) {
		this.bgColor = bgColor;
	}

	public Explosion getExplosion() {
		return explosion;
	}

	public void setExplosion(Explosion explosion) {
		this.explosion = explosion;
	}

	public InfectEffect[] getInfectFxList() {
		return infectFxList;
	}

	public void setInfectFxList(InfectEffect[] infectFxList) {
		this.infectFxList = infectFxList;
	}

	public Dog[] getDogList() {
		return dogList;
	}

	public void setDogList(Dog[] dogList) {
		this.dogList = dogList;
	}

	public int getDogCount() {
		return dogCount;
	}

	public void setDogCount(int dogCount) {
		this.dogCount = dogCount;
	}

	public boolean isReset() {
		return reset;
	}

	public void setReset(boolean reset) {
		this.reset = reset;
	}

	public LandMine[] getMineList() {
		return mineList;
	}

	public void setMineList(LandMine[] mineList) {
		this.mineList = mineList;
	}

	public int getMaxLandMines() {
		return maxLandMines;
	}

	public void setMaxLandMines(int maxLandMines) {
		this.maxLandMines = maxLandMines;
	}

	public int getLandMineCount() {
		return landMineCount;
	}

	public void setLandMineCount(int landMineCount) {
		this.landMineCount = landMineCount;
	}

	public boolean isCurable() {
		return curable;
	}

	public void setCurable(boolean curable) {
		this.curable = curable;
	}
}
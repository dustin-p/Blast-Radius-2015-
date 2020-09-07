package com.porterdustin.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.porterdustin.zombienuke.BearTrap;
import com.porterdustin.effects.BoomEffect;
import com.porterdustin.zombienuke.Dog;
import com.porterdustin.zombienuke.Explosion;
import com.porterdustin.zombienuke.GlobalVariables;
import com.porterdustin.zombienuke.Human;
import com.porterdustin.effects.InfectEffect;
import com.porterdustin.zombienuke.LandMine;
import com.porterdustin.zombienuke.Level;
import com.porterdustin.effects.PaperRipEffect;
import com.porterdustin.zombienuke.PowerUp;
import com.porterdustin.zombienuke.R;
import com.porterdustin.effects.SplatterEffect;
import com.porterdustin.zombienuke.RoundInfo;
import com.porterdustin.zombienuke.Zombie;
import com.porterdustin.effects.ZombiePopEffect;

public class GameActivity extends Activity {

	Zombie[] zombieList;
	Human[] humanList;
	
	SplatterEffect[] splatterList;
	Explosion exp;
	
	Double scale;
	float newZombieChance;
	float zombieBoostSpeed;
	RoundInfo roundInfo;
	int lastClickX = 0;
	int lastClickY = 0;
	int sWidth;
	int sHeight;
	int sSize;
	int vertPadding;
	int horzPadding;
	int scaleTime;
	int zoomCount = 0;
	int dogCount = 0;
	int zoomType = 0;
	int currentLevel = 0;
	private int fadeTime;
	private int maxZBar;
	private int stSteps;
	boolean canReact;
	boolean readyToFinish;
	boolean holdingDown;
	boolean paused;
	boolean gameLost;
	boolean canResume;
	boolean isLandscape;
	boolean firstLoad;
	boolean levelComplete;
	private boolean lastMile;
	private long startTime = 0L;
	private long timeInMilliseconds = 0L;
	private long pausedTime = 0L;
	private long startPause = 0L;
	private float originalBackgroundScale;
	private GlobalVariables global;
	private ViewGroup layout;
	private int explosionX;
	private int explosionY;
	private BearTrap[] bearTraps;
	private LandMine[] landMines;
	private Dog[] dogList;
	private ZombiePopEffect[] zombiePopList;
	private InfectEffect[] infectFxList;
	private BoomEffect[] boomList;
	private PaperRipEffect[] paperRipList;
	private int[] powerUpSelection;
	private double zBarScale;
	static final int SELECT_POWERUP_REQUEST = 1;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        global = ((GlobalVariables) this.getApplication());
        layout = (ViewGroup) findViewById(R.id.main);
        Intent intent = getIntent();
        currentLevel = intent.getIntExtra("level", 0);
        sWidth = getScreenWidth();
        sHeight = getScreenHeight();
        sSize = Math.min(sWidth, sHeight);
        bearTraps = null;
        landMines = null;
        dogList = null;
        global.setReset(false);
        
        ((AnimationDrawable) ((ImageView)findViewById(R.id.paperWrinkle)).getDrawable()).start();
        
        
        exp = new Explosion(this);
        global.setExplosion(exp);
        layout.addView(exp);
        firstLoad = true;
        
        if (getResources().getConfiguration().orientation == 1) {
        	vertPadding = Math.abs(sWidth - sHeight)/2;
        	horzPadding = 0;
			isLandscape = false;
        } else {
        	vertPadding = 0;
        	horzPadding = Math.abs(sWidth - sHeight)/2;
        	isLandscape = true;
        }
        
        global.setHorzPadding(horzPadding);
        global.setVertPadding(vertPadding);
        originalBackgroundScale = findViewById(R.id.gameBackground).getScaleX();
        
		LayoutParams menuTopSize = findViewById(R.id.menuTop).getLayoutParams();
		LayoutParams menuBottomSize = findViewById(R.id.menuBottom).getLayoutParams();
		LayoutParams menuLeftSize = findViewById(R.id.menuLeft).getLayoutParams();
		LayoutParams menuRightSize = findViewById(R.id.menuRight).getLayoutParams();
		LayoutParams stars = findViewById(R.id.starRating).getLayoutParams();
		LayoutParams digit1 = findViewById(R.id.digitOne).getLayoutParams();
		LayoutParams digit2 = findViewById(R.id.digitTwo).getLayoutParams();
		LayoutParams digit3 = findViewById(R.id.digitThree).getLayoutParams();
		LayoutParams heart = findViewById(R.id.heartImage).getLayoutParams();
		LayoutParams lives = findViewById(R.id.livesDigit).getLayoutParams();
		LayoutParams zbar = findViewById(R.id.zombieBarOutline).getLayoutParams();
		LayoutParams zbarback = findViewById(R.id.zombieBarBack).getLayoutParams();
		
		menuTopSize.height = vertPadding;
		menuBottomSize.height = vertPadding;
		menuLeftSize.width = horzPadding;
		menuRightSize.width = horzPadding;
		stars.height = sHeight/8;
		stars.width = sHeight/3;
		digit1.height = sSize/4;
		digit2.height = sSize/4;
		digit3.height = sSize/4;
		digit1.width = sSize/4;
		digit2.width = sSize/4;
		digit3.width = sSize/4;
		heart.width = sSize/5;
		heart.height = 2*sSize/15;
		lives.width = 2*sSize/15;
		lives.height = 2*sSize/15;
		zbar.width = sSize/3;
		zbar.height = sSize/12;
		zbarback.width = 7*sSize/24;
		
		findViewById(R.id.menuTop).setLayoutParams(menuTopSize);
		findViewById(R.id.menuBottom).setLayoutParams(menuBottomSize);
		findViewById(R.id.menuLeft).setLayoutParams(menuLeftSize);
		findViewById(R.id.menuRight).setLayoutParams(menuRightSize);
		findViewById(R.id.starRating).setLayoutParams(stars);
		findViewById(R.id.digitOne).setLayoutParams(digit1);
		findViewById(R.id.digitTwo).setLayoutParams(digit2);
		findViewById(R.id.digitThree).setLayoutParams(digit3);
		findViewById(R.id.heartImage).setLayoutParams(heart);
		findViewById(R.id.livesDigit).setLayoutParams(lives);
		findViewById(R.id.livesDigit).setLayoutParams(lives);
		
		findViewById(R.id.starRating).setY((vertPadding/2) - (sHeight/16));
		findViewById(R.id.heartImage).setX(horzPadding + 3*sSize/5);
		findViewById(R.id.heartImage).setY((vertPadding/2) - (sSize/15));
		findViewById(R.id.livesDigit).setX(horzPadding + 49*sSize/64);
		findViewById(R.id.livesDigit).setY((vertPadding/2) - (sSize/15));
		findViewById(R.id.zombieBarBack).setX(horzPadding + 5*sSize/48);
		findViewById(R.id.zombieBarBack).setY((vertPadding/2) - (sSize/24));
		findViewById(R.id.zombieBarOutline).setX(horzPadding + sSize/12);
		findViewById(R.id.zombieBarOutline).setY((vertPadding/2) - (sSize/24));
		
		((AnimationDrawable) ((ImageView) findViewById(R.id.starRating)).getDrawable()).start();
		((AnimationDrawable) ((ImageView) findViewById(R.id.heartImage)).getDrawable()).start();
		((AnimationDrawable) ((ImageView) findViewById(R.id.zombieBarOutline)).getDrawable()).start();
		
		findViewById(R.id.playPause).setVisibility(View.GONE);
		findViewById(R.id.levelStatus).setVisibility(View.GONE);
		findViewById(R.id.starRating).setVisibility(View.GONE);
		findViewById(R.id.digitOne).setVisibility(View.GONE);
		findViewById(R.id.digitTwo).setVisibility(View.GONE);
		findViewById(R.id.digitThree).setVisibility(View.GONE);
		
		
		createLevel(currentLevel);

        LayoutParams anchor = findViewById(R.id.anchor).getLayoutParams();
		anchor.height = Math.min(sWidth,sHeight);
		anchor.width = anchor.height; 
		findViewById(R.id.anchor).setLayoutParams(anchor);
        
		maxZBar = 7*sSize/24;
		
		final Handler menuHandler = new Handler();
        Runnable menuRunnable = new Runnable() {
        	private float randomXScale;
        	private float randomYScale;

			@Override
        	public void run() {
        		randomXScale = (float) (0.9 + Math.random()/5);
        		randomYScale = (float) (0.9 + Math.random()/5);
        		
        		findViewById(R.id.livesDigit).setScaleY(0.9f + (float)Math.random()/5);
				findViewById(R.id.livesDigit).setScaleX(0.9f + (float)Math.random()/5);
				
				findViewById(R.id.zombieBarBack).setScaleX(0.66f + randomXScale/3);
				findViewById(R.id.zombieBarBack).setScaleY(randomYScale);
				findViewById(R.id.zombieBarOutline).setScaleX(0.66f + randomXScale/3);
				findViewById(R.id.zombieBarOutline).setScaleY(randomYScale);
				
				if (gameLost || levelComplete) {
					findViewById(R.id.digitOne).setScaleY(0.9f + (float)Math.random()/5);
					findViewById(R.id.digitOne).setScaleX(0.83f + (float)Math.random()/3);
					findViewById(R.id.digitTwo).setScaleY(0.9f + (float)Math.random()/5);
					findViewById(R.id.digitTwo).setScaleX(0.83f + (float)Math.random()/3);
					findViewById(R.id.digitThree).setScaleY(0.9f + (float)Math.random()/5);
					findViewById(R.id.digitThree).setScaleX(0.83f + (float)Math.random()/3);
				}
        		menuHandler.postDelayed(this, 150);
        	}
        };
        menuHandler.postDelayed(menuRunnable, 1);
		
        
        
        
        final Handler gameHandler = new Handler();
        Runnable gameRunnable = new Runnable() {
        	   

			private int hCount;
			private int zKills;
			private int steps;
			
			@Override
        	public void run() {
				if (paused || fadeTime > 25) {
					pausedTime = SystemClock.uptimeMillis() - startPause;
				} else {
					zKills = global.getKills();
					hCount = global.getHumanCount();
					steps++;
					global.setExpWaitTime(global.getExpWaitTime() - 1);
        		   
					if (steps > 1000)
						steps = 1;
        			   
					if (steps % 100 == 1) {
						updateSeconds();
					}
        			   
					if (steps % 5 == 0) {
						updateSecondTenths();
					}
        			   

					if (!levelComplete && !gameLost) {
						if (global.getZombieCount() > 0) {
							zombieList[steps % global.getZombieCount()].setDirection();
							zombieList[steps % global.getZombieCount()].infect(scale);
						}
						humanList[steps % humanList.length].setDirection();
						if (steps % 5 == (int)(Math.random()*5) && humanList[steps % humanList.length].hasWeapon())
							humanList[steps % humanList.length].setShootingDirection();
        				   
						if (steps % 5 < humanList.length) {
							humanList[steps % 5].decreaseImageTimer();
							if (global.hasGuns() && timeInMilliseconds > 1000) {
								humanList[steps % 5].shoot(stSteps, exp, scale);
							}	   
						}
        				   
						if (scaleTime > 0) {
							changeScale(0.025);
						}
						for (int k = 0; k < global.getZombieCount(); k++) {
							zombieList[k].move(scale);
						}
        				   
						for (int k = 0; k < global.getDogCount(); k++) {
							dogList[k].move(scale);
						}
        				   
						for (int k = 0; k < humanList.length; k++) {
							if (!humanList[k].isDead()) {
								humanList[k].move(scale);
							}
						}
					}
				}
        		   
				gameHandler.postDelayed(this, 1);
			}

			private void updateSeconds() {
				canReact = true;
				
				if (!gameLost && !levelComplete) {
					if (lastMile == false && global.getKills() > global.getKillsNeeded() - global.getZombieCount()) {
						for (int k = 0; k < global.getZombieCount(); k++) {
							if (k % 2 == 0)
								zombieList[k].changeSpeed(zombieList[k].getSpeed()/2);
						}
						lastMile = true;
					}
					
					if (Math.random() < newZombieChance && !lastMile) {
						   addZombies(1);
					}
					
					if (zKills >= global.getKillsNeeded() && !levelComplete) {
						readyToFinish = true;
						winGame();	
					} else if (hCount <= 0 && !readyToFinish) {
		     			  loseGame();
		     			  canReact = false;
		     		}

				}
			}
			
			private void updateSecondTenths() {
				timeInMilliseconds = SystemClock.uptimeMillis() - startTime;			
				setZBar();
				
				
				for (int k = 0; stSteps % 3 == 0 && k < bearTraps.length; k++) {
					bearTraps[k].snap();
				}
				if (global.getGift()) {
					//PowerUp gift = new PowerUp(thisActivity);
					//gift.setInPlay(global.getGiftX(), global.getGiftY(), global.getGiftTier());
					//layout.addView(gift);
					global.giveGift(false, 0, 0, 0);
				}
				
				if (!gameLost && !levelComplete) {
					if (stSteps % 3 == 0 && global.getDogCount() > 0)
						for (int k = 0; k < global.getDogCount(); k++)
							dogList[k].setDirection();
				}
				stSteps++;
				if (stSteps > 9)
					stSteps = 0;
			}
        };
       gameHandler.postDelayed(gameRunnable, 1);
    }
	
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_POWERUP_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
            	Bundle info = data.getExtras();
            	powerUpSelection = info.getIntArray("powerUpList");
            	PowerUp gift = new PowerUp(this, roundInfo);
        		gift.useGenerator(powerUpSelection);
        		layout.addView(gift);
        		global.giveGift(false, 0, 0, 0);
            } else if (resultCode == RESULT_CANCELED) {
            	this.finish();
            	this.overridePendingTransition(0, 0);
            }
        }
        unPauseGame();
        fadeInFromBlack();
    }
    
    
	
	public void createLevel(int levelNumber) {
		findViewById(R.id.blackFade).setAlpha(1f);
		fadeTime = 300;
		
		if (currentLevel <= 0 || global.getLives() <= 0) {
			this.finish();
			this.overridePendingTransition(0, 0);
		}
		Level thisLevel = new Level(levelNumber);
		global.setLevel(thisLevel);
		global.setScreenWidth(sSize);
		global.setPowerUpCount(thisLevel.getPowerUpCount());
		global.setPowerUpThresh(thisLevel.getPowerUpThresh());
		global.setCurable(false);
    	global.setDogCount(0);
    	global.setMaxLandMines(0);
    	global.setLandMineCount(0);
		
		startPreGame(thisLevel, levelNumber);
		
    	Human[] tempList = null;
    	
		if (!firstLoad) {
			//tempList = humanList.clone();
			clearLevel(gameLost);
		}
		
		bearTraps = new BearTrap[16];
		for (int k = 0; k < bearTraps.length; k++) {
	    	bearTraps[k] = new BearTrap(this, horzPadding, vertPadding);
	    	layout.addView(bearTraps[k]);
	    }
		
		landMines = new LandMine[16];
		for (int k = 0; k < landMines.length; k++) {
	    	landMines[k] = new LandMine(this, horzPadding, vertPadding);
	    	layout.addView(landMines[k]);
	    }
		
		scale = (double)(sSize)/1280;
    	zombieList = new Zombie[thisLevel.getZombieCount()];
    	humanList = new Human[thisLevel.getHumanCount()];
    	splatterList = new SplatterEffect[humanList.length];
    	zombiePopList = new ZombiePopEffect[zombieList.length];
    	infectFxList = new InfectEffect[humanList.length];
    	boomList = new BoomEffect[4];
    	paperRipList = new PaperRipEffect[boomList.length];
    	roundInfo = new RoundInfo(this, zombieList, humanList, scale);
    	
    	newZombieChance = thisLevel.getNewZombieChance();
    	zombieBoostSpeed = thisLevel.getZombieBoostSpeed();
    	zoomType = thisLevel.getZoomType();
    	readyToFinish = false;
    	startTime = SystemClock.uptimeMillis();
    	firstLoad = false;
    	levelComplete = false;
    	global.setGameLost(false);
    	gameLost = false;
    	timeInMilliseconds = 0L;
    	pausedTime = 0L;
    	startPause = 0L;
    	lastMile = false;
    	global.setGamePaused(false);
    	global.setHasGuns(false);
    	global.setScale(scale);
    	global.setLevelComplete(false);
    	global.setHumanCount(humanList.length);
    	global.setKillsNeeded(thisLevel.getZombiesToKill());
    	global.setZombieCount(0);
    	global.setKills(0);
    	global.setDeaths(0);
    	global.setInfections(0);
    	
    	global.resetInfectCylce();
    	global.resetPopCycle();


    	for (int k = 0; k < paperRipList.length; k++) {
    		paperRipList[k] = new PaperRipEffect(this);
        	layout.addView(paperRipList[k]);
    	}
    	
    	for (int k = 0; k < landMines.length; k++) {
    		landMines[k].bringToFront();
    	}
    	
    	for (int k = 0; k < bearTraps.length; k++) {
    		bearTraps[k].bringToFront();
    	}

    	for (int k = 0; k < splatterList.length; k++) {
    		splatterList[k] = new SplatterEffect(this);
        	layout.addView(splatterList[k]);
    	}
    	
    	exp.bringToFront();
    	
    	
    	
    	for (int k = 0; k < zombieList.length; k++) {
        	zombieList[k] = new Zombie(this, thisLevel.getZombieSpeed(), thisLevel.getSmartChance(),
        			thisLevel.getFastChance(), thisLevel.getStrongChance(), thisLevel.getInvisibleChance(), 
        			sSize, horzPadding, vertPadding);
        	zombieList[k].setLayoutParams(new LayoutParams((int)(scale*96), (int)(scale*96)));
        	zombieList[k].setSize((int) (scale*96));
        	layout.addView(zombieList[k]);
        }
    	
    	for (int k = 0; k < humanList.length; k++) {
    		if (tempList != null && k < tempList.length && tempList[k].hasEquipment() && !tempList[k].isDead()) {
    			tempList[k].newRound(sSize, horzPadding, vertPadding, k, thisLevel.getLevelType() == 1);
    			humanList[k] = tempList[k];
    		} else
    			humanList[k] = new Human(this, sSize, horzPadding, vertPadding, k, thisLevel.getLevelType() == 1);
    		humanList[k].setLayoutParams(new LayoutParams((int)(scale*96), (int)(scale*96)));
    		humanList[k].setSize((int) (scale*96));
    		layout.addView(humanList[k]);
    	}
    	
    	
    	
    	dogList = new Dog[humanList.length*2];
    	
		for (int k = 0; k < dogList.length; k++) {
			if (k < humanList.length)
				dogList[k] = new Dog(this, sSize, horzPadding, vertPadding, humanList[k]);
			else
				dogList[k] = new Dog(this, sSize, horzPadding, vertPadding, humanList[k - humanList.length]);
			dogList[k].setLayoutParams(new LayoutParams((int)(scale*96), (int)(scale*96)));
    		dogList[k].setSize((int) (scale*96));
    		layout.addView(dogList[k]);
    	}
    	
		findViewById(R.id.paperWrinkle).bringToFront();
    	for (int k = humanList.length -1; k > -1; k--)
    		humanList[k].bringToFront();
		for (int k = zombieList.length -1; k > -1; k--)
			zombieList[k].bringToFront();
		
    	for (int k = 0; k < zombiePopList.length; k++) {
    		zombiePopList[k] = new ZombiePopEffect(this);
        	layout.addView(zombiePopList[k]);
    	}
    	
    	for (int k = 0; k < infectFxList.length; k++) {
    		infectFxList[k] = new InfectEffect(this);
        	layout.addView(infectFxList[k]);
    	}
    	
    	for (int k = 0; k < boomList.length; k++) {
    		boomList[k] = new BoomEffect(this);
        	layout.addView(boomList[k]);
    	}
    	
    	addZombies(1);
    	
    	exp.update(scale, zombieList, humanList, splatterList);
    	tempList = null;
    	
    	global.setHumanList(humanList);
    	global.setZombieList(zombieList);
    	global.setTrapList(bearTraps);
    	global.setMineList(landMines);
    	global.setDogList(dogList);
    	global.setBoomList(boomList);
    	global.setPaperRipList(paperRipList);
    	global.setZombiePopList(zombiePopList);
    	global.setInfectFxList(infectFxList);
    	
    	
    	for (int k = 0; k < zombieList.length; k++) {
        	zombieList[k].initialize();
        }
    	
    	setLivesImage();
    	
    	if (levelNumber % 2 == 0) {
    		findViewById(R.id.gameBackground).setScaleX(-originalBackgroundScale);
    		findViewById(R.id.paperWrinkle).setScaleX(-originalBackgroundScale);
    	} else {
    		findViewById(R.id.gameBackground).setScaleX(originalBackgroundScale);
    		findViewById(R.id.paperWrinkle).setScaleX(originalBackgroundScale);
    	}
    	findViewById(R.id.gameBackground).setScaleY(originalBackgroundScale);
    	findViewById(R.id.paperWrinkle).setScaleY(originalBackgroundScale);
    	
        findViewById(R.id.menuBottom).bringToFront();
        findViewById(R.id.menuTop).bringToFront();
        findViewById(R.id.menuLeft).bringToFront();
        findViewById(R.id.menuRight).bringToFront();
        findViewById(R.id.heartImage).bringToFront();
        findViewById(R.id.zombieBarBack).bringToFront();
        findViewById(R.id.zombieBarOutline).bringToFront();
        findViewById(R.id.livesDigit).bringToFront();
        findViewById(R.id.playPause).bringToFront();
        findViewById(R.id.blackFade).bringToFront();

        
        setBackground(thisLevel.getBackground(), thisLevel.getBackgroundColor());
    	addZombies(thisLevel.getStartingZombies() - 1);
    	
    }
	
	
	
	
	
	
	
	
	
	private void startPreGame(Level thisLevel, int levelNumber) {
		if (thisLevel.getPowerUpTeir() > 0 && global.getLives() > 0 && currentLevel != 0) {
			Intent preGame = new Intent(this, PreGameActivity.class);
			preGame.putExtra("level", levelNumber);
			preGame.putExtra("teir", thisLevel.getPowerUpTeir());
			startActivityForResult(preGame, SELECT_POWERUP_REQUEST);
			overridePendingTransition(0, 0);
		} else
			fadeInFromBlack();
	}


	private void setLivesImage() {
		int[] numbersList = {
				R.drawable.one1, R.drawable.two1, R.drawable.three1, R.drawable.four1,
				R.drawable.five1, R.drawable.six1, R.drawable.seven1, R.drawable.eight1, R.drawable.nine1
			};
		
		if (global.getLives() < 2)
			findViewById(R.id.livesDigit).setBackgroundResource(R.drawable.zero1);
		else
			findViewById(R.id.livesDigit).setBackgroundResource(numbersList[global.getLives() - 2]);
		if (global.getLives() < 1) {
			findViewById(R.id.heartImage).getBackground().setColorFilter(Color.BLACK, Mode.MULTIPLY);
		} else if (global.getLives() < 2) {
			findViewById(R.id.livesDigit).getBackground().setColorFilter(Color.rgb(255, 80, 80), Mode.MULTIPLY);
			findViewById(R.id.heartImage).getBackground().setColorFilter(Color.rgb(255, 80, 80), Mode.MULTIPLY);
		} else if (global.getLives() < 4) {
			findViewById(R.id.livesDigit).getBackground().setColorFilter(Color.WHITE, Mode.MULTIPLY);
			findViewById(R.id.heartImage).getBackground().setColorFilter(Color.rgb(224, 192, 128), Mode.MULTIPLY);
		} else {
			findViewById(R.id.livesDigit).getBackground().setColorFilter(Color.WHITE, Mode.MULTIPLY);
			findViewById(R.id.heartImage).getBackground().setColorFilter(Color.rgb(42, 168, 143), Mode.MULTIPLY);
		}
	}


	public void clearLevel(boolean lostGame) {
		
    	for (int k = 0; k < zombieList.length; k++) {
        	layout.removeView(zombieList[k]);
        	zombieList[k] = null;
        }
    	
    	for (int k = 0; k < humanList.length; k++) {
    		layout.removeView(humanList[k]);
    		humanList[k] = null;
    	}
    	
    	for (int k = 0; k < zombiePopList.length; k++) {
        	layout.removeView(zombiePopList[k]);
        	zombiePopList[k] = null;
    	}
    	
    	for (int k = 0; k < infectFxList.length; k++) {
        	layout.removeView(infectFxList[k]);
        	infectFxList[k] = null;
    	}
    	
    	for (int k = 0; k < bearTraps.length; k++) {
        	layout.removeView(bearTraps[k]);
        	bearTraps[k] = null;
    	}
    	
    	for (int k = 0; k < landMines.length; k++) {
        	layout.removeView(landMines[k]);
        	landMines[k] = null;
    	}
    	
    	for (int k = 0; k < dogList.length; k++) {
        	layout.removeView(dogList[k]);
        	dogList[k] = null;
    	}
    	
    	for (int k = 0; k < splatterList.length; k++) {
        	layout.removeView(splatterList[k]);
        	splatterList[k] = null;
    	}
    	
    	for (int k = 0; k < boomList.length; k++) {
        	layout.removeView(boomList[k]);
        	boomList[k] = null;
    	}
    	
    	for (int k = 0; k < paperRipList.length; k++) {
        	layout.removeView(paperRipList[k]);
        	paperRipList[k] = null;
    	}
    }
    
    
    
    
    
    
    

	public void setBackground(int backgroundId, int backgroundColor) {
		findViewById(R.id.gameBackground).setBackgroundResource(backgroundId);
		global.setBgColor(backgroundColor);
		
		findViewById(R.id.gameBackground).getBackground().setColorFilter(global.getBgColor(), Mode.MULTIPLY);
		for (int k = 0; k < global.getPaperRipList().length; k++) {
			global.getPaperRipList()[k].getBackground().setColorFilter(global.getBgColor(), Mode.MULTIPLY);
		}
		
		switch(zoomType) {
			case(1): scaleTime = 8;
					break;
			case(2): scaleTime = 16;
					break;
			case(3): scaleTime = 24;
					break;
			case(4): scaleTime = 32;
					break;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public void addZombies(int moreZombies) {
		int zCount = global.getZombieCount();
		while (moreZombies > 0 && zCount < zombieList.length - global.getHumanCount()) {
			global.setZombieCount(zCount + 1);
			zombieList[zCount].addInPlay(scale, true);
			moreZombies--;
		}
	}
	
	public int getScreenWidth() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}
	
	public int getScreenHeight() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}
	
	
	public String MD5(String md5) {
		   try {
		        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    }
		    return null;
		}
	
	
	public void fadeInFromBlack() {
		findViewById(R.id.blackFade).setAlpha(1);
		fadeTime = 300;
		
		final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
        	
        	@Override
        	public void run() {
        		fadeTime--;
        		if (fadeTime < 31)
        		findViewById(R.id.blackFade).setAlpha(fadeTime/30f);
        		if (fadeTime > 0)
        			handler.postDelayed(this, 1);
        	}
        };
        handler.postDelayed(runnable, 1);
	}
	
	
	public void pauseGame() {
		global.setGamePaused(true);
		startPause = SystemClock.uptimeMillis();
		exp.releaseScreen();
		for (int k = 0; k < global.getZombieCount(); k ++) {
			((AnimationDrawable) zombieList[k].getDrawable()).stop();
		}
		
		for (int k = 0; k < dogList.length; k ++) {
			((AnimationDrawable) dogList[k].getBackground()).stop();
		}
		
		for (int k = 0; k < humanList.length; k ++) {
			((AnimationDrawable) humanList[k].getBackground()).stop();
		}
		((AnimationDrawable) ((ImageView)findViewById(R.id.paperWrinkle)).getDrawable()).stop();
		
		
		findViewById(R.id.playPause).setBackgroundResource(R.drawable.pause_animation);
		findViewById(R.id.playPause).setVisibility(View.VISIBLE);
		paused = true;
	}
	
	
	public void unPauseGame() {
   		findViewById(R.id.playPause).setVisibility(View.GONE);
   		for (int k = 0; k < global.getZombieCount(); k ++) {
			((AnimationDrawable) zombieList[k].getDrawable()).start();
		}
   		
   		for (int k = 0; k < humanList.length; k ++) {
			((AnimationDrawable) humanList[k].getBackground()).start();
		}
   		
   		((AnimationDrawable) ((ImageView)findViewById(R.id.paperWrinkle)).getDrawable()).start();
   		
   		global.setGamePaused(false);
		paused = false;
		startTime += pausedTime;
	}
	
	
	
	
	private void setLevelNumbers() {
		int[] numbersList = {
				R.drawable.zero1, R.drawable.one1, R.drawable.two1, R.drawable.three1, R.drawable.four1,
				R.drawable.five1, R.drawable.six1, R.drawable.seven1, R.drawable.eight1, R.drawable.nine1
			};
		
		ImageView digit1 = (ImageView) findViewById(R.id.digitOne);
    	ImageView digit2 = (ImageView) findViewById(R.id.digitTwo);
    	ImageView digit3 = (ImageView) findViewById(R.id.digitThree);
    	
    	digit1.setTag(currentLevel/100);
    	digit1.setImageResource(numbersList[(Integer) digit1.getTag()]);
    	digit2.setTag((currentLevel/10) % 10);
    	digit2.setImageResource(numbersList[(Integer) digit2.getTag()]);
    	digit3.setTag(currentLevel % 10);
    	digit3.setImageResource(numbersList[(Integer) digit3.getTag()]);
    	
    	if (currentLevel < 100) {
			digit1.setVisibility(View.GONE);
			digit2.setX(sWidth/2 - sSize/4);
			digit3.setX(sWidth/2);
		} else {
			digit1.setX(sWidth/2 - sSize/4 - sSize/8);
			digit2.setX(sWidth/2 - sSize/8);
			digit3.setX(sWidth/2 + sSize/8);
		}
    	
    	if (levelComplete) {
    		digit1.setColorFilter(Color.WHITE, Mode.MULTIPLY);
    		digit2.setColorFilter(Color.WHITE, Mode.MULTIPLY);
    		digit3.setColorFilter(Color.WHITE, Mode.MULTIPLY);
    	} else {
    		digit1.setColorFilter(Color.rgb(255, 80, 80), Mode.MULTIPLY);
    		digit2.setColorFilter(Color.rgb(255, 80, 80), Mode.MULTIPLY);
    		digit3.setColorFilter(Color.rgb(255, 80, 80), Mode.MULTIPLY);
    	}
    	
	}
	
	
	public void setZBar() {
		if (zBarScale != 1 - ((double)(global.getKills())/global.getKillsNeeded()) && global.getKillsNeeded() > 0) {
			zBarScale = 1 - ((double)(global.getKills())/global.getKillsNeeded());
			LayoutParams zbarback = findViewById(R.id.zombieBarBack).getLayoutParams();
			zbarback.width = (int) (maxZBar*zBarScale);
			//findViewById(R.id.zombieBarBack).setLayoutParams(zbarback);
		}
	}
	
	
	public void changeScale(double changeAmount) {
		scale += scale*changeAmount;
		roundInfo.setScale(scale);
		global.setScale(scale);
		
		
		if (currentLevel % 2 == 0) {
			findViewById(R.id.gameBackground).setScaleX(findViewById(R.id.gameBackground).getScaleX() - (float)changeAmount);
			findViewById(R.id.paperWrinkle).setScaleX(findViewById(R.id.gameBackground).getScaleX() - (float)changeAmount);
		} else {
			findViewById(R.id.gameBackground).setScaleX(findViewById(R.id.gameBackground).getScaleX() + (float)changeAmount);
			findViewById(R.id.paperWrinkle).setScaleX(findViewById(R.id.gameBackground).getScaleX() + (float)changeAmount);
		}
		findViewById(R.id.gameBackground).setScaleY(findViewById(R.id.gameBackground).getScaleY() + (float)changeAmount);
		findViewById(R.id.paperWrinkle).setScaleY(findViewById(R.id.gameBackground).getScaleY() + (float)changeAmount);
		
		for (int k = 0; k < splatterList.length; k++) {
			int newX = (int) ((splatterList[k].getX()-horzPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + horzPadding;
			int newY = (int) ((splatterList[k].getY()-vertPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + vertPadding;
			splatterList[k].setX(newX);
			splatterList[k].setY(newY);
		}
		
		for (int k = 0; k < bearTraps.length && bearTraps[0].isReady(); k++) {
			int newX = (int) ((bearTraps[k].getXval()-horzPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + horzPadding;
			int newY = (int) ((bearTraps[k].getYval()-vertPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + vertPadding;
			bearTraps[k].setXval(newX);
			bearTraps[k].setYval(newY);
			bearTraps[k].setSize(scale);
		}
		
		for (int k = 0; k < landMines.length && landMines[0].isReady(); k++) {
			int newX = (int) ((landMines[k].getXval()-horzPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + horzPadding;
			int newY = (int) ((landMines[k].getYval()-vertPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + vertPadding;
			landMines[k].setXval(newX);
			landMines[k].setYval(newY);
			landMines[k].setSize(scale);
		}
		
		for (int k = 0; k < humanList.length; k++) {
			int newX = (int) ((humanList[k].getXval()-horzPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + horzPadding;
			int newY = (int) ((humanList[k].getYval()-vertPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + vertPadding;
			humanList[k].setXval(newX);
			humanList[k].setYval(newY);
			humanList[k].setSize((int) (scale*96));
			LayoutParams human = humanList[k].getLayoutParams();
			human.width = (int) (scale*96);
			human.height = (int) (scale*96);
			humanList[k].setLayoutParams(human);
		}
		
		for (int k = 0; k < dogList.length; k++) {
			int newX = (int) ((dogList[k].getXval()-horzPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + horzPadding;
			int newY = (int) ((dogList[k].getYval()-vertPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + vertPadding;
			dogList[k].setXval(newX);
			dogList[k].setYval(newY);
			dogList[k].setSize((int) (scale*96));
			LayoutParams dog = dogList[k].getLayoutParams();
			dog.width = (int) (scale*96);
			dog.height = (int) (scale*96);
			dogList[k].setLayoutParams(dog);
		}
		
		for (int k = 0; k < zombieList.length; k++) {
			if (!zombieList[k].outOfBounds()) {
				int newX = (int) ((zombieList[k].getXpos()-horzPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + horzPadding;
				int newY = (int) ((zombieList[k].getYpos()-vertPadding)*(1 + changeAmount) - (sSize*(1 + changeAmount) - sSize)/2) + vertPadding;
				zombieList[k].setXPos(newX);
				zombieList[k].setYPos(newY);
				if (Math.random() < 0.25) {
					((AnimationDrawable) zombieList[k].getDrawable()).stop();
				((AnimationDrawable) zombieList[k].getDrawable()).start();
				}
			}
			zombieList[k].setSize((int) (scale*96));
			LayoutParams zombie = zombieList[k].getLayoutParams();
			zombie.width = (int) (scale*96);
			zombie.height = (int) (scale*96);
			zombieList[k].setLayoutParams(zombie);
		}
		
		scaleTime--;
	}
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
		if (!levelComplete && !gameLost && !paused)
			pauseGame();
		canResume = false;
	}
	
	@Override
	public void finish() {
		super.finish();
		global.reset();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		global.setLives(getSharedPreferences("progress", 0).getInt("lives", 0));
		canResume = true;
		if (currentLevel == 0) {
        	this.finish();
    		this.overridePendingTransition(0, 0);
        }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (!paused && !levelComplete && !gameLost) {
	    		pauseGame();
	    	} else {
	    		this.finish();
	    		this.overridePendingTransition(0, 0);
	    	}
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    switch (event.getAction()) {
	        case MotionEvent.ACTION_DOWN: {
	        	if (((int)event.getY() > vertPadding && (int)event.getY() < sSize + vertPadding
	        			&& (int)event.getX() > horzPadding && (int)event.getX() < sSize + horzPadding)) {
		        	if (canResume && !levelComplete) {
			        	if (!paused && !gameLost && timeInMilliseconds > 1000) {
			        		explosionX = (int)event.getX();
			        		explosionY = (int)event.getY();
			        		exp.tapScreen(explosionX, explosionY, scale, zombieBoostSpeed);
			        	} else if ((int)event.getX() > horzPadding + sSize/3 && (int)event.getX() < (2*sSize)/3 + horzPadding
			       				&& (int)event.getY() > vertPadding + sSize/3 && (int)event.getY() < (2*sSize)/3 + vertPadding)
			        		findViewById(R.id.playPause).setBackgroundResource(R.drawable.play);
			       	}
	        	}
		   	    break;
	        }
	        
	        case MotionEvent.ACTION_MOVE: {
	        	if ((Math.abs(event.getX() - explosionX) < sSize/5 && Math.abs(event.getY() - explosionY) < sSize/5)
	        			|| scaleTime > 0) {
	        		explosionX = (int)event.getX();
	        		explosionY = (int)event.getY();
	        		if (!levelComplete && !gameLost && !paused)
	        			exp.expand(explosionX, explosionY);
	        		break;
	        	} else if (((int)event.getY() < vertPadding || (int)event.getY() > sSize + vertPadding
	        			|| (int)event.getX() < horzPadding || (int)event.getX() > sSize + horzPadding)) {
		        	if (canResume && !levelComplete) {
			        	if (!paused && !gameLost && timeInMilliseconds > 1000) {
			        		exp.releaseScreen();
			        	}
		        	}
	        	} if (paused)
	        		break;
	        }
	        
	        case MotionEvent.ACTION_UP:	{
	        	if (!paused && !gameLost && !levelComplete && canResume) {
		       		exp.releaseScreen();
	        	} else if (!paused && canResume && (int)event.getY() > vertPadding && (int)event.getY() < sSize + vertPadding
		    			&& (int)event.getX() > horzPadding && (int)event.getX() < sSize + horzPadding) {
		       		if (canReact && (levelComplete || gameLost)) {
		       			if (global.getLives() == 0) {
		       				this.finish();
		       				this.overridePendingTransition(0, 0);
		       			} else {
			       			currentLevel += levelComplete? 1 : 0;
			       			createLevel(currentLevel);
			       			findViewById(R.id.menuTop).setBackgroundResource(R.drawable.divider);
			       			findViewById(R.id.heartImage).setVisibility(View.VISIBLE);
			       			findViewById(R.id.zombieBarOutline).setVisibility(View.VISIBLE);
			       			findViewById(R.id.zombieBarBack).setVisibility(View.VISIBLE);
			       			findViewById(R.id.livesDigit).setVisibility(View.VISIBLE);
			       			findViewById(R.id.levelStatus).setVisibility(View.GONE);
			       			findViewById(R.id.starRating).setVisibility(View.GONE);
			       			findViewById(R.id.digitOne).setVisibility(View.GONE);
			       			findViewById(R.id.digitTwo).setVisibility(View.GONE);
			       			findViewById(R.id.digitThree).setVisibility(View.GONE);		       			
		       			}
		       		}
		       	} else if (paused && canResume) {
		       		if ((int)event.getX() > horzPadding + sSize/3 && (int)event.getX() < (2*sSize)/3 + horzPadding
		       				&& (int)event.getY() > vertPadding + sSize/3 && (int)event.getY() < (2*sSize)/3 + vertPadding) 
		       			unPauseGame();
		       		else {
		       			findViewById(R.id.playPause).setBackgroundResource(R.drawable.pause_animation);
		       			((AnimationDrawable) findViewById(R.id.playPause).getBackground()).start();
		       		}
		       	}
	        }
	    }
	    return false;
	}
	
	
	
	public void winGame() {
		//beat level won game
		
		findViewById(R.id.levelStatus).setVisibility(View.VISIBLE);
		findViewById(R.id.starRating).setVisibility(View.VISIBLE);
		findViewById(R.id.digitOne).setVisibility(View.VISIBLE);
		findViewById(R.id.digitTwo).setVisibility(View.VISIBLE);
		findViewById(R.id.digitThree).setVisibility(View.VISIBLE);
		findViewById(R.id.heartImage).setVisibility(View.GONE);
		findViewById(R.id.livesDigit).setVisibility(View.GONE);
		findViewById(R.id.zombieBarBack).setVisibility(View.GONE);
		findViewById(R.id.zombieBarOutline).setVisibility(View.GONE);
		findViewById(R.id.levelStatus).bringToFront();
		findViewById(R.id.starRating).bringToFront();
		findViewById(R.id.digitOne).bringToFront();
		findViewById(R.id.digitTwo).bringToFront();
		findViewById(R.id.digitThree).bringToFront();
		
		findViewById(R.id.menuTop).setBackgroundColor(Color.BLACK);
		findViewById(R.id.levelStatus).setBackgroundResource(R.drawable.finished);
		
		
		
		
		SharedPreferences savedData = getSharedPreferences("progress", 0);
        SharedPreferences.Editor editor = savedData.edit();

		if (global.getHumanCount() >= 4*humanList.length/5) {
			//three stars
			findViewById(R.id.starRating).setBackgroundResource(R.drawable.threestar);
			if (getSharedPreferences("progress", 0).getInt(MD5("stars" + currentLevel), 0) <
			(4*currentLevel) + currentLevel*currentLevel)
				editor.putInt(MD5("stars" + currentLevel), (4*currentLevel) + currentLevel*currentLevel);
		} else if (global.getHumanCount() > (2+humanList.length)/3) {
			//two stars
			findViewById(R.id.starRating).setBackgroundResource(R.drawable.twostar);
			if (getSharedPreferences("progress", 0).getInt(MD5("stars" + currentLevel), 0) <
					(3*currentLevel) + currentLevel*currentLevel)
				editor.putInt(MD5("stars" + currentLevel), (3*currentLevel) + currentLevel*currentLevel);
		} else if (global.getHumanCount() > 0) {
			//one star
			findViewById(R.id.starRating).setBackgroundResource(R.drawable.onestar);
			if (getSharedPreferences("progress", 0).getInt(MD5("stars" + currentLevel), 0) <
					(2*currentLevel) + currentLevel*currentLevel)
				editor.putInt(MD5("stars" + currentLevel), (2*currentLevel) + currentLevel*currentLevel);
		} else {
			//no stars
			findViewById(R.id.starRating).setBackgroundResource(0);
			if (getSharedPreferences("progress", 0).getInt(MD5("stars" + currentLevel), 0) <
					currentLevel + currentLevel*currentLevel)
				editor.putInt(MD5("stars" + currentLevel), currentLevel + currentLevel*currentLevel);
		}

		//if (currentLevel + 1 > global.getHighestLevel())
    		//global.setHighestLevel(currentLevel + 1);
        
        //editor.putInt("level", global.getHighestLevel());
        editor.commit();

        global.setLevelComplete(true);
		levelComplete = true;
		canReact = false;
		setLevelNumbers();
	}
	
	


	public void loseGame() {
		if (gameLost == false) {
			if (global.getLives() > 1) {
				findViewById(R.id.digitOne).setVisibility(View.VISIBLE);
				findViewById(R.id.digitTwo).setVisibility(View.VISIBLE);
				findViewById(R.id.digitThree).setVisibility(View.VISIBLE);
				
				findViewById(R.id.levelStatus).setBackgroundResource(R.drawable.failed);
				setLevelNumbers();
			} else {
				findViewById(R.id.levelStatus).setBackgroundResource(R.drawable.gameover);
			}
			findViewById(R.id.levelStatus).setVisibility(View.VISIBLE);
			findViewById(R.id.levelStatus).bringToFront();
			findViewById(R.id.digitOne).bringToFront();
			findViewById(R.id.digitTwo).bringToFront();
			findViewById(R.id.digitThree).bringToFront();
			global.setLives(getSharedPreferences("progress", 0).getInt("lives", 0) - 1);
	        
			for (int k = 0; k < global.getZombieCount(); k ++) {
				((AnimationDrawable) zombieList[k].getDrawable()).stop();
			}
			
			for (int k = 0; k < dogList.length; k ++) {
				((AnimationDrawable) dogList[k].getBackground()).stop();
			}
			
			setLivesImage();
			global.setGameLost(true);
			gameLost = true;
		}
	}
}
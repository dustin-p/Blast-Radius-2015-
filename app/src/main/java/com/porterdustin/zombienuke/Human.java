package com.porterdustin.zombienuke;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.porterdustin.effects.SplatterEffect;

public class Human extends ImageView{
	private Position position;
	private Position start;
	private Weapon weapon;
	private double direction;
	private double speed;
	private float hSpeed;
	private float vSpeed;
	private int shootingDirection;
	private int screenSize;
	private int[] minX = new int[2];
	private int[] maxX = new int[2];
	private int[] minY = new int[2];
	private int[] maxY = new int[2];
	private int id;
	private int runRadius;
	private int hPad;
	private int vPad;
	private int size;
	private boolean dead;
	private boolean panic;
	private boolean facingLeft;
	private AnimationDrawable frameAnimation;
	private GlobalVariables global;
	private Human[] humanList;
	private Zombie[] zombieList;
	private int turnSpeed;
	private boolean hasHelmet;
	private boolean constantPanic;
	private boolean hasExpBelt;
	private boolean hasBlastShield;
	private boolean helmetCracked;
	private boolean hasSpeed;
	private boolean hasCamo;
	private int swingTime;
	private int imageTimer;
	private int mineTimer;

	
	
	public Human(Context context, int screenWidth, int horzPadding, int vertPadding, int count, boolean wideSpread) {
		super(context);
		global = ((GlobalVariables) getContext().getApplicationContext());
		screenSize = screenWidth;
		hPad = horzPadding;
		vPad = vertPadding;
		id = count;
		direction = Math.random()*360;
		setStartingPosition(screenWidth, horzPadding, vertPadding);
		runRadius = (int) (screenWidth/12);
		constantPanic = wideSpread;
		panic = wideSpread;
		minX[0] = hPad + (int)(screenSize/2.2);
		minY[0] = vPad + (int)(screenSize/2.2);
		maxX[0] = hPad + screenSize - (int)(screenSize/2.2);
		maxY[0] = vPad + screenSize - (int)(screenSize/2.2);
		minX[1] = hPad + screenSize/4;
		minY[1] = vPad + screenSize/4;
		maxX[1] = hPad + screenSize - screenSize/4;
		maxY[1] = vPad + screenSize - screenSize/4;
		speed = 1.33 + Math.random()/1.5;
		weapon = null;

		turnSpeed = (int) ((Math.random() < 0.5? -20 : 20));
		setBackgroundResource(R.drawable.human_animation);
		((AnimationDrawable) getBackground()).start();
		setImage((int)direction);
	}
	

	
	public void newRound(int screenWidth, int horzPadding, int vertPadding, int count, boolean wideSpread) {
		screenSize = screenWidth;
		hPad = horzPadding;
		vPad = vertPadding;
		id = count;
		constantPanic = wideSpread;
		panic = wideSpread;
		runRadius = (int) (screenWidth/12);
		setStartingPosition(screenWidth, horzPadding, vertPadding);
		direction = directionTo(screenSize/2, screenSize/2);
		speed = 1.5 + Math.random()/3;
		shootingDirection = -1;
		global = ((GlobalVariables) getContext().getApplicationContext());
		if (hasRangedWeapon())
			global.setHasGuns(true);
		turnSpeed = (int) ((Math.random() < 0.5? -20 : 20));
		setImage((int)direction);
	}
	
	public void revive(float newX, float newY) {
		global.setDeaths(global.getDeaths() - 1);
		global.setHumanCount(global.getHumanCount() + 1);
		global.setZombieCount(global.getZombieCount() - 1);
		global.getZombieList()[global.getZombieCount()].setVisibility(GONE);
		setVisibility(VISIBLE);
		dead = false;
		position.set(newX, newY);
		setX((float) (position.getX() - size/2));
		setY((float) (position.getY() - size/2));
	}
	
	
	public void shoot (int stSteps, Explosion exp, double scale) {
		if (hasRangedWeapon() && shootingDirection != -1) {
			((RangedWeapon) weapon).step(shootingDirection);
		}
	}
	
	
	
	public void move(double scale) {
		position.setX(hSpeed*scale, true);
		position.setY(vSpeed*scale, true);
		setX(position.getX() - size/2);
		setY(position.getY() - size/2);
	}
	
	
	
	public void setDirection() {
		if (hasMeleeWeapon())
			swingTime--;
		if ((position.getX() < minX[panic? 1 : 0] && direction > 90 && direction < 270)
			|| (position.getX() > maxX[panic? 1 : 0] && (direction < 90 || direction > 270))
			|| (position.getY() < minY[panic? 1 : 0] && direction > 180) || (position.getY() > maxY[panic? 1 : 0] && direction < 180)) {
			direction += turnSpeed;
		} else {
			direction += 30 - Math.random()*60;
		}
		while (direction > 360)
			direction -= 360;
		while (direction < 0)
			direction += 360;
		
		setImage();
		if (panic && !constantPanic)
			panic = false;
		hSpeed = (float) (speed*(panic? 1 : 0.5)*Math.cos(Math.toRadians(direction)));
		vSpeed = (float) (speed*(panic? 1 : 0.5)*Math.sin(Math.toRadians(direction)));
	}
	
	
	public void setDirection(int direction) {
		if (!outOfPlay())
			this.direction = direction;
		while (direction > 360)
			direction -= 360;
		while (direction < 0)
			direction += 360;
		setImage();
	}
	
	public void scare() { 
		mineTimer--;
		if (mineTimer < 0 && global.getLandMineCount() < global.getMaxLandMines()) {
			int i = 0;
			for (int k = 0; k < global.getMineList().length; k++) {
				if (!global.getMineList()[k].isReady()) {
					if (i == 0)
						i = k;
					mineTimer = 8;
				} else if (distanceFrom(global.getMineList()[k].getXval(), global.getMineList()[k].getYval())
						< screenSize/16) {
					k = global.getMineList().length;
					mineTimer = -1;
				}
			} if (mineTimer > 0) {
				global.getMineList()[i].drop((int) position.getX(),(int) position.getY());
			}
		}
	}
	
	public void kill(double scale, SplatterEffect guts) {
		weapon = null;
		
		if (guts != null)
			guts.create((int)position.getX(), (int)position.getY(), scale, 0, true);
		if (hasExpBelt)
			new Bomb(getContext(), scale, (int)(scale*320), (int) position.getX(), (int) position.getY());
		hasExpBelt = false;
		
		global.setDeaths(global.getDeaths() + 1);
		global.setHumanCount(global.getHumanCount() - 1);
		setVisibility(GONE);
		dead = true;
	}
	
	public void setImage() {
		if (hasRangedWeapon())
			setImage(shootingDirection);
		else if (imageTimer < 1) {
			if (!facingLeft && direction > 120 && direction < 230) {
				setScaleX(-1);
				facingLeft = true;
				imageTimer = (int) (3 + Math.random()*5);
			} else if (facingLeft && (direction <= 60 || direction >= 300)) {
				setScaleX(1);
				facingLeft = false;
				imageTimer = (int) (3 + Math.random()*5);
			}
		}
	}
	
	
	public void setImage(int facingDirection) {
		
		if (facingDirection > 90 && facingDirection < 270) {
			setScaleX(-1);
			facingLeft = true;
		} else if (facingDirection <= 90 || facingDirection >= 270) {
			setScaleX(1);
			facingLeft = false;
		}
	}
	
	
	
	public void swingAxe () {
		swingTime = 5;
		setImageResource(0);
		setImage((int)direction);
		setImageResource(R.drawable.axe_animation);
		frameAnimation = (AnimationDrawable) this.getDrawable();
		frameAnimation.start();
	}

	public void setShootingDirection() {
		if (global.hasGuns() && !hasMeleeWeapon()) {
			zombieList = global.getZombieList();
			int zCount = global.getZombieCount();
			int minDistance = screenSize/2;
			int zombieToShoot = -1;
			for(int i = 0; i < zCount; i++) {
				if  (!zombieList[i].isInvisible() && distanceFrom((float)zombieList[i].getXpos(),
						(float)zombieList[i].getYpos()) < minDistance) {
					minDistance = distanceFrom((float)zombieList[i].getXpos(), (float)zombieList[i].getYpos());
					zombieToShoot = i;
				}
			}
			if (zombieToShoot != -1) {
				shootingDirection = (int) directionTo(
						(float) zombieList[zombieToShoot].getXpos(), (float) zombieList[zombieToShoot].getYpos());
				
				while (shootingDirection > 360)
					shootingDirection -= 360;
				while (shootingDirection < 0)
					shootingDirection += 360;
				
				setImage(shootingDirection);
			} else
				shootingDirection = -1;
		}
	}
	
	public void setXval(int x) {
		position.setX(x);
	}
	
	public void setYval(int y) {
		position.setY(y);
	}
	
	public double getXval() {
		return position.getX();
	}
	
	public double getYval() {
		return position.getY();
	}
	
	public double directionTo(float x2, float y2) {
		return Math.atan2(y2-position.getY(), x2-position.getX()) * (180 / Math.PI);
	}
	
	public int distanceFrom(float x2, float y2) {
		return (int) Math.sqrt((x2-position.getX())*(x2-position.getX())+(y2-position.getY())*(y2-position.getY()));
	}

	public boolean outOfBounds(double scale) {
		if (position.getX() < hPad + scale*32 || position.getY() < vPad + scale*32 || position.getX() > screenSize + hPad - scale*32 || position.getY() > screenSize + vPad - scale*32)
			return true;
		return false;
	}
	
	public boolean outOfPlay() {
		if (position.getX() < minX[1] || position.getX() > maxX[1] || position.getY() < minY[1] || position.getY() > maxY[1])
			return true;
		return false;
	}

	public boolean isDead() {
		return dead;
	}
	
	public int getId() {
		return id;
	}

	public int getSize() {
		return size;
	}

	public int getRunRadius(double scale) {
		return (int) (runRadius*scale);
	}
	public void setSize(int size) {
		this.size = size;
	}

	public void giveWeapon(Weapon weapon) {
		this.weapon = weapon;
		setImageResource(weapon.getImageId());
		setImage((int)direction);
	}
	
	public void giveExpBelt() {
		setBackgroundResource(R.drawable.expbelt_animation);
		((AnimationDrawable) getBackground()).start();
		hasExpBelt = true;
		hasHelmet = false;
		hasBlastShield = false;
		hasCamo = false;
	}
	
	public void giveBlastShield() {
		setBackgroundResource(R.drawable.blastshield_animation);
		((AnimationDrawable) getBackground()).start();
		hasBlastShield = true;
		hasExpBelt = false;
		hasHelmet = false;
		hasCamo = false;
	}
	
	public void giveSpeed() {
		if (!hasApparel())
			setBackgroundResource(R.drawable.speed_animation);
		
		final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
			@Override
        	   public void run() {
				((AnimationDrawable) getBackground()).start();
			}
        };
        handler.postDelayed(runnable, (int)(Math.random()*450));
		
		speed *= 2.25;
		hasSpeed = true;
	}
	
	public void giveCamo() {
		setBackgroundResource(R.drawable.camo_animation);
		((AnimationDrawable) getBackground()).start();
		hasBlastShield = false;
		hasExpBelt = false;
		hasHelmet = false;
		hasCamo = true;
	}
	
	public void setHelmet(boolean isNew) {
		if (hasHelmet && !isNew) {
			if (helmetCracked) {
				setBackgroundResource(R.drawable.human_animation);
				((AnimationDrawable) getBackground()).start();
				hasHelmet = false;
			} else {
				setBackgroundResource(R.drawable.helmetcracked_animation);
				((AnimationDrawable) getBackground()).start();
				helmetCracked = true;
			}
		} else {
			setBackgroundResource(R.drawable.helmet_animation);
			((AnimationDrawable) getBackground()).start();
			hasHelmet = true;
			hasBlastShield = false;
			hasExpBelt = false;
			helmetCracked = false;
			hasCamo = false;
		}
	}
	
	public boolean hasMeleeWeapon() {
		return (weapon != null && weapon.getType() == Weapon.Type.MELEE);
	}
	
	public boolean hasRangedWeapon() {
		return (weapon != null && weapon.getType() == Weapon.Type.RANGED);
	}

	public boolean hasHelmet() {
		return hasHelmet;
	}
	
	public boolean hasExpBelt() {
		return hasExpBelt;
	}

	public boolean hasBlastShield() {
		return hasBlastShield;
	}
	
	public boolean hasCamo() {
		return hasCamo;
	}
	
	public boolean hasSpeed() {
		return hasSpeed;
	}
	
	public boolean canSwingAxe() {
		return (swingTime <= 0 && hasMeleeWeapon());
	}

	public boolean hasEquipment() {
		return (hasHelmet || hasExpBelt || hasBlastShield || hasWeapon() || hasSpeed || hasCamo);
	}
	
	public boolean hasWeapon() {
		return (weapon != null);
	}

	public boolean hasApparel() {
		return (hasHelmet || hasExpBelt || hasBlastShield || hasCamo);
	}
	
	public void panic(boolean shouldPanic) {
		panic = shouldPanic || constantPanic;
	}

	public void decreaseImageTimer() {
		imageTimer--;
	}

	private void setStartingPosition(int screenWidth, int horzPadding, int vertPadding) {
		start = new Position(horzPadding + 2*screenWidth/5 + Math.random()*screenWidth/5,
				vertPadding + 2*screenWidth/5 + Math.random()*screenWidth/5, 1);
		position = new Position(start);
		setX(position.getX());
		setY(position.getY());
	}


	public Position getPosition() {
		return position;
	}

	public boolean isPanicing() {
		return panic;
	}
}


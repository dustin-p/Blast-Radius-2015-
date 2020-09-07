package com.porterdustin.zombienuke;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;

import com.porterdustin.effects.SplatterEffect;

public class Zombie extends ImageView{
	private Position position;
	private float x;
	private float y;
	private double direction;
	private double speed;
	private int xStart;
	private int yStart;
	private int size;
	private int maxX;
	private int color;
	private int hPad;
	private int vPad;
	private int startingDistance;
	private float smartChance;
	private float fastChance;
	private float strongChance;
	private float invisibleChance;
	private boolean smart;
	private boolean fast;
	private boolean invisible;
	private boolean facingLeft;
	private boolean canCure;
	private int food;
	private int health;
	private int minDistance;
	private float hSpeed;
	private float vSpeed;
	private GlobalVariables global;
	private Human[] humanList;
	private Zombie[] zombieList;
	private int distanceToHuman;
	
	
	public Zombie(Context context, float startSpeed, float smart, float fast, float strong,
			float invisible, int screenX, int horzPadding, int vertPadding) {
		super(context);
		speed = (float) (startSpeed + Math.random()/3);
		hPad = horzPadding;
		vPad = vertPadding;
		maxX = screenX;
		minDistance = maxX;
		setX(-32);
		setY(-32);
		position = new Position(-32,-32);
		direction = 0;
		startingDistance = (int) (Math.sqrt(2*(maxX*maxX))/1.95);
		smartChance = smart;
		fastChance = fast;
		strongChance = strong;
		invisibleChance = invisible;
		global = ((GlobalVariables) getContext().getApplicationContext());
		facingLeft = true;
		setColorFilter(color, Mode.MULTIPLY);
		setImage();
		hSpeed = (float) (speed*Math.cos(Math.toRadians(direction)));
		vSpeed = (float) (speed*Math.sin(Math.toRadians(direction)));
		setBackgroundResource(R.drawable.zombie_a0);
		setImageResource(R.drawable.zombiea_animation);
		setVisibility(GONE);
	}
	
	
	public void initialize() {
		zombieList = global.getZombieList();
		humanList = global.getHumanList();
	}
	
	public void move(double scale){
		setX((float) (x += hSpeed*scale) - size/2);
		setY((float) (y += vSpeed*scale) - size/2);
		position.set(x, y);
	}
	
	
	public void setDirection() {
		if (smart && global.getHumanCount() > 0) {
			setDirectionSmart();
		} else {
			if (outOfPlay()) {
				direction = directionTo(hPad + maxX/2, vPad + maxX/2);
			} else {
				direction += 5 - Math.random()*10;
			}
			hSpeed = (float) ((fast? smart? 1.75: 2 : 1)*speed*Math.cos(Math.toRadians(direction)));
			vSpeed = (float) ((fast? smart? 1.75: 2 : 1)*speed*Math.sin(Math.toRadians(direction)));
		}
		while (direction > 360)
			direction -= 360;
		while (direction < 0)
			direction += 360;
		setImage();
	}
	
	
	
	public void setDirectionSmart() {
		if (Math.random() < 0.1 || humanList[food].isDead()) {
			minDistance = maxX;
			for (int i = 0; i < humanList.length; i++) {
				if  (!humanList[i].isDead() &&
						distanceFrom(humanList[i].getXval(), humanList[i].getYval()) < minDistance) {
					minDistance = distanceFrom(humanList[i].getXval(), humanList[i].getYval());
					food = i;
				}
			}
		}
		direction = directionTo((int) humanList[food].getXval(), (int) humanList[food].getYval());
		hSpeed = (float) ((fast? smart? 1.75: 2 : 1)*speed*Math.cos(Math.toRadians(direction)));
		vSpeed = (float) ((fast? smart? 1.75: 2 : 1)*speed*Math.sin(Math.toRadians(direction)));
	}
	
	
	public void infect (double scale) {
		zombieList = global.getZombieList();
		if (!outOfPlay())
		for (int i = 0; i < humanList.length; i++) {
			if (!humanList[i].isDead()) {
				distanceToHuman = distanceFrom(humanList[i].getXval(),humanList[i].getYval());
				
				if (distanceToHuman < scale*40) {
					if (!humanList[i].canSwingAxe()) {
						if (humanList[i].hasExpBelt()) {
							humanList[i].kill(scale, null);
						} else {
							global.setInfections(global.getInfections() + 1);
							humanList[i].kill(scale, new SplatterEffect(getContext()));
							global.setKillsNeeded(global.getKillsNeeded() + 1);
							zombieList[global.getZombieCount()].replaceHuman((int)humanList[i].getXval(),
									(int)humanList[i].getYval());
							global.setZombieCount(global.getZombieCount() + 1);
							food = (int) (Math.random()*humanList.length);
						}
						
						
					} else {
						if (!outOfBounds()) {
							humanList[i].setDirection((int)humanList[i].directionTo((int)x, (int)y));
							humanList[i].swingAxe();
							humanList[i].panic(true);
							while (health > 0 && Math.random() < 0.75)
								health--;
							kill(scale);
						}
					}
				} else if (distanceToHuman < maxX/(7.5 - (speed*1.5))) {
					if (invisible && !fast) {
						setImageResource(R.drawable.zombied_animation);
						setColorFilter(color, Mode.MULTIPLY);
						((AnimationDrawable) getDrawable()).start();
						setDirectionSmart();
						speed *= 1.2;
						hSpeed *= 1.2;
						vSpeed *= 1.2;
						fast = true;
					}
					if (!humanList[i].isPanicing() && !humanList[i].hasMeleeWeapon() || Math.random() < 0.2) {
						humanList[i].setDirection((int)directionTo((int)humanList[i].getXval(), (int)humanList[i].getYval()));
						humanList[i].panic(true);
						humanList[i].scare();
					}
				} 
			}
		}
	}

	public double directionTo(int x2, int y2) {
		return Math.atan2(y2-y, x2-x) * (180 / Math.PI);
	}
	
	public int distanceFrom(double x2, double y2) {
		return (int) Math.sqrt((x2-x)*(x2-x)+(y2-y)*(y2-y));
	}
	
	public void changeSpeed (double newSpeed) {
		speed += newSpeed;
	}
	
	public void addInPlay(double scale, boolean firstStart) {
		humanList = global.getHumanList();
		
		smart = Math.random() < smartChance;
		fast = Math.random() < fastChance;
		invisible = Math.random() < invisibleChance;
		health = (Math.random() < strongChance)? 1 + (Math.random() < strongChance? 1 : 0) : 0;
		canCure = false;
		
		if (fast) {
			health = 0;
			invisible = false;
		}else if (invisible) {
			smart = true;
		}
		
		if (global.getKills() + global.getZombieCount() > global.getKillsNeeded()) {
			xStart = -512;
			yStart = -512;
			speed = 0;
		} else if (firstStart) {
			xStart = Math.random() < 0.5? hPad - size*2 : hPad + maxX + size*2;
			yStart = (int) (vPad + Math.random()*maxX);
			
		} else {
			/*
			double startDirection = 360*Math.random();
			xStart = (int) (hPad + maxX/2 + startingDistance*Math.cos(startDirection));
			yStart = (int) (vPad + maxX/2 + startingDistance*Math.sin(startDirection));
			*/
			if (Math.random() < 0.5) {
				xStart = Math.random() < 0.5? hPad - size : hPad + maxX + size;
				yStart = (int) (vPad + Math.random()*maxX);
			} else {
				xStart = (int) (hPad + Math.random()*maxX);
				yStart = Math.random() < 0.5? vPad - size : vPad + maxX + size;
			}
		}
		
		x = xStart;
		y = yStart;
		setX((float) x - size/2);
		setY((float) y - size/2);
		position.set(x, y);
		
		setColor();
		setImage();
		
		if (invisible) {
			setImageResource(R.drawable.human_animation);
		} else if (health > 0) {
			if (health == 1)
				setImageResource(R.drawable.zombiec_animation);
			else
				setImageResource(R.drawable.zombieb_animation);
		} else if (fast)
			setImageResource(R.drawable.zombied_animation);
		else 
			setImageResource(R.drawable.zombiea_animation);
		if (Math.random() < 0.25)
			((AnimationDrawable) getDrawable()).stop();
		((AnimationDrawable) getDrawable()).start();
		setVisibility(VISIBLE);
	}
	
	
	
	public int getColor() {
		return color;
	}
	
	
	private void setColor() {
		color = Color.HSVToColor( new float[]{ 80 + (float)(Math.random()*64),
				0.15f + (float)(Math.random()*0.2), 0.7f + (float)(Math.random()*0.1)} );
		if (health > 0) {
			color = Color.HSVToColor( new float[]{8f + (float)(Math.random()*32), 0.2f, 0.6f } );
		} else if (fast) {
			color = Color.HSVToColor( new float[]{160f + (float)(Math.random()*32), 0.2f, 0.7f } );
		}
		
		if (invisible) {
			color = Color.rgb(245,218,198);
			setColorFilter(Color.WHITE, Mode.MULTIPLY);
		} else { 
			setColorFilter(color, Mode.MULTIPLY);
		}
	}

	public void paint(int color) {
			this.color = color;
			if (invisible) {
				facingLeft = !facingLeft;
				invisible = false;
				setImage();
			}
			global.getZombiePopList()[global.getPopCycle()].create(size, (int)x, (int)y, color, facingLeft? -1 : 1);
			global.setPopCycle();
			setColorFilter(color, Mode.MULTIPLY);
	}
	
	public void setImage() {
		if (!facingLeft && direction > 90 && direction < 270) {
			setScaleX(-1);
			facingLeft = true;
		}
		if (facingLeft && direction <= 90 || direction >= 270){
			setScaleX(1);
			facingLeft = false;
		}
	}
	
	public void setHealth(int health) {
		this.health = health;
	}

	public void hit(double scale) {
		kill(scale);

	}

	public void kill(double scale) {
		global.getZombiePopList()[global.getPopCycle()].create(size, (int)x, (int)y, color, facingLeft? -1 : 1);
		global.setPopCycle();
		if (health == 0) {
			if (invisible) {
				speed *= fast? 0.83 : 1;
			}
			global.setKills(global.getKills() + 1);
			addInPlay(scale, false);
		} else {
			health--;
			if (health == 1)
				setImageResource(R.drawable.zombiec_animation);
			else if (health > 0)
				setImageResource(R.drawable.zombieb_animation);
			else
				setImageResource(R.drawable.zombiea_animation);
			((AnimationDrawable) getDrawable()).start();
		}
	}
	
	public void cure () {
		humanList = global.getHumanList();
		for (int k = 0; k < humanList.length; k++) {
			if (humanList[k].isDead()) {
				zombieList = global.getZombieList();
				humanList[k].revive(x, y);
				this.switchWith(zombieList[global.getZombieCount()]);
				global.getExplosion().killCharacters(false);
				global.setKills(global.getKills() + 1);
				if (global.getExpCycle() > 0)
					global.getBoomList()[global.getExpCycle() - 1]
							.getBackground().setColorFilter(Color.argb(160, 160, 224, 176), Mode.MULTIPLY);
				else
					global.getBoomList()[global.getBoomList().length - 1]
							.getBackground().setColorFilter(Color.argb(160, 160, 224, 176), Mode.MULTIPLY);
				k = humanList.length;
			}
		}
	}
	
	private void switchWith(Zombie zombie) {
		zombie.setVisibility(GONE);
		this.x = zombie.x;
		this.y = zombie.y;
		setX(x - size/2);
		setY(y - size/2);
		this.xStart = zombie.xStart;
		this.yStart = zombie.yStart;
		this.health = zombie.health;
		this.fast = zombie.fast;
		this.smart = zombie.smart;
		this.color = zombie.color;
		this.invisible = zombie.invisible;
		this.speed = zombie.speed;
		this.canCure = zombie.canCure;
		zombie.canCure = false;
		
		if (invisible) {
			setImageResource(R.drawable.human_animation);
		} else if (health > 0) {
			if (health == 1)
				setImageResource(R.drawable.zombiec_animation);
			else
				setImageResource(R.drawable.zombieb_animation);
		} else if (fast)
			setImageResource(R.drawable.zombied_animation);
		else 
			setImageResource(R.drawable.zombiea_animation);
		if (Math.random() < 0.25)
			((AnimationDrawable) getDrawable()).stop();
		((AnimationDrawable) getDrawable()).start();
		if (zombie != global.getZombieList()[global.getZombieCount()])
			setVisibility(VISIBLE);
	}


	private void replaceHuman(int xVal, int yVal) {
		double startDirection = 360*Math.random();
		xStart = (int) (hPad + maxX/2 + startingDistance*Math.cos(startDirection));
		yStart = (int) (vPad + maxX/2 + startingDistance*Math.sin(startDirection));
		x = xVal;
		y = yVal;
		setX(x - size/2);
		setY(y - size/2);
		
		global.getInfectFxList()[global.getInfectCycle()].create((int)(size*1.5), (int)x, (int)y);
		global.setInfectCycle();
		
		invisible = false;
		smart = Math.random() < smartChance;
		health = 0;
		fast = false;
		canCure = global.isCurable();
		
		direction = Math.random()*360;
		if (direction > 90 && direction < 270)
			facingLeft = false;
		else
			facingLeft = true;
		hSpeed = (float) (speed*Math.cos(Math.toRadians(direction)));
		vSpeed = (float) (speed*Math.sin(Math.toRadians(direction)));
		color = Color.rgb(224,250,192);
		setColorFilter(color, Mode.MULTIPLY);
		setImage();
		setImageResource(R.drawable.zombiea_animation);
		((AnimationDrawable) getDrawable()).start();
		setVisibility(View.VISIBLE);
	}

	public boolean outOfBounds() {
		return (x < -size/2 + hPad || y < -size/2 + vPad || x > maxX + size/2 + hPad|| y > maxX + size/2 + vPad);
	}
	
	public boolean outOfPlay() {
		return (x < size + hPad || y < size + vPad || x > maxX - size + hPad|| y > maxX - size + vPad);
	}

	public boolean isInvisible() {
		return invisible;
	}
	
	public boolean isCurable() {
		return canCure;
	}
	
	public void setXPos(int newX) {
		x = newX;
		
	}

	public void setYPos(int newY) {
		y = newY;
	}

	public double getXpos() {
		return x;
	}
	
	public double getYpos() {
		return y;
	}

	public int getHealth() {
		return health;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public double getSpeed() {
		return speed;
	}
	
	public void setSmart(boolean isSmart) {
		smart = isSmart;
	}

	public void setSmartChance(float newChance) {
		smartChance = newChance;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

}

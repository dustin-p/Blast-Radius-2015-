package com.porterdustin.zombienuke;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.view.View;
import android.view.ViewManager;
import android.widget.RelativeLayout;


public class PaintBall extends View{

	private int size;
	private int x;
	private int y;
	private int direction;
	private int speed;
	private float hSpeed;
	private float vSpeed;
	private Zombie[] zombieList;
	private GlobalVariables global;
	private int color;
	
	public PaintBall(Context context, int xStart, int yStart, int targetDirection, final int hPad, final int vPad, final int maxX,
			final double scale, final Explosion exp) {
		super(context);
		setBackgroundResource(R.drawable.paintball);
		color = Color.HSVToColor( new float[]{(float)(Math.random()*360), 1f, 1f } );
		x = xStart;
		y = yStart;
		speed = 28;
		size = (int) (16*scale);
		direction = targetDirection;
		hSpeed = (float) (scale*speed*Math.cos(Math.toRadians(direction)));
		vSpeed = (float) (scale*speed*Math.sin(Math.toRadians(direction)));
		setX(-size);
		setY(-size);
		global = ((GlobalVariables) getContext().getApplicationContext());
		this.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
		getBackground().setColorFilter(color, Mode.MULTIPLY);
		
		
		
		final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
        	int k;
			@Override
        	   public void run() {
        		   if (!global.getGamePaused()) {
        			   zombieList = global.getZombieList();
	        		   setX(x += hSpeed);
	        		   setY(y += vSpeed);
	        		   for (k = 0; k < global.getZombieCount(); k++) {
	        			   if (zombieList[k].distanceFrom(x, y) < scale*48) {
	        				   zombieList[k].paint(color);
	        				   x = -hPad;
	        				   y = -vPad;
	        				   k = zombieList.length + 1;
	        			   }
	        		   }
	        	   }
        		   if (x < hPad + hSpeed || x > hPad + maxX - hSpeed|| y < vPad - vSpeed
        				   || y > vPad + maxX - vSpeed) {
        			   destroy();
        		   } else {
        			   handler.postDelayed(this, 10);
        		   }
        	}
        };
        handler.postDelayed(runnable, 1);
	}
	
	public void destroy() {
		global = null;
		zombieList = null;
		((ViewManager)this.getParent()).removeView(this);
	}
	

}

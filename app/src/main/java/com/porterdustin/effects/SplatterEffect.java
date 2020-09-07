package com.porterdustin.effects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.porterdustin.zombienuke.R;

public class SplatterEffect extends ImageView {
	int size;
	int fadeAway = 0;
	int fadeTime;
	int alphaVal = 255;
	int redVal;
	int greenVal;
	int blueVal;
	int xVal;
	int yVal;
	
	public SplatterEffect(Context context) {
		super(context);
		setImageResource(R.drawable.splatter1);
		
		setVisibility(GONE);
		
		/*final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
        	   @Override
        	   public void run() {
        		   if (getVisibility() == VISIBLE) {
        			   if (fadeAway > fadeTime) {
	        			   alphaVal -= 12;
	        			   setColorFilter(Color.argb(alphaVal, redVal, greenVal, blueVal), Mode.MULTIPLY);
	        			   if (alphaVal < 32)
	        			   destroy();
        			   } else {
    	        		   fadeAway++;
            		   }
        		   }
        		   handler.postDelayed(this, 50);
        	   }
        };
        handler.postDelayed(runnable, 5000);*/
	}
	public void create(int x, int y, double scale, int color, boolean isHuman) {
		fadeAway = 0;
		
		if (isHuman) {
			size = (int) (scale*(128 + Math.random()*32));
			alphaVal = 224;
			redVal = 224;
			greenVal = 96;
			blueVal = 96;
			fadeTime = (int) 24;
		} else {
			size = (int) (scale*(96 + Math.random()*96));
			alphaVal = (int) (160 + Math.random()*32);
			redVal = (int) (Color.red(color)/1.3);
			greenVal = (int) (Color.green(color)/1.3);
			blueVal = (int) (Color.blue(color)/1.3);
			fadeTime = 0;
		}
		setColorFilter(Color.argb(alphaVal, redVal, greenVal, blueVal), Mode.MULTIPLY);
		setVisibility(VISIBLE   );
		xVal = x;
		yVal = y;
		this.setLayoutParams(new RelativeLayout.LayoutParams(size, (int) (size/1.3)));
		setX(xVal - size/2);
		setY(yVal - size/2);
	}
	
	private void destroy() {
		setVisibility(GONE);
	}

}

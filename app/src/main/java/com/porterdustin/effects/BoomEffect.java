package com.porterdustin.effects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.porterdustin.zombienuke.R;

public class BoomEffect extends ImageView{
	AnimationDrawable frameAnimation;
	
	public BoomEffect(Context context) {
		super(context);
		setBackgroundResource(R.drawable.explode_animation);
		setVisibility(INVISIBLE);
	}
	
	public void create(int size, int x, int y) {
		setX(x-size);
		setY(y-size);
		setScaleX(Math.random() < 0.5? -1.5f : 1.5f);
		setScaleY(1.35f);
		setLayoutParams(new RelativeLayout.LayoutParams(size*2, size*2));
		setVisibility(VISIBLE);
		getBackground().setColorFilter(Color.WHITE, Mode.MULTIPLY);
		frameAnimation = (AnimationDrawable) getBackground();
		frameAnimation.start();
		
		final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
			@Override
        	   public void run() {
				frameAnimation = null;
				((AnimationDrawable) getBackground()).stop();
				setVisibility(INVISIBLE);
			}
        };
        handler.postDelayed(runnable, 815);
        
	}
}

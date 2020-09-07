package com.porterdustin.effects;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.porterdustin.zombienuke.R;

public class ZombiePopEffect extends ImageView{
	boolean done = false;
	AnimationDrawable frameAnimation;
	
	public ZombiePopEffect(Context context) {
		super(context);
		setImageResource(R.drawable.zpopa_animation);
		frameAnimation = (AnimationDrawable) getDrawable();
		setVisibility(GONE);
	}
	
	public void create(int size, int x, int y, int color, int xScale) {
		setX(x-size/2);
		setY(y-size/2);
		setColorFilter(color, Mode.MULTIPLY);
		this.setLayoutParams(new RelativeLayout.LayoutParams(size, size));
		frameAnimation.start();
		setScaleX(xScale);
		setVisibility(VISIBLE);

		final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
        	@Override
        	public void run() {
        		destroy();
        	}
        };
        handler.postDelayed(runnable, 190);
	}
	public void destroy() {
		setVisibility(GONE);
	}
}

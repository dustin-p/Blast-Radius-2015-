package com.porterdustin.effects;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.porterdustin.zombienuke.R;

public class PaperRipEffect extends ImageView{
	AnimationDrawable frameAnimation;
	
	public PaperRipEffect(Context context) {
		super(context);
		setBackgroundResource(R.drawable.paperhole_animation);
		frameAnimation = (AnimationDrawable) getBackground();
		setVisibility(GONE);
	}
	
	public void create(int size, int x, int y) {
		setX(x-size);
		setY(y-size);
		this.setLayoutParams(new RelativeLayout.LayoutParams(size*2, size*2));
		
		setScaleX(Math.random() < 0.5? -1 : 1);
		setScaleY(Math.random() < 0.5? -1 : 1);
		setVisibility(VISIBLE);
		frameAnimation.start();
		
		final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
			@Override
        	   public void run() {
				setVisibility(GONE);
			}
        };
        handler.postDelayed(runnable, 1700);
		
	}
}

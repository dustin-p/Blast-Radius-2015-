package com.porterdustin.zombienuke;

import android.content.Context;
import android.widget.RelativeLayout;

public class Bullet extends Projectile{
	
	public Bullet(Context context, double speed) {
		super(context);
		int size = 16;
		setBackgroundResource(R.drawable.bullet);
		setLayoutParams(new RelativeLayout.LayoutParams(size, size));
		setSize(size);
		setSpeed(speed);
	}
}

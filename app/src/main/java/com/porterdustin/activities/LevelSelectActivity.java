
package com.porterdustin.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.porterdustin.zombienuke.Level;
import com.porterdustin.zombienuke.R;

public class LevelSelectActivity extends Activity {
	private int screenHeight;
	private int levelValue;
	private int maxLevel;
	private int[] numbersList = {
		R.drawable.zero1, R.drawable.one1, R.drawable.two1, R.drawable.three1, R.drawable.four1,
		R.drawable.five1, R.drawable.six1, R.drawable.seven1, R.drawable.eight1, R.drawable.nine1
	};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelselect);
        
        maxLevel = 150;
        screenHeight = getScreenHeight();    
        levelValue = maxLevel;
        
        
        setLayoutSizes();
        setDigits();
        setPreview();
        setStars();
    }

    @Override
    protected void onRestart() {
    	super.onRestart();
    	this.finish();
		this.overridePendingTransition(0, 0);
    }
    
    public void setDigits() {
    	ImageView digit1 = (ImageView) findViewById(R.id.digitOne);
    	ImageView digit2 = (ImageView) findViewById(R.id.digitTwo);
    	ImageView digit3 = (ImageView) findViewById(R.id.digitThree);
    	
    	digit1.setTag(levelValue/100);
    	digit1.setImageResource(numbersList[(Integer) digit1.getTag()]);
    	digit1.setColorFilter(Color.WHITE);
    	digit2.setTag((levelValue/10) % 10);
    	digit2.setImageResource(numbersList[(Integer) digit2.getTag()]);
    	digit2.setColorFilter(Color.WHITE);
    	digit3.setTag(levelValue % 10);
    	digit3.setImageResource(numbersList[(Integer) digit3.getTag()]);
    	digit3.setColorFilter(Color.WHITE);
    }
    
    
    public void changeNumber (View v) {
    	if (v.getId() == R.id.arrowOneUp) {
    		levelValue += 100;
    	}
    	if (v.getId() == R.id.arrowTwoUp) {
    		levelValue += 10;
    	}
    	if (v.getId() == R.id.arrowThreeUp) {
    		levelValue += 1;
    	}
    	if (v.getId() == R.id.arrowOneDown) {
    		levelValue -= 100;
    	}
    	if (v.getId() == R.id.arrowTwoDown) {
    		levelValue -= 10;
    	}
    	if (v.getId() == R.id.arrowThreeDown) {
    		levelValue -= 1;
    	}
    	
    	if (levelValue > maxLevel)
    		levelValue = maxLevel;
    	if (levelValue > 999)
    		levelValue = 999;
    	if (levelValue < 1)
    		levelValue = 1;
    	
    	setPreview();
    	setDigits();
    	setStars();
    }
    
    public void setStars() {
    	if (getSharedPreferences("progress", 0).getInt(MD5("stars" + levelValue), 0) == 0 ||
    	getSharedPreferences("progress", 0).getInt(MD5("stars" + levelValue), 0) == levelValue + levelValue*levelValue) {
    		findViewById(R.id.starRating).setBackgroundResource(0);
    	}
    	if (getSharedPreferences("progress", 0).getInt(MD5("stars" + levelValue), 0) == 2*levelValue + levelValue*levelValue) {
    		findViewById(R.id.starRating).setBackgroundResource(R.drawable.onestar);
    	}
    	if (getSharedPreferences("progress", 0).getInt(MD5("stars" + levelValue), 0) == 3*levelValue + levelValue*levelValue) {
    		findViewById(R.id.starRating).setBackgroundResource(R.drawable.twostar);
    	}
    	if (getSharedPreferences("progress", 0).getInt(MD5("stars" + levelValue), 0) == 4*levelValue + levelValue*levelValue) {
    		findViewById(R.id.starRating).setBackgroundResource(R.drawable.threestar);
    	}

    }
    
    public void setPreview() {
    	((ImageView) findViewById(R.id.levelBack)).setBackgroundResource(new Level(levelValue).getBackground());
    	((ImageView) findViewById(R.id.levelBack)).getBackground()
    	.setColorFilter(new Level(levelValue).getBackgroundColor(), Mode.MULTIPLY);
    	if (levelValue % 2 == 0)
    		findViewById(R.id.levelBack).setScaleX(-1);
    	else
    		findViewById(R.id.levelBack).setScaleX(1);
    }
    
    public void setLayoutSizes() {
    	LayoutParams stars = findViewById(R.id.starRating).getLayoutParams();
		stars.height = screenHeight/8;
		stars.width = screenHeight/3;
		findViewById(R.id.starRating).setLayoutParams(stars);
		((AnimationDrawable) ((ImageView) findViewById(R.id.starRating)).getDrawable()).start();
	
		
		LayoutParams levelBack = findViewById(R.id.levelBack).getLayoutParams();
		levelBack.height = 5*screenHeight/14;
		levelBack.width = 5*screenHeight/14;
		findViewById(R.id.levelBack).setLayoutParams(levelBack);
		((AnimationDrawable) ((ImageView) findViewById(R.id.levelBack)).getDrawable()).start();
		((AnimationDrawable) ((ImageView) findViewById(R.id.playLevel)).getBackground()).start();
		
		
		LayoutParams levelNum = findViewById(R.id.levelNumber).getLayoutParams();
		levelNum.height = screenHeight/6;
		levelNum.width = 9*screenHeight/4;
		findViewById(R.id.levelNumber).setLayoutParams(levelNum);
		
		LayoutParams digit1 = findViewById(R.id.digitOne).getLayoutParams();
		LayoutParams digit2 = findViewById(R.id.digitTwo).getLayoutParams();
		LayoutParams digit3 = findViewById(R.id.digitThree).getLayoutParams();
		digit1.height = screenHeight/8;
		digit2.height = screenHeight/8;
		digit3.height = screenHeight/8;
		digit1.width = screenHeight/8;
		digit2.width = screenHeight/8;
		digit3.width = screenHeight/8;
		findViewById(R.id.digitOne).setLayoutParams(digit1);
		findViewById(R.id.digitTwo).setLayoutParams(digit2);
		findViewById(R.id.digitThree).setLayoutParams(digit3);
		
		
		LayoutParams arrow1 = findViewById(R.id.arrowOneUp).getLayoutParams();
		LayoutParams arrow2 = findViewById(R.id.arrowTwoUp).getLayoutParams();
		LayoutParams arrow3 = findViewById(R.id.arrowThreeUp).getLayoutParams();
		LayoutParams arrow4 = findViewById(R.id.arrowOneDown).getLayoutParams();
		LayoutParams arrow5 = findViewById(R.id.arrowTwoDown).getLayoutParams();
		LayoutParams arrow6 = findViewById(R.id.arrowThreeDown).getLayoutParams();
		arrow1.height = screenHeight/12;
		arrow2.height = screenHeight/12;
		arrow3.height = screenHeight/12;
		arrow4.height = screenHeight/12;
		arrow5.height = screenHeight/12;
		arrow6.height = screenHeight/12;
		arrow1.width = screenHeight/12;
		arrow2.width = screenHeight/12;
		arrow3.width = screenHeight/12;
		arrow4.width = screenHeight/12;
		arrow5.width = screenHeight/12;
		arrow6.width = screenHeight/12;
		findViewById(R.id.arrowOneUp).setLayoutParams(arrow1);
		findViewById(R.id.arrowTwoUp).setLayoutParams(arrow2);
		findViewById(R.id.arrowThreeUp).setLayoutParams(arrow3);
		findViewById(R.id.arrowOneDown).setLayoutParams(arrow4);
		findViewById(R.id.arrowTwoDown).setLayoutParams(arrow5);
		findViewById(R.id.arrowThreeDown).setLayoutParams(arrow6);
		
		if (maxLevel < 100) {
			findViewById(R.id.arrowOneUp).setVisibility(View.GONE);
			findViewById(R.id.digitOne).setVisibility(View.GONE);
			findViewById(R.id.arrowOneDown).setVisibility(View.GONE);
			
			findViewById(R.id.digitTwo).setX(-screenHeight/16);
			findViewById(R.id.digitThree).setX(-screenHeight/16);
			findViewById(R.id.arrowTwoUp).setX(-screenHeight/16);
			findViewById(R.id.arrowThreeUp).setX(-screenHeight/16);
			findViewById(R.id.arrowTwoDown).setX(-screenHeight/16);
			findViewById(R.id.arrowThreeDown).setX(-screenHeight/16);
		}
    }
    
    public void play(View v) {
    	
    	final Intent playGame = new Intent(this, GameActivity.class);
    	final View startButton = v;
    	playGame.putExtra("level", levelValue);
    	
    	startButton.setBackgroundResource(R.drawable.pframe4);
    	
    	final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
        	@Override
        	public void run() {
        		//startButton.setBackgroundResource(R.drawable.pframe5);
        		finish();
        		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            	startActivity(playGame);
        	}
        };
        handler.postDelayed(runnable, 1);
    	
    	
    	
    	
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
}

package com.porterdustin.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.porterdustin.zombienuke.GlobalVariables;
import com.porterdustin.zombienuke.R;

public class MainActivity extends Activity {
	int levelSelected = 0;
	private GlobalVariables global;
	private int screenWidth;
	private int screenHeight;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        global = ((GlobalVariables) this.getApplication());
        //global.setHighestLevel(getSharedPreferences("progress", 0).getInt("level", 1));
        screenWidth = getScreenWidth();
        screenHeight = getScreenHeight();
        
        setLayout();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	setButtons();
    	
    	if (getSharedPreferences("progress", 0).getInt("lives", 0) <= 0) {
    		//gameOver or new game
        	global.setLives(4);
    	} else {
        	global.setLives(getSharedPreferences("progress", 0).getInt("lives", 0));
    	}
        
    }

    public void setButtons() {
    	findViewById(R.id.levelSelectBtn).setBackgroundResource(R.drawable.btn_levelselect);
    	findViewById(R.id.armoryBtn).setBackgroundResource(R.drawable.btn_armory);
    	findViewById(R.id.leaderboardBtn).setBackgroundResource(R.drawable.btn_leaderboard);
    	findViewById(R.id.achievementBtn).setBackgroundResource(R.drawable.btn_achievements);
    }
    
    public void setLayout() {
    	LayoutParams mainPic = findViewById(R.id.mainMenuImage).getLayoutParams();
		mainPic.width = screenWidth;
		mainPic.height = 27*screenHeight/80;
		findViewById(R.id.mainMenuImage).setLayoutParams(mainPic);
		
		findViewById(R.id.levelSelectBtn).setY(screenHeight/24);
		findViewById(R.id.armoryBtn).setY(43*screenHeight/240);
		findViewById(R.id.achievementBtn).setY(19*screenHeight/60);
		findViewById(R.id.leaderboardBtn).setY(109*screenHeight/240);
		findViewById(R.id.helpBtn).setY(71*screenHeight/120);
		
		LayoutParams btn1 = findViewById(R.id.levelSelectBtn).getLayoutParams();
		LayoutParams btn2 = findViewById(R.id.armoryBtn).getLayoutParams();
		LayoutParams btn3 = findViewById(R.id.achievementBtn).getLayoutParams();
		LayoutParams btn4 = findViewById(R.id.leaderboardBtn).getLayoutParams();
		LayoutParams btn5 = findViewById(R.id.helpBtn).getLayoutParams();
		btn1.width = screenHeight/2;
		btn1.height = screenHeight/8;
		btn2.width = screenHeight/2;
		btn2.height = screenHeight/8;
		btn3.width = screenHeight/2;
		btn3.height = screenHeight/8;
		btn4.width = screenHeight/2;
		btn4.height = screenHeight/8;
		btn5.width = screenHeight/2;
		btn5.height = screenHeight/8;
		
    }
    
    
    public void levelSelect (View v) {
    	final Intent selectLevel = new Intent(this, LevelSelectActivity.class);
    	v.setBackgroundResource(R.drawable.btn_levelselect_clicked);
    	
    	final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
        	@Override
        	public void run() {
            	startActivity(selectLevel);
            	overridePendingTransition(0, 0);
        	}
        };
        handler.postDelayed(runnable, 1);

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

package com.porterdustin.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.porterdustin.zombienuke.R;
import com.porterdustin.zombienuke.PowerUpGenerator;

public class PreGameActivity extends Activity {
	private int userClicked = 0;
	private int screenHeight;
	private int[] powerUpImageList = {R.drawable.pud0, R.drawable.pud1, R.drawable.pud2, R.drawable.pud3, R.drawable.pud4,
			0, R.drawable.pud6, R.drawable.pud7, R.drawable.pud8, R.drawable.pud9, R.drawable.pud10,
			R.drawable.pud11, R.drawable.pud12, R.drawable.pud13, R.drawable.pud14 };
	private int[] powerUpList;
	private int[] emptyList = {0, 0, 0, 0};
	private int thisLevel;
	private int levelTeir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregame);

        screenHeight = getScreenHeight();
        setLayout();
        setButtons();

        Intent intent = getIntent();
        thisLevel = intent.getIntExtra("level", 0);
        levelTeir = intent.getIntExtra("teir", 0);
    }

    @Override
    protected void onRestart() {
    	super.onRestart();
    	Intent result = new Intent();
    	Bundle selection = new Bundle();
    	selection.putIntArray("powerUpList", emptyList);
    	result.putExtras(selection);
    	setResult(Activity.RESULT_CANCELED, result);
    	this.finish();
		this.overridePendingTransition(0, 0);
    }

    public void setDescription(int choice) {
    	((ImageView) findViewById(R.id.descriptionBack)).setImageResource(0);

    	powerUpList = new PowerUpGenerator().getPowerUpList(thisLevel, choice - 1, levelTeir);

    	if (choice == 4) {
    		findViewById(R.id.powerUp1).setBackgroundResource(R.drawable.pud_none);
    		findViewById(R.id.powerUp2).setBackgroundResource(R.drawable.pud0);
    		findViewById(R.id.powerUp3).setBackgroundResource(R.drawable.pud0);
    	} else if (powerUpList[1] != -1) {
    		findViewById(R.id.powerUp1).setBackgroundResource(powerUpImageList[powerUpList[1]]);
    		findViewById(R.id.powerUp2).setBackgroundResource(powerUpImageList[powerUpList[2]]);
    		findViewById(R.id.powerUp3).setBackgroundResource(powerUpImageList[powerUpList[3]]);
    	} else {
    		findViewById(R.id.powerUp1).setBackgroundResource(R.drawable.pud_random);
    		findViewById(R.id.powerUp2).setBackgroundResource(R.drawable.pud_random);
    		findViewById(R.id.powerUp3).setBackgroundResource(R.drawable.pud_random);
    	}
    }

    public void setLayout() {

    	findViewById(R.id.descriptionBack).setY(23*screenHeight/64);
    	LayoutParams descriptionBack = findViewById(R.id.descriptionBack).getLayoutParams();
		descriptionBack.height = screenHeight/2;
		findViewById(R.id.descriptionBack).setLayoutParams(descriptionBack);
		((ImageView) findViewById(R.id.descriptionBack)).setImageResource(R.drawable.powerup_description);

		findViewById(R.id.powerUp1).setY(23*screenHeight/64);
		findViewById(R.id.powerUp2).setY(101*screenHeight/192);
		findViewById(R.id.powerUp3).setY(133*screenHeight/192);
    	LayoutParams pud1 = findViewById(R.id.powerUp1).getLayoutParams();
    	LayoutParams pud2 = findViewById(R.id.powerUp2).getLayoutParams();
    	LayoutParams pud3 = findViewById(R.id.powerUp3).getLayoutParams();
    	pud1.width = 2*screenHeight/3;
		pud1.height = screenHeight/6;
		pud2.width = 2*screenHeight/3;
		pud2.height = screenHeight/6;
		pud3.width = 2*screenHeight/3;
		pud3.height = screenHeight/6;
		findViewById(R.id.powerUp1).setLayoutParams(pud1);
		findViewById(R.id.powerUp2).setLayoutParams(pud2);
		findViewById(R.id.powerUp3).setLayoutParams(pud3);

		findViewById(R.id.preGameTitle).setY(screenHeight/64);
    	LayoutParams title = findViewById(R.id.preGameTitle).getLayoutParams();
		title.width = screenHeight/2;
		title.height = screenHeight/20;
		findViewById(R.id.preGameTitle).setLayoutParams(title);

		findViewById(R.id.readyBtn).setY(43*screenHeight/48);
    	LayoutParams ready = findViewById(R.id.readyBtn).getLayoutParams();
		ready.width = screenHeight/4;
		ready.height = screenHeight/12;
		findViewById(R.id.readyBtn).setLayoutParams(ready);

		LayoutParams btn1 = findViewById(R.id.btnOff).getLayoutParams();
		LayoutParams btn2 = findViewById(R.id.btnDef).getLayoutParams();
		LayoutParams btn3 = findViewById(R.id.btnRan).getLayoutParams();
		LayoutParams btn4 = findViewById(R.id.btnNone).getLayoutParams();
		btn1.height = screenHeight/8;
		btn1.width = screenHeight/8;
		btn2.height = screenHeight/8;
		btn2.width = screenHeight/8;
		btn3.height = screenHeight/8;
		btn3.width = screenHeight/8;
		btn4.height = screenHeight/8;
		btn4.width = screenHeight/8;

		findViewById(R.id.btnOff).setY(28*screenHeight/320);
		findViewById(R.id.btnDef).setY(28*screenHeight/320);
		findViewById(R.id.btnRan).setY(67*screenHeight/320);
		findViewById(R.id.btnNone).setY(67*screenHeight/320);

    }

    public void setButtons() {
    	findViewById(R.id.btnOff).setBackgroundColor(Color.rgb(42, 168, 143));
		findViewById(R.id.btnOff).setScaleX(0.75f);
		findViewById(R.id.btnOff).setScaleY(0.75f);
		findViewById(R.id.btnDef).setBackgroundColor(Color.rgb(42, 168, 143));
		findViewById(R.id.btnDef).setScaleX(0.75f);
		findViewById(R.id.btnDef).setScaleY(0.75f);
		findViewById(R.id.btnRan).setBackgroundColor(Color.rgb(42, 168, 143));
		findViewById(R.id.btnRan).setScaleX(0.75f);
		findViewById(R.id.btnRan).setScaleY(0.75f);
		findViewById(R.id.btnNone).setBackgroundColor(Color.rgb(42, 168, 143));
		findViewById(R.id.btnNone).setScaleX(0.75f);
		findViewById(R.id.btnNone).setScaleY(0.75f);
    }


    public void play (View v) {
    	if (userClicked != 0) {
	    	Intent result = new Intent();
	    	Bundle selection = new Bundle();
	    	selection.putIntArray("powerUpList", powerUpList);
	    	result.putExtras(selection);
	    	setResult(Activity.RESULT_OK, result);
	    	this.finish();
	    	overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    	}
    }

    public void clickOffense(View v) {
    	setButtons();
    	findViewById(R.id.btnOff).setBackgroundColor(Color.rgb(186, 87, 106));
		findViewById(R.id.btnOff).setScaleX(0.9f);
		findViewById(R.id.btnOff).setScaleY(0.9f);
		setDescription(1);
    	userClicked = 1;
    }

    public void clickDefense(View v) {
    	setButtons();
    	findViewById(R.id.btnDef).setBackgroundColor(Color.rgb(47, 131, 191));
		findViewById(R.id.btnDef).setScaleX(0.9f);
		findViewById(R.id.btnDef).setScaleY(0.9f);
		setDescription(2);
    	userClicked = 2;
    }

    public void clickRandom(View v) {
    	setButtons();
    	findViewById(R.id.btnRan).setBackgroundColor(Color.rgb(160, 80, 160));
		findViewById(R.id.btnRan).setScaleX(0.9f);
		findViewById(R.id.btnRan).setScaleY(0.9f);
		setDescription(3);
    	userClicked = 3;
    }

    public void clickNone(View v) {
    	setButtons();
    	findViewById(R.id.btnNone).setBackgroundColor(Color.rgb(144, 144, 144));
		findViewById(R.id.btnNone).setScaleX(0.9f);
		findViewById(R.id.btnNone).setScaleY(0.9f);
		setDescription(4);
    	userClicked = 4;
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
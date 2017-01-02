package com.thousandonestories.game;


import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.thousandonestories.game.management.GameConnector;
import com.thousandonestories.game.management.GlobalConstants;

public class MainActivity extends Activity {

	Drawable myImage;
	Thread t;
	GameManager gm;
    GameConnector gmt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    Log.d("bleh", "onCreate called");

	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);      
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    //Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    // setContentView(R.layout.activity_main);
        gmt = new GameConnector(getResources());
        gm = new GameManager(this, gmt);
		GlobalConstants.gm = gm;
        gmt.setPanel(gm);

        setContentView(gm);
	}
	
	protected void onResume()
	{
		super.onResume();
	    Log.d("bleh", "onResume called");
	    if(gm.getThread() != null) gm.getThread().setRunning(true);
	}
	
	protected void onPause()
	{
		super.onPause();
		Log.d("bleh", "onPause was called");
		gm.getThread().setRunning(false);
	}
	
	protected void onSaveInstanceState(Bundle b) {
		  super.onSaveInstanceState(b);
		  b.putAll(b);
	}
	
	
	//protected void onRestoreInstanceState
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startButtonListener(View v)
	{
		v.getId();
		gm = new GameManager(this, gmt); // ???

		setContentView(gm);
	}

    @Override
    public void onBackPressed() {
        gm.restartGame();
    }

}

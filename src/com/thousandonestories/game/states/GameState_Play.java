package com.thousandonestories.game.states;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.R;
import com.thousandonestories.game.gameobjects.Block;
import com.thousandonestories.game.gameobjects.NewHeroSprite;
import com.thousandonestories.game.levels.Level;
import com.thousandonestories.game.levels.Tutorial;
import com.thousandonestories.game.ui.Camera;
import com.thousandonestories.game.ui.Hud;
import com.thousandonestories.game.ui.LifeMeter;
import com.thousandonestories.game.ui.RectangleOverlay;
import com.thousandonestories.game.ui.TextDisplay;

public class GameState_Play extends GameState{

	private Camera camera;
	
	private Hud hud;
	private Level currentLevel;
	private NewHeroSprite hro;
	private TextDisplay speechDisplay; 
	
	/**
	 * Block to be drawn to screen for tutorial purposes
	 */
	private Block inputDemoBlock;
	private RectangleOverlay rOverlay;

	public GameState_Play(GameManager gm) {
		super(gm);
		
		//Tutorial:
		rOverlay = new RectangleOverlay();
		inputDemoBlock = new Block(0,0 , gm.getGameWidth(), gm.getGameHeight(), Color.argb(0,255,255,255)); 
		rOverlay.add(inputDemoBlock);
		//
		
		//Speech:
		speechDisplay  = new TextDisplay( gm,"Hello!", "This is the start of your quest!", "Are you ready?");
		//
		
		//Hud:
		hud = new Hud();
		hud.addElement(new LifeMeter(gm) );
		//
		
		//Hero:
		hro = new NewHeroSprite( gm,  200, 100, 1 );
		//
		
		//Level:
		currentLevel = new Tutorial(gm);
		currentLevel.loadBitmaps();
		currentLevel.start();
		//
			
		//Music: (Move above state / level [menuState can use so can levelOne, levelTwo] )
		loadSong( R.raw.shooter);

		//Camera:
		camera = new Camera(gm.getGameWidth(), gm.getGameHeight());
		
	}

	@Override
	public boolean handleInput(float x, float y, int action) {

		//TODO: quantize input events to frames
		
		if( speechDisplay.hasText() 
				&& action==MotionEvent.ACTION_DOWN 
				&& speechDisplay.checkBounds(x,y) )
		{
			speechDisplay.advanceText();
		}
		else
		{
			switch( action )
			{
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				hro.run( (x - gm.getGameHeight()/2 ) /100 );
				break;
			case MotionEvent.ACTION_UP:
				hro.stopRunning();
				break;
			case MotionEvent.ACTION_POINTER_INDEX_MASK:
				camera.pan(x/100);
				break;
			}
		}
		return true;

	}

	@Override
	public void update(long dt) {
		hud.update(dt);
		hro.update(dt);
	}

	@Override
	public void doDraw(Canvas c) {
		currentLevel.doDraw(c, camera);  	//scroll
		hro.doDraw(c, camera);				//scroll
		//enemies.doDraw(c)			//scroll
		hud.doDraw(c);				//don't scroll
		rOverlay.doDraw(c);			//don't scroll
		speechDisplay.doDraw(c);	//don't scroll
	}

	public Level getLevel()
	{
		return currentLevel;
	}

	@Override
	public boolean handleFling(float velocityX, float velocityY) {
		hro.setDx(velocityX/500);
		hro.setDy(velocityY/500);
		Log.d("z", "FLING! dx= "+ velocityX + "dy= " + velocityY);
		return true;
	}

	public Hud getHud()
	{
		return hud;
	}
	


}

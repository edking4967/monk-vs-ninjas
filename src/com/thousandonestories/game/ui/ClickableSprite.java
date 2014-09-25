package com.thousandonestories.game.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.GameTimer;
import com.thousandonestories.game.bitmap.BitmapCache;
import com.thousandonestories.game.gameobjects.HeroSprite;
import com.thousandonestories.game.gameobjects.OldPanel;
import com.thousandonestories.game.gameobjects.Sprite;


public class ClickableSprite extends Sprite
{
	/*
	 * Constants used for the action performed when a button is activated: 
	 */
	
	public static final int RESTART_GAME = 0;
	public static final int START_LEVEL_ONE = 1;
	public static final int START_LEVEL_TWO = 2;
	public static final int START_LEVEL_THREE = 3;
	public static final int CHOOSE_PROJECTILE = 4;
	public static final int CHOOSE_SWORD = 5;

	GameTimer clickTimer;
	
	/**
	 * The action to be performed when a button is activated.
	 */
	private int action;
	
	/**
	 * Whether the sprite has been clicked;
	 */
	private boolean clicked;
	
	public ClickableSprite(Resources res, int x, int y, Bitmap bmps[], float scalefactor)	
	{
		super(res, x, y, bmps, null, scalefactor);
		clicked = false;
		
		
		//Adjust coordinates to reference MIDDLE of the sprite, not top-left, for easy centering.
		mX -= mWidth /2;
		mY -= mHeight/2;
		
		clickTimer = new GameTimer();
		
		clickTimer.setDuration(10); //.1 sec
		
	}
	
	public ClickableSprite(Resources res, int x, int y, int bmps_nums[], float scalefactor)	
	{
		this(res, x, y, BitmapCache.getBitmaps(bmps_nums), scalefactor);
		
	}

	
	public boolean checkClick( float x, float y)
	{
		if(clickTimer.hasElapsed())
		{
			clickTimer.reset();
			clicked = false;
		}
		
		if( x <= getRightBound() && x >= getLeftBound() && 
				   y <= getBottomBound() && y >= getTopBound() )
		{
			return true;
		}
		else return false;
	}
	
	public void click(GameManager gm)
	{
		
		doAction(this.action, gm);
		
		clicked = true;
		
		clickTimer.start();
	}
	
	public void doAction(int action, GameManager gm)
	{
		switch(action)
		{
			case RESTART_GAME:
				Log.d("red", "restart game called from ClickableSprite");
				gm.restartGame();
				break;
			case START_LEVEL_ONE:
				gm.leaveMenu();
				break;
			case START_LEVEL_TWO:
				gm.leaveMenu();
				break;
			case START_LEVEL_THREE:
				gm.leaveMenu();
				break;
			case CHOOSE_PROJECTILE:
				OldPanel.hero.setWeapon( HeroSprite.WEAPON_PROJECTILE );
				break;
			case CHOOSE_SWORD:
				OldPanel.hero.setWeapon( HeroSprite.WEAPON_SWORD );
				break;
		}
	}
	
	public void setAction(int action)
	{
		this.action = action; 
	}
	
	public boolean hasBeenClicked()
	{
		return clicked;
	}

	
}
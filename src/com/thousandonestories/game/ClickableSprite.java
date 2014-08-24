package com.thousandonestories.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;


public class ClickableSprite extends Sprite
{
	/*
	 * Constants used for the action performed when a button is activated: 
	 */
	
	public static final int RESTART_GAME = 0;
	public static final int START_LEVEL_ONE = 1;
	public static final int START_LEVEL_TWO = 2;
	
	/**
	 * The action to be performed when a button is activated.
	 */
	private int action;
	
	/**
	 * Whether the sprite has been clicked;
	 */
	private boolean clicked;
	
	public ClickableSprite(Resources res, int x, int y, Bitmap bmps[], Bitmap reversedbmps[], float scalefactor)	
	{
		super(res, x, y, bmps, reversedbmps, scalefactor);
		clicked = false;
		
		
		//Adjust coordinates to reference MIDDLE of the sprite, not top-left, for easy centering.
		mX -= mWidth /2;
		mY -= mHeight/2;
	}
	
	public boolean checkClick( float x, float y)
	{
		if( x <= getRightBound() && x >= getLeftBound() && 
				   y <= getBottomBound() && y >= getTopBound() )
		{
			return true;
		}
		else return false;
	}
	
	public void click(Panel panel)
	{
		
		//panel.color=Color.RED;
		
//		//start game again:
//		if(panel.isGameOver())
//			panel.restartGame( panel.getContext() );
//		if(panel.isMenu())
//			panel.leaveMenu();
		
		doAction(this.action, panel);
		
		clicked = true;
	}
	
	public void doAction(int action, Panel panel)
	{
		switch(action)
		{
			case RESTART_GAME:
				Log.d("red", "restart game called from ClickableSprite");
				panel.restartGame( panel.getContext() );
				break;
			case START_LEVEL_ONE:
				Panel.setLevel(1);
				panel.leaveMenu();
				break;
			case START_LEVEL_TWO:
				Panel.setLevel(2);
				panel.leaveMenu();
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
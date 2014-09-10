package com.thousandonestories.game.ui;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Canvas;
import com.thousandonestories.game.GameManager;

public abstract class Menu {
	private CopyOnWriteArrayList<ClickableSprite> menuItems;
	private GameManager gm;

	public Menu(GameManager gm)
	{
		menuItems = new CopyOnWriteArrayList<ClickableSprite>();
		this.gm = gm;
		
	}

	public void addItem(ClickableSprite c)
	{
		menuItems.add(c);
	}
	
	public boolean handleInput(float x, float y, int action)
	{
		for( ClickableSprite menuItem: menuItems)
		{
			if( menuItem.checkClick( x, y ) )
			{
				if(!menuItem.hasBeenClicked())
				{
					menuItem.click( gm );
					return true;
				}
			}
		}
		return false;
	}
	
	public void update(long dt)
	{
		for( ClickableSprite menuItem: menuItems)
		{
			menuItem.update(dt);
		}
	}
	
	public void doDraw(Canvas c)
	{
		for( ClickableSprite menuItem: menuItems)
		{
			menuItem.doDraw(c);
		}

	}
}

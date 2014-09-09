package com.thousandonestories.game.ui;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.R;

public class Menu {
	private CopyOnWriteArrayList<ClickableSprite> menuItems;
	private GameManager gm;

	public Menu(GameManager gm)
	{
		menuItems = new CopyOnWriteArrayList<ClickableSprite>();
		this.gm = gm;
		
		Bitmap[] b = { BitmapFactory.decodeResource(gm.getResources() , R.drawable.qi ) };
		ClickableSprite s = new ClickableSprite(gm.getResources(), 100, 100, b, 1 );
		s.setAction(ClickableSprite.START_LEVEL_ONE);
		this.addItem( s ) ;
		
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

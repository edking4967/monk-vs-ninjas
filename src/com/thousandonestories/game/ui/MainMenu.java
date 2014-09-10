package com.thousandonestories.game.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.R;

public class MainMenu extends Menu{

	public MainMenu(GameManager gm) {
		super(gm);
		Bitmap[] b = { BitmapFactory.decodeResource(gm.getResources() , R.drawable.qi ) };
		ClickableSprite s = new ClickableSprite(gm.getResources(), 100, 100, b, 1 );
		s.setAction(ClickableSprite.START_LEVEL_ONE);
		this.addItem( s ) ;

	}

}

package com.thousandonestories.game.levels;

import android.graphics.Color;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.gameobjects.Block;

public class Tutorial extends Level {

	
	public Tutorial(GameManager gm) {
		super(gm);

		addBlock( new Block( 0, gm.getGameHeight()-100, gm.getGameWidth(), gm.getGameHeight(), Color.WHITE ) );
	}

	@Override
	public void loadBitmaps() {
		
	}

	@Override
	public void start() {
		
	}
	

}

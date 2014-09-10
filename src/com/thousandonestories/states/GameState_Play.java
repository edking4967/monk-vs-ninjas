package com.thousandonestories.states;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.R;
import com.thousandonestories.game.gameobjects.NewHeroSprite;
import com.thousandonestories.game.levels.Level;
import com.thousandonestories.game.levels.LevelOne;
import com.thousandonestories.game.ui.ClickableSprite;
import com.thousandonestories.game.ui.HUD;

public class GameState_Play extends GameState{

	private HUD hud;
	private Level currentLevel;
	
	private NewHeroSprite hro;

	public GameState_Play(GameManager gm) {
		super(gm);
		hud = new HUD();
		Bitmap[] uiElem = { BitmapFactory.decodeResource(gm.getResources(), R.drawable.goku) };
		hud.addElement(new ClickableSprite(gm.getResources(), 500, 500, uiElem, 1  ));

		currentLevel = new LevelOne(gm);

		currentLevel.loadBitmaps();
		currentLevel.start();

		hro = new NewHeroSprite( gm,  200, 100, 1 );
			
	}

	@Override
	public boolean handleInput(float x, float y, int action) {
		
		return false;
			
	}

	@Override
	public void update(long dt) {
		hud.update(dt);
		hro.update(dt);
	}

	@Override
	public void doDraw(Canvas c) {
		currentLevel.doDraw(c);
		hro.doDraw(c);
		hud.doDraw(c);
	}

	public Level getLevel()
	{
		return currentLevel;
	}



}

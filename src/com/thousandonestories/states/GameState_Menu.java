package com.thousandonestories.states;


import android.graphics.Canvas;
import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.ui.Menu;

public class GameState_Menu extends GameState{

	private Menu menu;
	
	public GameState_Menu(GameManager gm) {
		super(gm);
		menu = new Menu(gm);
	}

	@Override
	public boolean handleInput(float x, float y, int action) {
		return menu.handleInput(x,y,action);
	}

	@Override
	public void update(long dt) {
		menu.update(dt);
	}

	@Override
	public void doDraw(Canvas c) {
		menu.doDraw(c);
	}

}

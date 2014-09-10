package com.thousandonestories.states;


import android.graphics.Canvas;
import android.media.MediaPlayer;
import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.R;
import com.thousandonestories.game.ui.MainMenu;
import com.thousandonestories.game.ui.Menu;

public class GameState_Menu extends GameState{

	private Menu menu;
	private MediaPlayer menuSong;
	
	public GameState_Menu(GameManager gm) {
		super(gm);
		menu = new MainMenu(gm);
		menuSong = MediaPlayer.create(gm.getContext(), R.raw.shooter);
		menuSong.setLooping(true);
		menuSong.start();
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

	@Override
	public boolean handleFling(float velocityX, float velocityY) {
		return false;
		
	}

}

package com.thousandonestories.game;

import android.content.res.Resources;
import android.graphics.Canvas;
import com.thousandonestories.game.ui.NewPanel;
import com.thousandonestories.states.GameState;
import com.thousandonestories.states.GameState_Menu;
import com.thousandonestories.states.GameState_Play;

public class GameManager {
	
	private GameState currentState;
	private Resources resources;
	private int panelWidth = 0;
	private int panelHeight= 0;
	
	public GameManager( NewPanel p )
	{
		resources = p.getResources();
		currentState = new GameState_Menu(this);
	}
	
	public GameState getGameState()
	{
		return currentState;
	}
	
	public boolean handleInput( float x, float y, int action)
	{
		return currentState.handleInput(x,y,action);
	}

	public void leaveMenu() {
		currentState = new GameState_Play(this);
	}
	
	public Resources getResources()
	{
		return resources;
	}
	
	public void restartGame() {
		
	}

	public void setDimensions(int width, int height) {
		panelWidth = width;
		panelHeight = height;
	}
	
	public int getGameWidth()
	{
		return panelWidth;
	}
	
	public int getGameHeight()
	{
		return panelHeight;
	}

	public void doDraw(Canvas c) {
		currentState.doDraw(c);
	}
	
	public void update(long dt)
	{
		currentState.update(dt);
	}


}

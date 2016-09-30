package com.thousandonestories.management.game;

import com.thousandonestories.game.levels.Level;

public class GameManager {
	
	private Level currentLevel;
	private GameState currentState;
	
	public GameManager()
	{
		
	}
	
	public GameState getGameState()
	{
		return currentState;
	}
	
	public Level getLevel()
	{
		return currentLevel;
	}
}

package com.thousandonestories.game.management;
<<<<<<< HEAD
=======
import com.thousandonestories.game.GameState;
>>>>>>> temp-head
import com.thousandonestories.game.levels.Level;
import com.thousandonestories.game.GameState;

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

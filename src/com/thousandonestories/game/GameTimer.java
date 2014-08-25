package com.thousandonestories.game;

public class GameTimer {

	private long duration;
	private long initialTime;
	private boolean initialized;
	
	
	public GameTimer()
	{
		initialized=false;
	}
	
	public void addTimer(long duration) {
		
		if( !initialized )
		{
			this.duration = duration;
			initialTime = System.currentTimeMillis();
			initialized = true;
		}
	}

	public boolean hasElapsed() {
		if(System.currentTimeMillis() - initialTime >= duration)
			return true;
		else 
			return false;
	}
	
	public void initialize()
	{
		initialized = false;
	}
	
	public void reset()
	{
		initialTime = System.currentTimeMillis();
	}
	
}

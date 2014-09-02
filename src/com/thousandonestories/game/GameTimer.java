package com.thousandonestories.game;

public class GameTimer {

	private long duration;

	private long initialTime;
	private boolean initialized;
	
	
	public GameTimer()
	{
		initialized=false;
	}
	
	/**
	 * Sets timer to duration and begins the timer.
	 * @param duration
	 */
	public void setTimer(long duration) {
		
		if( !initialized )
		{
			this.duration = duration;
			initialTime = System.currentTimeMillis();
			initialized = true;
		}
	}

	public boolean hasElapsed() {
		
		if(!initialized ) return false;
		
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
	
	public void start()
	{
		setTimer(duration);
	}
	
	public long getDuration() {
		return duration;
	}

	/**
	 * Sets timer's duration without starting it.
	 * @param duration
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

}

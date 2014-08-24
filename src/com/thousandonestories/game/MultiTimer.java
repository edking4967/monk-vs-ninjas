package com.thousandonestories.game;

import java.util.ArrayList;

public class MultiTimer {

	
	/**
	 * Whether or not start time has been set.
	 */
	
	private boolean startTimeSet;
	private long startTime;
	private long delayDuration;
	private ArrayList<Integer> timers;
	private ArrayList<Long> startTimes;
	
	
	public MultiTimer()
	{
		startTimeSet=false;
		timers = new ArrayList<Integer>();
	}
	
	public boolean checkTimer() {
		
		if(!startTimeSet)
		{
			startTime = System.currentTimeMillis();
			startTimeSet=true;
		}
		
		if(System.currentTimeMillis() - startTime >= delayDuration)
		{
			return true;
		}
		else
			return false;
	}
	
	public void setDelay(long duration) {
		delayDuration = duration;
	}

	public int addTimer(String id, int delayDuration) {
		
		if( true )  // timer with ID "id" doesn't already exist
		{
			timers.add( delayDuration );
			startTimes.add( System.currentTimeMillis() );
			return timers.size();

		}
		
		else return -1;
		
		
	}

	public boolean checkTimer(int timerNum) {
		if(System.currentTimeMillis() - startTimes.get( timerNum ) >= timers.get(timerNum))
		{
			return true;
		}
		else return false;
		
	}

	
}

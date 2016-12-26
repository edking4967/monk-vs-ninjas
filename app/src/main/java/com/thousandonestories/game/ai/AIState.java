package com.thousandonestories.game.ai;

public abstract class AIState<InstanceType>
{

	private InstanceType e;
	
	public AIState(InstanceType e)
	{
		this.e = e;
	}
	
	public abstract void transition();
	/**
	 * Checks conditions to change state 
	 * @return true if conditions are met 
	 */
	public abstract boolean doChecks();
	
	/**
	 * Actions to do in this state
	 */
	public abstract void doActions( long mElapsed );
	
	public InstanceType getInstance()
	{
		return e;
	}
}

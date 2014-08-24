package com.thousandonestories.game;

public class PhysicsStuff {

	private float mass;
	private float jumpVel;

	public PhysicsStuff( float Mass, float JumpVel )
	{
		setMass(Mass);
		setJumpVel(JumpVel);
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getJumpVel() {
		return jumpVel;
	}

	public void setJumpVel(float jumpVel) {
		this.jumpVel = jumpVel;
	}
	
}

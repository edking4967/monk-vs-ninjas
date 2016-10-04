package com.thousandonestories.game.gameobjects;


import com.thousandonestories.game.GameTimer;
import com.thousandonestories.game.OldPanel;
import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.SpriteResources;

import com.thousandonestories.game.ai.AIState;
import com.thousandonestories.game.graphics.BitmapMgr;
import com.thousandonestories.game.management.GameObjectMgr;

import android.graphics.Bitmap;
import android.graphics.Color;


public class Ninja extends NewPhysicsSprite {

	/**
	 * Animation numbers:
	 */
	private int RUN_ANIMATION_NUM=0;
	
	/**
	 * Timer for when to fire fireballs.
	 */
	private GameTimer projectileTimer;

	/**
	 * whether enemy can perform slash move
	 */
	private boolean canSlash;
	
	private GameTimer tintTimer;

	
	private AIState<Ninja> currentState;
	
	public Ninja(SpriteResources spriteRes, float x, float y,
			int scalefactor, PhysicsStuff phys) {
		super(spriteRes, x, y, scalefactor, phys);
		projectileTimer = new GameTimer();
		tintTimer = new GameTimer();

		/*
		 * Set interval to shoot fireballs:
		 */
		projectileTimer.setTimer(1200);
		
		tintTimer.setDuration(500);

		this.setRunning(false);
		
		canSlash = false;
		
		setState( new InitialState(this) );

	}

	public void update(long elapsedTime)
	{
		super.update(elapsedTime);
		
		/*
		 * Manage state
		 */
		
		currentState.doActions( elapsedTime );
		
		if(currentState.doChecks())
		{
			currentState.transition();
		}
		
		/*
		 * Fire projectile:
		 */
		if(projectileTimer.hasElapsed())
		{
			fire();
			projectileTimer.reset();
		}

		/*
		 * Remove dying enemies:
		 */
		if( tintTimer.hasElapsed() ) 
		{
			OldPanel.removeFromLists( this );
		}

		/*
		 * Check for collisions with hero's projectile:
		 */
		for( Projectile p: OldPanel.getProjList() )
		{
			if(p.getType() == Projectile.TYPE_HERO)
			{
				if( GameObjectMgr.checkCollision(this, p) )
				{
					OldPanel.removeFromLists(p); //delete projectile
					die();
					this.setDx(10 * p.mDx / Math.abs(p.mDx) );
				}
			}
		}

	}


	public void fire()
	{
		float startX;
		int dir;
		if ( this.isFlipped() )
		{
			startX=this.getLeftBound() - 25;
			dir = -1;
		}
		else
		{
			startX=this.getRightBound() +25;
			dir=1;
		}

		Projectile proj = new Projectile( getSpriteResources().getAndroidResources() , startX,  ( getTopBound() + getBottomBound() )/2, 10 * dir, 
				BitmapMgr.projBmp, Projectile.TYPE_ENEMY, 2 );

		OldPanel.getProjList().add( proj);
		OldPanel.getGameObjList().add(proj);

	}
	
	public void land(Block b)
	{
		super.land(b);
		canSlash = true;
	}

	public void die() {

		this.flash(Color.RED, tintTimer.getDuration() );
		tintTimer.start();
	}
	
	public void startRunning( float velocity )
	{
		this.setDx(velocity);
		this.startAnimation( this.RUN_ANIMATION_NUM );
		this.setRunning(true);
	}
	
	public void stopRunning()
	{
		this.stopOnFrame(RUN_ANIMATION_NUM, 0);
		this.setRunning(false);
	}

	/**
	 * Set up animations for this instance of the class. 
	 * @param runAnim The animation for running.
	 */
	public void setAnims( int runAnim )
	{
		RUN_ANIMATION_NUM = runAnim;
	}

	public void doSlash()
	{
		startAnimation(1, 1, 0, false);
		canSlash = false;
	}
	
	public void setState(AIState<Ninja> s)
	{
		this.currentState = s;
	}

	/*
	 * STATES:
	 */
	
	class InitialState extends AIState<Ninja>
	{
		public InitialState(Ninja e) {
			super(e);
		}

		/**
		 * Transitions when Sprite lands
		 */
		@Override
		public boolean doChecks() {
			//return !isInAir() ;
			
			return false;
		}

		@Override
		public void doActions(long elapsedTime) {
			
		}

		@Override
		public void transition() {
			getInstance().setState( new Running( getInstance() ) );
		}
		
	}
	class Running extends AIState<Ninja>
	{
		public Running(Ninja e) {
			super(e);
			// TODO Auto-generated constructor stub
		}

		/**
		 * Jump if enemy has reached boundary
		 */
		@Override
		public boolean doChecks() {
			return ( getInstance().getLeftBound() - getInstance().getOnBlock().getLeftBound() <= 10 );
		}

		@Override
		public void doActions(long elapsedTime) {
			if( !getInstance().isRunning() )
			{
				getInstance().startRunning( - getInstance().getWidth() / elapsedTime );
			}
		}

		@Override
		public void transition() {
			getInstance().setState( new Jumping( getInstance() ) );
			
		}

	
	}
	class Jumping extends AIState<Ninja>
	{
		public Jumping(Ninja e) {
			super(e);
		}

		/**
		 * Slash if hero is within reach
		 */
		@Override
		public boolean doChecks() {
			return ( getInstance().getLeftBound() - GameObjectMgr.hero.getRightBound() <= 15 );
		}

		@Override
		public void doActions(long elapsedTime) {
			if( !getInstance().isInAir() )
			{
				getInstance().jump();
			}
		}

		@Override
		public void transition() {
			getInstance().setState( new Slashing( getInstance() ) );
		}
	}
	class Slashing extends AIState<Ninja>
	{
		public Slashing(Ninja e) {
			super(e);
			// TODO Auto-generated constructor stub
		}

		public void transition() 
		{
			getInstance().setState( new Running( getInstance() ) );
		}

		@Override
		public boolean doChecks() {
			return !getInstance().isInAir();
		}

		@Override
		public void doActions(long elapsedTime) {
			if(getInstance().canSlash)
			{
				getInstance().doSlash();
			}
		}

	}
}

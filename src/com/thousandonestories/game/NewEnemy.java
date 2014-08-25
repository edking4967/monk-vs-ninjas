package com.thousandonestories.game;

import android.graphics.Color;


public class NewEnemy extends NewPhysicsSprite {

	/**
	 * Timer for when to fire fireballs.
	 */
	private GameTimer projectileTimer;

	private GameTimer gt;

	/**
	 * The time when the sprite starts running
	 */

	/**
	 * Whether enemy is guarding.
	 */
	private boolean hasGuardUp;

	/**
	 * whether enemy can perform slash move
	 */
	private boolean canSlash;

	/**
	 * The time when the enemy becomes tinted, shortly after which it will die
	 */
	private long tintTime;

	/**
	 * the amount of time the enemy is tinted for before it dies.
	 */
	private final long tintInterval = 500;

	/**
	 * The current state of this instance's AI.
	 */
	private int aiState;

	public static final int STATE_SLEEP = 0 ;
	public static final int STATE_FOLLOW = 1 ;
	public static final int STATE_BATTLE = 2 ;

	/*
	 * AI: determines whether to jump.
	 */
	private float AIMoveLB, AIMoveRB;
	private boolean AIInBounds;

	private boolean vulnerable;

	/**
	 * Enemy's safety buffer for AI
	 */
	float safetyBound; 

	public NewEnemy(SpriteResources spriteRes, float x, float y,
			int scalefactor, PhysicsStuff phys) {
		super(spriteRes, x, y, scalefactor, phys);
		projectileTimer = new GameTimer();

		/*
		 * Set interval to shoot fireballs:
		 */
		projectileTimer.addTimer(1200);

		hasGuardUp=false;

		tintTime = 0;

		vulnerable = false;	

		AIInBounds = false;

		aiState = STATE_FOLLOW;
		setMovementState(STATE_STILL);
		canSlash = false;

	}

	public void update(long elapsedTime)
	{
		super.update(elapsedTime);

		updateAI();

		if(projectileTimer.hasElapsed())
		{
			fire();
			projectileTimer.reset();
		}

		if(tintTime != 0) // enemy has been tinted
		{
			if( System.currentTimeMillis() - tintTime >= tintInterval)
			{
				Panel.removeFromLists( this );
			}
		}

		for( Projectile p: Panel.getProjList() )
		{
			if(p.getType() == Projectile.TYPE_HERO)
			{
				if( Panel.checkCollision(this, p) )
				{
					Panel.removeFromLists(p); //delete projectile
					die();
				}
			}
		}

	}

	public void updateAI()
	{
		//Update the boundaries where the enemy will move:
		if(getOnBlock() != null)
		{
			AIMoveLB = getOnBlock().getLeftBound() +20;
			AIMoveRB = getOnBlock().getRightBound()-20;

		}

		switch(aiState)
		{
		case STATE_SLEEP: 
			updateAISleep();
			break;
		case STATE_FOLLOW:
			updateAIFollow();
			break;
		case STATE_BATTLE:
			updateAIBattle();
			break;
		}

		if(getPositionState() == STATE_INAIR && getLeftBound() - Panel.hero.getRightBound() <= 50 && canSlash)
			doSlash();

	}

	private void updateAIBattle() {

		checkGuard();

	}

	private void checkGuard()
	{
		if( isVulnerable() && !hasGuardUp )
		{

			//wait .5 seconds:
			gt.addTimer( 500 );


			if( gt.hasElapsed( ) )
			{
				guard() ;
				gt.initialize();
			}


			//doDelayed( 500, guard() );

		}
		else if ( this.hasGuardUp && !isVulnerable()  ) // It's safe to let guard down
		{
			gt.addTimer(500);
			if( gt.hasElapsed( ) )
			{
				unGuard() ;
				gt.initialize();
			}

		}	
	}

	public void guard()
	{
		this.setTint(Color.GREEN);
		hasGuardUp=true;

	}

	public void unGuard()
	{
		hasGuardUp=false;
	}

	private void updateAISleep() {
		float heroX = ( Panel.hero.getLeftBound() + Panel.hero.getRightBound()  ) / 2;

		float thisX = ( this.getLeftBound() + this.getRightBound() ) / 2;

		if( Math.abs(heroX - thisX) <= safetyBound * 4 )
		{
			goToFollowMode();
		}

	}

	public void run() {
		setMovementState( NewPhysicsSprite.STATE_RUN);
		startAnimation(0);

	}



	private void updateAIFollow() {

		if(this.getPositionState() == NewPhysicsSprite.STATE_ONBLOCK && this.getMovementState()!= STATE_RUN)
		{
			this.mDx = -8;
			this.run();
			AICheckMoveBounds();
		}

		checkGuard();	


		/*
		 *  to other states:
		 */
		float heroX = ( Panel.hero.getLeftBound() + Panel.hero.getRightBound()  ) / 2;

		float thisX = ( this.getLeftBound() + this.getRightBound() ) / 2;

		if(isVulnerable() && Math.abs( heroX-thisX ) <= safetyBound )
		{
			goToBattleMode();
		}
	}


	public void AICheckMoveBounds()
	{
		int x;
		x = (int) (getLeftBound()+getRightBound())/2;
		if( (x > AIMoveRB || x < AIMoveLB) && AIInBounds )
		{
			jump();
		}
		if( x < AIMoveRB && x > AIMoveLB) //Enemy is within bounds of a block
		{
			AIInBounds=true;
		}

	}

	public void land(Block b)
	{
		super.land(b);

		AIInBounds=true;
		AIMoveLB = b.getLeftBound() +20;
		AIMoveRB = b.getRightBound()-20;

		this.stopOnFrame(0,0);

		setPositionState(STATE_ONBLOCK); 

	}

	private void goToBattleMode() {

		aiState = STATE_BATTLE;

	}

	private void goToFollowMode() {

		aiState = STATE_FOLLOW;

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
				Panel.projBmp, Projectile.TYPE_ENEMY, 2 );

		Panel.getProjList().add( proj);
		Panel.getGameObjList().add(proj);

	}

	public boolean isVulnerable()
	{
		return vulnerable;
	}

	public void jump()
	{
		super.jump();
		canSlash = true;
	}

	public void die() {

		if( hasGuardUp )
			return;			// guard is up, cannot be attacked.

		this.flash(Color.RED, tintInterval);

		tintTime = System.currentTimeMillis() ;

	}


	public void doSlash()
	{
		startAnimation(1, 1, 0, false);
		canSlash = false;
	}
}

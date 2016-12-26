package com.thousandonestories.game.gameobjects;

import java.util.concurrent.CopyOnWriteArrayList;

import com.thousandonestories.game.GameTimer;
import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.SpriteResources;


import com.thousandonestories.game.ai.AIState;
import com.thousandonestories.game.management.GameObjectMgr;

import android.graphics.Canvas;
import android.graphics.Color;

//relative positioning for body parts (relative to a fixed pt on Dragon) or absolute? (:relative to screen)?

public class Dragon implements GameObject {

	private CopyOnWriteArrayList<NewSprite> bodyPartsList; 
	private NewSprite dragonHead;
	private float mWidth;
	private float mHeight;
	private float mX, mY;
	private boolean hidden;

	private AIState<Dragon> currentState;

	private float offsetFactor; // a position offset factor based on the height of the dragon's head. 
	private int numBodyParts;

	/**
	 * Whether the dragon is spewing fire.
	 */
	private boolean spewingFire;

	/**
	 * timesave for "snakelike motion" animation
	 */
	private long snakeLike_timesave;

	/**
 	 *  length of one frame of "snakelike motion" animation, in ms.
	 */
	private long snakeLike_animSpeed = 20;

	/**
	 * 
	 */

	/**
	 * 
	 * @param headRes = SpriteResources object for the head
	 * @param bodyRes = SpriteResources object for the body blocks
	 * @param x
	 * @param y
	 * @param scalefactor
	 * @param numBodyParts
	 */
	public Dragon(SpriteResources headRes, SpriteResources bodyRes, float x, float y, int scalefactor, int numBodyParts) {

		bodyPartsList = new CopyOnWriteArrayList<NewSprite>();

		this.numBodyParts = numBodyParts;

		dragonHead = new NewSprite( headRes, x, y, scalefactor) ;

		//offsetFactor = dragonHead.getHeight();

		offsetFactor = dragonHead.getSpriteResources().getInitialFrame().getHeight() * scalefactor;

		float bodyOffset = offsetFactor * 2/3 ;
		float initialOffset = offsetFactor*  2/3;

		NewSprite bodyPart;		

		for(int i = numBodyParts-1 ; i >= 0 ; i -- )  	//iterate backwards so that body parts overlap each other visually 
		{
			bodyPart = new NewSprite( 
					bodyRes, 								// pass resources for body part 
					(float) (x + offsetFactor/2 * (1 + Math.sin(i) )), 		// offset from head's x
					y + bodyOffset * i + initialOffset , 	// add a y offset for each additional body part
					scalefactor 							// retain scaleFactor of Dragon as a whole 
					) ;
			bodyPart.setHealth(5);
			//bodyPart.setVelocity( __ );
			bodyPartsList.add(bodyPart);
		}

		bodyPart = null; // don't need this reference any more

		this.setHeight( 
				bodyPartsList.get( numBodyParts - 1 ).getBottomBound() // bottom of lowest body part

				- y  // top of dragon's head

				);

		this.setWidth( dragonHead.getWidth() ) ;

		mX = x;
		mY = y;

		snakeLike_timesave = System.currentTimeMillis() ;


		spewingFire = false; // dragon is not spewing fire.

		currentState = new InitialState(this);

	}

	float ex = 0;
	@Override
	public void update(long elapsedTime)
	{

		currentState.doActions(elapsedTime);
		if(currentState.doChecks())
		{
			currentState.transition();
		}

		//Update head:
		dragonHead.update(elapsedTime);

		//Update body parts:
		for( NewSprite bp: bodyPartsList )
		{
			bp.update( elapsedTime );

			//Check projectile collisions:

			for( Projectile proj: GameManager.getProjList() )
			{
				if( GameObjectMgr.checkCollision(bp, proj) )
				{
					if(proj.getType() == Projectile.TYPE_HERO)
					{
						bp.flash(Color.RED, 500);

						//TODO: gets called multiple times
						bp.decrementHealth();
						
						proj.hide();  //remove?

						if(bp.getHealth()<=0) 
						{
						    bodyPartsList.remove(bp);
						}
						
						GameManager.blip2.start();

						if(!spewingFire) 
						{
							spewFire();
						}

					}
				}
			}

		}


		//Rotate head when spewing fire:

		if(spewingFire)
		{

			dragonHead.rotate(2);
			if(dragonHead.getRotation()==0)
			{
				spewingFire = false;
			}


		}


		//Do snakelike motion:
		float twoPi = (float) (Math.PI * 2);
		if( System.currentTimeMillis() - snakeLike_timesave >= snakeLike_animSpeed )
		{

			ex += Math.PI / 50 ; 

			dragonHead.setLeftBound( (float) ( mX + - offsetFactor/4 + offsetFactor/2 * ( Math.sin( ex ) ) ) );

			int i=1;
			for( NewSprite bp: bodyPartsList )
			{
				bp.setLeftBound( 
						(float) (

								mX + offsetFactor/2 * ( Math.sin( ex + twoPi * i / (numBodyParts+1) ) ) 

								)						
						);
				i = ( i+1 ) % (  numBodyParts+1 );
			}

			if( ex >= twoPi)
			{
				ex=0;
			}

			snakeLike_timesave = System.currentTimeMillis();
		}

	}

	/**
	 * Spew fire!
	 */
	public void spewFire()
	{
		dragonHead.rotate(-40);
		dragonHead.startAnimation(0, 20, 1, false);
		spewingFire = true;
	}

	@Override
	public void scroll(float amt, long elapsedTime) {

		this.mX += amt*elapsedTime;

		dragonHead.scroll(amt, elapsedTime);

		for( NewSprite bp: bodyPartsList )
		{
			bp.scroll(amt, elapsedTime);
		}

	}

	@Override
	public void doDraw(Canvas c)
	{

		for( NewSprite bp: bodyPartsList )
		{
			bp.doDraw(c);
		}

		dragonHead.doDraw(c);

	}

	@Override
	public float getLeftBound() {
		return mX;
	}

	@Override
	public float getRightBound() {
		return mX + mWidth;
	}

	@Override
	public float getTopBound() {
		return mY;
	}

	@Override
	public float getBottomBound() {
		return mY + mHeight;
	}

	@Override
	public void deactivate() {

	}

	@Override
	public void hide() {
		hidden = true;
	}

	@Override
	public void show() {
		hidden = false;
	}

	@Override
	public boolean hidden() {
		return hidden;
	}

	@Override
	public boolean isPersistent() {
		return true;
	}

	@Override
	public float getWidth() {
		return mWidth;
	}

	@Override
	public float getHeight() {
		return mHeight;
	}

	@Override
	public void setWidth(float width) {
		mWidth = width;
	}

	@Override
	public void setHeight(float height) {
		mHeight = height;
	}

	
	//TODO this sucks
	public void moveBy( float x, float y )
	{
		mX += x; mY += y;

		dragonHead.setLeftBound( dragonHead.getLeftBound() + x);
		dragonHead.setTopBound( dragonHead.getTopBound() + y);

		for(NewSprite b: bodyPartsList)
		{
			b.setLeftBound( b.getLeftBound() + x);
			b.setTopBound( b.getTopBound() + y);

		}

	}

	public void setState(AIState<Dragon> s)
	{
		this.currentState = s;
	}

	/*
	 * STATES:
	 */

	public class InitialState extends AIState<Dragon>
	{

		private boolean init=false;

		public InitialState(Dragon e) {
			super(e);
		}

		@Override
		public void transition() {
			getInstance().setState( new InPlaceState( getInstance() ) );
		}

		@Override
		public boolean doChecks() {
			return (getInstance().getTopBound() <= 10);
		}

		@Override
		public void doActions( long elapsedTime ) {
			if(!init)
			{
				getInstance().moveBy( 0, GameManager.mHeight );
				init = true;
			}
			else
			{
				getInstance().moveBy( 0, - getInstance().getWidth() / 2 / elapsedTime );
			}
		}

	}

	public class InPlaceState extends AIState<Dragon>
	{

		private GameTimer t;

		public InPlaceState(Dragon e) {
			super(e);
			t = new GameTimer();
			t.setTimer(2000);
		}

		@Override
		public void transition() {

		}

		@Override
		public boolean doChecks() {
			return false;
		}

		@Override
		public void doActions(long elapsedTime) {
			if(t.hasElapsed() && !getInstance().spewingFire)
			{
				getInstance().spewFire();
				t.reset();
			}
		}

	}


}

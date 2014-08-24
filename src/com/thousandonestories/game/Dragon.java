package com.thousandonestories.game;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Canvas;
import android.graphics.Color;

public class Dragon implements GameObject {

	private CopyOnWriteArrayList<NewSprite> bodyPartsList; 
	private NewSprite dragonHead;
	private float mWidth;
	private float mHeight;
	private float mX, mY;
	private boolean hidden;
	
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
		
		offsetFactor = dragonHead.getResources().getInitialFrame().getHeight() * scalefactor;
		
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
		
	}
	
	float ex = 0;
	@Override
	public void update(long elapsedTime)
	{
		//Update head:
		dragonHead.update(elapsedTime);
				
		//Update body parts:
		for( NewSprite bp: bodyPartsList )
		{
			bp.update( elapsedTime );
			
			//Check collisions:
			
			for( Projectile proj: Panel.getmProjList() )
			{
				if( Panel.checkCollision(bp, proj) )
				{
					if(proj.getType() == Projectile.TYPE_HERO)
					{
						bp.flash(Color.RED, 500);
						
						if(!spewingFire) 
						{
							spewFire();
							dragonHead.startAnimation(0, 20, 1, false);
						}
					}
				}
			}
			
		}
		
		
		//Spewing fire:
		
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

}

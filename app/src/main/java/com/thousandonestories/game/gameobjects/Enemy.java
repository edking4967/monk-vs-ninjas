package com.thousandonestories.game.gameobjects;

import java.util.concurrent.CopyOnWriteArrayList;

import com.thousandonestories.game.GameTimer;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;

public class Enemy extends GravitySprite
{
	private long tintTime=0;
	private long spawnTime;
	private long lastFireTime;
	private boolean redTinted=false;
	private CopyOnWriteArrayList<Projectile> mProjList;
	private CopyOnWriteArrayList<GameObject> mGameObjList;
	private CopyOnWriteArrayList<Block> mBlockList;
	private Paint tintPaint;
	private boolean AIInBounds;
	
	private GameTimer gt;
	
	/**
	 * Whether the sprite is guarding.
	 */
	private boolean hasGuardUp;
	
	int[] a;
	Bitmap projectileBmps[];
		
	private float AIMoveLB, AIMoveRB;
	
	/*
	 * AI States (experimental): 
	 */
	
	/**
	 * The threshold distance between this instance and the player before this instance will go into BATTLE MODE
	 */
	private float safetyBound;
	
	/**
	 * The current state of this instance's AI.
	 */
	private int aiState;
	
	public static final int STATE_SLEEP = 0 ;
	public static final int STATE_FOLLOW = 1 ;
	public static final int STATE_BATTLE = 2 ;
	
	
	public Enemy(Resources res, int x, int y, Bitmap bmps[], Bitmap reversebmps[], Bitmap projectilebmps[], CopyOnWriteArrayList<Projectile> projList, CopyOnWriteArrayList<Block> blockList, 
			CopyOnWriteArrayList<GameObject> gameObjList, float scalefactor) {
		super(res, x, y, bmps, reversebmps, scalefactor);
		this.flipBmp=true;
		spawnTime = System.currentTimeMillis();
		lastFireTime=spawnTime;
		tintPaint = new Paint();
		mGameObjList = gameObjList;
		mProjList = projList;
		projectileBmps = projectilebmps;
		AIMoveLB = blockLB+20;
		AIMoveRB = blockRB-20;
		AIInBounds=false;
		mBlockList=blockList;
		jumpVel /= 2;
        tintPaint = new Paint();
        hasGuardUp = false;
        
        safetyBound = this.getWidth();
		
        gt = new GameTimer();
        
		aiState = 0;
	}
	

	
	@Override
	public void update(long elapsedTime)
	{
		
		super.update(elapsedTime);
		
		updateAI();
		

				
		if( this.hidden() ) 
		{
			return; 
		}
		
		if(System.currentTimeMillis() - lastFireTime >= 1000)
		{
			Log.d("blah", "FIRE!");
			fire();
			lastFireTime = System.currentTimeMillis();
		}
	}
	
	public void fire()
	{
		float startX;
		int dir;
		if (this.flipBmp)
		{
			startX=this.getLeftBound() - 25;
			dir = -1;
		}
		else
		{
			startX=this.getRightBound() +25;
			dir=1;
		}
		
		   Projectile proj = new Projectile( r, startX,  (bY+mY)/2, 10 * dir, 
				   projectileBmps, Projectile.TYPE_ENEMY, 1 );
		   mProjList.add( proj);
		   mGameObjList.add(proj);
		
	}

	@Override
	public boolean checkProjectile(CopyOnWriteArrayList<Projectile> mProjList) {
		if(redTinted) return false; // this sprite is dead, don't need to check collisions
		else return super.checkProjectile(mProjList);
		
	}
	
   // Render bitmap at current location
   public void doDraw(Canvas canvas) {
	   //this.currentImg=0;
		if(!flipBmp)
		{
			canvas.drawBitmap(mBitmaps[currentImg], mX, mY, tintPaint); // goes by top left
		}
		else
		{
			canvas.drawBitmap(mReversedBitmaps[currentImg], mX, mY, tintPaint); // goes by top left
		}
   }


	public void die( long currentTime ) {
		
		if( hasGuardUp )
			return;			// guard is up, cannot be attacked.

        ColorFilter cf = new PorterDuffColorFilter( Color.RED, Mode.MULTIPLY);
        tintPaint.setColorFilter(cf);
		
        tintTime = currentTime;
        redTinted=true;
        
        //hide();
		
	}
	
	
	public void AICheckMoveBounds()
	{
		int x;
		x = (int) (bX+mX)/2;
		if( (x > AIMoveRB || x < AIMoveLB) && AIInBounds )
		{
			if(AIHasPlatformToLand() ) //there exists a platform to jump to
			{
				jump();
			}
			else
			{
				this.mDx *= -1; 
				this.flipBmp = ! (this.flipBmp);

			}
			
		
		}
		if( x < AIMoveRB && x > AIMoveLB) //Enemy is within bounds of a block
		{
			AIInBounds=true;
		}

	}
	
	private boolean AIHasPlatformToLand()
	{
		int dir;
		
		float x;
		x=(mX+bX)/2;
		
		
		if(getDirection() == true)
			dir=1;
		else
			dir=-1;
		
		//dir=jumpVel^2;
		float blockDistanceX;
		float blockDistanceXTemp;
		
		blockDistanceX=10000; // ie a high number
		
		//TODO: store the closest block and only update when necessary
		for(Block block: mBlockList)
		{
			//if(block)
			//find nearest block
			if( dir < 0) // sprite faces left 
			{
				blockDistanceXTemp = x - block.getRightBound();
			}
			else //sprite faces right
			{
				blockDistanceXTemp = block.getLeftBound() - x;
			}
			
			if(blockDistanceXTemp < blockDistanceX)
			{
				blockDistanceX = blockDistanceXTemp;
			}
						
		}
		//Now blockDistanceX contains how far away in x coordinates is the closest platform
		
		return true;
	}
	
	public void jump()
	{
		super.jump();
		AIInBounds=false;
	}
	

	
	/**
	 * 
	 * @return true if enemy is within range of the player's projectile.
	 */
	public boolean isVulnerable()
	{
		if(OldPanel.hero == null)
			return false;
		else
		{
			float y = ( this.getTopBound() + this.getBottomBound() ) / 2;
			return ( y >= OldPanel.hero.getTopBound() && y <= OldPanel.hero.getBottomBound() ) ;
		}
	}
	
	public void guard()
	{
					
        ColorFilter cf = new PorterDuffColorFilter( Color.GREEN, Mode.MULTIPLY);
        
        if(!redTinted)    				 	// don't override damage red tint 
        	tintPaint.setColorFilter(cf);
        hasGuardUp=true;
        
	}
	
	public void unGuard()
	{
		tintPaint.setColorFilter(null);
		hasGuardUp=false;
	}

	public void checkHide(long currentTimeMillis , CopyOnWriteArrayList<Enemy> enemyList) {
		if( !redTinted )
		{
			return;
		}
		else if ( (currentTimeMillis - tintTime) >= 500)
		{
			hide();
			enemyList.remove(this);
		}
	}
	
	public void updateAI()
	{
			//Update the boundaries where the enemy will move:
		AIMoveLB = blockLB+20;
		AIMoveRB = blockRB-20;
		
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
		
	}



	private void updateAIBattle() {
		
		checkGuard();
		
	}

	private void updateAISleep() {
		float heroX = ( OldPanel.hero.getLeftBound() + OldPanel.hero.getRightBound()  ) / 2;
		
		float thisX = ( this.getLeftBound() + this.getRightBound() ) / 2;
		
		if( Math.abs(heroX - thisX) <= safetyBound * 4 )
		{
			goToFollowMode();
		}

	}


	private void updateAIFollow() {
		
		if(this.movementState == STATE_ONGROUND)
		{
			this.mDx = -8;
			this.run( System.currentTimeMillis() );
			AICheckMoveBounds();
		}

		checkGuard();	
		
		
		/*
		 *  to other states:
		 */
		float heroX = ( OldPanel.hero.getLeftBound() + OldPanel.hero.getRightBound()  ) / 2;
		
		float thisX = ( this.getLeftBound() + this.getRightBound() ) / 2;
		
		if(isVulnerable() && Math.abs( heroX-thisX ) <= safetyBound )
		{
			goToBattleMode();
		}
	}



	private void goToBattleMode() {
		
		aiState = STATE_BATTLE;
		
	}

	private void goToFollowMode() {

		aiState = STATE_FOLLOW;
		
	}
	
	private void checkGuard()
	{
		if( isVulnerable() && !hasGuardUp )
		{
			
			//wait .5 seconds:
			gt.setTimer( 500 );
			 
			
			if( gt.hasElapsed( ) )
			{
				guard() ;
				gt.initialize();
			}
			
			
			//doDelayed( 500, guard() );

		}
		else if ( this.hasGuardUp && !isVulnerable()  ) // It's safe to let guard down
		{
			gt.setTimer(500);
			if( gt.hasElapsed( ) )
			{
				unGuard() ;
				gt.initialize();
			}

		}	
	}
	

}
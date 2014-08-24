package com.thousandonestories.game;

import java.util.concurrent.CopyOnWriteArrayList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

//TODO: IMPLEMENT PROPER STATE MACHINE

public class GravitySprite extends Sprite {

	
	//Bitmaps: 
	public static final int BMP_JUMP = 5;
	public static final int BMP_REST = 0;
	private static final int BMP_FIRE = 21;
	//todo: define ANIM_JUMP, ANIM_FIRE etc as a series of frames
	
	//States: 
	public static final int STATE_ONGROUND=0;
	public static final int STATE_RUN=1;
	public static final int STATE_INAIR=2;
	public static final int STATE_GUARDING=3;
	public static final int STATE_MED=4;

	public int runLength=21; //length of run animation in frames
	
	protected Resources r;
	public Bitmap mBitmaps[]; 
	public Bitmap mReversedBitmaps[];
	protected int currentImg;
	private int savedImg;
	 
	protected boolean flipBmp;
	public long runStart;
	
	/**
	 * The state that of movement that the sprite is in.
	 */
	protected int movementState;
	
   private int gravity = 2;
   protected float blockLB;
   protected float blockRB;
   protected Block onBlock;
   protected int jumpVel; // velocity of jump.
         
   private long timeSave_Fire;

	
	//DEBUG:
	public int db_landcount=0;
	public int fallcount=0;

	

	public Bitmap bmp_jump; 
	

	public GravitySprite(Resources res, float x, float y, Bitmap[] bitmaps, Bitmap[] reversedbitmaps, float scalefactor) {
		   
	      super(res,x,y,bitmaps, reversedbitmaps, scalefactor);	      
	      
	      mBitmaps = bitmaps;
	      mReversedBitmaps = reversedbitmaps;
	      currentImg=0;
	      flipBmp=false;
	      movementState = STATE_INAIR;
	      jumpVel = (int) (80*scalefactor);
	      // Set velocity (gravity)
	      
	      mDx = 0;
	      mDy = 5;
	      r = res;
	      
	   }
	
	public void flipSprite() {
		drawBitmap=mReversedBitmaps[currentImg];
	}
	
	public void doDraw(Canvas canvas)
	{
		   //drawRect.set( (int) getLeftBound(), (int) getTopBound(), (int) ( getLeftBound()+mWidth) , (int) (getTopBound()+mHeight) );
			//canvas.drawRect(drawRect, paint );

		if(!flipBmp)
		{
			canvas.drawBitmap(mBitmaps[currentImg], mX, mY, null); // goes by top left
		}
		else
		{
			canvas.drawBitmap(mReversedBitmaps[currentImg], mX, mY, null); // goes by top left
		}
		
	}

	
	//int frame_counter;
	public void update(long elapsedTime)  //TODO: need to pass "scrollPos"?
	{
		super.update(elapsedTime);
		if(movementState==STATE_RUN)
		{
			if(t - runStart >= 2)
			{
				currentImg = ( currentImg + 1 ) % (runLength);
				//currentImg = frame_counter ;
				runStart = t;
			}
		}
		if(movementState==STATE_INAIR)
		{
			mD2y = gravity;
			currentImg=BMP_JUMP;
		}
		if(movementState==STATE_ONGROUND)
		{
			mDy=0;
			mD2y=0;
			//currentImg=BMP_REST;
		}
		
		if( currentImg == BMP_FIRE  ) // don't want to be stuck in pose
		{
			
			if( (System.currentTimeMillis() - timeSave_Fire) >= 100) // 100ms has passed since we struck the "fire" pose. TODO: triggers always.
			{
					this.currentImg=savedImg;
					Log.d("blah", " Current: "+ System.currentTimeMillis() + " Timesave: " + timeSave_Fire );
			}
		}
		if(movementState != STATE_INAIR) // sprite is on a block
		{
			blockLB = onBlock.getLeftBound();
			blockRB = onBlock.getRightBound();
		}
	}

	
	public int getState()
	{
		return movementState;
	}

	public void fall() {
		// on ground or running >> in air
		// triggers when: --walk off the edge of a platform
		
		if(movementState==STATE_ONGROUND || movementState==STATE_RUN )
		{
			mD2y = gravity;
			movementState=STATE_INAIR;
			fallcount++;
			
		}
	}
	
	
	
	public void rest() {
		// running >> on ground
		// triggers when: touch is lifted
		
		if(movementState != STATE_INAIR)
		{
			currentImg=BMP_REST;
			movementState = STATE_ONGROUND;

		}
	}
	
	public void jump() {
		// on ground / running >> in air
		// triggers when: jump button is pushed 
		
		if(movementState==STATE_ONGROUND || movementState==STATE_RUN) // can jump
		{
			currentImg=BMP_JUMP;
			mDy=-jumpVel;
			movementState=STATE_INAIR;

		}
	}
	
	public void run(long st) {
		// on ground >> running
		// triggers when: touch is applied 
		
		if(movementState==STATE_ONGROUND)
		{
			runStart=st;
			movementState=STATE_RUN;
		}
			
	}
	
	public void fire(long time) {
		savedImg = currentImg; //save img to go back to
		currentImg=BMP_FIRE;
		timeSave_Fire=System.currentTimeMillis();		
		Log.d("blah", ""+System.currentTimeMillis());
	}

	public void saveBoundaries(int leftBound, int rightBound) {
		blockLB=leftBound;
		blockRB=rightBound;
	}

	public boolean checkFallFromBlock() {
		//Check if a sprite has moved off its current block		
		int x;
		x = (int) (bX+mX)/2;
		if( x > blockRB || x < blockLB )
		{
			// The sprite has moved off its current block.
			//fall();
			return true;
			
		}
		else
			return false;
		
	}
	
	public boolean checkProjectile(CopyOnWriteArrayList<Projectile> mProjList) {
		
		int sprite_type;
		
		if(this instanceof Enemy)
		{
			sprite_type = Projectile.TYPE_ENEMY;
		}
		else 
		{
			sprite_type = Projectile.TYPE_HERO;
		}	
		
		for( Projectile proj : mProjList )
		{
			if(proj.hidden() ) //this projectile has exploded and can't harm anyone any more
				continue;
			else if(horizCol( proj ) && vertCol( proj ) && proj.getType()!=sprite_type )
			{
				proj.hide(); // projectile explodes
				mProjList.remove(proj);
				return true;
			}			
			
		}
		
		return false;
		
	}


	private boolean horizCol( Projectile p )
	{
		//check for a horizontal collision between this object and Projectile p.
		if(this.getRightBound() >= p.getLeftBound() && this.getLeftBound() <= p.getRightBound() )
		{
			return true;
		}
		else if(p.getRightBound() >= this.getLeftBound() && p.getLeftBound() <= this.getRightBound() )
		{
			return true;
		}
		else return false;
		
	}
	
	private boolean vertCol( Projectile p )
	{
		//check for a vertical collision between this object and Projectile p.
		if(this.getBottomBound() >= p.getTopBound() && this.getTopBound() <= p.getBottomBound() )
		{
			return true;
		}
		else if(p.getBottomBound() >= this.getTopBound() && p.getTopBound() <= this.getBottomBound() )
		{
			return true;
		}
		else return false;
		
	}
	
	public void checkLanded( CopyOnWriteArrayList<Block> blockList, long mElapsed)  // check for GravitySprite collisions with Blocks
	{
		  float x;
		  boolean hor_col;
		  boolean ver_col;
		  float deltaY;
		  		  		  
		  x = ( getLeftBound() + getRightBound() ) / 2;
	        	 
 	 for( Block block: blockList)
 	 {
 		 hor_col=false;
 		 ver_col=false;
 		 
 		 //////////////////////////////
 		 // CHECK FOR HORIZONTAL MATCH:
 		 
 		 if( block.getRightBound() > x && block.getLeftBound() < x) 
 		 {
 			  hor_col=true;			        		 
 		 }
 		 //
 		 /////////////////////////////
 		 
 		//////////////////////////////// 
 		// CHECK FOR VERTICAL CROSSOVER:
 		 
 		 deltaY = this.mDy * mElapsed / 20f;
 		 if(this.getBottomBound() <= block.getTopBound() && (this.getBottomBound() + deltaY ) >= block.getTopBound() )
		 {
 			 ver_col=true;
		 }
 		 
 		 //
 		 /////////////////////////////
			 
 		 // if a collision has happened:
		 if(hor_col && ver_col)
		 {
 			 moveFeet(block.getTopBound()); //line feet up with block
 			 land(block);
 			 break;
     		
     	 }
      
 	 	}		 
 	  
	}
	
	public void updateBlock( Block mBlock )
	{
		//update the sprite's block
		onBlock = mBlock;
		blockRB = onBlock.getRightBound(); 
		blockLB = onBlock.getLeftBound();	
	}
	
	public void land( Block mBlock) {
		
		// in air >> on ground
		// triggers when: collision with block is detected
		
		 onBlock = mBlock;  //save the block that we landed on.

		blockRB = onBlock.getRightBound(); //TODO: is it bad to pass a block? (memory) 
		blockLB = onBlock.getLeftBound();

		 mDy=0;
		 mD2y=0;
		 db_landcount++;
		
		 if(mDx==0)
		 {
			 currentImg=BMP_REST;
			 movementState=STATE_ONGROUND;
		 }
		 else
		 {
			 movementState=STATE_RUN;
		 }
			 

	}
	
	public void setState( int newstate )
	{
		movementState = newstate;
	}

	public int getCurrentImg()
	{
		return currentImg;
	}
	
	public void incrementImage()
	{
		currentImg = ( currentImg + 1 ) % (runLength);
	}
	
	public boolean getDirection()
	{
		return flipBmp;
	}
	
	public void setGravity(int grav)
	{
		gravity = grav;
	}
	
	public void setRunLength(int l)
	{
		runLength=l;
	}
}

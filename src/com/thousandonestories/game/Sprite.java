package com.thousandonestories.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Sprite implements GameObject{
   protected float mX;
   protected float mY;
   protected float bX;
   protected float bY;
   public float mDx;
   public float mDy;
   public float mD2x;
   public float mD2y;
   public Bitmap drawBitmap;
   public float mHeight;
   public float mWidth;
   public long t;
   private boolean hidden=false;
   public static final int VEL_MAX= 15;
   public float sf;
   private boolean persistent;
   protected Rect drawRect;
   protected Paint paint;
   
   // Constructor
   public Sprite(Resources res, float x, float y, Bitmap bmps[], Bitmap reversedbmps[], float scalefactor) {
	   
      drawBitmap = bmps[0];
      sf = scalefactor;

      mWidth = drawBitmap.getWidth() * sf;
      mHeight = drawBitmap.getHeight() * sf;
      
      // Store upper left corner coordinates
      mX = x - mWidth/2 ;
      mY = y - mHeight/2;
      
      // Store bottom right corner coordinates:
      bX = x + mWidth/2;
      bY = y + mHeight/2;

      
      t = System.currentTimeMillis();
      
      persistent=false;
      // Set random velocity (floaty objects)
//      Random rand = new Random();
//      mDx = rand.nextInt(7) - 3;
//      mDy = rand.nextInt(7) - 3;
      
      drawRect = new Rect();
      paint = new Paint();
      paint.setColor(Color.WHITE);
      drawRect.set( (int) getLeftBound(), (int) getTopBound(), (int) ( getRightBound()) , (int) (getBottomBound() ) );
   }

   // Render bitmap at current location
   public void doDraw(Canvas canvas) {
	   
	   //canvas.drawRect(drawRect, paint);
	   
//	   if( sf==1 )
//	   {
//		   canvas.drawBitmap(drawBitmap, mX, mY, null); // goes by top left
//	   }
//	   else
	   {
		   canvas.drawBitmap(drawBitmap, null, drawRect, paint);
	   }
   }

   // Update (time-based) position
   public void update(long elapsedTime) {
	   
	   mDx += mD2x * (elapsedTime / 20f ); 
	   if(mDy<= VEL_MAX) // upper limit
		{
		   mDy += mD2y * (elapsedTime / 20f ); 
		}

      mX += mDx * (elapsedTime / 20f);
      mY += mDy * (elapsedTime / 20f);

      //TODO: careful: order of changing velocity vs position?
      
      bX = mX + mWidth;
      bY = mY + mHeight;
      
      t = System.currentTimeMillis(); //TODO: how long does this take?
      
      //checkBoundary();
      drawRect.set( (int) getLeftBound(), (int) getTopBound(), (int) ( getRightBound()) , (int) (getBottomBound() ) );
           
   }
   
   public void hide()
   {
	   hidden=true;
   }
   
   public void show()
   {
	   hidden=false;
   }
   
   public boolean hidden()
   {
	   return hidden;
   }
   
   public void deactivate()
   {
	   
   }
   
   public void moveFeet(float feetpos)
   {
	   bY=feetpos;
	   mY= bY - mHeight;
   }
   
   public float getLeftBound()
   {
	   return mX;
   }
   
   public float getRightBound()
   {
	   return bX;
   }
   
   public float getTopBound()
   {
	   return mY;
   }
   
   public float getBottomBound()
   {
	   return bY;
   }
   
   public void changeXDir()
   {
	   mDx *= -1;
   }
   
   public void changeYDir()
   {
	   mDy *= -1;
   }
   public float getScaleFactor()
   {
	   return sf;
   }
   

   @Override
	public void scroll(float vel, long elapsedTime) {
		   mX += vel * elapsedTime;	
		   bX += vel * elapsedTime;	
	}

	@Override
	public boolean isPersistent() {
		return persistent;
	}
	
	public void setPersistent(boolean val)
	{
		persistent=val;
	}
	
	public float getHeight()
	{
		return mHeight;
	}
	
	public float getWidth()
	{
		return mWidth;
	}
	
	public float getVelocity()
	{
		return (float) Math.pow( ( Math.pow( mDx, 2)  + Math.pow(mDy, 2) ), .5 );
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
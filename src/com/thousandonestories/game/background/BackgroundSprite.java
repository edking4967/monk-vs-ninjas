package com.thousandonestories.game.background;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class BackgroundSprite implements BackgroundScenery {

	private Bitmap mImg;
	private float mX, mY, mWidth, mHeight;
	private Rect drawRect;
	private float sf;
	
	public BackgroundSprite(Bitmap img, float x, float y, float scalefactor)
	{
		mImg = img;
		mX=x;
		mY=y;
		
		sf = scalefactor;
		
		mWidth = img.getWidth() * scalefactor;
		mHeight = img.getHeight() * scalefactor;
		
		drawRect = new Rect();
	}
	
	@Override
	public void doDraw(Canvas c) {

		if(sf !=1 )
		{
			drawRect.set( (int) mX, (int) mY, (int) ( mX+mWidth) , (int) (mY+mHeight) );
			c.drawBitmap( mImg, null, drawRect, null );

		}
		else
			c.drawBitmap( mImg, mX, mY, null );
	}

	@Override
	public void scroll(float vel, long elapsedTime) {
		mX += vel * elapsedTime;

	}

	public float getWidth() {
		return mWidth;
	}

}

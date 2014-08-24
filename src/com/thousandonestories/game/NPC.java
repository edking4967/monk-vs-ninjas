package com.thousandonestories.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class NPC extends NewSprite
{

	private String speechString;
	private long speechStartTime;
	private boolean speaking;
	private long speechTime;
	private Bitmap speechBubbleImg;
	private Paint mPaint;
	private Rect speechRect;
	private float speechWidth, speechHeight;
	
	public NPC( SpriteResources spriteRes, float x, float y, int scalefactor )
	{
		super( spriteRes, x, y, scalefactor );
		speaking = false;
		
		speechBubbleImg=spriteRes.getSpeechBubble();
			
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		
		mPaint.setTextSize(20);
		
		
		// set up speech bubble: 
		
		speechRect = new Rect();
		
		int speechBubble_scalefactor = 5;
		
		speechWidth = speechBubbleImg.getWidth() * speechBubble_scalefactor;
		speechHeight= speechBubbleImg.getHeight() * speechBubble_scalefactor;
		
		speechRect.set( (int) getLeftBound(), (int) getTopBound(), (int) ( getLeftBound()+speechWidth) , (int) (getTopBound()+speechHeight) );
		
	}
	
	public void update(long elapsedTime)
	{
		super.update(elapsedTime);
		if( System.currentTimeMillis() - speechStartTime >= speechTime)
		{
			speaking = false;
		}
		
		//update position of speech bubble:
		
	   speechRect.set( (int) getLeftBound(), (int) getTopBound(), (int) ( getLeftBound()+speechWidth) , (int) (getTopBound()+speechHeight) );

	}
	
	public void speak(String str, long time)
	{
		//Make the NPC "say" str for "time" milliseconds.
		speechString = str;
		speaking=true;
		speechTime=time;
		speechStartTime = System.currentTimeMillis();
	}
	
	public void doDraw(Canvas canvas)
	{
		super.doDraw(canvas);
		if(speaking)
		{
			
			canvas.drawBitmap( speechBubbleImg, null, speechRect, null);
			canvas.drawText(speechString, (  getLeftBound() + speechWidth/2 ) ,  // draw in the middle of speech bubble
					( getTopBound() + speechHeight /2 ) , mPaint);
		}
	}

	
	
}
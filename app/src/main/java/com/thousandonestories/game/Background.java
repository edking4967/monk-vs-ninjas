package com.thousandonestories.game;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Background {
	
	private int mWidth, mHeight;
	private Bitmap drawBitmap;
	private boolean repeatVert, repeatHor;
	private Paint mPaint;
	
	public Background( Resources res, int imgId, boolean repeatVertical, boolean repeatHorizontal)
	{
	      // Get bitmap from resource file
	      drawBitmap = BitmapFactory.decodeResource(res, imgId);
	      
	      mWidth = drawBitmap.getWidth();
	      mHeight = drawBitmap.getHeight();
	      repeatVert=repeatVertical;
	      repeatHor = repeatHorizontal;
	      mPaint = new Paint(); 
	      mPaint.setColor(Color.WHITE);
	      
	}
	
	public void doDraw(Canvas canvas)
	{
		int x=0; int y=0;
		while(y < GameManager.mHeight)
		{
			while(x  < GameManager.mWidth )
			{
				canvas.drawBitmap(drawBitmap, (float) x, (float) y, null);
				x+=mWidth;
			}
			y+=mHeight;
			x=0;
		}
		
	}

}

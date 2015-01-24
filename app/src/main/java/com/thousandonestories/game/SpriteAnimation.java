package com.thousandonestories.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

public class SpriteAnimation {
	
	private Bitmap [] bmps;
	private Bitmap [] bmps_flipped;
	private int length;
	private long startTime;
	private Bitmap currentFrame;
	private int currentFrameNum;
	private boolean playing;
	private long currentTime;
	
	/**
	 * Whether the sprite is flipped.
	 */
	private boolean flipped;
	private int animSpeed = 1;
	private float height[];
	private float width[];
	
	/**
	 * Whether or not to repeat the animation.
	 */
	private boolean repeat;
	
	public SpriteAnimation(int[] bmp_nums, Resources res, boolean pixelart, int sampleSize )
	{
		int i;
		//import bitmaps designated in bmp_nums
		length=bmp_nums.length;
		
		bmps = new Bitmap[length];
		
		playing=false;
		flipped=false;
		setRepeat(true);
		
		height = new float[length];
		width = new float[length];
		
		BitmapFactory.Options o = new BitmapFactory.Options();

		o.inScaled = false;
		
		o.inSampleSize = sampleSize;
		
		if(pixelart)
		{
			o.inDither = false;
		}
				
		for(i=0; i<bmp_nums.length; i++)
		{
			bmps[i] = BitmapFactory.decodeResource( res, bmp_nums[i], o);
			height[i] = bmps[i].getHeight();
			width[i] = bmps[i].getWidth();
		}
		
		currentFrameNum=0;
		
		currentFrame = bmps[0];
		
	}
	
	public void start(int startFrame)
	{
		startTime = System.currentTimeMillis();
		currentFrameNum = startFrame;
		currentFrame = getBitmap(startFrame);
		playing=true;
	}
	
	
	/**
	 * update: update animation on time
	 * @return true if animation changes.
	 */
	public boolean update()
	{
		currentTime = System.currentTimeMillis();
		if(!playing)
		{
			return false;
		}
		else
		{
			if(currentTime - startTime >= 20 * animSpeed) // it's time to go on to the next frame
			{
				
				if(currentFrameNum == length-1 && isRepeat() == false)
				{
					playing = false;
					currentFrameNum = 0;
					currentFrame =getBitmap(currentFrameNum);
					return true;
				}
				
				currentFrameNum =  ( currentFrameNum + 1 ) % (length);
				currentFrame=getBitmap(currentFrameNum);
				startTime = currentTime;
				return true; 
			}
			else return false;
		}
						
	}
	
	public Bitmap getCurrentFrame()
	{
		return currentFrame;
	}
	
	public void setCurrentFrame(int frameNum)
	{
		currentFrame = getBitmap(frameNum);
	}
	
	//
	public int getLength()
	{
		return length;
	}
	
	public Bitmap getInitialFrame( )
	{
		return getBitmap(0);
	}
	
	public void setBmps(Bitmap[] newBmps)
	{
		bmps = newBmps;
	}
	
	public void makeFlippedBmps()
	{
		bmps_flipped = flipBmpHorizontal( bmps );
	}
	
	public Bitmap[] flipBmpHorizontal( Bitmap[] input )
	{
		int i;
		Bitmap output[] = new Bitmap[input.length];
		Matrix m = new Matrix();
	    m.preScale(-1, 1);
		for(i=0; i< input.length; i++)
	    {
    	    //flip bitmap:
			
    	    output[i]=Bitmap.createBitmap(input[i], 0, 0, input[i].getWidth(), input[i].getHeight(), m, false);
    	    output[i].setDensity(DisplayMetrics.DENSITY_DEFAULT);

	    }
		return output;
	}
	
	public Bitmap getBitmap( int bmpNum )
	{
		if( !flipped )
			return bmps[bmpNum];
		else
			return bmps_flipped[bmpNum];
	}

	public Bitmap getFinalFrame() {
		return getBitmap( length-1 );
	}
	
	public void setAnimSpeed(int speed)
	{
		animSpeed=speed;
	}

	public boolean isRepeat() {
		return repeat;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public void stop() {
		playing = false;
	}
	
	public boolean isPlaying()
	{
		return playing;
	}
	
}

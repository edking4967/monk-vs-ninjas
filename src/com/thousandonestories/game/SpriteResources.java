package com.thousandonestories.game;

import android.content.res.Resources;
import android.graphics.Bitmap;

//Sprite resources: store resources for a sprite.

public class SpriteResources{

	private SpriteAnimation[] anims; //array of animations for a sprite.
	private SpriteAnimation currentAnimation;
	private Resources mRes;
	
	public SpriteResources( Resources res, boolean pixelart, int sampleFactor, int[]... animations )
	{
		anims = new SpriteAnimation[animations.length];
		int i;
		
		for( i = 0; i< animations.length; i++)
		{
			anims[i] = new SpriteAnimation(animations[i], res, pixelart, sampleFactor);
		}
		
		mRes = res;
		
		currentAnimation=anims[0];
	}
	

	public SpriteAnimation[] getSpriteAnimations()
	{
		return anims;
	}
	
	public Bitmap getCurrentFrame()
	{
		return currentAnimation.getCurrentFrame();
	}
	
	public Bitmap getInitialFrame()
	{
		return currentAnimation.getInitialFrame();
	}


	public void startAnimation(int animationNum, int startFrame) {
		
		currentAnimation = anims[animationNum];
		currentAnimation.start(startFrame) ;

	}
	
	public void stopOnFrame(int animationNum, int frameNum)
	{
		currentAnimation = anims[animationNum];
		currentAnimation.setCurrentFrame(frameNum);
		currentAnimation.stop();
	}
	
	public boolean updateAnimation()
	{
		return currentAnimation.update();
	}

	public void createFlippedBitmaps() {
		int i;
		for(i=0; i<anims.length; i++)
		{
			anims[i].makeFlippedBmps();
		}
	}

	public Bitmap getLastFrame() {
		return currentAnimation.getFinalFrame();
	}

	public Bitmap getSpeechBubble() {
		 
		return anims[ anims.length - 1 ].getInitialFrame();
	}
	
	public Resources getAndroidResources()
	{
		return mRes;
	}


	public SpriteAnimation getCurrentAnimation() {
		// TODO Auto-generated method stub
		return currentAnimation;
	}
	
}
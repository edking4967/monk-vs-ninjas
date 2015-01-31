package com.thousandonestories.game.gameobjects;

import com.thousandonestories.game.SpriteResources;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;

public class NewSprite implements GameObject
{
	private SpriteResources spriteResources;

	private float mLeftBound; 
	private float mTopBound;
	private float mRightBound; 
	private float mBottomBound;

	private float mWidth;
	private float mHeight;

	
	private Rect drawRect;
	
	private float sf;

	private boolean hidden;
	private boolean persistent;
	private boolean flipped;

	private Paint paint;

	/**
	 * health of this sprite 
	 */
	private int health;

	/**
	 * Amount the sprite will be rotated when drawn.
	 */
	private float rotation;

	/**
	 * The time when a tint is set by the "flash" function 
	 */
	private long tintStartTime;

	/**
	 * The duration of a tint set by the "flash" function 
	 */
	private long tintDuration;

	public NewSprite( SpriteResources spriteRes, float x, float y, float scalefactor )
	{
		spriteResources=spriteRes;
		setLeftBound(x);
		setTopBound(y);

		setWidth(spriteResources.getCurrentFrame().getWidth() * scalefactor );
		setHeight(spriteResources.getCurrentFrame().getHeight() * scalefactor);

		setRightBound(x+getWidth() );
		setBottomBound(y + getHeight() );

		sf = scalefactor;

		flipped = false;

		drawRect = new Rect(); 

		paint = new Paint();
	}

	public void update( long elapsedTime)
	{
		updateAnimation( elapsedTime ); 

		setRightBound(getLeftBound() + getWidth());
		setBottomBound(getTopBound() + getHeight());

		drawRect.set( (int) getLeftBound(), (int) getTopBound(), (int)  getRightBound() , (int) getBottomBound() );

		if(System.currentTimeMillis() - tintStartTime >= tintDuration)
		{
			clearFlash();
		}
	}

	public void doDraw( Canvas canvas )
	{
		if(getRotation() != 0)
		{
			Bitmap bitmap = spriteResources.getCurrentFrame();
			Rect rect = new Rect((int) getLeftBound(), (int) getTopBound(),bitmap.getWidth(), bitmap.getHeight());
			Matrix matrix = new Matrix();
			float px = rect.exactCenterX();
			float py = rect.exactCenterY();
			matrix.postTranslate(-bitmap.getWidth()/2, -bitmap.getHeight()/2);
			matrix.postRotate(getRotation());
			matrix.postTranslate(px, py);

			matrix.postScale(sf, sf);

			canvas.drawBitmap(bitmap, matrix, null);
			matrix.reset();
			bitmap = null;
			return;
		}

		canvas.drawBitmap( spriteResources.getCurrentFrame(), null, drawRect, paint );



	}

	public boolean updateAnimation( long elapsedTime )
	{
		if( spriteResources.updateAnimation() ) // animation has changed
		{
			// Update boundaries:
			setWidth(spriteResources.getCurrentFrame().getWidth() * sf );
			setHeight(spriteResources.getCurrentFrame().getHeight() * sf);
			return true;
		}
		else return false;

	}

	public void startAnimation( int animationNumber )
	{
		spriteResources.startAnimation( animationNumber, 0 ) ;
	}

	public void stopOnFrame(int animationNum, int frameNum)
	{
		spriteResources.stopOnFrame(animationNum, frameNum);
	}

	/**
	 *  @param  speed Speed of animation.
	 */	
	public void startAnimation(int animationNumber, int speed) {
		spriteResources.getSpriteAnimations()[animationNumber].setAnimSpeed(speed);
		spriteResources.startAnimation( animationNumber, 0 ) ; 
	}

	/**
	 * 
	 * @param number 	Animation number to play.
	 * @param speed 	Speed of animation.
	 * @param repeat	Whether to repeat the animation.
	 */
	public void startAnimation(int number, int speed, int startFrame, boolean repeat) {
		spriteResources.getSpriteAnimations()[number].setAnimSpeed(speed);
		spriteResources.getSpriteAnimations()[number].setRepeat(false);
		spriteResources.startAnimation( number, startFrame ) ; 

	}

	public void flash(int color, long duration)
	{
		ColorFilter cf = new PorterDuffColorFilter( color, Mode.MULTIPLY);
		paint.setColorFilter(cf);
		tintDuration = duration;
		tintStartTime = System.currentTimeMillis();
	}

	public void setTint(int color)
	{
		ColorFilter cf = new PorterDuffColorFilter( color, Mode.MULTIPLY);
		paint.setColorFilter(cf);

	}

	public boolean animationIsPlaying()
	{
		return spriteResources.getCurrentAnimation().isPlaying();
	}
	
	public void clearTint()
	{
		paint.setColorFilter(null);
	}

	public void clearFlash()
	{
		paint.setColorFilter(null);
	}

	@Override
	public float getLeftBound() {
		return mLeftBound;
	}

	@Override
	public float getRightBound() {
		return mRightBound;
	}

	@Override
	public float getTopBound() {
		return mTopBound;
	}

	@Override
	public float getBottomBound() {
		return mBottomBound;
	}

	@Override
	public void scroll(float amt, long elapsedTime) {
		setLeftBound(getLeftBound() + amt * elapsedTime);

	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		hidden=true;		
	}

	@Override
	public void show() {
		hidden=false;		
	}

	@Override
	public boolean hidden() {
		return hidden;
	}

	public boolean isPersistent()
	{
		return persistent;
	}

	public void setLeftBound(float mLeftBound) {
		this.mLeftBound = mLeftBound;
	}

	public void setTopBound(float mTopBound) {
		this.mTopBound = mTopBound;
	}

	public void setRightBound(float mRightBound) {
		this.mRightBound = mRightBound;
	}

	public void setBottomBound(float mBottomBound) {
		this.mBottomBound = mBottomBound;
	}

	public float getWidth() {
		return mWidth;
	}

	public void setWidth(float mWidth) {
		this.mWidth = mWidth;
	}

	public float getHeight() {
		return mHeight;
	}

	public void setHeight(float mHeight) {
		this.mHeight = mHeight;
	}

	public void setPersistent(boolean val) {
		persistent = val;		
	}

	public void rotate(float degrees) {
		setRotation(getRotation() + degrees);
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public SpriteResources getSpriteResources() {
		return spriteResources;
	}


	public void flip()
	{
		flipped = !flipped;
	}

	public boolean isFlipped()
	{
		return flipped;
	}

	public Paint getPaint()
	{
		return paint;
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void decrementHealth()
	{
		this.health--;
	}

}


package com.thousandonestories.game;

import android.graphics.Canvas;

public interface GameObject
{
	public float getLeftBound();
	public float getRightBound();
	
	public float getTopBound();
	
	public float getBottomBound();
	
	void doDraw(Canvas canvas);
	
	void update(long elapsedTime);
	public void scroll(float amt, long elapsedTime);
	
	public void deactivate(); //remove a GameObject that's no longer needed. TODO: deconstructor?
	
	public void hide();
	public void show();
	public boolean hidden();
	public boolean isPersistent();
	public float getWidth();
	public float getHeight();
	
	public void setWidth( float width );
	public void setHeight( float height);
	
}
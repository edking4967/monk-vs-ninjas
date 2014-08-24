package com.thousandonestories.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Block implements GameObject
{
	private Rect mRect;
	private Paint paint;
	public boolean fixed;
	private float LB;
	private float RB;
	private float TB;
	private float BB;
	private boolean hidden = false;
	private boolean persistent;
	private float mHeight;
	private float mWidth;
	
	public Block(float lB, float rB, float tB, float bB, int color)
	{
		mRect = new Rect((int) lB, (int) tB, (int) rB, (int) bB);
		paint = new Paint();
		paint.setColor(color);
		fixed = true;
		LB=lB; RB=rB; TB=tB; BB=bB;
		persistent=true;
		mHeight = BB-TB;
		mWidth = RB-LB;
	}
	

	
	public float getLeftBound()
	{
		return LB;
	}
	public float getRightBound()
	{
		return RB;
	}
	public float getTopBound()
	{
		return TB;
	}
	public float getBottomBound()
	{
		return BB;
	}
	
	public void doDraw(Canvas canvas)
	{
		canvas.drawRect(mRect, paint);
	}
	
	public void update(long elapsedTime)
	{
		mRect.set((int) LB, (int) TB, (int) RB, (int) BB);
	}

	@Override
	public void scroll(float vel, long elapsedTime ) {
			LB+=vel*elapsedTime;
			RB+=vel*elapsedTime;
		
	}



	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	public float getHeight()
	{
		return mHeight;
	}
	
	public float getWidth()
	{
		return mWidth;
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



	@Override
	public void setWidth(float width) {
		mWidth =  width;
	}



	@Override
	public void setHeight( float height ) {
		mHeight = height;
	}
	
}
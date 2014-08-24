package com.thousandonestories.game;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class Projectile extends Sprite
{

	public static final int TYPE_ENEMY=1;
	public static final int TYPE_HERO=0;
	
	private int type;
	
	public Projectile(Resources res, float x, float y, int dx , Bitmap bmps[], int projtype, float scalefactor) {
		super(res, x, y, bmps, bmps, scalefactor);
		mDx = dx;
		type=projtype;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public void update(long elapsedTime, long scrollPos)
	{
		super.update(elapsedTime);
		
		
		if( getLeftBound() >= Panel.mWidth  || getRightBound() <= 0 )
		{
			//deactivate();
			hide();
		}
		
	}
	
	public void deactivate()
	{
		//this=null;
	}
	
}
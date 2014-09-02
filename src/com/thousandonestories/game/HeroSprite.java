package com.thousandonestories.game;

import java.util.concurrent.CopyOnWriteArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class HeroSprite extends GravitySprite {
	
	CopyOnWriteArrayList<Projectile> mProjList;
	private int health;	
	
	public static final int WEAPON_PROJECTILE=0;
	public static final int WEAPON_SWORD = 1;
	
	/**
	 * The weapon that the hero currently has
	 */
	private int weapon; 
	
	private Bitmap swordBmp;
	
	public HeroSprite(Resources res, int x, int y, Bitmap bmps[], Bitmap reversebmps[], CopyOnWriteArrayList<Projectile> projList, float scalefactor) {
		super(res, x, y, bmps, reversebmps, scalefactor);
		jumpVel = (int) (40*scalefactor);
		mProjList=projList;
		health=100;
		
		setWeapon(WEAPON_PROJECTILE);
		
		Log.d("bleh", "hero constructor called");
		
		this.swordBmp = Panel.swordBmp;
	}

	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void doDraw(Canvas c)
	{
		super.doDraw(c);
		if(weapon==WEAPON_SWORD)
		{
			c.drawBitmap(swordBmp, getRightBound(), getYMiddle(), null );
		}
	}
	
	@Override
	public boolean checkProjectile(CopyOnWriteArrayList<Projectile> mProjList) {
		if( super.checkProjectile(mProjList))
		{
			setHealth( getHealth() - 25);
			
			Panel.blip.start();
			
			return true;
		}
		else
			return false;
	}

	@Override
	public void update(long elapsedTime)
	{
		super.update(elapsedTime);
		checkProjectile(mProjList);
	}
	
	public void setWeapon( int weapon )
	{
		this.weapon= weapon;
	}
}

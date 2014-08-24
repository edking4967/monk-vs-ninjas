package com.thousandonestories.game;

import java.util.concurrent.CopyOnWriteArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

public class HeroSprite extends GravitySprite {
	
	CopyOnWriteArrayList<Projectile> mProjList;
	private int health;	
	
	public HeroSprite(Resources res, int x, int y, Bitmap bmps[], Bitmap reversebmps[], CopyOnWriteArrayList<Projectile> projList, float scalefactor) {
		super(res, x, y, bmps, reversebmps, scalefactor);
		jumpVel = (int) (40*scalefactor);
		mProjList=projList;
		health=100;
		
		Log.d("bleh", "hero constructor called");
	}

	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	@Override
	public boolean checkProjectile(CopyOnWriteArrayList<Projectile> mProjList) {
		if( super.checkProjectile(mProjList))
		{
			setHealth( getHealth() - 10);
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
	
}

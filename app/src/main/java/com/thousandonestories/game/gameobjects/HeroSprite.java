package com.thousandonestories.game.gameobjects;

import java.util.concurrent.CopyOnWriteArrayList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.management.GlobalConstants;
import com.thousandonestories.synth.SynthCore;

public class HeroSprite extends GravitySprite {
	
	CopyOnWriteArrayList<Projectile> mProjList;
	private int health;	
    GameManager gm;
	
	public static final int WEAPON_PROJECTILE=0;
	public static final int WEAPON_SWORD = 1;
	
	/**
	 * The weapon that the hero currently has
	 */
	private int weapon; 
	
	private Bitmap swordBmp;

    private SynthCore synth;
    Resources res;
	
	public HeroSprite(Resources res, int x, int y, Bitmap bmps[], Bitmap reversebmps[], CopyOnWriteArrayList<Projectile> projList, float scalefactor) {
		super(res, x, y, bmps, reversebmps, scalefactor);
		jumpVel = (int) (40*scalefactor);
		mProjList=projList;
		health=100;
		
		setWeapon(WEAPON_PROJECTILE);
		
		Log.d("bleh", "hero constructor called");
		
		this.swordBmp = GameManager.swordBmp;

        //synth = new SynthCore();
        //
        gm = GlobalConstants.gm;
        this.res = res;
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
			
			GameManager.blip.start();
			
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

    int toneIndex = 0;
    @Override
    public void jump()
    {
        super.jump();
        /*
        synth.addNote(synth.getTone(toneIndex), 8000, 100, 1000, false, 0);
        if(toneIndex < synth.getNumTones()-1)
            toneIndex++;
        else
            toneIndex = 0;
            */
    }

    @Override
    public void land( Block mBlock) {
        super.land(mBlock);
        if(mBlock.tags.contains("loadLevelTwo")) // Don't do string compare
        {
            gm.gameStartLevelTwo();  // TODO: progressToLevelTwo: do after timer / music etc
        }
    }
}

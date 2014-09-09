package com.thousandonestories.game.gameobjects;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Canvas;

import com.thousandonestories.game.BitmapCache;
import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.SpriteResources;

public class NewHeroSprite extends NewPhysicsSprite {

	private CopyOnWriteArrayList<Projectile> projectileList;
	private GameManager gm;
	
	public NewHeroSprite(GameManager gm, float x, float y, int scalefactor)
	{
		
		super( BitmapCache.getHeroRes(gm), x, y, scalefactor, new PhysicsStuff(10,10) );
		this.gm=gm;
		projectileList = new CopyOnWriteArrayList<Projectile>();
	}
	
	public NewHeroSprite(SpriteResources spriteRes, float x, float y,
			int scalefactor, PhysicsStuff phys) {
		super(spriteRes, x, y, scalefactor, phys);
		
		projectileList = new CopyOnWriteArrayList<Projectile>();
	}
	
	@Override
	public void update(long dt)
	{
		super.update(dt);
		
		for(Projectile p: projectileList)
		{
			p.update(dt);
		}

	}
	
	@Override
	public void doDraw(Canvas c)
	{
		super.doDraw(c);
		
		for(Projectile p: projectileList)
		{
			p.doDraw(c);
		}
		
	}

	
	public CopyOnWriteArrayList<Projectile> getProjList() 
	{
		return projectileList;
	}

}

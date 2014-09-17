package com.thousandonestories.game.gameobjects;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Canvas;
import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.bitmap.BitmapCache;
import com.thousandonestories.game.ui.Camera;

public class NewHeroSprite extends NewPhysicsSprite {

	private CopyOnWriteArrayList<Projectile> projectileList;
	
	public NewHeroSprite(GameManager gm, float x, float y, int bitmap_sample_factor)
	{
		
		super( BitmapCache.getHeroRes(gm, bitmap_sample_factor), x, y, 1, new PhysicsStuff(10,60) );
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
	public void doDraw(Canvas c, Camera camera)
	{
		super.doDraw(c,camera);
		
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

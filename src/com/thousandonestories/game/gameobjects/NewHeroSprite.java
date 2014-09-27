package com.thousandonestories.game.gameobjects;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.bitmap.BitmapCache;

public class NewHeroSprite extends NewPhysicsSprite {

	
	public NewHeroSprite(GameManager gm, float x, float y, int bitmap_sample_factor)
	{
		
		super( BitmapCache.getHeroRes(gm, bitmap_sample_factor), x, y, 1, new PhysicsStuff(4,60) );
	}
	
	public NewHeroSprite(SpriteResources spriteRes, float x, float y,
			int scalefactor, PhysicsStuff phys) {
		super(spriteRes, x, y, scalefactor, phys);
		
	}
	
	@Override
	public void update(long dt)
	{
		super.update(dt);

	}
	
}

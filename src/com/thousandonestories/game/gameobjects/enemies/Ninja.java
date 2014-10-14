package com.thousandonestories.game.gameobjects.enemies;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.graphics.BitmapCache;
import com.thousandonestories.game.graphics.SpriteResources;

public class Ninja extends NewEnemy{

	public Ninja(SpriteResources spriteRes, float x, float y, int scalefactor,
			PhysicsStuff phys) {
		super(null, spriteRes, x, y, scalefactor, phys);
	}
	
	public Ninja(GameManager gm, float x, float y, int bitmap_sample_factor)
	{
		super( gm, BitmapCache.getNinjaRes(gm, bitmap_sample_factor), x, y, 1, new PhysicsStuff(5,60) );
	}


}

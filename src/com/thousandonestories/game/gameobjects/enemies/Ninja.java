package com.thousandonestories.game.gameobjects.enemies;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.bitmap.BitmapCache;

public class Ninja extends NewEnemy{

	public Ninja(SpriteResources spriteRes, float x, float y, int scalefactor,
			PhysicsStuff phys) {
		super(spriteRes, x, y, scalefactor, phys);
	}
	
	public Ninja(GameManager gm, float x, float y, int bitmap_sample_factor)
	{
		super( BitmapCache.getNinjaRes(gm, bitmap_sample_factor), x, y, 1, new PhysicsStuff(5,60) );
	}


}

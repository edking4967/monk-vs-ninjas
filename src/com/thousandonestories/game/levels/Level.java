package com.thousandonestories.game.levels;

import java.util.concurrent.CopyOnWriteArrayList;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.thousandonestories.background.BackgroundScenery;
import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.gameobjects.Block;
import com.thousandonestories.game.gameobjects.GameObject;

public abstract class Level {

	private CopyOnWriteArrayList<Block> blockList;
	private CopyOnWriteArrayList<GameObject> gameObjList;
	private CopyOnWriteArrayList<BackgroundScenery> bgSceneryList;
	
	private Resources mRes;
	protected GameManager gm;

	public Level(GameManager gm)
	{
		blockList = new CopyOnWriteArrayList<Block>();
		gameObjList = new CopyOnWriteArrayList<GameObject>();
		bgSceneryList = new CopyOnWriteArrayList<BackgroundScenery>();
		this.gm=gm;
		mRes = gm.getResources();
	}

	public CopyOnWriteArrayList<Block> getBlockList()
	{
		return blockList;
	}

	public CopyOnWriteArrayList<GameObject> getGameObjList()
	{
		return gameObjList;
	}
	
	public CopyOnWriteArrayList<BackgroundScenery> getBgSceneryList()
	{
		return bgSceneryList;
	}


	public abstract void loadBitmaps();  //?

	public void loadBitmaps(int[][]... bitmaps )
	{
		for( int i=0; i<bitmaps.length; i++)
		{
			new SpriteResources(mRes, false, 1, bitmaps[i]);
		}
	}

	public void addBlock(Block b)
	{
		blockList.add(b);
	}

	public abstract void start();

	public void doDraw(Canvas c)
	{
		for( BackgroundScenery bg: bgSceneryList)
		{
			bg.doDraw(c);
		}
		for ( Block b: blockList )
		{
			b.doDraw(c);
		}
	}

}
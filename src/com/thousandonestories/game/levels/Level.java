package com.thousandonestories.game.levels;

//import com.badlogic.gdx.JsonWriter;

import java.util.concurrent.CopyOnWriteArrayList;

import android.content.res.Resources;
import org.json.JSONObject;

import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.gameobjects.Block;
import com.thousandonestories.game.gameobjects.GameObject;

public abstract class Level {

	private CopyOnWriteArrayList<Block> blockList;
	private CopyOnWriteArrayList<GameObject> gameObjList;
	private Resources mRes;
	//private TextBlock textBlock;

	public Level(Resources res)
	{
		blockList = new CopyOnWriteArrayList<Block>();
		gameObjList = new CopyOnWriteArrayList<GameObject>();
		mRes = res;

	}

	public CopyOnWriteArrayList<Block> getBlockList()
	{
		return blockList;
	}
	
	public CopyOnWriteArrayList<GameObject> getGameObjList()
	{
		return gameObjList;
	}
	
	public abstract void loadBitmaps();  //?
	
	public void loadBitmaps(int[][]... bitmaps )
	{
		for( int i=0; i<bitmaps.length; i++)
		{
			new SpriteResources(mRes, false, 1, bitmaps[i]);
		}
	}
	
	public abstract void start();
}

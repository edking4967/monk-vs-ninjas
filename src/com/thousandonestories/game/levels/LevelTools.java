package com.thousandonestories.game.levels;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.R;
import com.thousandonestories.game.gameobjects.Block;

public class LevelTools {
	
	private Bitmap platformBitmap;
	
	
	public LevelTools(GameManager gm)
	{
		
		BitmapFactory.Options o = new BitmapFactory.Options();
		
		o.inScaled = false;
		
		platformBitmap = BitmapFactory.decodeResource( gm.getResources(), R.drawable.platform, o);
	}
	
	public static void addBlock(CopyOnWriteArrayList<Block> blockList, int left, int right, int top, int bottom, int color)
	{
		
		blockList.add(new Block(left, top, right, bottom, color));
		
//		mBlockList.add( block );
//		mGameObjList.add( block);
//
//		//Add platform image:
//		m3dPlatformList.add(
//				new BackgroundSprite(platformBitmap,left, top - 30, 6) );

	}

}

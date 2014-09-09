package com.thousandonestories.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.thousandonestories.game.gameobjects.GameObject;
import com.thousandonestories.game.gameobjects.NewHeroSprite;
import com.thousandonestories.game.gameobjects.Ninja;

public class BitmapCache {

	public static Context context;
	private static SpriteResources heroRes;
	private static SpriteResources ninjaRes;
	private static GameManager gm;

	public static Bitmap getBitmap(int imgId)
	{
		return BitmapFactory.decodeResource(context.getResources(), imgId);
	}

	public static SpriteResources getSpriteRes( GameObject sprite )
	{
		if(sprite instanceof Ninja)
		{
			return getNinjaRes();
		}
		else if(sprite instanceof NewHeroSprite)
		{
			return getHeroRes();
		}
		else return null;
	}

	public static SpriteResources getHeroRes()
	{

		if(heroRes != null) return heroRes;
		else
		{
			int imgs[] = new int[22];
			imgs[0]=R.drawable.monk01;
			imgs[1]=R.drawable.monk02;
			imgs[2]=R.drawable.monk03;
			imgs[3]=R.drawable.monk04;
			imgs[4]=R.drawable.monk05;
			imgs[5]=R.drawable.monk06;
			imgs[6]=R.drawable.monk07;
			imgs[7]=R.drawable.monk08;
			imgs[8]=R.drawable.monk09;
			imgs[9]=R.drawable.monk10;
			imgs[10]=R.drawable.monk11;
			imgs[11]=R.drawable.monk12;
			imgs[12]=R.drawable.monk13;
			imgs[13]=R.drawable.monk14;
			imgs[14]=R.drawable.monk15;
			imgs[15]=R.drawable.monk16;
			imgs[16]=R.drawable.monk17;
			imgs[17]=R.drawable.monk18;
			imgs[18]=R.drawable.monk19;
			imgs[19]=R.drawable.monk20;
			imgs[20]=R.drawable.monk21;
			
			if(context.getResources()==null) Log.d("hey", "resources is null");
			else Log.d("hey", "resources is not null");

			return new SpriteResources(context.getResources(), false, 1, imgs);

		}

	}
	
	public static SpriteResources getHeroRes(GameManager gm)
	{

		if(heroRes != null) return heroRes;
		else
		{
			int imgs[] = new int[22];
			imgs[0]=R.drawable.monk01;
			imgs[1]=R.drawable.monk02;
			imgs[2]=R.drawable.monk03;
			imgs[3]=R.drawable.monk04;
			imgs[4]=R.drawable.monk05;
			imgs[5]=R.drawable.monk06;
			imgs[6]=R.drawable.monk07;
			imgs[7]=R.drawable.monk08;
			imgs[8]=R.drawable.monk09;
			imgs[9]=R.drawable.monk10;
			imgs[10]=R.drawable.monk11;
			imgs[11]=R.drawable.monk12;
			imgs[12]=R.drawable.monk13;
			imgs[13]=R.drawable.monk14;
			imgs[14]=R.drawable.monk15;
			imgs[15]=R.drawable.monk16;
			imgs[16]=R.drawable.monk17;
			imgs[17]=R.drawable.monk18;
			imgs[18]=R.drawable.monk19;
			imgs[19]=R.drawable.monk20;
			imgs[20]=R.drawable.monk21;
			
			if(context.getResources()==null) Log.d("hey", "resources is null");
			else Log.d("hey", "resources is not null");

			return new SpriteResources(gm.getResources(), false, 1, imgs);

		}

	}

	public static SpriteResources getNinjaRes()
	{
		if(ninjaRes != null) return ninjaRes;
		else
		{
			int imgs[] = new int[22];
			imgs[0]=R.drawable.ninja_0;
			imgs[1]=R.drawable.ninja_1;
			imgs[2]=R.drawable.ninja_2;
			imgs[3]=R.drawable.ninja_3;
			imgs[4]=R.drawable.ninja_4;
			imgs[5]=R.drawable.ninja_5;
			imgs[6]=R.drawable.ninja_6;
			imgs[7]=R.drawable.ninja_7;
			imgs[8]=R.drawable.ninja_8;
			imgs[9]=R.drawable.ninja_9;
			imgs[10]=R.drawable.ninja_10;
			imgs[11]=R.drawable.ninja_11;
			imgs[12]=R.drawable.ninja_12;
			imgs[13]=R.drawable.ninja_13;
			imgs[14]=R.drawable.ninja_14;
			imgs[15]=R.drawable.ninja_15;
			imgs[16]=R.drawable.ninja_16;
			imgs[17]=R.drawable.ninja_17;
			imgs[18]=R.drawable.ninja_18;
			imgs[19]=R.drawable.ninja_19;
			imgs[20]=R.drawable.ninja_20;

			return new SpriteResources(context.getResources(), false, 1, imgs);
		}
	}

	public static void setContext(Context c) {
		context = c;
	}

}

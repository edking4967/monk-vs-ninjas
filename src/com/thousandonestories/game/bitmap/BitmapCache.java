package com.thousandonestories.game.bitmap;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.R;
import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.R.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class BitmapCache {

	private static SpriteResources heroRes;
	private static SpriteResources ninjaRes;
	private static Context context;

	public static SpriteResources getHeroRes(GameManager gm)
	{

		if(heroRes != null) return heroRes;
		else
		{
			int imgs[] ={
					R.drawable.monk01,
					R.drawable.monk02,
					R.drawable.monk03,
					R.drawable.monk04,
					R.drawable.monk05,
					R.drawable.monk06,
					R.drawable.monk07,
					R.drawable.monk08,
					R.drawable.monk09,
					R.drawable.monk10,
					R.drawable.monk11,
					R.drawable.monk12,
					R.drawable.monk13,
					R.drawable.monk14,
					R.drawable.monk15,
					R.drawable.monk16,
					R.drawable.monk17,
					R.drawable.monk18,
					R.drawable.monk19,
					R.drawable.monk20,
					R.drawable.monk21,
			};
			
//			int imgs2[] =
//				{
//					R.drawable.
//				};
			heroRes = new SpriteResources(gm.getResources(), false, 1, imgs);

			return heroRes;

		}

	}

	public static SpriteResources getNinjaRes(GameManager gm)
	{
		if(ninjaRes != null) return ninjaRes;
		else
		{
			int imgs[] ={
					R.drawable.ninja_1,
					R.drawable.ninja_2,
					R.drawable.ninja_3,
					R.drawable.ninja_4,
					R.drawable.ninja_5,
					R.drawable.ninja_6,
					R.drawable.ninja_7,
					R.drawable.ninja_8,
					R.drawable.ninja_9,
					R.drawable.ninja_10,
					R.drawable.ninja_11,
					R.drawable.ninja_12,
					R.drawable.ninja_13,
					R.drawable.ninja_14,
					R.drawable.ninja_15,
					R.drawable.ninja_16,
					R.drawable.ninja_17,
					R.drawable.ninja_18,
					R.drawable.ninja_19,
					R.drawable.ninja_20,
			};

			ninjaRes = new SpriteResources(gm.getResources(), false, 1, imgs);
			return ninjaRes;
		}
	}

	public static Bitmap[] getBitmaps(  int[] bmps_nums) {
		Bitmap [] bmps = new Bitmap[bmps_nums.length];
		for(int i = 0; i<bmps_nums.length; i++)
		{
			bmps[i] = BitmapFactory.decodeResource(context.getResources(), bmps_nums[i]);
		}
		return bmps;
	}
	public static Bitmap getBitmaps( int bmp_num) {
		int [] bmp_nums = { bmp_num }; 
		return getBitmaps( bmp_nums )[0];
	}

	public static void setContext(Context c)
	{
		BitmapCache.context = c;
	}
}

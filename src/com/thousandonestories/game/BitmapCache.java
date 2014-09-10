package com.thousandonestories.game;


public class BitmapCache {

	private static SpriteResources heroRes;
	private static SpriteResources ninjaRes;

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


}

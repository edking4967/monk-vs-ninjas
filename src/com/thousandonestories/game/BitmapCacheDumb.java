package com.thousandonestories.game;


public class BitmapCacheDumb {

	private SpriteResources heroRes;
	private GameManager gm;

	public BitmapCacheDumb(GameManager gm)
	{
		this.gm=gm;
	}
	
	public SpriteResources getHeroRes()
	{

		if(heroRes != null) return heroRes;
		else
		{
			int imgs[] = new int[21];
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
			
			return new SpriteResources(gm.getResources(), false, 1, imgs);

		}

	}

}

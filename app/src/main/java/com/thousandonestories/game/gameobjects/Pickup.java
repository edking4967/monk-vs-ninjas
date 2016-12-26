package com.thousandonestories.game.gameobjects;


import android.content.res.Resources;
import android.graphics.Bitmap;

public class Pickup extends Sprite
{
	public Pickup(Resources res, int x, int y, Bitmap[] bmps, float scalefactor)
	{
		super(res, x, y, bmps, bmps, scalefactor);
	}
}

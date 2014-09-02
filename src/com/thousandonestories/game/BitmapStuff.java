package com.thousandonestories.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapStuff {
	
	public static Context context;
	
	public static Bitmap getBitmap(int imgId)
	{
		return BitmapFactory.decodeResource(context.getResources(), imgId);
	}
}

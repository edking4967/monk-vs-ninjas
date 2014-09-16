package com.thousandonestories.game.background;

import com.thousandonestories.game.gameobjects.Sprite;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class Scenery extends Sprite {

	public Scenery(Resources res, float x, float y, Bitmap[] bmps,
			Bitmap[] reversedbmps, float scalefactor) {
		super(res, x, y, bmps, reversedbmps, scalefactor);
		//adjust coordinates to be drawn from top left, not center.
		mX = x;
		mY = y;
		
		bX = x + mWidth;
		bY = y + mHeight;
		setPersistent(true);
	}

}

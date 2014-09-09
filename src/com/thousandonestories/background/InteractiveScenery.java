package com.thousandonestories.background;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class InteractiveScenery extends Scenery implements BackgroundScenery{

	public InteractiveScenery(Resources res, float x, float y, Bitmap[] bmps,
			Bitmap[] reversedbmps, float scalefactor, boolean pixelart) {
		super(res, x, y, bmps, reversedbmps, scalefactor);
		// TODO Auto-generated constructor stub
				
		//set options for no blurred scaling with pixel art images:
		if(pixelart)
		{
		   paint.setAntiAlias(false);
		   paint.setFilterBitmap(false);
		   paint.setDither(false);
		}
	}
		

}

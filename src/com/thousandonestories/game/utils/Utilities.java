package com.thousandonestories.game.utils;

import java.io.File;
import java.io.FileOutputStream;
import android.graphics.Bitmap;
import android.os.Environment;

public class Utilities {

	public void saveBitmap( Bitmap bmp, int num){


		File file; 

		try {
			file = new File(Environment.getExternalStorageDirectory() + "/zMonkPics/sign" + num + ".jpg");
			bmp.compress(Bitmap.CompressFormat.JPEG, 20, new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return;
	}

	
}

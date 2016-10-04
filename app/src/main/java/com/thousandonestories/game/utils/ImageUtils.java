package com.thousandonestories.game.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

public class ImageUtils {

    public static Bitmap[] flipBmpHorizontal(Bitmap[] input )
    {
        int i;
        Bitmap output[] = new Bitmap[input.length];
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        for(i=0; i< input.length; i++)
        {
            //flip bitmap:

            output[i]=Bitmap.createBitmap(input[i], 0, 0, input[i].getWidth(), input[i].getHeight(), m, false);
            output[i].setDensity(DisplayMetrics.DENSITY_DEFAULT);

        }
        return output;
    }

}

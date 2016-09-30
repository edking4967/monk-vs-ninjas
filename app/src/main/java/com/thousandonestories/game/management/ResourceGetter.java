package com.thousandonestories.game.management; 
import android.content.Context;
import android.content.res.Resources;


public class ResourceGetter {
    private Resources res;

    public ResourceGetter(Context c)
    {
        res = c.getResources();
    }
}

package com.thousandonestories.game.management; 
import android.util.Log;
import java.util.*;
import java.io.*;
import android.R;
import android.content.Context;
import java.lang.reflect.Field;

public class ResourceGetter {
    public static int[] getArray(ArrayList<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
    public static int getResourceId(String name, Class res)
    {
        int drawableId = -1;
        try{
            Field field = res.getField(name);
            drawableId = field.getInt(null);
        }
        catch (IllegalAccessException e)
        {
            Log.e( ResourceGetter.class.getSimpleName(),
                e.getClass().getSimpleName());
        }
        catch (NoSuchFieldException e)
        {
            Log.e( ResourceGetter.class.getSimpleName(),
                e.getClass().getSimpleName());
        }
        return drawableId;
    }


    public static int[] getResourceList(Context c, String filename)
    {
        ArrayList<Integer> l = new ArrayList<Integer>();
        BufferedReader bufferedReader;
        try {
            int resId = getResourceId(filename, R.raw.class);
            // FileReader reads text files in the default encoding.
            InputStream inputStream = c.getResources().openRawResource(resId);

            InputStreamReader inputreader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputreader);
            String line = null;

            while((line = bufferedReader.readLine()) != null) {
                l.add(getResourceId(line, R.drawable.class ));
            }   

            // Always close files.
            bufferedReader.close();  



        }
        catch(FileNotFoundException ex) {
            Log.e( ResourceGetter.class.getSimpleName(),
                    "Unable to open file '" + 
                    filename + "'");                
        }
        catch(IOException ex) {
            Log.e( ResourceGetter.class.getSimpleName(),
                    "Error reading file '" 
                    + filename + "'");                  
        }

        return getArray(l);
    }
}

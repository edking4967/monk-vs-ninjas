package com.thousandonestories.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

public class Mountain implements BackgroundScenery{

	private Path outline;
	private Paint paint;
	float[][] pathcoords;
    public int color;
	
	public Mountain( int color, float[][] coords ) 
	{
		pathcoords = coords;
		
		paint = new Paint();
		outline = new Path();
		
        this.color = color;
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(color);
		paint.setStyle(Style.FILL);
				
		outline.reset(); // only needed when reusing this outline for a new build
		
		
		outline.moveTo( coords[0][0], coords[0][1]); // used for first point

		for(int i=1; i< coords.length; i++)
		{
			outline.lineTo( coords[i][0], coords[i][1]); 
		}
		outline.lineTo( coords[0][0], coords[0][1]);  // close the shape
		
	}
	
	public void doDraw( Canvas c )
	{
		c.drawPath( outline, paint );
	}

    public void setColor(int color)
    {
        this.color = color;
        paint.setColor(color);
    }
	
	public void scroll( float vel, long elapsedTime )
	{
		outline.offset(vel*elapsedTime, 0);
	}
	
}

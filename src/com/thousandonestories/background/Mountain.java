package com.thousandonestories.background;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;

public class Mountain implements BackgroundScenery{

	private Path outline;
	private Paint paint;
	float[][] pathcoords;
	
	public Mountain( int color, float[][] coords ) 
	{
		pathcoords = coords;
		
		paint = new Paint();
		outline = new Path();
		
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
		
		c.drawText("" + pathcoords[pathcoords.length-1][0] + " " + pathcoords[pathcoords.length-1][1], 0, 200, paint);
	}
	
	public void scroll( float vel, long elapsedTime )
	{
		outline.offset(vel*elapsedTime, 0);
	}
	
}

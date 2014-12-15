package com.thousandonestories.game;

import android.graphics.Canvas;

public interface BackgroundScenery {
	public void doDraw( Canvas c );
	
	public void scroll( float vel, long elapsedTime );


}

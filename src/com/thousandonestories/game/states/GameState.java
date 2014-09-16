package com.thousandonestories.game.states;

import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.Log;

import com.thousandonestories.game.GameManager;

public abstract class GameState {
	
	protected GameManager gm;
	private MediaPlayer song;
	
	public GameState( GameManager gm )
	{
		this.gm = gm;
	}
	
	public abstract boolean handleInput(float x, float y, int action);
	public abstract void update(long dt);
	public abstract void doDraw(Canvas c);

	public abstract boolean handleFling(float velocityX, float velocityY);

	protected void loadSong( int id )
	{
		song = MediaPlayer.create(gm.getContext(), id);
		song.setLooping(true);
		song.start();
		Log.d("zz", "loadSong called");
	}
	
	public void closeOut() {
		if(song != null)
		{
			song.stop();
			song.release();

		}
	}

}

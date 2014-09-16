package com.thousandonestories.game.levels;

import android.graphics.Color;
import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.background.BackgroundScenery;
import com.thousandonestories.game.background.Mountain;


public class LevelOne extends Level {

	public LevelOne(GameManager gm)
	{
		super(gm);
	}
	
	@Override
	public void loadBitmaps() {
		

	}

	@Override
	public void start() {
		
		generateMountains();
		
		for(int i =0; i<10; i++)
		{
			LevelTools.addBlock( getBlockList(), i * 600, i* 600 + 400,600,700, Color.WHITE);

		}
		
	}
	
	public void generateMountains()
	{
		float coords[][] = { 
				{0,gm.getGameHeight()},
				{0, gm.getGameHeight()/2} ,
				{gm.getGameWidth()/4,gm.getGameHeight()/6}, 
				{gm.getGameWidth()/2,gm.getGameHeight()/2}, 
				{gm.getGameWidth()*3/4, gm.getGameHeight()/5},
				{gm.getGameWidth(), (float) (gm.getGameHeight() /2)},
				{gm.getGameWidth(), gm.getGameHeight() }    		  
		};


		int numrepeats = 5; 

		Mountain mountain;

		int numMountains = 3;

		String mtnColors[] = { "#7b86b8", "#74778f", "#a0a3b2" };

		float xoffset = 50;
		float yoffset = 200;

		for(int k=0; k<numMountains; k++)
		{

			float newcoords[][] = new float[numrepeats * coords.length][2];

			for(int i=-1; i< numrepeats-1; i++)
			{
				for(int j =0; j< coords.length; j++)
				{
					newcoords[(i+1) * coords.length + j][0] = coords[j][0] + gm.getGameWidth() * i;  // x coord: shift
					newcoords[(i+1) * coords.length + j][1] = coords[j][1];   // y coord: keep
				}
			}

			for(int i = 0; i< newcoords.length; i++)
			{
				newcoords[i][0] += xoffset*k; 
				newcoords[i][1] += yoffset*k; 

			}

			mountain = new Mountain( Color.parseColor( mtnColors[k] ) , newcoords );
			
			getBgSceneryList().add((BackgroundScenery) mountain);

		}
		
		
		gm.restartGame();
	}
	
	
}

package com.thousandonestories.game.ui;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.gameobjects.Block;

public class TextDisplay {
	
	protected ArrayList<String> speechItems;
	private Iterator<String> speechItr;
	private Block bgBlock;
	private String speechString;
	private Paint p;
	
	public TextDisplay( GameManager gm, String ... speechStrings )
	{
		bgBlock = new Block(0,0,gm.getGameWidth(),gm.getGameHeight()/3, Color.WHITE);
		speechItems = new ArrayList<String>();
		for(int i=0; i<speechStrings.length; i++)
			speechItems.add(speechStrings[i]);
		
		speechItr = speechItems.iterator();
		speechString = speechItr.next();
		p = new Paint(Color.BLACK);
        Typeface tf = Typeface.create("Helvetica",Typeface.BOLD);
		p.setTypeface(tf);
		p.setFlags(Paint.ANTI_ALIAS_FLAG);
		p.setTextSize(25);

	}

	public boolean hasText()
	{
		return speechString != "";
	}

	public void addSpeech(String s)
	{
		speechItems.add(s);
	}
	
	public void advanceText() {
		if(speechItr.hasNext() )
			speechString = speechItr.next();	
		else
		{
			speechString = "";
		}
	}
	
	public void doDraw(Canvas c)
	{
		if( hasText() )
		{
			bgBlock.doDraw(c);
			c.drawText(speechString, 100, 100, p);
			
		}
	}

	public boolean checkBounds(float x, float y) {
		// TODO Auto-generated method stub
		return bgBlock.getLeftBound() <= x && bgBlock.getRightBound() >= x 
				&& bgBlock.getTopBound() <= y && bgBlock.getBottomBound() >= y;
	}

}

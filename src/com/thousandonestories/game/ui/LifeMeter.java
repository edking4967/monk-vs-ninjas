package com.thousandonestories.game.ui;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.R;

public class LifeMeter extends UIBlock {

	private int lifeAmount;
	private GameManager gm;
	
	public LifeMeter( GameManager gm)
	{
		super();
		this.gm = gm;
		
		int [] imgs = { R.drawable.healthbox };
		
		for(int i=0;i<4; i++)
			uiItems.add(new ClickableSprite(gm.getResources(), 100+i*100, 100, imgs, 2f));
	}
	
	@Override
	public void update(long dt)
	{
		super.update(dt);
		//lifeAmount = gm.getHero().getLifeAmount();
	}
}

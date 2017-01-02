package com.thousandonestories.game.management;
import android.content.res.Resources;

import com.thousandonestories.game.GameManager;

public class GameConnector {

    GameObjectMgr gameobjectMgr;
    GameStateMgr gamestateMgr;
    GameManager panel;
	
	public GameConnector(Resources r)
	{
        GlobalConstants.gameConnector = this;
		gameobjectMgr = new GameObjectMgr(this, r);
		gamestateMgr = new GameStateMgr(this, r);
	}

	public void update(long elapsedTime)
	{
		gameobjectMgr.update(elapsedTime);
	}

    public void setPanel(GameManager panel) {
        this.panel = panel;
    }
}

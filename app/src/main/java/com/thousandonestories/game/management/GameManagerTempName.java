package com.thousandonestories.game.management;
import android.content.res.Resources;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.levels.Level;
import com.thousandonestories.game.management.GlobalConstants;

public class GameManagerTempName {

    GameObjectMgr gameobjectMgr;
    GameStateMgr gamestateMgr;
    GameManager panel;
	
	public GameManagerTempName(Resources r)
	{
        GlobalConstants.gameManager = this;
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

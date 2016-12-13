package com.thousandonestories.game.management;
import android.content.res.Resources;

import com.thousandonestories.game.OldPanel;
import com.thousandonestories.game.levels.Level;
import com.thousandonestories.game.management.GlobalConstants;

public class GameManager {

    GameObjectMgr gameobjectMgr;
    GameStateMgr gamestateMgr;
    OldPanel panel;
	
	public GameManager(Resources r)
	{
        GlobalConstants.gameManager = this;
		gameobjectMgr = new GameObjectMgr(this, r);
		gamestateMgr = new GameStateMgr(this, r);
	}

	public void update(long elapsedTime)
	{
		gameobjectMgr.update(elapsedTime);
	}

    public void setPanel(OldPanel panel) {
        this.panel = panel;
    }
}

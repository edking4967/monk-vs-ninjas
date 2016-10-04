package com.thousandonestories.game.management;
import android.content.res.Resources;

import com.thousandonestories.game.OldPanel;
import com.thousandonestories.game.levels.Level;

public class GameManager {

    GameObjectMgr gameobjectMgr;
    GameStateMgr gamestateMgr;
    OldPanel panel;
	
	public GameManager(Resources r)
	{
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

package com.thousandonestories.game.ui;

import com.thousandonestories.game.management.GameObjectMgr;

public class UIMgr
{
    public static void updateHealthBar()
    {
        int numHealthBars = GameObjectMgr.hero.getHealth()/25;
        if(numHealthBars < GameObjectMgr.healthBarList.size()) {
            GameObjectMgr.healthBarList.remove(numHealthBars);
        }
    }
}

package com.thousandonestories.game.gameobjects;
import android.graphics.Color;

import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.ai.Goal;
import com.thousandonestories.game.management.GameObjectMgr;

/**
 * Created by edk on 1/21/15.
 */
public class GoalOrb extends NewSprite {
    private Sprite img;
    private Goal goal;

    public GoalOrb(SpriteResources res, float x, float y, float scaleFactor)
    {
        super(res,x,y,scaleFactor);
    }

    public void setGoal(Goal goal)
    {
        this.goal = goal;
    }

    class BlehTask implements Runnable
    {
        @Override
        public void run() {
            try {
                wait(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //OldPanel.gameOver();
        }
    }

    @Override
    public void update(long elapsedTime)
    {
        super.update(elapsedTime);
        if(OldPanel.checkCollision(this, GameObjectMgr.hero))
        {
            this.flash(Color.RED,5000);
            goal.complete();
        }
    }
}

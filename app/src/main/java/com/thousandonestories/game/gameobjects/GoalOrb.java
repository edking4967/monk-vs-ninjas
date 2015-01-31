package com.thousandonestories.game.gameobjects;
import android.graphics.Color;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.ai.Goal;
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

    @Override
    public void update(long elapsedTime)
    {
        super.update(elapsedTime);
        if(OldPanel.checkCollision(this, OldPanel.hero))
        {
            this.flash(Color.RED,5000);
        }
    }
}

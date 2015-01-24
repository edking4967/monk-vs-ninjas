package com.thousandonestories.game.gameobjects;
import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.ai.Goal;
/**
 * Created by edk on 1/21/15.
 */
public class GoalOrb extends NewSprite {
    private Sprite img;
    private Goal goal;

    public GoalOrb(SpriteResources res, float x, float y, int scaleFactor)
    {
        super(res,x,y,scaleFactor);
    }

    public void setGoal(Goal goal)
    {
        this.goal = goal;
    }
}

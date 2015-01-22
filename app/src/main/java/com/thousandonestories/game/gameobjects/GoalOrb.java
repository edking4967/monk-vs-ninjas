package com.thousandonestories.game.gameobjects;
import com.thousandonestories.game.ai.Goal;
/**
 * Created by edk on 1/21/15.
 */
public class GoalOrb {
    private Sprite img;
    private Goal goal;

    public GoalOrb()
    {

    }

    public void setGoal(Goal goal)
    {
        this.goal = goal;
    }
}

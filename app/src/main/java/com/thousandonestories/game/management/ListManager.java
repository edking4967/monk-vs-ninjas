package com.thousandonestories.game.management; 
import com.thousandonestories.game.ui.ClickableSprite;
import com.thousandonestories.game.gameobjects.*;
import com.thousandonestories.game.BackgroundSprite;
import com.thousandonestories.game.BackgroundScenery;
import com.thousandonestories.game.InteractiveScenery;
import java.util.concurrent.CopyOnWriteArrayList;

public class ListManager {

    //Lists containing different game objects: 
    public static CopyOnWriteArrayList<GameObject> mGameObjList;
    public static CopyOnWriteArrayList<GravitySprite> mGravSpriteList;
    public static CopyOnWriteArrayList<Block> mBlockList;
    public static CopyOnWriteArrayList<Projectile> mProjList;
    public static CopyOnWriteArrayList<Enemy> mEnemyList;
    public static CopyOnWriteArrayList<NPC> mNPCList;
    public static CopyOnWriteArrayList<InteractiveScenery> iSceneryList;
    public static CopyOnWriteArrayList<BackgroundScenery> bgSceneryList;
    public static CopyOnWriteArrayList<ClickableSprite> gameUIList;
    public static CopyOnWriteArrayList<ClickableSprite> healthBarList;
    public static CopyOnWriteArrayList<BackgroundSprite> m3dPlatformList;

    /**
     * Holds bitmaps that will be drawn to the menu screen
     */
    public static CopyOnWriteArrayList<ClickableSprite> menuItemList;

    public static void initializeLists() {

        ListManager.mGameObjList = new CopyOnWriteArrayList<GameObject>();
        ListManager.mGravSpriteList = new CopyOnWriteArrayList<GravitySprite>();
        ListManager.mBlockList = new CopyOnWriteArrayList<Block>();
        ListManager.mProjList = new CopyOnWriteArrayList<Projectile>();
        ListManager.mEnemyList = new CopyOnWriteArrayList<Enemy>();
        ListManager.mNPCList = new CopyOnWriteArrayList<NPC>();
        ListManager.iSceneryList = new CopyOnWriteArrayList<InteractiveScenery>();
        ListManager.bgSceneryList = new CopyOnWriteArrayList<BackgroundScenery>();
        ListManager.m3dPlatformList = new CopyOnWriteArrayList<BackgroundSprite>();		
        ListManager.menuItemList = new CopyOnWriteArrayList<ClickableSprite>();		
        ListManager.gameUIList = new CopyOnWriteArrayList<ClickableSprite>();
        ListManager.healthBarList = new CopyOnWriteArrayList<ClickableSprite>();
    }
}

package com.thousandonestories.game.management; 
import com.thousandonestories.game.ui.ClickableSprite;
import com.thousandonestories.game.gameobjects.*;
import com.thousandonestories.game.BackgroundSprite;
import com.thousandonestories.game.BackgroundScenery;
import com.thousandonestories.game.InteractiveScenery;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameObjectMgr {

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
    public static HeroSprite hero;

    /**
     * Holds bitmaps that will be drawn to the menu screen
     */
    public static CopyOnWriteArrayList<ClickableSprite> menuItemList;

    public static void initializeLists() {

        GameObjectMgr.mGameObjList = new CopyOnWriteArrayList<GameObject>();
        GameObjectMgr.mGravSpriteList = new CopyOnWriteArrayList<GravitySprite>();
        GameObjectMgr.mBlockList = new CopyOnWriteArrayList<Block>();
        GameObjectMgr.mProjList = new CopyOnWriteArrayList<Projectile>();
        GameObjectMgr.mEnemyList = new CopyOnWriteArrayList<Enemy>();
        GameObjectMgr.mNPCList = new CopyOnWriteArrayList<NPC>();
        GameObjectMgr.iSceneryList = new CopyOnWriteArrayList<InteractiveScenery>();
        GameObjectMgr.bgSceneryList = new CopyOnWriteArrayList<BackgroundScenery>();
        GameObjectMgr.m3dPlatformList = new CopyOnWriteArrayList<BackgroundSprite>();
        GameObjectMgr.menuItemList = new CopyOnWriteArrayList<ClickableSprite>();
        GameObjectMgr.gameUIList = new CopyOnWriteArrayList<ClickableSprite>();
        GameObjectMgr.healthBarList = new CopyOnWriteArrayList<ClickableSprite>();
    }
}

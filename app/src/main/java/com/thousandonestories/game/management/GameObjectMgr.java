package com.thousandonestories.game.management; 
import android.content.res.Resources;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.graphics.BitmapMgr;
import com.thousandonestories.game.screen.ScreenMgr;
import com.thousandonestories.game.time.TimeMgr;
import com.thousandonestories.game.ui.ClickableSprite;
import com.thousandonestories.game.gameobjects.*;
import com.thousandonestories.game.BackgroundSprite;
import com.thousandonestories.game.BackgroundScenery;
import com.thousandonestories.game.InteractiveScenery;
import com.thousandonestories.game.ui.UIMgr;
import com.thousandonestories.game.Mountain;

import java.util.concurrent.CopyOnWriteArrayList;

public class GameObjectMgr {
    public GameConnector gc;
    Resources r;

    public GameObjectMgr(GameConnector gc, Resources r)
    {
        this.gc = gc;
        this.r = r;
    }

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
    public static CopyOnWriteArrayList<Mountain> mountainList;
    public static HeroSprite hero;

    /**
     * Holds bitmaps that will be drawn to the menu screen
     */
    public static CopyOnWriteArrayList<ClickableSprite> menuItemList;

    public static void initializeLists() {

        mGameObjList = new CopyOnWriteArrayList<GameObject>();
        mGravSpriteList = new CopyOnWriteArrayList<GravitySprite>();
        mBlockList = new CopyOnWriteArrayList<Block>();
        mProjList = new CopyOnWriteArrayList<Projectile>();
        mEnemyList = new CopyOnWriteArrayList<Enemy>();
        mNPCList = new CopyOnWriteArrayList<NPC>();
        iSceneryList = new CopyOnWriteArrayList<InteractiveScenery>();
        bgSceneryList = new CopyOnWriteArrayList<BackgroundScenery>();
        m3dPlatformList = new CopyOnWriteArrayList<BackgroundSprite>();
        menuItemList = new CopyOnWriteArrayList<ClickableSprite>();
        gameUIList = new CopyOnWriteArrayList<ClickableSprite>();
        healthBarList = new CopyOnWriteArrayList<ClickableSprite>();
        mountainList = new CopyOnWriteArrayList<Mountain>();
    }

    public static boolean checkCollision(GameObject obj1, GameObject obj2) {
        return (obj1.getLeftBound() < obj2.getRightBound() && obj1.getRightBound() > obj2.getLeftBound() &&
                obj1.getTopBound() < obj2.getBottomBound() && obj1.getBottomBound() > obj2.getTopBound() ) ;
    }

    public void updateEnemyAI()
    {
        for( Enemy enemy: GameObjectMgr.mEnemyList )
        {
            if( enemy.getState() != GravitySprite.STATE_INAIR ) //enemy is on the ground
            {

                //if enemy has a block
                enemy.AICheckMoveBounds();

            }
        }
    }

    public void update(long elapsedTime) {
        if(GameObjectMgr.hero==null) return;

        //if(GameStateMgr.currentState == GameStateMgr.GameState.MENU ) return;
        if(GameManager.gameState == GameManager.STATE_MENU ) return;

        if(hero.getHealth() <= 0)
        {
            gc.gamestateMgr.gameOver();

        }
        UIMgr.updateHealthBar();

        //CAMERA CONTROL:

        int moveBox = 100; // amount hero can move before camera locks to him again

        ScreenMgr.scrollLock = true;

        if(!ScreenMgr.scrollLock) // scroll lock off: screen fixed, hero moves
        {
            float heroX = (hero.bX+hero.mX) / 2; //center of the hero
            if( heroX >= ScreenMgr.heroSavedPos + moveBox  || heroX <= ScreenMgr.heroSavedPos-moveBox  )
            {
                ScreenMgr.scrollLock = true;
                ScreenMgr.heroSavedPos = (hero.bX+hero.mX)/2; //save hero's position
                ScreenMgr.heroSavedDir=hero.getDirection(); //save hero's direction
            }
        }
        else // scroll lock is on: hero fixed, screen scrolling
        {
            if(ScreenMgr.heroSavedDir != hero.getDirection() ) // hero direction has changed
            {
                ScreenMgr.heroSavedDir = hero.getDirection();

                ScreenMgr.scrollLock=false;
            }
        }
        //end camera control


        //TODO: fold these checks into one main GameObject loop.
        //Check sprite collisions with blocks:
        GameManager.checkBlocks(elapsedTime);

        //Check enemy collisions with projectiles:
        GameManager.checkEnemyProjectiles(elapsedTime);

        //update enemy AI:
        updateEnemyAI();

        float offset = 1;

        for(BackgroundScenery bgObj: bgSceneryList) // Mountains
        {
            ScreenMgr.scrollLock=true;
            if(ScreenMgr.scrollLock)
            {
                bgObj.scroll( ScreenMgr.scrollSpeed/ ( 20f + 10*offset ) ,  elapsedTime );
                offset++;
            }
        }

        for( BackgroundSprite platform: m3dPlatformList)
        {
            platform.scroll( ScreenMgr.scrollSpeed/ ( 20f ) ,  elapsedTime );
        }
        for (GameObject mObj : mGameObjList) {

            mObj.update(elapsedTime); //update object's position etc
            ScreenMgr.scrollLock=true;
            if( ScreenMgr.scrollLock)
            {
                mObj.scroll(ScreenMgr.scrollSpeed/20f,  elapsedTime ) ; // scroll object as necessary
            }


            //remove offscreen objects:
            if(mObj.getRightBound() <= 0 && !mObj.isPersistent() )
            {

                mObj.hide();

                mGameObjList.remove( mObj );

                if( mObj instanceof Enemy )
                {
                    mEnemyList.remove(mObj);
                }

                if( mObj instanceof Block)
                {
                    mBlockList.remove(mObj);
                }

                if( mObj instanceof Projectile)
                {
                    mProjList.remove(mObj);
                }

            }

            if( mObj.getTopBound() >= gc.panel.getHeight() && mObj instanceof Enemy)
            {
                mGameObjList.remove( mObj );
                mEnemyList.remove( mObj );
            }

        }

        //Make more enemies:
        if( ( System.currentTimeMillis() - TimeMgr.enemySpawnTime >= TimeMgr.enemySpawnInterval) //enough time has passed
                && (mEnemyList.size() < 10 ) )  // we're not saturated with enemies
        {
            Enemy newCroc = new Enemy(r, 1100, 100, BitmapMgr.crocbitmap, BitmapMgr.crocbitmap_f,
                    BitmapMgr.projBmp, mProjList, mBlockList, mGameObjList, 1 );
            mEnemyList.add(newCroc);
            mGameObjList.add(newCroc);
            mGravSpriteList.add(newCroc);

            TimeMgr.enemySpawnTime = System.currentTimeMillis();

        }

        for (InteractiveScenery iScenery: iSceneryList)
        {
            if( checkCollision( hero, iScenery ) )
            {
                iScenery.hide();
            }
        }


        //check if hero has fallen below the screen:
        if(hero.getTopBound() >= GameManager.mHeight)
        {
            gc.gamestateMgr.gameOver();
        }
    }
}

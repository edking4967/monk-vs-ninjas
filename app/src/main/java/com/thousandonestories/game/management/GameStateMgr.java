package com.thousandonestories.game.management;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.R;
import com.thousandonestories.game.gameobjects.GameObject;
import com.thousandonestories.game.ui.ClickableSprite;
import com.thousandonestories.game.utils.ImageUtils;

import java.util.concurrent.CopyOnWriteArrayList;

public class GameStateMgr {
    public enum GameState { UNINITIALIZED, RUNNING, MENU, GAMEOVER };
    public static GameState currentState;
    GameConnector gc;
    Resources r;

    public GameStateMgr(GameConnector gc, Resources r)
    {
        this.gc = gc;
        this.r = r;
    }

    public void gameOver()
    {
        Log.d("red", "gameOver() function started");

        for( GameObject object: GameObjectMgr.mGameObjList )
        {
            object.hide();
        }

        GameObjectMgr.mGravSpriteList.clear();
        GameObjectMgr.mBlockList.clear();
        GameObjectMgr.mEnemyList.clear();
        GameObjectMgr.mProjList.clear();
        GameObjectMgr.mGameObjList.clear();

        GameObjectMgr.menuItemList = new CopyOnWriteArrayList<ClickableSprite>();

        Bitmap gameoverbitmap[] = new Bitmap[1];
        Bitmap gameoverbitmap_r[] = new Bitmap[1];
        gameoverbitmap[0]= BitmapFactory.decodeResource(r, R.drawable.gameover);
        gameoverbitmap_r = ImageUtils.flipBmpHorizontal(gameoverbitmap);

        ClickableSprite gameOverSprite= new ClickableSprite( r, (int) GameManager.mWidth/2,
                (int) GameManager.mHeight/2, gameoverbitmap, gameoverbitmap_r, 1 );
        gameOverSprite.setAction(ClickableSprite.RESTART_GAME);

        GameObjectMgr.menuItemList.add(gameOverSprite);

        Log.d("red", "menuitemlist size = " + GameObjectMgr.menuItemList.size() );
        Log.d("red","GameManager gamestate = " + GameManager.gameState);
        GameManager.gameState = GameManager.STATE_GAMEOVER;
        currentState = GameState.GAMEOVER;
    }
}

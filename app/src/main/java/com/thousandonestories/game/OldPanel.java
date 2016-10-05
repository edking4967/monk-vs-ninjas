package com.thousandonestories.game;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.concurrent.CopyOnWriteArrayList;

import com.thousandonestories.game.gameobjects.Block;
import com.thousandonestories.game.gameobjects.Dragon;
import com.thousandonestories.game.gameobjects.Enemy;
import com.thousandonestories.game.gameobjects.FlyingSprite;
import com.thousandonestories.game.gameobjects.GameObject;
import com.thousandonestories.game.gameobjects.GoalOrb;
import com.thousandonestories.game.gameobjects.GravitySprite;
import com.thousandonestories.game.gameobjects.HeroSprite;
import com.thousandonestories.game.gameobjects.NPC;
import com.thousandonestories.game.gameobjects.Ninja;
import com.thousandonestories.game.gameobjects.Projectile;
import com.thousandonestories.game.graphics.BitmapMgr;
import com.thousandonestories.game.management.GameManager;
import com.thousandonestories.game.management.GameObjectMgr;
import com.thousandonestories.game.R.drawable;
import com.thousandonestories.game.ai.Goal;
import com.thousandonestories.game.screen.ScreenMgr;
import com.thousandonestories.game.time.TimeMgr;
import com.thousandonestories.game.ui.ClickableSprite;
import com.thousandonestories.game.utils.ImageUtils;

//gravity stuff does not go in panel

//TODO: scaleFactor does nothing

public class OldPanel extends SurfaceView implements SurfaceHolder.Callback {

    /*
     * Game states:
     */
    //TODO: use enum
    public static final int STATE_UNINITIALIZED=0;
    public static final int STATE_GAMEOVER = 1;
    public static final int STATE_GAME_RUNNING= 2;
    public static final int STATE_MENU= 3;

    public static int gameState;

    /*
     *  DEBUG/DEVELOPMENT SETTINGS:
     */

    /**
     * Draw bounding boxes of platforms:
     */
    public static final boolean DRAW_BLOCKS = false;

    /**
     * Experimental: Save bitmaps drawn to screen, for exporting to gif/movie 
     */
    public static final boolean SAVE_BITMAPS =false;

    /**
     * The currentLevel that the player is currently on.
     */
    private static int currentLevel;
    //private 	DiskLruCache mDiskLruCache;

    public static float mWidth;
    public static float mHeight;

    private Paint mPaint;
    private ViewThread mThread;

    private Enemy croc;
    private FlyingSprite flyingHero;


    public static Bitmap swordBmp;

    Bitmap cloudBitmap;

    Bitmap qiBmp[];
    Bitmap heroBmps[];
    Bitmap heroReverseBmps[];

    Bitmap pagoda_left[];
    Bitmap pagoda_right[];
    Bitmap pagoda_repeat[];

    Bitmap dragonCube;

    Bitmap platformBitmap;

    SpriteResources newMonkRes;

    SpriteResources coffeeCupRes;

    SpriteResources flyingResources;

    SpriteResources tomatomanRes;

    SpriteResources dragonHeadRes;

    SpriteResources dragonBodyRes;

    SpriteResources ninjaRes;

    SpriteResources goalOrbRes;

    ClickableSprite menuSprite;

    InteractiveScenery door;
    Bitmap door_b[];

    Bitmap logos_b;

    BackgroundSprite logos;

    NPC blebleguy;
    NPC dragontest;

    Dragon dragon;
    MediaPlayer bgSong;

    public static MediaPlayer blip;
    public static MediaPlayer blip2;
    GameManager gm;

    public OldPanel(Context context, GameManager gm) {

        super(context);

        setGameState(STATE_UNINITIALIZED);

        currentLevel = 2; // why?

        Log.d("bleh", "Panel constructor called");

        getHolder().addCallback(this);

        GameObjectMgr.menuItemList = new CopyOnWriteArrayList<ClickableSprite>();

        loadBitmapsLevelOne();

        showMenu();

        this.gm = gm;

        mThread = new ViewThread(this, gm);

        bgSong = MediaPlayer.create(context, R.raw.shooter);

        blip = MediaPlayer.create(context, R.raw.blip);

        blip2 = MediaPlayer.create(context, R.raw.blip2);

    }

    public void printText( String str, Canvas canvas, Paint paint, int x, int y )
    {
        canvas.drawText(str, 10, 10, paint);
    }

    /* Stays in NewPanel: */
    @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // Create and start new thread
            Log.d("bleh","surfaceCreated called");

            if (!mThread.isAlive()) {
                mThread = new ViewThread(this, gm);
                mThread.setRunning(true);
                mThread.start();
            }
        }

    @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d("bleh","surfaceChanged called");

            // Store new extents
            mWidth = width;
            mHeight = height;
        }

    @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d("bleh","surfaceDestroyed called");

            // Stop thread
            if (mThread.isAlive()) {
                mThread.setRunning(false);
            }

        }
    /* End stays in NewPanel */

    /* Goes where? */
    // Draw all sprites and blocks in list
    //int color = 0;
    int skyorig = 0xff7ec0ee; // TODO: Settable by user!
    int skycolor = skyorig;
    int counter = 0;
    int skyStep = -0x0010101;
    public void doDraw(Canvas canvas) {
        Log.d("color", "skycolor = " + Integer.toHexString(skycolor) + " skystep = " +skyStep  );
        counter++;
        if(counter == 10) {
            skycolor += skyStep;
            counter = 0;
        }
        if(skycolor <= 0xff000000)
        {
            skycolor = 0xff004270;
            skyStep = Math.abs(skyStep);
        }
        if( skycolor > skyorig)
        {
            skycolor = skyorig;
            skyStep = -Math.abs(skyStep);
        }
        canvas.drawColor( skycolor );

        if( isGameOver() )
        {
            Paint mPaint = new Paint();
            mPaint.setColor(Color.WHITE);
            for (ClickableSprite cs: GameObjectMgr.menuItemList) {
                cs.doDraw(canvas);
            }
            canvas.drawText("Touch \"game over\" to restart.", 500, 500, mPaint);
            return;
        }

        if(isMenu() )
        {
            Typeface tf = Typeface.create("Helvetica",Typeface.BOLD);

            Paint helveticaPaint = new Paint();
            helveticaPaint.setColor(Color.WHITE);
            helveticaPaint.setTypeface(tf);
            helveticaPaint.setTextSize(50);
            helveticaPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

            for(ClickableSprite menuSprite : GameObjectMgr.menuItemList)
            {
                menuSprite.doDraw(canvas);
            }

            canvas.drawText("MONK VS NINJAS",100,100, helveticaPaint);
            helveticaPaint.setTextSize(30);
            canvas.drawText("Touch the button to begin", 100, 150, helveticaPaint);
            return;
        }

        if( getGameState()==STATE_GAME_RUNNING )  // game running
        {	
            //Draw background color:
            canvas.drawColor( skycolor );


            /*
             * Background Scenery: paths
             */

            for( BackgroundScenery bgObj: GameObjectMgr.bgSceneryList )
            {
                bgObj.doDraw(canvas);

            }

            /*
             * Background Sprites: bitmaps
             */	

            for( BackgroundSprite bSprite: GameObjectMgr.m3dPlatformList)
            {
                bSprite.doDraw(canvas);
            }

            /* 
             *  GameObjects: objects in the game world you can interact with.
             */

            for( GameObject object: GameObjectMgr.mGameObjList)
            {
                if(object instanceof Block && !DRAW_BLOCKS)
                    continue;

                else if(!object.hidden() )
                {
                    object.doDraw(canvas);
                }

            }

            for( ClickableSprite ui: GameObjectMgr.gameUIList )
            {
                ui.doDraw(canvas);
            }

            for( ClickableSprite healthBar: GameObjectMgr.healthBarList )
            {
                healthBar.doDraw(canvas);
            }


            //Debugging stuff:

            /*
            if(currentLevel == 1)
            {
                canvas.drawText(" CrocLB: " + croc.blockLB + " crocRB: " + croc.blockRB + 
                        " LandCount "+hero.db_landcount+" density: "+  getResources().getDisplayMetrics().density + " elapsed: " 
                        +" FPS: "+1000f/ ViewThread.mElapsed , 10, 10, mPaint);
                canvas.drawText("Enemy_state " + croc.getState(), 10, 40, mPaint);
                canvas.drawText("Health = " + hero.getHealth() + "Velocity = " + hero.getVelocity() + "x= " + hero.getLeftBound()
                        + " rightB= " + hero.getRightBound(), 10, 25, mPaint);
            }
            */
        }


    } //end of doDraw function

    public void addBlock(int left, int right, int top, int bottom, int color)
    {
        Block block = new Block(left, top, right, bottom, color);
        GameObjectMgr.mBlockList.add( block );
        GameObjectMgr.mGameObjList.add( block);

        //Add platform image:
        GameObjectMgr.m3dPlatformList.add(
                new BackgroundSprite(platformBitmap,left, top - 30, 6) ); //TODO: keeps loading bmps!
    }

    @Override
        public boolean onTouchEvent(MotionEvent event) {


            for( ClickableSprite clickSprite : GameObjectMgr.menuItemList)
            {
                if( clickSprite.checkClick(event.getX(), event.getY()) )
                {
                    if(!clickSprite.hasBeenClicked())
                    {
                        clickSprite.click( this );
                        return true;
                    }
                }
            }

            for( ClickableSprite uiSprite : GameObjectMgr.gameUIList)
            {
                if( uiSprite.checkClick(event.getX(), event.getY()) )
                {
                    if(!uiSprite.hasBeenClicked())
                    {
                        uiSprite.click( this );
                        return true;
                    }
                }
            }

            if( GameObjectMgr.hero != null && gameState==STATE_GAME_RUNNING )
            {
                return touchHandlePlatformerMode(event);
            }
            else return true;


        }  //END ONTOUCHEVENT

    public boolean touchHandleFlying(MotionEvent event)
    {
        return flyingHero.handleEvent(event);

    }

    public boolean touchHandlePlatformerMode(MotionEvent event)
    {
        int jumpY = 200; // y amount above which will be a jump

        // check orientation of the hero:
        if( (int) (event.getX()-OldPanel.mWidth/2) < 0 )
        {
            GameObjectMgr.hero.flipBmp=true;
        }
        else
            GameObjectMgr.hero.flipBmp=false;

        int vel;
        int startpt;
        Projectile proj;

        // touch at top of screen:
        if(event.getY()< jumpY)
        {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
                GameObjectMgr.hero.jump();
        }
        else if(event.getY()< 400)
        {
            //shoot a fireball :

            if(event.getAction()==MotionEvent.ACTION_DOWN)
            {
                // check hero orientation:
                if(!GameObjectMgr.hero.flipBmp)
                {
                    vel = 100;
                    startpt = (int) GameObjectMgr.hero.getRightBound();
                }
                else
                {
                    vel= -100;
                    startpt = (int) GameObjectMgr.hero.getLeftBound();
                }

                GameObjectMgr.hero.fire(System.currentTimeMillis());
                proj = new Projectile(getResources(), startpt, (int) ( (GameObjectMgr.hero.bY+GameObjectMgr.hero.mY)/2), vel, 
                        BitmapMgr.projBmp, Projectile.TYPE_HERO, 4);
                GameObjectMgr.mProjList.add( proj);
                GameObjectMgr.mGameObjList.add(proj);
            }
        }
        else
        {

            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                moveHero( (event.getX()-OldPanel.mWidth/2) /20 * GameObjectMgr.hero.getScaleFactor() );
                //hero.mDx=(int) (event.getX()-Panel.mWidth/2) /20 * hero.getScaleFactor();
                GameObjectMgr.hero.run(System.currentTimeMillis());

            }
            if(event.getAction() == MotionEvent.ACTION_MOVE)
            {
                moveHero(  (event.getX()-OldPanel.mWidth/2) /20 * GameObjectMgr.hero.getScaleFactor() );
                //hero.mDx=(int) (event.getX()-Panel.mWidth/2) /20 ;
            }

            if(event.getAction() == MotionEvent.ACTION_UP)
            {
                moveHero(0);
                GameObjectMgr.hero.rest();
            }
        }

        if( event.getActionMasked()==MotionEvent.ACTION_POINTER_DOWN)
        {
            GameObjectMgr.hero.jump();
        }

        //return super.onTouchEvent(event);
        return true;

    }

    public void moveHero(float speed)
    {
        GameObjectMgr.hero.mDx = speed;

        ScreenMgr.scrollLock = true;

        if(ScreenMgr.scrollLock)
        {
            ScreenMgr.scrollSpeed = - speed;
        }

    }	   

    public static void checkBlocks(long mElapsed) {
        //check for new collisions between GravitySprites and Blocks or a fall() event
        for( GravitySprite sprite : GameObjectMgr.mGravSpriteList)
        {
            if( sprite.getState() == GravitySprite.STATE_INAIR )
            {
                sprite.checkLanded(GameObjectMgr.mBlockList, mElapsed);
            }
            else // sprite is on a block
            {
                for (Block block: GameObjectMgr.mBlockList)
                {
                    if( ! sprite.checkFallFromBlock() ) 
                    {
                        //sprite is on a block
                        break;
                    }

                    else
                        sprite.updateBlock(block);
                }
                //check the last block:
                if( sprite.checkFallFromBlock() )
                    sprite.fall();
            }
        }		
    }

    public static void checkEnemyProjectiles(long mElapsed) {
        for( Enemy enemy: GameObjectMgr.mEnemyList)
        {
            //check for hits by projectiles:
            if( enemy.checkProjectile( GameObjectMgr.mProjList ) )
            {
                enemy.die( System.currentTimeMillis() );
            }

            //check if it's time to hide:
            enemy.checkHide( System.currentTimeMillis(), GameObjectMgr.mEnemyList );

        }
    }

    public void showMenu()
    {
        GameObjectMgr.menuItemList = new CopyOnWriteArrayList<ClickableSprite>();

        Bitmap menuimg = BitmapFactory.decodeResource(getResources(), R.drawable.menuimg);
        Bitmap menuimg2 = BitmapFactory.decodeResource(getResources(), R.drawable.menuimg2);
        Bitmap [] mimgarray = { menuimg };
        Bitmap [] mimgarray_2 = {menuimg2};
        menuSprite = new ClickableSprite(getResources(), (int) 100, (int) 200, mimgarray, mimgarray, 1);
        menuSprite.setAction(ClickableSprite.START_LEVEL_ONE);

        ClickableSprite menuSprite2 = new ClickableSprite(getResources(), (int) 400, (int) 200, mimgarray_2, mimgarray_2, 1);
        menuSprite2.setAction(ClickableSprite.START_LEVEL_TWO);

        ClickableSprite menuSprite3 = new ClickableSprite(getResources(), (int) 600, (int) 200, mimgarray, mimgarray, 1);
        menuSprite3.setAction(ClickableSprite.START_LEVEL_THREE);		

        ClickableSprite menuSprite4 = new ClickableSprite(getResources(), (int) 800, (int) 200, mimgarray, mimgarray, 1);
        menuSprite4.setAction(ClickableSprite.START_LEVEL_FOUR);		
        GameObjectMgr.menuItemList.add(menuSprite);
        GameObjectMgr.menuItemList.add(menuSprite2);
        GameObjectMgr.menuItemList.add(menuSprite3);
        GameObjectMgr.menuItemList.add(menuSprite4);

        setGameState(STATE_MENU);
    }

    public boolean isGameOver()
    {
        return getGameState()==STATE_GAMEOVER;
    }

    public boolean isMenu()
    {
        return getGameState()==STATE_MENU;
    }

    public static int getCurrentLevel() {
        return currentLevel;
    }

    public static void setCurrentLevel(int currentLevel) {
        OldPanel.currentLevel = currentLevel;
    }


    private void setupUI()
    {
        // Setup UI:	
        ClickableSprite ns = new ClickableSprite( getResources(), 100, 200, BitmapMgr.projBmp, BitmapMgr.projBmp, 3);
        ns.setAction(ClickableSprite.CHOOSE_PROJECTILE);
        ns.setPersistent(true);
        GameObjectMgr.gameUIList.add(ns);

        Bitmap[] sb = {swordBmp};
        ClickableSprite ss = new ClickableSprite( getResources(), 200, 200, sb, sb, 3);
        ss.setAction(ClickableSprite.CHOOSE_SWORD);
        ss.setPersistent(true);
        GameObjectMgr.gameUIList.add(ss);

        Bitmap health[] = { BitmapFactory.decodeResource(getResources(), R.drawable.healthbox) };
        int numHealthBars = GameObjectMgr.hero.getHealth() / 25;
        for(int i=0; i<numHealthBars; i++)
        {
            ClickableSprite hs = new ClickableSprite( getResources(), 100 + 100*i, 100, health,
                    health, 2);
            hs.setPersistent(true);
            GameObjectMgr.healthBarList.add(hs);

        }
    }

    public void gameStartLevelOne(Context context)
    {
        Log.d("red", "start of gamestartlevelone" );

        GameObjectMgr.initializeLists();

        initializeHero();

        generateMountains();

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);


        ScreenMgr.scrollSpeed=0;
        ScreenMgr.scrollLock = true;


        Resources res = getResources();
        // setup game:
        // mp = MediaPlayer.create(context, R.raw.excellent);

        // add platforms:
        for(int i =0; i<10; i++)
        {
            addBlock( i * 600, i* 600 + 400,450,550, Color.WHITE);
        }

        croc = new Enemy(res, 1000, 100, BitmapMgr.crocbitmap, BitmapMgr.crocbitmap_f,
                BitmapMgr.projBmp, GameObjectMgr.mProjList, GameObjectMgr.mBlockList, GameObjectMgr.mGameObjList, 1 );
        GameObjectMgr.mEnemyList.add(croc);
        GameObjectMgr.mGameObjList.add(croc);
        GameObjectMgr.mGravSpriteList.add(croc);

        class LvlOneGoal extends Goal
        {
            public void complete()
            {

            }

            Runnable r ;
        }

        LvlOneGoal lvlOneGoal = new LvlOneGoal();

        GoalOrb goalOrb = new GoalOrb(goalOrbRes, 2000, 200, .5f);
        goalOrb.setGoal(lvlOneGoal);

        GameObjectMgr.mGameObjList.add(goalOrb);
        goalOrb.startAnimation(0, 5);

        TimeMgr.gameStartTime = System.currentTimeMillis();
        TimeMgr.enemySpawnTime = TimeMgr.gameStartTime;

        //flyingHero = new FlyingSprite(flyingResources, 100, 100, 1);
        //ListManager.mGameObjList.add(flyingHero);
        door = new InteractiveScenery(res, 500, 500, door_b, door_b, 4, true);
        GameObjectMgr.mGameObjList.add(door);
        GameObjectMgr.iSceneryList.add(door);

        blebleguy = new NPC( tomatomanRes, 500,500, 2 );
        blebleguy.startAnimation(0, 20);
        blebleguy.speak("hello", 9000);
        GameObjectMgr.mGameObjList.add(blebleguy);

        //		    dragontest = new NPC( dragonHeadRes, mWidth-200, 0, 3 );
        //		    dragontest.startAnimation(0, 20);
        //		    ListManager.mGameObjList.add(dragontest);



        //START MUSIC:

        //mp.setLooping(true);
        //mp.start();


        setGameState(STATE_GAME_RUNNING);



    } // END GAMESTART LEVEL ONE



    public void gameStartLevelTwo(Context context)
    {
        Log.d("bloh", "start of gamestartlevelone" );

        GameObjectMgr.initializeLists();

        initializeHero();

        generateMountains();

        logos = new BackgroundSprite(logos_b, (float) mWidth - logos_b.getWidth()*12, (float) 150, 12);
        GameObjectMgr.bgSceneryList.add(1, logos);

        dragon = new Dragon( dragonHeadRes, dragonBodyRes, mWidth-200, 0, 2, 5 );
        GameObjectMgr.mGameObjList.add(dragon);

        addBlock(  0,  400,450,550, Color.WHITE);


        setGameState(STATE_GAME_RUNNING);

    }

    private void gameStartLevelThree(Context context) {
        GameObjectMgr.initializeLists();

        initializeHero();

        generateMountains();

        // 
        addBlock(  0,  400,450,550, Color.WHITE);
        //	
        addBlock(  600,  1000,450,550, Color.WHITE);
        //	
        PhysicsStuff phys = new PhysicsStuff(1, 20);
        //	
        Ninja ninj = new Ninja(ninjaRes, 0, 0, 2, phys);
        //	
        GameObjectMgr.mGameObjList.add(ninj);
        //
        ninj = new Ninja(ninjaRes, 700, 0, 2, phys);
        //
        GameObjectMgr.mGameObjList.add(ninj);

        PhysicsStuff physLight = new PhysicsStuff(0, 20);

        Ninja floatingGuy = new Ninja(coffeeCupRes, 400, 0, 2, physLight);

        GameObjectMgr.mGameObjList.add(floatingGuy);

        floatingGuy.startAnimation(0);

        setupUI();

        setGameState(STATE_GAME_RUNNING);

    }

    private void gameStartLevelFour(Context context)
    {
        /*
           ListManager.initializeLists();

           initializeHero();

           addBlock(  0,  400,450,550, Color.WHITE);

           PhysicsStuff phys = new PhysicsStuff(0, 20);

           Ninja floatingGuy = new Ninja(coffeeCupRes, 0, 0, 2, phys);

           ListManager.mGameObjList.add(floatingGuy);

           setupUI();

           setGameState(STATE_GAME_RUNNING);
           */
        GameObjectMgr.initializeLists();

        initializeHero();

        // 
        addBlock(  0,  400,450,550, Color.WHITE);
        //	
        addBlock(  600,  1000,450,550, Color.WHITE);
        //	
        PhysicsStuff phys = new PhysicsStuff(1, 20);
        //	
        Ninja ninj = new Ninja(ninjaRes, 0, 0, 2, phys);
        //	
        GameObjectMgr.mGameObjList.add(ninj);
        //
        ninj = new Ninja(ninjaRes, 700, 0, 2, phys);
        //
        GameObjectMgr.mGameObjList.add(ninj);

        setupUI();

        setGameState(STATE_GAME_RUNNING);

    }

    private void initializeHero() {
        GameObjectMgr.hero = new HeroSprite(getResources(), 250, 100, heroBmps, heroReverseBmps, GameObjectMgr.mProjList, 1);
        GameObjectMgr.mGravSpriteList.add(GameObjectMgr.hero);
        GameObjectMgr.mGameObjList.add(GameObjectMgr.hero);
        ScreenMgr.heroSavedPos = (GameObjectMgr.hero.bX+GameObjectMgr.hero.mX)/2 ;
    }

    public void leaveMenu()
    {
        switch(currentLevel)
        {
            case ClickableSprite.START_LEVEL_ONE:
                gameStartLevelOne(getContext());
                break;
            case ClickableSprite.START_LEVEL_TWO:
                gameStartLevelTwo(getContext());
                break;
            case ClickableSprite.START_LEVEL_THREE:
                gameStartLevelThree(getContext());
                break;
            case ClickableSprite.START_LEVEL_FOUR:
                gameStartLevelFour(getContext());
                break;
        }

    }

    public void restartGame(Context context)
    {
        showMenu();
    }

    // move out to somewhere else
    public void loadBitmapsLevelOne()
    {
        int i;
        Resources mRes = getResources();

        BitmapFactory.Options options_pixellated = new BitmapFactory.Options();
        options_pixellated.inDither = false;
        options_pixellated.inScaled = false;

        swordBmp = BitmapFactory.decodeResource(mRes, R.drawable.herosword);

        platformBitmap = BitmapFactory.decodeResource(mRes, R.drawable.platform, options_pixellated);

        //load monk animation frames:
        //int imgs[] = ResourceGetter.getResourceList(getContext(), "monk");
        int imgs[] = new int[22];
        imgs[0]=R.drawable.monk01;
        imgs[1]=R.drawable.monk02;
        imgs[2]=R.drawable.monk03;
        imgs[3]=R.drawable.monk04;
        imgs[4]=R.drawable.monk05;
        imgs[5]=R.drawable.monk06;
        imgs[6]=R.drawable.monk07;
        imgs[7]=R.drawable.monk08;
        imgs[8]=R.drawable.monk09;
        imgs[9]=R.drawable.monk10;
        imgs[10]=R.drawable.monk11;
        imgs[11]=R.drawable.monk12;
        imgs[12]=R.drawable.monk13;
        imgs[13]=R.drawable.monk14;
        imgs[14]=R.drawable.monk15;
        imgs[15]=R.drawable.monk16;
        imgs[16]=R.drawable.monk17;
        imgs[17]=R.drawable.monk18;
        imgs[18]=R.drawable.monk19;
        imgs[19]=R.drawable.monk20;
        imgs[20]=R.drawable.monk21;
        imgs[21]=R.drawable.monk06;  // jump image

        BitmapFactory.Options o = new BitmapFactory.Options();


        o.inSampleSize=4;
        o.inScaled = false;

        //projectile images:
        BitmapMgr.projBmp = new Bitmap[1];
        BitmapMgr.projBmp[0] = BitmapFactory.decodeResource(mRes, R.drawable.projectile_small, o);


        heroBmps = new Bitmap[imgs.length];

        heroReverseBmps = new Bitmap[imgs.length];

        for(i=0; i<imgs.length; i++ )
        {
            heroBmps[i] = BitmapFactory.decodeResource( mRes, imgs[i], o );
        }

        heroReverseBmps = ImageUtils.flipBmpHorizontal( heroBmps );

        BitmapMgr.crocbitmap = new Bitmap[22];  //TODO: don't need to duplicate "jump" bitmap.
        BitmapMgr.crocbitmap_f = new Bitmap[22];

        int ninjaBmps[] = new int[22];
        ninjaBmps[0]=R.drawable.ninja_0;
        ninjaBmps[1]=R.drawable.ninja_1;
        ninjaBmps[2]=R.drawable.ninja_2;
        ninjaBmps[3]=R.drawable.ninja_3;
        ninjaBmps[4]=R.drawable.ninja_4;
        ninjaBmps[5]=R.drawable.ninja_5;
        ninjaBmps[6]=R.drawable.ninja_6;
        ninjaBmps[7]=R.drawable.ninja_7;
        ninjaBmps[8]=R.drawable.ninja_8;
        ninjaBmps[9]=R.drawable.ninja_9;
        ninjaBmps[10]=R.drawable.ninja_10;
        ninjaBmps[11]=R.drawable.ninja_11;
        ninjaBmps[12]=R.drawable.ninja_12;
        ninjaBmps[13]=R.drawable.ninja_13;
        ninjaBmps[14]=R.drawable.ninja_14;
        ninjaBmps[15]=R.drawable.ninja_15;
        ninjaBmps[16]=R.drawable.ninja_16;
        ninjaBmps[17]=R.drawable.ninja_17;
        ninjaBmps[18]=R.drawable.ninja_18;
        ninjaBmps[19]=R.drawable.ninja_19;
        ninjaBmps[20]=R.drawable.ninja_20;
        ninjaBmps[21]=R.drawable.ninja_5;  // jump image

        for(i=0; i<imgs.length; i++ )
        {
            BitmapMgr.crocbitmap[i] = BitmapFactory.decodeResource( mRes, ninjaBmps[i], o );
        }

        BitmapMgr.crocbitmap_f = ImageUtils.flipBmpHorizontal( BitmapMgr.crocbitmap );

        qiBmp = new Bitmap[1];

        qiBmp[0]= BitmapFactory.decodeResource(mRes, R.drawable.qi);

        int[] tomatoman_imgs= { R.drawable.tomatoman_1, R.drawable.tomatoman_2};
        int speechbubble[] = { R.drawable.speech_bubble };

        tomatomanRes = new SpriteResources( mRes, true, 1, tomatoman_imgs, speechbubble ); 

        pagoda_left = new Bitmap[1];
        pagoda_left[0]= BitmapFactory.decodeResource(mRes, R.drawable.pagoda_left);
        pagoda_repeat = new Bitmap[1];
        pagoda_repeat[0]= BitmapFactory.decodeResource(mRes, R.drawable.pagoda_repeating);
        pagoda_right = new Bitmap[1];
        pagoda_right[0]= BitmapFactory.decodeResource(mRes, R.drawable.pagoda_right);

        int [] monk_med = new int[21];
        monk_med[0] = R.drawable.amonk_med0001;
        monk_med[1] = R.drawable.amonk_med0002;
        monk_med[2] = R.drawable.amonk_med0003;
        monk_med[3] = R.drawable.amonk_med0004;
        monk_med[4] = R.drawable.amonk_med0005;
        monk_med[5] = R.drawable.amonk_med0006;
        monk_med[6] = R.drawable.amonk_med0007;
        monk_med[7] = R.drawable.amonk_med0008;
        monk_med[8] = R.drawable.amonk_med0009;
        monk_med[9] = R.drawable.amonk_med0010;
        monk_med[10] = R.drawable.amonk_med0011;
        monk_med[11] = R.drawable.amonk_med0012;
        monk_med[12] = R.drawable.amonk_med0013;
        monk_med[13] = R.drawable.amonk_med0014;
        monk_med[14] = R.drawable.amonk_med0015;
        monk_med[15] = R.drawable.amonk_med0016;
        monk_med[16] = R.drawable.amonk_med0017;
        monk_med[17] = R.drawable.amonk_med0018;
        monk_med[18] = R.drawable.amonk_med0019;
        monk_med[19] = R.drawable.amonk_med0020;
        monk_med[20] = R.drawable.amonk_med0021;

        //newMonkRes = new SpriteResources(mRes, imgs, monk_med);

        int [] flyingBmps = new int[1];
        //flyingBmps[0] = R.drawable.flyer_remove;
        //flyingResources = new SpriteResources(mRes, true, 1, flyingBmps);

        door_b = new Bitmap[1];

        door_b[0] = BitmapFactory.decodeResource(mRes, R.drawable.door, options_pixellated);

        cloudBitmap =  BitmapFactory.decodeResource(mRes, R.drawable.cloud);

        logos_b = BitmapFactory.decodeResource(mRes, R.drawable.logos, options_pixellated);

        int dragonBmps[] = {R.drawable.dragon1, R.drawable.dragon2 };

        dragonHeadRes = new SpriteResources(mRes, true, 1, dragonBmps);

        int dragonCubeBmp[] = {R.drawable.dragoncube};

        dragonBodyRes = new SpriteResources(mRes, true, 1, dragonCubeBmp );

        int ninjaJumpBmp[] =
        { R.drawable.ninjajump_0,
            R. drawable.ninjajump_1,
            R. drawable.ninjajump_2,
            R. drawable.ninjajump_3,
            R. drawable.ninjajump_4,
            R. drawable.ninjajump_5,
        };

        ninjaRes = new SpriteResources(mRes, false, 4, ninjaBmps, ninjaJumpBmp );

        int goalOrbBmp[] =
        {
            drawable.drawing1,
            R.drawable.drawing2,
            R.drawable.drawing3,
            R.drawable.drawing4,
            R.drawable.drawing5,
            R.drawable.drawing6,
            R.drawable.drawing7,
            R.drawable.drawing6,
            R.drawable.drawing5,
            R.drawable.drawing4,
            R.drawable.drawing3,
            R.drawable.drawing2,
            R.drawable.drawing1

        };
        goalOrbRes = new SpriteResources(mRes,true, 1, goalOrbBmp);

        int ccup[] = {
            R.drawable.coffeecup01,
            R.drawable.coffeecup02,
            R.drawable.coffeecup03,
            R.drawable.coffeecup04,
            R.drawable.coffeecup05,
            R.drawable.coffeecup06,
            R.drawable.coffeecup07,
            R.drawable.coffeecup08,
            R.drawable.coffeecup09,
            R.drawable.coffeecup10,
            R.drawable.coffeecup11,
            R.drawable.coffeecup12,
            R.drawable.coffeecup13,
            R.drawable.coffeecup14,
            R.drawable.coffeecup15,
            R.drawable.coffeecup16,
            R.drawable.coffeecup17,
            R.drawable.coffeecup18,
            R.drawable.coffeecup19,
            R.drawable.coffeecup20,
            R.drawable.coffeecup21,
            R.drawable.coffeecup22,
            R.drawable.coffeecup23,
            R.drawable.coffeecup24,
            R.drawable.coffeecup25
        };

        coffeeCupRes = new SpriteResources(mRes,true, 1, ccup);

    }

    public static void removeFromLists( GameObject mObj )
    {
        GameObjectMgr.mGameObjList.remove(mObj);

        if(mObj instanceof Enemy)
            GameObjectMgr.mEnemyList.remove(mObj);

        if(mObj instanceof GravitySprite)
            GameObjectMgr.mGravSpriteList.remove(mObj);

        if(mObj instanceof Projectile)
            GameObjectMgr.mProjList.remove(mObj);

        if(mObj instanceof Block)
            GameObjectMgr.mBlockList.remove(mObj);

        if(mObj instanceof NPC)
            GameObjectMgr.mNPCList.remove(mObj);
    }


    public void generateMountains()
    {
        float coords[][] = { 
            {0,OldPanel.mHeight},
            {0, OldPanel.mHeight/2} ,
            {OldPanel.mWidth/4,OldPanel.mHeight/6}, 
            {OldPanel.mWidth/2,OldPanel.mHeight/2}, 
            {OldPanel.mWidth*3/4, OldPanel.mHeight/5},
            {OldPanel.mWidth, (float) (OldPanel.mHeight /2)},
            {OldPanel.mWidth, OldPanel.mHeight }    		  
        };


        int numrepeats = 5; 

        Mountain mountain;

        BackgroundScenery cloud;

        int numMountains = 3;

        String mtnColors[] = { "#7b86b8", "#74778f", "#a0a3b2" };

        float xoffset = 50;
        float yoffset = 200;

        for(int k=0; k<numMountains; k++)
        {

            float newcoords[][] = new float[numrepeats * coords.length][2];

            for(int i=-1; i< numrepeats-1; i++)
            {
                for(int j =0; j< coords.length; j++)
                {
                    newcoords[(i+1) * coords.length + j][0] = coords[j][0] + OldPanel.mWidth * i;  // x coord: shift
                    newcoords[(i+1) * coords.length + j][1] = coords[j][1];   // y coord: keep
                }
            }

            for(int i = 0; i< newcoords.length; i++)
            {
                newcoords[i][0] += xoffset*k; 
                newcoords[i][1] += yoffset*k; 

            }

            mountain = new Mountain( Color.parseColor( mtnColors[k] ) , newcoords );

            cloud = new BackgroundSprite(cloudBitmap, OldPanel.mWidth * k /4, OldPanel.mHeight/3, 1 );

            GameObjectMgr.bgSceneryList.add((BackgroundScenery) mountain);
            GameObjectMgr.bgSceneryList.add((BackgroundScenery) cloud);

        }
    }

    public void loadBitmapsLevelOneTwo()
    {

    }

    public ViewThread getThread()
    {
        return mThread;
    }

    public static CopyOnWriteArrayList<Projectile> getProjList() {
        return GameObjectMgr.mProjList;
    }

    public static CopyOnWriteArrayList<Block> getBlockList() {
        return GameObjectMgr.mBlockList;
    }

    public static CopyOnWriteArrayList<GameObject> getGameObjList() {
        return GameObjectMgr.mGameObjList;
    }

    public static int getGameState() {
        return gameState;
    }

    public void setGameState(int gameState) {
        OldPanel.gameState = gameState;
    }

}

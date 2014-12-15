package com.thousandonestories.game.gameobjects; 

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.concurrent.CopyOnWriteArrayList;

import com.thousandonestories.game.BackgroundScenery;
import com.thousandonestories.game.BackgroundSprite;
import com.thousandonestories.game.InteractiveScenery;
import com.thousandonestories.game.Mountain;
import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.R;
import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.ViewThread;
import com.thousandonestories.game.R.drawable;
import com.thousandonestories.game.R.raw;
import com.thousandonestories.game.ui.ClickableSprite;

//gravity stuff does not go in panel

//TODO: scaleFactor does nothing

public class OldPanel extends SurfaceView implements SurfaceHolder.Callback {

	/*
	 * Game states:
	 */
	
	public static final int STATE_UNINITIALIZED=0;
	public static final int STATE_GAMEOVER = 1;
	public static final int STATE_GAME_RUNNING= 2;
	public static final int STATE_MENU= 3;

	private static int gameState;
	
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

	//Lists containing different game objects: 
	private static CopyOnWriteArrayList<GameObject> mGameObjList;
	//private CopyOnWriteArrayList<Sprite> mSpriteList = new CopyOnWriteArrayList<Sprite>();
	private static CopyOnWriteArrayList<GravitySprite> mGravSpriteList;
	private static CopyOnWriteArrayList<Block> mBlockList;
	private static CopyOnWriteArrayList<Projectile> mProjList;
	private static CopyOnWriteArrayList<Enemy> mEnemyList;
	private static CopyOnWriteArrayList<NPC> mNPCList;
	private static CopyOnWriteArrayList<InteractiveScenery> iSceneryList;
	private static CopyOnWriteArrayList<BackgroundScenery> bgSceneryList;
	private static CopyOnWriteArrayList<ClickableSprite> gameUIList;
	private static CopyOnWriteArrayList<ClickableSprite> healthBarList;
	private static CopyOnWriteArrayList<BackgroundSprite> m3dPlatformList;

	/**
	 * Holds bitmaps that will be drawn to the menu screen
	 */
	private static CopyOnWriteArrayList<ClickableSprite> menuItemList;


	/**
	 * The level that the player is currently on.
	 */
	private static int level;
	//private 	DiskLruCache mDiskLruCache;

	public static float mWidth;
	public static float mHeight;

	public static float scrollSpeed;
	private boolean scrollLock;
	private float heroSavedPos; // saved position for camera
	private boolean heroSavedDir; // saved direction character is pointing
	private long gameStartTime;
	private long crocSpawnTime;

	private Paint mPaint;
	private ViewThread mThread;

	public static HeroSprite hero; 
	private Enemy croc;

	private FlyingSprite flyingHero;

	
	public static Bitmap swordBmp;

	Bitmap cloudBitmap;

	Bitmap crocbitmap[];
	Bitmap crocbitmap_f[];

	public static Bitmap projBmp[];
	Bitmap qiBmp[];
	Bitmap heroBmps[];
	Bitmap heroReverseBmps[];

	Bitmap pagoda_left[];
	Bitmap pagoda_right[];
	Bitmap pagoda_repeat[];

	Bitmap dragonCube;

	Bitmap platformBitmap;

	SpriteResources newMonkRes;

	SpriteResources flyingResources;

	SpriteResources tomatomanRes;

	SpriteResources dragonHeadRes;

	SpriteResources dragonBodyRes;

	SpriteResources ninjaRes;

	ClickableSprite gameOverSprite;
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


	public OldPanel(Context context) {
	
		super(context);

		setGameState(STATE_UNINITIALIZED);
		
		level = 2;

		Log.d("bleh", "Panel constructor called");

		getHolder().addCallback(this);

		menuItemList = new CopyOnWriteArrayList<ClickableSprite>();


		showMenu();

		loadBitmapsLevelOne();


		mThread = new ViewThread(this);

		bgSong = MediaPlayer.create(context, R.raw.shooter);
		
		blip = MediaPlayer.create(context, R.raw.blip);
		
		blip2 = MediaPlayer.create(context, R.raw.blip2);

	}

	public void printText( String str, Canvas canvas, Paint paint, int x, int y )
	{
		canvas.drawText(str, 10, 10, paint);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Create and start new thread
		Log.d("bleh","surfaceCreated called");

		if (!mThread.isAlive()) {
			mThread = new ViewThread(this);
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

	// Update position and velocity of all sprites in list
	public void update(long elapsedTime) {

		if(hero==null) return;
		
		if(isMenu() ) return;
		
		if(hero.getHealth() <= 0)
		{
			gameOver();

		}
		
		updateHealthBar();

		//CAMERA CONTROL:

			int moveBox = 100; // amount hero can move before camera locks to him again


			if(!scrollLock) // scroll lock off: screen fixed, hero moves
			{
				float heroX = (hero.bX+hero.mX) / 2; //center of the hero
				if( heroX >= heroSavedPos + moveBox  || heroX <= heroSavedPos-moveBox  )
				{
					scrollLock = true;
					heroSavedPos = (hero.bX+hero.mX)/2; //save hero's position
					heroSavedDir=hero.getDirection(); //save hero's direction
				}
			}
			else // scroll lock is on: hero fixed, screen scrolling
			{
				if(heroSavedDir != hero.getDirection() ) // hero direction has changed
				{
					heroSavedDir = hero.getDirection();

					scrollLock=false;
				}
			}
			//end camera control

			scrollLock = true;

			//TODO: fold these checks into one main GameObject loop.
			//Check sprite collisions with blocks:
			checkBlocks(elapsedTime);

			//Check enemy collisions with projectiles:
			checkEnemyProjectiles(elapsedTime);

			//update enemy AI:
			updateEnemyAI();

			float offset = 1;

			for(BackgroundScenery bgObj: bgSceneryList)
			{
				if(scrollLock)
				{
					bgObj.scroll( scrollSpeed/ ( 20f + 10*offset ) ,  elapsedTime );
					offset++;
				}
			}

			for( BackgroundSprite platform: m3dPlatformList)
			{
				platform.scroll( scrollSpeed/ ( 20f ) ,  elapsedTime );
			}

			for (GameObject mObj : mGameObjList) {

				mObj.update(elapsedTime); //update object's position etc
				if( scrollLock) 
				{
					mObj.scroll(scrollSpeed/20f,  elapsedTime ) ; // scroll object as necessary
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

					if( mObj instanceof Block )
					{
						mBlockList.remove(mObj);
					}

					if( mObj instanceof Projectile )
					{
						mProjList.remove(mObj);
					}

				}

				if( mObj.getTopBound() >= this.getHeight() && mObj instanceof Enemy)
				{
					mGameObjList.remove( mObj );
					mEnemyList.remove( mObj );
				}

			}

			//Make more enemies:
			if( ( System.currentTimeMillis() - crocSpawnTime >= 5000) //enough time has passed
					&& (mEnemyList.size() < 10 ) )  // we're not saturated with enemies 
			{
				Enemy newCroc = new Enemy(getResources(), 1100, 100, crocbitmap, crocbitmap_f, projBmp, mProjList, mBlockList, mGameObjList, 1 );
				mEnemyList.add(newCroc);
				mGameObjList.add(newCroc);
				mGravSpriteList.add(newCroc);

				crocSpawnTime = System.currentTimeMillis();

			}

			for (InteractiveScenery iScenery: iSceneryList)
			{
				if( checkCollision( hero, iScenery ) )
				{
					iScenery.hide();
				}
			}


			//check if hero has fallen below the screen:
			if(hero.getTopBound() >= OldPanel.mHeight)
			{
				gameOver();
			}

	} // end of update function


	public static boolean checkCollision(GameObject obj1, GameObject obj2) {		
		return (obj1.getLeftBound() < obj2.getRightBound() && obj1.getRightBound() > obj2.getLeftBound() &&
			    obj1.getTopBound() < obj2.getBottomBound() && obj1.getBottomBound() > obj2.getTopBound() ) ; 
	}


	// Draw all sprites and blocks in list
	//int color = 0;
	String color = "#7ec0ee";
	public void doDraw(Canvas canvas) {


		canvas.drawColor( Color.parseColor(color) );

		if( isGameOver() )
		{
			Paint mPaint = new Paint();
			mPaint.setColor(Color.WHITE);
			gameOverSprite.doDraw( canvas  );
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

			for(ClickableSprite menuSprite : menuItemList)
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
			canvas.drawColor( Color.parseColor(color) );


			/*
			 * Background Scenery: paths
			 */

			for( BackgroundScenery bgObj: bgSceneryList )
			{
				bgObj.doDraw(canvas);

			}

			/*
			 * Background Sprites: bitmaps
			 */	

			for( BackgroundSprite bSprite: m3dPlatformList)
			{
				bSprite.doDraw(canvas);
			}

			/* 
			 *  GameObjects: objects in the game world you can interact with.
			 */

			for( GameObject object: mGameObjList)
			{
				if(object instanceof Block && !DRAW_BLOCKS)
					continue;

				else if(!object.hidden() )
				{
					object.doDraw(canvas);
				}

			}

			for( ClickableSprite ui: gameUIList )
			{
				ui.doDraw(canvas);
			}
			
			for( ClickableSprite healthBar: healthBarList )
			{
				healthBar.doDraw(canvas);
			}


			//Debugging stuff:

			if(level == 1)
			{
				canvas.drawText(" CrocLB: " + croc.blockLB + " crocRB: " + croc.blockRB + 
						" LandCount "+hero.db_landcount+" density: "+  getResources().getDisplayMetrics().density + " elapsed: " 
						+" FPS: "+1000f/ ViewThread.mElapsed , 10, 10, mPaint);
				canvas.drawText("Enemy_state " + croc.getState(), 10, 40, mPaint);
				canvas.drawText("Health = " + hero.getHealth() + "Velocity = " + hero.getVelocity() + "x= " + hero.getLeftBound()
						+ " rightB= " + hero.getRightBound(), 10, 25, mPaint);
			}
		}


	} //end of doDraw function

	public void addBlock(int left, int right, int top, int bottom, int color)
	{
		Block block = new Block(left, top, right, bottom, color);
		mBlockList.add( block );
		mGameObjList.add( block);

		//Add platform image:
		m3dPlatformList.add(
				new BackgroundSprite(platformBitmap,left, top - 30, 6) );

	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {


		for( ClickableSprite clickSprite : menuItemList)
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
		
		for( ClickableSprite uiSprite : gameUIList)
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

		if( hero != null && gameState==STATE_GAME_RUNNING )
		{
			//touchHandleFlying(event);
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
		// check orientation of the hero:
		if( (int) (event.getX()-OldPanel.mWidth/2) < 0 )
		{
			hero.flipBmp=true;
		}
		else
			hero.flipBmp=false;

		int vel;
		int startpt;
		Projectile proj;

		// touch at top of screen:
		if(event.getY()< 200) 
		{
			if(event.getAction() == MotionEvent.ACTION_DOWN)
				hero.jump();
		}
		else if(event.getY()< 400)
		{
			//shoot a fireball :

			if(event.getAction()==MotionEvent.ACTION_DOWN)
			{
				// check hero orientation:
				if(!hero.flipBmp)
				{
					vel = 100;
					startpt = (int) hero.getRightBound();
				}
				else
				{
					vel= -100;
					startpt = (int) hero.getLeftBound();
				}

				hero.fire(System.currentTimeMillis());
				proj = new Projectile(getResources(), startpt, (int) ( (hero.bY+hero.mY)/2), vel, 
						projBmp, Projectile.TYPE_HERO, 4);
				mProjList.add( proj);
				mGameObjList.add(proj);
			}
		}
		else
		{

			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				moveHero( (event.getX()-OldPanel.mWidth/2) /20 * hero.getScaleFactor() );
				//hero.mDx=(int) (event.getX()-Panel.mWidth/2) /20 * hero.getScaleFactor();
				hero.run(System.currentTimeMillis());

			}
			if(event.getAction() == MotionEvent.ACTION_MOVE)
			{
				moveHero(  (event.getX()-OldPanel.mWidth/2) /20 * hero.getScaleFactor() );
				//hero.mDx=(int) (event.getX()-Panel.mWidth/2) /20 ;
			}

			if(event.getAction() == MotionEvent.ACTION_UP)
			{
				moveHero(0);
				hero.rest();
			}
		}

		if( event.getActionMasked()==MotionEvent.ACTION_POINTER_DOWN)
		{
			hero.jump();
		}

		//return super.onTouchEvent(event);
		return true;

	}



	public void moveHero(float speed)
	{
		hero.mDx = speed;

		scrollLock = true;

		if(scrollLock) 
		{
			scrollSpeed = - speed;
		}

	}	   

	public void checkBlocks(long mElapsed) {
		//check for new collisions between GravitySprites and Blocks or a fall() event
		for( GravitySprite sprite : mGravSpriteList)
		{
			if( sprite.getState() == GravitySprite.STATE_INAIR )
			{
				sprite.checkLanded(mBlockList, mElapsed);
			}
			else // sprite is on a block
			{
				for (Block block: mBlockList)
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

	public void updateEnemyAI()
	{
		for( Enemy enemy: mEnemyList )
		{
			if( enemy.getState() != GravitySprite.STATE_INAIR ) //enemy is on the ground
			{

				//if enemy has a block
				enemy.AICheckMoveBounds();

			}
		}
	}

	public void checkEnemyProjectiles(long mElapsed) {
		for( Enemy enemy: mEnemyList)
		{
			//check for hits by projectiles:
			if( enemy.checkProjectile( mProjList ) )
			{
				enemy.die( System.currentTimeMillis() );
			}

			//check if it's time to hide:
			enemy.checkHide( System.currentTimeMillis(), mEnemyList );

		}
	}

	public Bitmap[] flipBmpHorizontal( Bitmap[] input )
	{
		int i;
		Bitmap output[] = new Bitmap[input.length];
		Matrix m = new Matrix();
		m.preScale(-1, 1);
		for(i=0; i< input.length; i++)
		{
			//flip bitmap:

			output[i]=Bitmap.createBitmap(input[i], 0, 0, input[i].getWidth(), input[i].getHeight(), m, false);
			output[i].setDensity(DisplayMetrics.DENSITY_DEFAULT);

		}
		return output;
	}

	public void gameOver()
	{
		Log.d("red", "gameOver() function started");

		for( GameObject object: mGameObjList )
		{
			object.hide();
		}

		mGravSpriteList.clear();
		mBlockList.clear();
		mEnemyList.clear();
		mProjList.clear(); 
		mGameObjList.clear();

		menuItemList = new CopyOnWriteArrayList<ClickableSprite>();

		Bitmap gameoverbitmap[] = new Bitmap[1];
		Bitmap gameoverbitmap_r[] = new Bitmap[1];
		gameoverbitmap[0]= BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
		gameoverbitmap_r = flipBmpHorizontal(gameoverbitmap);

		gameOverSprite= new ClickableSprite( getResources(), (int) OldPanel.mWidth/2, (int) OldPanel.mHeight/2, gameoverbitmap, gameoverbitmap_r, 1 );
		gameOverSprite.setAction(ClickableSprite.RESTART_GAME);

		menuItemList.add(gameOverSprite);

		Log.d("red", "menuitemlist size = " + menuItemList.size() );
		
		setGameState(STATE_GAMEOVER);

	}

	public void showMenu()
	{
		menuItemList = new CopyOnWriteArrayList<ClickableSprite>();

		Bitmap menuimg = BitmapFactory.decodeResource(getResources(), R.drawable.menuimg);
		Bitmap [] mimgarray = { menuimg };
		menuSprite = new ClickableSprite(getResources(), (int) 100, (int) 200, mimgarray, mimgarray, 1);
		menuSprite.setAction(ClickableSprite.START_LEVEL_ONE);

		ClickableSprite menuSprite2 = new ClickableSprite(getResources(), (int) 400, (int) 200, mimgarray, mimgarray, 1);
		menuSprite2.setAction(ClickableSprite.START_LEVEL_TWO);
		
		ClickableSprite menuSprite3 = new ClickableSprite(getResources(), (int) 600, (int) 200, mimgarray, mimgarray, 1);
		menuSprite3.setAction(ClickableSprite.START_LEVEL_THREE);		


		menuItemList.add(menuSprite);
		menuItemList.add(menuSprite2);
		menuItemList.add(menuSprite3);
		
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

	public static int getLevel() {
		return level;
	}

	public static void setLevel(int level) {
		OldPanel.level = level;
	}

	public void gameStartLevelOne(Context context)
	{
		Log.d("red", "start of gamestartlevelone" );

		initializeLists();

		initializeHero();

		generateMountains();

		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);


		scrollSpeed=0;
		scrollLock = false;


		Resources res = getResources();
		// setup game:
		// mp = MediaPlayer.create(context, R.raw.excellent);



		//background (TODO: experimental)

		//TODO: fix this--less lists

		// add platforms:
		for(int i =0; i<10; i++)
		{
			addBlock( i * 600, i* 600 + 400,600,700, Color.WHITE);

		}




		croc = new Enemy(res, 1000, 100, crocbitmap, crocbitmap_f, projBmp, mProjList, mBlockList, mGameObjList, 1 );
		mEnemyList.add(croc);
		mGameObjList.add(croc);
		mGravSpriteList.add(croc);



		gameStartTime = System.currentTimeMillis();
		crocSpawnTime = gameStartTime;

		//flyingHero = new FlyingSprite(flyingResources, 100, 100, 1);
		//mGameObjList.add(flyingHero);
		door = new InteractiveScenery(res, 500, 500, door_b, door_b, 4, true);
		mGameObjList.add(door);
		iSceneryList.add(door);



		blebleguy = new NPC( tomatomanRes, 500,500, 2 );
		blebleguy.startAnimation(0, 20);
		blebleguy.speak("hello", 9000);
		mGameObjList.add(blebleguy);

		//		    dragontest = new NPC( dragonHeadRes, mWidth-200, 0, 3 );
		//		    dragontest.startAnimation(0, 20);
		//		    mGameObjList.add(dragontest);



		//START MUSIC:

		//mp.setLooping(true);
		//mp.start();


		setGameState(STATE_GAME_RUNNING);



	} // END GAMESTART LEVEL ONE

	

	public void gameStartLevelTwo(Context context)
	{
		Log.d("bloh", "start of gamestartlevelone" );

		initializeLists();

		initializeHero();

		generateMountains();

		logos = new BackgroundSprite(logos_b, (float) mWidth - logos_b.getWidth()*12, (float) 150, 12);
		bgSceneryList.add(1, logos);

		dragon = new Dragon( dragonHeadRes, dragonBodyRes, mWidth-200, 0, 2, 5 );
		mGameObjList.add(dragon);

		addBlock(  0,  400,600,700, Color.WHITE);


		setGameState(STATE_GAME_RUNNING);

	}

	private void gameStartLevelThree(Context context) {
		initializeLists();

		initializeHero();
		
		
		addBlock(  0,  400,600,700, Color.WHITE);
		
		addBlock(  600,  1000,600,700, Color.WHITE);
		
		PhysicsStuff phys = new PhysicsStuff(1, 20);
		
		NewEnemy ninj = new NewEnemy(ninjaRes, 0, 0, 2, phys);
		
		mGameObjList.add(ninj);

		ninj = new NewEnemy(ninjaRes, 700, 0, 2, phys);
				
		mGameObjList.add(ninj);
		
		setGameState(STATE_GAME_RUNNING);
		
		
		ClickableSprite ns = new ClickableSprite( getResources(), 100, 200, projBmp, projBmp, 3);
		ns.setAction(ClickableSprite.CHOOSE_PROJECTILE);
		ns.setPersistent(true);
		gameUIList.add(ns);
		
		Bitmap[] sb = {swordBmp};
		ClickableSprite ss = new ClickableSprite( getResources(), 200, 200, sb, sb, 3);
		ss.setAction(ClickableSprite.CHOOSE_SWORD);
		ss.setPersistent(true);
		gameUIList.add(ss);

		Bitmap health[] = { BitmapFactory.decodeResource(getResources(), R.drawable.healthbox) };
		int numHealthBars = hero.getHealth() / 25;
		for(int i=0; i<numHealthBars; i++)
		{
			ClickableSprite hs = new ClickableSprite( getResources(), 100 + 100*i, 100, health, health, 2);
			hs.setPersistent(true);
			healthBarList.add(hs);

		}

	}
	
	private void initializeHero() {

		hero = new HeroSprite(getResources(), 250, 100, heroBmps, heroReverseBmps, mProjList, 1);

		mGravSpriteList.add(hero);
		mGameObjList.add(hero);
		heroSavedPos = (hero.bX+hero.mX)/2 ; 		
	}

	private void initializeLists() {

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
		
	}

	public void leaveMenu()
	{
		switch(level)
		{
		case 1:
			gameStartLevelOne(getContext());
			break;
		case 2:
			gameStartLevelTwo(getContext());
			break;
		case 3:
			gameStartLevelThree(getContext());
			break;
		}

	}



	public void restartGame(Context context)
	{
		showMenu();
	}

	public void loadBitmapsLevelOne()
	{
		int i;
		Resources mRes = getResources();

		BitmapFactory.Options options_pixellated = new BitmapFactory.Options();
		options_pixellated.inDither = false;
		options_pixellated.inScaled = false;

		swordBmp = BitmapFactory.decodeResource(mRes, R.drawable.herosword);

		platformBitmap = BitmapFactory.decodeResource(mRes, R.drawable.platform, options_pixellated);

		//CopyOnWriteArrayList<int> imgs = new CopyOnWriteArrayList(2)
		//load monk animation frames:
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
		projBmp = new Bitmap[1];
		projBmp[0] = BitmapFactory.decodeResource(mRes, R.drawable.projectile_small, o);


		heroBmps = new Bitmap[imgs.length];

		heroReverseBmps = new Bitmap[imgs.length];

		for(i=0; i<imgs.length; i++ )
		{
			heroBmps[i] = BitmapFactory.decodeResource( mRes, imgs[i], o );
		}

		heroReverseBmps = flipBmpHorizontal( heroBmps );

		crocbitmap = new Bitmap[22];  //TODO: don't need to duplicate "jump" bitmap.
		crocbitmap_f = new Bitmap[22];

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
			crocbitmap[i] = BitmapFactory.decodeResource( mRes, ninjaBmps[i], o );
		}



		crocbitmap_f = flipBmpHorizontal( crocbitmap );

		qiBmp = new Bitmap[1];

		qiBmp[0]= BitmapFactory.decodeResource(mRes, R.drawable.qi);

		int tomatoman_imgs[]= { R.drawable.tomatoman_1, R.drawable.tomatoman_2 };
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
	}

	public static void removeFromLists( GameObject mObj )
	{
		mGameObjList.remove(mObj);

		if(mObj instanceof Enemy)
			mEnemyList.remove(mObj);

		if(mObj instanceof GravitySprite)
			mGravSpriteList.remove(mObj);

		if(mObj instanceof Projectile)
			mProjList.remove(mObj);

		if(mObj instanceof Block)
			mBlockList.remove(mObj);

		if(mObj instanceof NPC)
			mNPCList.remove(mObj);
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

			bgSceneryList.add((BackgroundScenery) mountain);
			bgSceneryList.add((BackgroundScenery) cloud);

		}
	}
	
	public void updateHealthBar()
	{
		int numHealthBars = hero.getHealth()/25;
		if(numHealthBars < healthBarList.size())
			healthBarList.remove(numHealthBars);

	}

	public ViewThread getThread()
	{
		return mThread;
	}

	public static CopyOnWriteArrayList<Projectile> getProjList() {
		return mProjList;
	}

	public static CopyOnWriteArrayList<Block> getBlockList() {
		return mBlockList;
	}

	public static CopyOnWriteArrayList<GameObject> getGameObjList() {
		return mGameObjList;
	}

	public static int getGameState() {
		return gameState;
	}

	public void setGameState(int gameState) {
		OldPanel.gameState = gameState;
	}

}

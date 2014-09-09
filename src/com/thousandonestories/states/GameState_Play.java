package com.thousandonestories.states;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.thousandonestories.game.GameManager;
import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.R;
import com.thousandonestories.game.SpriteResources;
import com.thousandonestories.game.gameobjects.HeroSprite;
import com.thousandonestories.game.gameobjects.NewHeroSprite;
import com.thousandonestories.game.gameobjects.NewPhysicsSprite;
import com.thousandonestories.game.gameobjects.OldPanel;
import com.thousandonestories.game.gameobjects.Projectile;
import com.thousandonestories.game.levels.Level;
import com.thousandonestories.game.levels.LevelOne;
import com.thousandonestories.game.ui.ClickableSprite;
import com.thousandonestories.game.ui.HUD;

public class GameState_Play extends GameState{

	private HUD hud;
	private Level currentLevel;
	private HeroSprite hero;
	
	private NewHeroSprite hro;

	private Bitmap[] projBmp;

	public GameState_Play(GameManager gm) {
		super(gm);
		hud = new HUD();
		Bitmap[] uiElem = { BitmapFactory.decodeResource(gm.getResources(), R.drawable.goku) };
		hud.addElement(new ClickableSprite(gm.getResources(), 500, 500, uiElem, 1  ));

		currentLevel = new LevelOne(gm);

		currentLevel.loadBitmaps();
		currentLevel.start();

		//getBmps("hero");

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

		Bitmap[] heroBmps = new Bitmap[imgs.length];
		Bitmap[] heroReverseBmps = new Bitmap[imgs.length];

		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inScaled=false;

		for(int i=0; i<imgs.length; i++)
		{
			heroBmps[i] = BitmapFactory.decodeResource(gm.getResources(), imgs[i], o);
		}

		heroReverseBmps = OldPanel.flipBmpHorizontal(heroBmps);

		projBmp = new Bitmap[1];
		projBmp[0] = BitmapFactory.decodeResource(gm.getResources(), R.drawable.projectile);

		hero = new HeroSprite( gm.getResources(), 200, 100, heroBmps, heroReverseBmps , .5f );
		
		SpriteResources hRes = new SpriteResources(gm.getResources(), false, 1, imgs) ;
		
		PhysicsStuff p = new PhysicsStuff(10, 10);
		
		hro = new NewHeroSprite( gm, 200, 100, 1 );
		
	}

	@Override
	public boolean handleInput(float x, float y, int action) {
		if( (int) (x-OldPanel.mWidth/2) < 0 )
		{
			hero.setFlipBmp(true);
		}
		else
			hero.setFlipBmp(false);

		int vel;
		int startpt;
		Projectile proj;

		// touch at top of screen:
		if(y< 200) 
		{
			if(action == MotionEvent.ACTION_DOWN)
				hero.jump();
		}
		else if(y< 400)
		{
			//shoot a fireball :

			if(action==MotionEvent.ACTION_DOWN)
			{
				// check hero orientation:
				if(!hero.isFlipBmp())
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
				proj = new Projectile(gm.getResources(), startpt, 
						(int) ( (hero.getLeftBound()+hero.getRightBound())/2), vel, 
						projBmp, Projectile.TYPE_HERO, 1);
				hero.getProjList().add( proj);
			}
		}
		else
		{

			if(action == MotionEvent.ACTION_DOWN)
			{
				moveHero( (x-OldPanel.mWidth/2) /20 * hero.getScaleFactor() );
				//hero.mDx=(int) (x-Panel.mWidth/2) /20 * hero.getScaleFactor();
				hero.run(System.currentTimeMillis());

			}
			if(action == MotionEvent.ACTION_MOVE)
			{
				moveHero(  (x-OldPanel.mWidth/2) /20 * hero.getScaleFactor() );
				//hero.mDx=(int) (x-Panel.mWidth/2) /20 ;
			}

			if(action == MotionEvent.ACTION_UP)
			{
				moveHero(0);
				hero.rest();
			}
		}

		if( action==MotionEvent.ACTION_POINTER_DOWN)
		{
			hero.jump();
		}

		//return super.onTouchEvent(event);
		return true;		
	}

	@Override
	public void update(long dt) {
		hud.update(dt);
		//hero.update(dt);
		hro.update(dt);
	}

	@Override
	public void doDraw(Canvas c) {
		currentLevel.doDraw(c);
		//hero.doDraw(c);
		hro.doDraw(c);
		hud.doDraw(c);
	}

	public Level getLevel()
	{
		return currentLevel;
	}

	public void moveHero(float speed)
	{
		hero.mDx = speed;

	}	   


}

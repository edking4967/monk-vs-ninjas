package com.thousandonestories.game.gameobjects;

import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Canvas;
import android.graphics.Color;

import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.graphics.AnimationHandler;
import com.thousandonestories.game.graphics.SpriteResources;
import com.thousandonestories.game.states.GameState_Play;
import com.thousandonestories.game.ui.NewPanel;



public class NewPhysicsSprite extends NewSprite
{
	private static final float GRAVITY = 1;
	private static final float TERMINAL_VELOCITY = 15;

	/*
	 * Position states
	 */
	private boolean inAir;

	/*
	 * Movement states
	 */
	private boolean running;

	private float mass;
	private float jumpVel;

	protected float mDx;

	protected float mDy;

	protected float mD2x;
	protected float mD2y;
	
	private boolean canJump;
	private CopyOnWriteArrayList<Projectile> projectileList;

	private AnimationHandler ah;

	/**
	 * The block that this sprite is currently standing on.
	 */
	private Block onBlock;


	public NewPhysicsSprite(SpriteResources spriteRes, float x, float y, int scalefactor, PhysicsStuff phys) {

		super(spriteRes, x, y, scalefactor);

		spriteRes.createFlippedBitmaps();

		mass = phys.getMass();
		jumpVel = phys.getJumpVel();

		setInAir(true);
		setCanJump(false);
		setRunning(false);
		
		setOnBlock(null);
		projectileList = new CopyOnWriteArrayList<Projectile>();
		
		ah=new AnimationHandler();

	}

	@Override
	public void doDraw(Canvas c)
	{
		super.doDraw(c);

		for(Projectile p: projectileList)
		{
			p.doDraw(c);
		}
		
	}
	
	@Override
	public void update( long dt )
	{
		super.update(dt);



		mDx += mD2x * (dt / 20f ); 
		
		
		if(mDy<= TERMINAL_VELOCITY) // upper limit
		{
			mDy += mD2y * (dt / 20f ); 
		}

		setLeftBound(getLeftBound() + mDx * (dt / 20f));
		setTopBound(getTopBound() + mDy * (dt / 20f));

		setRightBound(getLeftBound() + getWidth());
		setBottomBound(getTopBound() + getHeight());
		
		if(mDx <0)
		{
			this.setFacingLeft(true);
		}
		else
		{
			this.setFacingLeft(false);
		}

		if( isInAir() )
		{
			this.setD2y(GRAVITY * mass);

			/*
			 * Animation:
			 */
			this.stopOnFrame(0, 6);
			
			/*
			 * Check for landing on a block:
			 */
			checkLanded( this.getBlockList() , dt);
		}
		else			
		{
			this.setD2y(0);
			this.setDy(0);
			checkFallFromBlock();
			
		}
		
		for(Projectile p: projectileList)
		{
			p.update(dt);
		}

	}


	private void setFacingLeft(boolean b) {
		facingLeft = b;
	}

	public void land( Block b )
	{
		setOnBlock(b);

		this.moveFeet( b.getTopBound() );

		this.setInAir(false);
		this.setCanJump(true);
		
		/*
		 * Animation:
		 */
		if(mDx == 0)
			stopOnFrame(0,0);
		else
			setRunning(true);

	}

	public void jump()
	{
		if(!canJump())
			return;
		else
		{
			this.setDy(-jumpVel);
			this.setOnBlock(null);
			this.setInAir(true);
			canJump = false;
		}
	}

	private void moveFeet(float feetPosition) {
		this.setBottomBound( feetPosition );
		this.setTopBound(feetPosition - getHeight() );
	}

	public void checkLanded( CopyOnWriteArrayList<Block> blockList, long mElapsed)  // check for GravitySprite collisions with Blocks
	{
		float x;
		boolean hor_col;
		boolean ver_col;
		float deltaY;

		x = ( getLeftBound() + getRightBound() ) / 2;

		for( Block block: blockList)
		{
			hor_col=false;
			ver_col=false;

			//////////////////////////////
			// CHECK FOR HORIZONTAL MATCH:

			if( block.getRightBound() > x && block.getLeftBound() < x) 
			{
				hor_col=true;			        		 
			}
			//
			/////////////////////////////

			//////////////////////////////// 
			// CHECK FOR VERTICAL CROSSOVER:

			deltaY = this.mDy * mElapsed / 20f;
			if(this.getBottomBound() -10 <= block.getTopBound() && (this.getBottomBound() + deltaY ) >= block.getTopBound() )
			{
				ver_col=true;
			}

			//
			/////////////////////////////

			// if a collision has happened:
			if(hor_col && ver_col)
			{
				moveFeet(block.getTopBound()); //line feet up with block
				land(block);
				break;

			}

		}		 

	}

	public boolean checkFallFromBlock()
	{
		int x;
		x = (int) (getLeftBound()+getRightBound())/2;
		if( x > getOnBlock().getRightBound() || x < getOnBlock().getLeftBound() )
		{
			// The sprite has moved off its current block.
			fall();
			return true;

		}
		else
			return false;
	}

	private void fall() {
		mD2y = GRAVITY * mass;
		this.setInAir(true);
		onBlock = null;
	}

	public Block getOnBlock() {
		return onBlock;
	}

	public void setOnBlock(Block onBlock) {
		this.onBlock = onBlock;
	}

	public float getDy() {
		return mDy;
	}

	public void setDy(float mDy) {
		this.mDy = mDy;
	}

	public float getD2y() {
		return mD2y;
	}

	public void setD2y(float mD2y) {
		this.mD2y = mD2y;
	}

	public float getDx() {
		return mDx;
	}

	public void setDx(float mDx) {
		this.mDx = mDx;
	}

	public boolean isInAir() {
		return inAir;
	}

	public void setInAir(boolean b) {
		this.inAir = b;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
	
	public CopyOnWriteArrayList<Block> getBlockList()
	{
		return ((GameState_Play) ( NewPanel.gm.getGameState() )).getLevel().getBlockList() ;
	}

	public void run(float x) {
		setRunning(true);
		setDx(x);
		if( !animationIsPlaying() )
			startAnimation(0);
	}
	
	public void stopRunning()
	{
		setRunning(false);
		setDx(0);
		stopOnFrame(0,0);
	}
	
	public boolean canJump() {
		return canJump;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	
	public void shoot()
	{
		float startX; 
		float startY = this.getHeight()/2 + this.getTopBound();
		
		if(this.isFacingRight())
			startX = this.getRightBound() + 25;
		else
			startX = this.getLeftBound() - 25;
		
	}
	
	public CopyOnWriteArrayList<Projectile> getProjList() 
	{
		return projectileList;
	}

}
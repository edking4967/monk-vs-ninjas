package com.thousandonestories.game.gameobjects;

import java.util.concurrent.CopyOnWriteArrayList;

import com.thousandonestories.game.OldPanel;
import com.thousandonestories.game.PhysicsStuff;
import com.thousandonestories.game.SpriteResources;



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
		setRunning(false);
		
		setOnBlock(null);

	}

	public void update( long elapsedTime )
	{
		super.update(elapsedTime);



		mDx += mD2x * (elapsedTime / 20f ); 
		if(mDy<= TERMINAL_VELOCITY) // upper limit
		{
			mDy += mD2y * (elapsedTime / 20f ); 
		}

		setLeftBound(getLeftBound() + mDx * (elapsedTime / 20f));
		setTopBound(getTopBound() + mDy * (elapsedTime / 20f));

		setRightBound(getLeftBound() + getWidth());
		setBottomBound(getTopBound() + getHeight());

		if( isInAir() )
		{
			this.setD2y(GRAVITY * mass);

			/*
			 * Check for landing on a block:
			 */

			//checkBlocks();

			checkLanded(OldPanel.getBlockList(), elapsedTime);
		}
		else			
		{
			this.setD2y(0);
			this.setDy(0);
			checkFallFromBlock();
			
		}
	}


	public void land( Block b )
	{
		setOnBlock(b);

		this.moveFeet( b.getTopBound() );

		this.setInAir(false);

	}

	public void jump()
	{
		this.setDy(-jumpVel);
		this.setOnBlock(null);
		this.setInAir(true);
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

	public void setInAir(boolean inAir) {
		this.inAir = inAir;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}


}
package com.thousandonestories.game;

public class NewPhysicsSprite extends NewSprite
{
	private static final float GRAVITY = 1;
	private static final float TERMINAL_VELOCITY = 15;

	/*
	 * Position states
	 */
	private int positionState;
	public static final int STATE_ONBLOCK=0;
	public static final int STATE_INAIR=1;

	/*
	 * Movement states
	 */
	private int movementState;
	public static final int STATE_STILL=0;
	public static final int STATE_RUN=1;

	private float mass;
	private float jumpVel;

	protected float mDx;
	protected float mDy;

	protected float mD2x;
	protected float mD2y;

	/**
	 * The state that this sprite is currently standing on.
	 */
	private Block onBlock;

	public NewPhysicsSprite(SpriteResources spriteRes, float x, float y, int scalefactor, PhysicsStuff phys) {

		super(spriteRes, x, y, scalefactor);

		spriteRes.createFlippedBitmaps();

		mass = phys.getMass();
		jumpVel = phys.getJumpVel();

		setPositionState(STATE_INAIR);

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

		switch (getPositionState())
		{
		case STATE_INAIR:
			this.setmD2y(GRAVITY * mass);
			checkBlocks();
			break;
		case STATE_ONBLOCK:
			this.setmD2y(0);
			this.setmDy(0);
			checkFallFromBlock();
			break;	      

		}
	}


	public float getmDy() {
		return mDy;
	}

	public void setmDy(float mDy) {
		this.mDy = mDy;
	}

	public float getmD2y() {
		return mD2y;
	}

	public void setmD2y(float mD2y) {
		this.mD2y = mD2y;
	}

	public void land( Block b )
	{
		setOnBlock(b);

		this.moveFeet( b.getTopBound() );

		this.setPositionState(STATE_ONBLOCK);
	}

	public void jump()
	{
		this.setmDy(-jumpVel);
		this.setOnBlock(null);
		this.setPositionState(STATE_INAIR);
	}

	private void moveFeet(float feetPosition) {
		this.setBottomBound( feetPosition );
		this.setTopBound(feetPosition - getHeight() );
	}

	public boolean checkBlocks()
	{
		for(Block b : Panel.getBlockList() )
		{
			if( Panel.checkCollision( b , this ) )
			{
				this.land( b );

				return true;
			}
		}
		return false;
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
		positionState=STATE_INAIR;
		onBlock = null;
	}

	public Block getOnBlock() {
		return onBlock;
	}

	public void setOnBlock(Block onBlock) {
		this.onBlock = onBlock;
	}

	public int getPositionState() {
		return positionState;
	}

	public void setPositionState(int positionState) {
		this.positionState = positionState;
	}

	public int getMovementState() {
		return movementState;
	}

	public void setMovementState(int movementState) {
		this.movementState = movementState;
	}

}
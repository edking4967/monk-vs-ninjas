package com.thousandonestories.game;

public class NewPhysicsSprite extends NewSprite
{
	private static final float GRAVITY = 1;
	private static final float TERMINALVELOCITY = 15;

	
	private boolean flipped;
	private float mass;
	private float jumpVel;
	
	protected float mDx;
	protected float mDy;

	protected float mD2x;
	protected float mD2y;

	
	public NewPhysicsSprite(SpriteResources spriteRes, float x, float y, int scalefactor, PhysicsStuff phys) {
		
		super(spriteRes, x, y, scalefactor);
		
		spriteRes.createFlippedBitmaps();
		
		flipped=false;
		
		mass = phys.getMass();
		jumpVel = phys.getJumpVel();
		
	}
	
	public void update( long elapsedTime )
	{
		super.update(elapsedTime);
		 
		this.setmD2y(GRAVITY * mass);
		
		mDx += mD2x * (elapsedTime / 20f ); 
		   if(mDy<= TERMINALVELOCITY) // upper limit
			{
			   mDy += mD2y * (elapsedTime / 20f ); 
			}

	      setLeftBound(getLeftBound() + mDx * (elapsedTime / 20f));
	      setTopBound(getTopBound() + mDy * (elapsedTime / 20f));
	      
	      setRightBound(getLeftBound() + getWidth());
	      setBottomBound(getTopBound() + getHeight());
	      
	}
	
	public void jump()
	{
		this.setmDy(-jumpVel);
	}
	
	public void flip()
	{
		flipped = !flipped;
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

}
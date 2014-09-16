package com.thousandonestories.game.ui;

public class Camera {
	private int width;
	private int height;
	private float offsetFromOrigin=0;

	
	public Camera(int gameWidth, int gameHeight) {

		setWidth( gameWidth);
		setHeight( gameHeight);
		
 	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getOffset() {
		return offsetFromOrigin;
	}

	public void setOffsetFromOrigin(float offsetFromOrigin) {
		this.offsetFromOrigin = offsetFromOrigin;
	}

	public void pan(float x) {
		offsetFromOrigin += x;
	}

}

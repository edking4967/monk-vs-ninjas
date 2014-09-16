package com.thousandonestories.game.ui;

import android.graphics.Rect;

public class Camera {
	private int width;
	private int height;
	private float offsetX;
	private float offsetY;
	
	public Camera(int gameWidth, int gameHeight) {

		setWidth( gameWidth);
		setHeight( gameHeight);
		offsetX=0;
		offsetY=0;
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

	public float getXOffset() {
		return offsetX;
	}

	public void setXOffset(float offsetFromOrigin) {
		this.offsetX = offsetFromOrigin;
	}

	public void pan(float x) {
		offsetX += x;
	}

	public float getYOffset() {
		return offsetY;
	}

	public Rect createPannedRect(Rect rect) {
		return new Rect(rect.left - (int) offsetX , rect.top - (int) offsetY, rect.right - (int) offsetX, rect.bottom - (int) offsetY);
	}

}

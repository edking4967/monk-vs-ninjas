package com.thousandonestories.game;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class Accelerometer implements SensorEventListener {
	private float mLastX, mLastY, mLastZ;
	
	private boolean jump;

	private boolean mInitialized; private SensorManager mSensorManager; private Sensor mAccelerometer; private final float NOISE = (float) 2.0;

	/** Called when the activity is first created. */
	Context mContext;


	public Accelerometer(Context context) {

		mInitialized = false; // 
		
		mContext = context;

		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
		jump=false;

	}


	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {  
			
			// initialization code:
			
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		
		} else {

			float deltaX = mLastX - x;
	
			float deltaY = mLastY - y;
	
			float deltaZ = mLastZ - z;
	
			if ( Math.abs(deltaX) < NOISE) deltaX = (float)0.0;
	
			if ( Math.abs(deltaY) < NOISE) deltaY = (float)0.0;
	
			if ( Math.abs(deltaZ) < NOISE) deltaZ = (float)0.0;
	
			mLastX = x;
	
			mLastY = y;
	
			mLastZ = z;
	
	
			if ( Math.abs(deltaX) > Math.abs(deltaY) ) {
				
				if( deltaX > 0 ){
	
					//iv.setImageResource(R.drawable.right);
				}
				else{
					//iv.setImageResource(R.drawable.left);
				}

			} else if ( Math.abs(deltaY) > Math.abs(deltaX) ) {
				
				if( deltaY > 0 ){
					
					//iv.setImageResource(R.drawable.up);
					jump = true;
					
				}
				else{
					//iv.setImageResource(R.drawable.down);
				}


			} else {

				//iv.setVisibility(View.INVISIBLE);

			}

		}

		}	
	protected void registerListener() {

		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

	}

	protected void unregisterListener() {


		mSensorManager.unregisterListener(this);

	}
	
	public boolean jumped()
	{
		boolean returnval = jump;
		jump=false;
		return returnval;
	}

	@Override

	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	// can be safely ignored for this demo

	}

}
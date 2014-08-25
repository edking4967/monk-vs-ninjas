package com.thousandonestories.game;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;

public class ViewThread extends Thread {
   private Panel mPanel;
   private SurfaceHolder mHolder;
   private boolean mRun = false;
   private long mStartTime;
   public static long mElapsed;
   
   //debug: timesaves
   private long mUpdateTime;
   private long mDrawTime;
   
   private Paint mPaint;
   
   private int saveNum = 0;
   
   
   
   /**
    * Bitmap for exported gif
    */
   private Bitmap saveBmp;
    
   public ViewThread(Panel panel) {
      mPanel = panel;
      mHolder = mPanel.getHolder();
      mPaint = new Paint();
      mPaint.setColor(Color.WHITE);
      
     
   }

   // Set current thread state
   public void setRunning(boolean run) {
      mRun = run;
   }

   @Override
   public void run() {
      Canvas canvas = null;

      // Retrieve time when thread starts
      mStartTime = System.currentTimeMillis();

      // Thread loop
      while (mRun) {
         // Obtain lock on canvas object
         canvas = mHolder.lockCanvas();
         
         if (canvas != null) {
        	             
            // Update state based on elapsed time
        	
        	 if(Panel.getGameState()==Panel.STATE_UNINITIALIZED)
        		 continue;
        	 
            mPanel.update(mElapsed);

            mUpdateTime = System.currentTimeMillis() - mStartTime;
            
            // Render updated state
            mPanel.doDraw(canvas);
            
            
            /**
             * Experimental: save canvas as a bitmap to create gameplay gifs/movies
             */
            if(Panel.SAVE_BITMAPS) {
				if (saveNum % 5 == 0) {
				 
				saveBmp = Bitmap.createBitmap( canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
				     
				 
				 canvas.setBitmap(saveBmp);
				 mPanel.doDraw(canvas);
				 //saveBitmap(saveBmp, saveNum / 10);
				 
				    new Thread(new Runnable() {
				        public void run() {
				     	   File file; 
				 	      
				 		  try {
				 			  file = new File(Environment.getExternalStorageDirectory() + "/zMonkPics/sign" + saveNum/5 + ".jpg");
				 	           saveBmp.compress(Bitmap.CompressFormat.JPEG, 20, new FileOutputStream(file));
				 	      } catch (Exception e) {
				 	           e.printStackTrace();
				 	      }

				 	      return;
				        }
				    }).start();

				 
				 
				 canvas.setBitmap(null);
				}
				saveNum++;
			}
            

            mDrawTime = System.currentTimeMillis() - mUpdateTime - mStartTime;
            
            Log.d("blah", "Draw Time = " + mDrawTime);
            
            canvas.drawText("draw time =  " + mDrawTime + " update time = " + mUpdateTime, 10, 60, mPaint);
            
            // Release lock on canvas object
            mHolder.unlockCanvasAndPost(canvas);
            
//            try {
//				sleep(32);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            
            mElapsed = System.currentTimeMillis() - mStartTime;
           
         }
        
         // Update start time
         mStartTime = System.currentTimeMillis();
         
         
      }
   }
   
   
   public void saveBitmap( Bitmap bmp, int num){
	      
	   
	   File file; 
	      
		  try {
			  file = new File(Environment.getExternalStorageDirectory() + "/zMonkPics/sign" + num + ".jpg");
	           bmp.compress(Bitmap.CompressFormat.JPEG, 20, new FileOutputStream(file));
	      } catch (Exception e) {
	           e.printStackTrace();
	      }

	      return;
	  }

}
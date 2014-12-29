package com.thousandonestories.synth;

import com.thousandonestories.synth.MainBuffer;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class SynthCore {
	
	//hardcoded:
	public static final int SQUARE = 1;
	public static final int SIN = 0;
	public static final int sr = 44100; // sample rate

	private MainBuffer mainBuff;
	private int buffsize; 				// size of audio buffer to use
	private AudioTrack audioTrack; 		// audio track to use to play sound
	private Thread t;  					// thread to synthesize audio
	private boolean isRunning = false;	// whether the thread is running
	private short[] samples; 			// array to store audio samples

	private int wf=SIN;  				// initial waveform
	
	public SynthCore()
	{
		//SET UP BUFFER:

		mainBuff = new MainBuffer( 40 ); //buffer size 40 frames, note size 1000 buffers

		// start a new thread to synthesize audio        
		t = new Thread() {
			public void run() {
				// set process priority
				setPriority(Thread.NORM_PRIORITY);

				buffsize = 400;

				//create an audiotrack object
				audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sr, 
						AudioFormat.CHANNEL_OUT_MONO, 
						AudioFormat.ENCODING_PCM_16BIT, 
						buffsize, 
						AudioTrack.MODE_STREAM);
				samples = new short[buffsize];

				// start audio
				audioTrack.play();
				int asd=1;
				// synthesis loop
				while(isRunning){

					if( mainBuff.playReady() )  // if there is a frame to play
					{
						mainBuff.playNextFrame(); // play the frame
					}


				}

				audioTrack.stop();
				audioTrack.release();
			}
		};
		isRunning=true;
		t.start();
		
	}
	
	public void addNote(double fr, int ampl, int attack, int notelength, boolean choke, int waveform)
	{
		mainBuff.addNote(fr, ampl, attack, notelength, choke, waveform);
	}

}

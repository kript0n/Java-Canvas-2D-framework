package com.kripton.canvas.framework.impl;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.kripton.canvas.framework.Audio;
import com.kripton.canvas.framework.FileIO;
import com.kripton.canvas.framework.Game;
import com.kripton.canvas.framework.Graphics;
import com.kripton.canvas.framework.Input;
import com.kripton.canvas.framework.Screen;

public abstract class AndroidGame extends Activity implements Game {
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		boolean isLandscape = getResources().getConfiguration().orientation 
				== Configuration.ORIENTATION_LANDSCAPE;
		
		int frameBufferWidth = isLandscape ? 1280 : 800;
		int frameBufferHeight = isLandscape ? 800 : 1280;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Config.RGB_565);
		
		Point size = new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		
		
		float scaleX = (float)frameBufferWidth / size.x;
		float scaleY = (float)frameBufferWidth / size.y;
		
		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = new AndroidGraphics(getAssets(), frameBuffer);
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
    }
	
	@Override 
	public void onResume() {
		super.onResume();
		screen.resume();
		renderView.resume();
	}
	
	@Override 
	public void onPause() {
		super.onPause();
		renderView.pause();
		screen.pause();
	}

	@Override
	public Input getInput() {
		return input;
	}

	@Override
	public FileIO getFileIO() {
		return fileIO;
	}

	@Override
	public Graphics getGraphics() {
		return graphics;
	}

	@Override
	public Audio getAudio() {
		return audio;
	}

	@Override
	public void setScreen(Screen screen) {
		if(screen == null) {
			throw new RuntimeException("Screen must not be null");
		}
		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	@Override
	public Screen getCurrentScreen() {
		return screen;
	}


}

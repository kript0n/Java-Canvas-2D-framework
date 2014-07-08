package com.kripton.canvas.framework.impl;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.kripton.canvas.framework.Audio;
import com.kripton.canvas.framework.Music;
import com.kripton.canvas.framework.Sound;

public class AndroidAudio implements Audio{
	AssetManager assets;
	SoundPool soundPool;
	
	public AndroidAudio(Activity activity) {
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	public Music newMusic(String filename) {
		try {
			AssetFileDescriptor desc = assets.openFd(filename);
			return new AndroidMusic(desc);
		}  catch(IOException e) {
			throw new RuntimeException("Невозможно загрузить медиафайл: " + filename);
		}
	}

	@Override
	public Sound newSound(String filename) {
		try {
			AssetFileDescriptor desc = assets.openFd(filename);
			int soundID = soundPool.load(desc, 0);
			return new AndroidSound(soundPool, soundID);
		} catch (IOException e) {
			throw new RuntimeException("Невозможно загрузить звук: " + filename);
		}
	}

}

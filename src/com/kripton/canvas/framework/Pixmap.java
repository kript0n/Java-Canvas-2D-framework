package com.kripton.canvas.framework;

import com.kripton.canvas.framework.Graphics.PixmapFormat;

public interface Pixmap {
	
	public int getWidth();
	
	public int getHeight();
	
	public PixmapFormat getFormat();
	
	public void dispose();

}

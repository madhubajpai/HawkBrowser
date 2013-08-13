package com.hawkbrowser.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtil {

	public static void showTips(Context ctx, String msg) {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(ctx, msg, duration);
		toast.show();
	}
	
	public static void showTips(Context ctx, int msgStringId) {
		
		Resources rc = ctx.getResources();
		String msg = rc.getString(msgStringId);
		showTips(ctx, msg);
	}
	
	public static String getExceptionStackTrace(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}
	
	@SuppressLint("NewApi")
	public static Point screenSize(Context ctx) {
		WindowManager wm = (WindowManager) 
				ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		if(android.os.Build.VERSION.SDK_INT >= 11) {
			Point size = new Point();
			display.getSize(size);
			return size;
		}
		else {
			return new Point(display.getWidth(), display.getHeight());
		}
		
	}
	
	public static long getExternalStorageFreeSpace() {
		
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		
		return stat.getAvailableBlocks() * stat.getBlockSize();
	}
	
	public static File getUniqueFile(String dir, String fileName) {
		
		int fileNameSuffix = 0;
		File file = new File(dir, fileName);
		
		while(file.exists()) {
			
			String newFileName;
			int dotIndex = fileName.lastIndexOf('.');
			
			if(-1 == dotIndex) {
				newFileName = String.format("%s%d", fileName, ++fileNameSuffix);
			} else {
				String name = fileName.substring(0, dotIndex);
				String ext = fileName.substring(dotIndex + 1);
				newFileName = String.format("%s%d.%s", 
					name, ++fileNameSuffix, ext);
			}
			
			file = new File(Environment.getExternalStorageDirectory(), 
					newFileName);
		} 
		
		return file;
	}
	
	public static String fileNameFromUrl(String url) {
		
		String name = null;
		
		int lastDot = url.lastIndexOf('/');
		if(-1 == lastDot) {
			name = url;
		} else { 
			name = url.substring(lastDot + 1);
		}
		
		return name;
	}
}

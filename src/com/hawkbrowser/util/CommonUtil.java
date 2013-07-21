package com.hawkbrowser.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
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
}

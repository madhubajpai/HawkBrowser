package com.example.hawkbrowser.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class CommonUtil {

	public static void showTips(Context ctx, String msg) {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(ctx, msg, duration);
		toast.show();
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

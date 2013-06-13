package com.example.hawkbrowser.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class HawkWebView extends WebView {

	private float	touchBeginX = 0;
	private float touchBeginY = 0;
	
	public HawkWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		Init();
	}

	public HawkWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		Init();
	}

	public HawkWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		Init();
	}
	
	private void Init() {
		getSettings().setJavaScriptEnabled(true);
		setWebViewClient(new HawkWebViewClient());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(event.getAction() == event.ACTION_MOVE) {
			
			if(0 == touchBeginX && 0 == touchBeginY) {
				touchBeginX = event.getX();
				touchBeginY = event.getY();
			}
			else {
				float moveX = event.getX() - touchBeginX;
				float moveY = event.getY() - touchBeginY;
				
				if(moveX > 0 || moveY < 0) {
					if(canGoForward()) {
						goForward();
						return true;
					}
				}
				
				if(moveX < 0 || moveY > 0) {
					if(canGoBack()) {
						goBack();
						return true;
					}
				}
			}
		}
		
		return super.onTouchEvent(event);
	}
}

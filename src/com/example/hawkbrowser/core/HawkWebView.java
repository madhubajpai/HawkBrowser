package com.example.hawkbrowser.core;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HawkWebView extends WebView {

	public HawkWebView(Context context) {
		super(context);
	}

	public HawkWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	
	public HawkWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	
	public void init(WebViewClient viewClient, 
			HawkWebChromeClient chromeClient) {
		
		getSettings().setJavaScriptEnabled(true);
		setWebViewClient(viewClient);
		setWebChromeClient(chromeClient);
	}
}

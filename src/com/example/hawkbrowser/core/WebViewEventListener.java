package com.example.hawkbrowser.core;

import android.graphics.Bitmap;
import android.webkit.WebView;

public interface WebViewEventListener {
	
	void onPageFinished(WebView view, String url);
	void onPageStarted(WebView view, String url, Bitmap favicon);
}

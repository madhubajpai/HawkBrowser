package com.example.hawkbrowser.core;

import android.webkit.WebViewClient;
import android.webkit.WebView;

public class HawkWebViewClient extends WebViewClient {

	private WebViewEventListener	wvEventListener;
	
	public HawkWebViewClient(WebViewEventListener wvel) {
		wvEventListener = wvel;
	}
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		return false;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		wvEventListener.onPageFinished(view, url);
		super.onPageFinished(view, url);
	}
}

package com.example.hawkbrowser.core;

import android.webkit.WebViewClient;
import android.webkit.WebView;

public class HawkWebViewClient extends WebViewClient {

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		return false;
	}

}

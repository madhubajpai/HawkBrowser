package com.example.hawkbrowser.shell;

import java.util.ArrayList;

import com.example.hawkbrowser.R;
import com.example.hawkbrowser.core.*;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public final class HawkBrowser extends Activity implements WebViewEventListener {

	private ArrayList<HawkWebView> mViews;
	private HawkWebView mCurrentView;
	private int mIndexOfWebView;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_frame);
		
		// LayoutInflater layoutInflater = 
		//		(LayoutInflater) getLayoutInflater();
		// View navigationBar = layoutInflater.inflate(R.layout.main_frame, null);
		
		mViews = new ArrayList<HawkWebView>();
		
		HawkWebView newView = (HawkWebView) findViewById(R.id.mainframe_webView);
		ViewGroup layout = (ViewGroup) findViewById(R.id.main_frame);
		mIndexOfWebView = layout.indexOfChild(newView);
		newView.init(new HawkWebViewClient(this));
		newView.loadUrl("http://www.baidu.com");
		showView(newView);
		
		final EditText et = (EditText) findViewById(R.id.mainframe_addressbar);
		final Button goBtn = (Button) findViewById(R.id.mainframe_gobtn);
		goBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String url = et.getText().toString();
				
				if(!url.startsWith("http")) {
					mCurrentView.loadUrl("http://" + url);
				}
			}
		});
	}
	
	private void showView(HawkWebView newView) {
		mCurrentView = newView;
		ViewGroup layout = (ViewGroup) findViewById(R.id.main_frame);
		View curView = layout.findViewById(R.id.mainframe_webView);
		
		if(curView != newView) {
			layout.removeView(curView);
			curView.setVisibility(View.INVISIBLE);
			
			newView.setVisibility(View.VISIBLE);
			layout.addView(newView, mIndexOfWebView);
			
			if(!mViews.contains(newView)) {
				mViews.add(newView);
			}
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem back = menu.findItem(R.id.menu_goback);
		back.setVisible(mCurrentView.canGoBack());
		
		MenuItem forward = menu.findItem(R.id.menu_goforward);
		forward.setVisible(mCurrentView.canGoForward());
		
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.option_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.menu_goback:
				mCurrentView.goBack();
				return true;
				
			case R.id.menu_goforward:
				mCurrentView.goForward();
				return true;
				
			case R.id.menu_newwindow:
				// Intent intent = new Intent(this, HawkBrowser.class);
				// startActivity(intent);
				HawkWebView newView = new HawkWebView(this);
				newView.setId(R.id.mainframe_webView);
				newView.init(new HawkWebViewClient(this));
				newView.setLayoutParams(
					new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				newView.loadUrl("http://www.baidu.com");
				showView(newView);
				return true;
				
			case R.id.menu_selectwindow:
				break;
		}
		
		return false;
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		if(android.os.Build.VERSION.SDK_INT >= 11) {
			invalidateOptionsMenu();
		}
	}

	protected void onStart() {
		super.onStart();
	}
	
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        for(HawkWebView v : mViews) {
        	v.destroy();
        }
    }
}

package com.example.hawkbrowser.shell;

import java.util.ArrayList;

import com.example.hawkbrowser.R;
import com.example.hawkbrowser.core.*;
import com.example.hawkbrowser.util.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public final class HawkBrowser extends Activity 
			implements NavigationBar.EventListener, WebViewEventListener {

	private ArrayList<HawkWebView> mViews;
	private HawkWebView mCurrentView;
	private LayoutParams mWebViewLayoutParams;
	private int mIndexOfWebView;
	private NavigationBar mNavigationBar;
	private WebViewSelecter mViewSelecter;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_frame);
		
		// LayoutInflater layoutInflater = 
		//		(LayoutInflater) getLayoutInflater();
		// View navigationBar = layoutInflater.inflate(R.layout.main_frame, null);
		
		mViews = new ArrayList<HawkWebView>();
		
		ViewGroup navigationBar = 
				(ViewGroup)findViewById(R.id.mainframe_navigationbar);
		mNavigationBar = new NavigationBar(this, navigationBar, this);
		
		HawkWebView newView = (HawkWebView) findViewById(R.id.mainframe_webView);
		mWebViewLayoutParams = newView.getLayoutParams();
		ViewGroup layout = (ViewGroup) findViewById(R.id.main_frame);
		mIndexOfWebView = layout.indexOfChild(newView);
		newView.init(new HawkWebViewClient(this));
		newView.loadUrl("http://www.baidu.com");
		showView(newView);
		
		// LayoutInflater layoutInflater = 
		// (LayoutInflater) getLayoutInflater();
		// View navigationBar = layoutInflater.inflate(R.layout.navigation_bar, layout);
		
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
			// curView.setVisibility(View.INVISIBLE);
			
			newView.setVisibility(View.VISIBLE);
			layout.addView(newView, mIndexOfWebView);
		}
		
		if(!mViews.contains(newView)) {
			mViews.add(newView);
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
	
	private void newWebView() {
		HawkWebView newView = new HawkWebView(this);
		newView.setId(R.id.mainframe_webView);
		newView.init(new HawkWebViewClient(this));
		newView.setLayoutParams(mWebViewLayoutParams);
		newView.loadUrl("http://www.baidu.com");
		showView(newView);
	}
	
	private void selectWebView() {
		if(null != mViewSelecter) {
			mViewSelecter.dismiss();
			mViewSelecter = null;
			return;
		}
		
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
		
		for(HawkWebView wv : mViews) {
			
			titles.add(wv.getTitle());	
			Bitmap webBmp = ImageUtil.loadBitmapFromView(wv);
			bitmaps.add(webBmp);
		}
		
		mViewSelecter = new WebViewSelecter(this, titles, bitmaps);       
        mViewSelecter.setEventListener(new WebViewSelecter.EventListener() {
			
			@Override
			public void onItemSelect(int i) {
							
				if(null != mViewSelecter) {
					mViewSelecter.dismiss();
					mViewSelecter = null;
				}
				
				showView(mViews.get(i));
			}
			
			@Override
			public void onItemClose(int i) {
				mViews.get(i).destroy();
				mViews.remove(i);
				
				if(mViews.isEmpty()) {
					mViewSelecter.dismiss();
					mViewSelecter = null;
					newWebView();
				} else {
					mViewSelecter.updateItems();
				}
			}
		});
        
        View selectWindow = findViewById(R.id.mainframe_navigationbar);
		// Point screenSize = CommonUtil.screenSize(this);
		// PopupWindow mPopup = new PopupWindow(mViewSelecter.getView(), screenSize.x, 200);
		// mPopup.showAsDropDown(selectWindow, 0, 0);
        mViewSelecter.show(selectWindow);
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
				newWebView();
				return true;
				
			case R.id.menu_selectwindow:
				selectWebView();
				return true;
		}
		
		return false;
	}
	
	@Override
	public void onGoBack() {
		mCurrentView.goBack();
	}
	
	@Override
	public void onGoForward() {
		mCurrentView.goForward();
	}
	
	@Override
	public void onNewWebView() {
		newWebView();
	}
	
	@Override
	public void onSelectWebView() {
		selectWebView();
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		setBackForwardState(view);
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		setBackForwardState(view);
	}

	@SuppressLint("NewApi")
	private void setBackForwardState(WebView view) {
		if(android.os.Build.VERSION.SDK_INT >= 11) {
			invalidateOptionsMenu();
		}
		
		mNavigationBar.setCanGoBack(view.canGoBack());
		mNavigationBar.setCanGoForward(view.canGoForward());
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

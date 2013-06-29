package com.hawkbrowser.shell;

import java.util.ArrayList;

import com.hawkbrowser.R;
import com.hawkbrowser.app.Bookmark;
import com.hawkbrowser.app.BookmarkActivity;
import com.hawkbrowser.core.*;
import com.hawkbrowser.util.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public final class HawkBrowser extends Activity 
	implements NavigationBar.EventListener,
		HawkWebViewClient.EventListener, 
		HawkWebChromeClient.EventListener,
		PopMenuBar.EventListener {

	private ArrayList<HawkWebView> 	mViews;
	private HawkWebView 			mCurrentView;
	private LayoutParams 			mWebViewLayoutParams;
	private int 					mIndexOfWebView;
	private NavigationBar 			mNavigationBar;
	private WebViewSelecter			mViewSelecter;
	private AddressBar				mAddressBar;
	private ProgressBar				mProgressBar;
	private PopMenuBar				mPopMenuBar;
		
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
		newView.init(new HawkWebViewClient(this), 
			new HawkWebChromeClient(this));
		newView.loadUrl(getResources().getString(R.string.homepageurl));
		showView(newView);
		
		ViewGroup addressBarView = (ViewGroup) 
				findViewById(R.id.mainframe_addressbar);
		mAddressBar = new AddressBar(this, addressBarView);
		mAddressBar.setEventListener(new AddressBar.EventListener() {
			@Override
			public void onGo(String url) {
				mCurrentView.loadUrl(url);
			}
		});		
		
		mProgressBar = (ProgressBar) findViewById(R.id.mainframe_progressbar);
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
			
			mAddressBar.setTitle(newView.getTitle());
			mProgressBar.setProgress(newView.getProgress());
		}
		
		if(!mViews.contains(newView)) {
			mViews.add(newView);
		}
	}
	
	/* disable options menu
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
				newWebView();
				return true;
				
			case R.id.menu_selectwindow:
				selectWebView();
				return true;
		}
		
		return false;
	}
	
	*/
	
	private void newWebView() {
		HawkWebView newView = new HawkWebView(this);
		newView.setId(R.id.mainframe_webView);
		newView.init(new HawkWebViewClient(this), 
			new HawkWebChromeClient(this));
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
			
			String title = wv.getTitle();
			if(title.isEmpty()) {
				title = getResources().getString(R.string.defaultpagetitle);
			}
			
			titles.add(title);	
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
					
					int nextView = i + 1;
					if(nextView >= mViews.size()) {
						nextView = mViews.size() - 1;
					}
					
					showView(mViews.get(nextView));
				}
			}
		});
        
        View selectWindow = findViewById(R.id.mainframe_navigationbar);
		// Point screenSize = CommonUtil.screenSize(this);
		// PopupWindow mPopup = new PopupWindow(mViewSelecter.getView(), screenSize.x, 200);
		// mPopup.showAsDropDown(selectWindow, 0, 0);
        mViewSelecter.show(selectWindow);
    }
	
	// NavigationBar Listener start
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
	public void onMenu() {
		if(null == mPopMenuBar) {
			mPopMenuBar = new PopMenuBar(this, this);
			View anchor = findViewById(R.id.navigationbar_menu);
			mPopMenuBar.show(anchor);
		} else {
			mPopMenuBar.dismiss();
			mPopMenuBar = null;
		}
			
	}
	
	@Override
	public void onSelectWebView() {
		selectWebView();
	}
	// NavigationBar Listener end
	
	// WebChromeClient Listener start
	@Override
	public void onReceivedTitle(WebView view, String title) {
		mAddressBar.setTitle(title);
	}
	
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		mProgressBar.setProgress(newProgress);
		
		final int MAX_PROGRESS = 100;
		
		if(MAX_PROGRESS == newProgress) {
			mProgressBar.setVisibility(View.GONE);
		}
	}
	// WebChromeClient Listener end
	
	// WebViewClient Listener start
	@Override
	public void onPageFinished(WebView view, String url) {
		mProgressBar.setVisibility(View.GONE);
		setBackForwardState(view);
	}
	
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		mProgressBar.setVisibility(View.VISIBLE);
		setBackForwardState(view);
	}
	// WebViewClient Listener end
	
	// PopMenuBar Listener begin
	@Override
	public void onQuit() {
		mPopMenuBar.dismiss();
		mPopMenuBar = null;
		
		finish();
	}
	
	public void onRefresh() {
		mPopMenuBar.dismiss();
		mPopMenuBar = null;
		
		mCurrentView.reload();
	}
	
	public void onAddBookmark() {
		Bookmark bm = new Bookmark();
		Bookmark.Item item = new Bookmark.Item(mCurrentView.getTitle(), 
				mCurrentView.getUrl(), Bookmark.Type.Link);
		bm.Add(item);
	}
	
	public void onShowBookmark() {
		mPopMenuBar.dismiss();
		mPopMenuBar = null;
		
		Intent intent = new Intent(this, BookmarkActivity.class);
		startActivity(intent);
	}
	// PopMenuBar Listener end

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
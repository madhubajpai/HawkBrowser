package com.hawkbrowser.app;

import java.util.List;

import com.hawkbrowser.R;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class BookmarkActivity extends Activity {
	
	private final String BOOKMARK_TAB_ID = "bookmark";
	private final String HISTORY_TAB_ID = "history";
	
	private TabHost	mTabHost;
	private TabWidget mTabWidget;
	private Bookmark mBookmark;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.bookmark_history);
		
		initLayout();
		setupListeners();
		loadBookmark();
	}
	
	private void initLayout() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabWidget = mTabHost.getTabWidget();
				
		TabSpec tabBookmark = mTabHost.newTabSpec(BOOKMARK_TAB_ID);
		tabBookmark.setContent(R.id.bookmarkhistory_bookmark);
		Resources rc = getResources();
		View vb = getCustomTab(rc.getString(R.string.bookmark), true);
		tabBookmark.setIndicator(vb);
		mTabHost.addTab(tabBookmark);

		TabSpec tabHistory = mTabHost.newTabSpec(HISTORY_TAB_ID);
		View vh = getCustomTab(rc.getString(R.string.history), false);
		tabHistory.setContent(R.id.bookmarkhistory_history);
		tabHistory.setIndicator(vh);
		mTabHost.addTab(tabHistory);
	}
	
	private void setupListeners() {
		
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				
				Log.d("Bookmark", String.format("Click tab  %s", tabId));
				
				for(int i = 0; i < mTabWidget.getChildCount(); ++i) {
					ViewGroup twChild = (ViewGroup) mTabWidget.getChildAt(i);
					View lineView = twChild.findViewById(
						R.id.bookmark_custom_tab_line);
					if(i != mTabHost.getCurrentTab()) {
						lineView.setVisibility(View.INVISIBLE);
					} else {
						lineView.setVisibility(View.VISIBLE);
					}
				}					
			}
		});
		
		View back = mTabHost.findViewById(R.id.bookmarkhistory_return);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private View getCustomTab(String title, boolean isSelectable) {
		ViewGroup vg = (ViewGroup) LayoutInflater.from(this).inflate(
			R.layout.bookmark_custom_tab, null);
		
		TextView titleView = (TextView)
			vg.findViewById(R.id.bookmark_custom_tab_title);
		titleView.setText(title);
		
		if(!isSelectable) {
			View lineView = vg.findViewById(R.id.bookmark_custom_tab_line);
			lineView.setVisibility(View.INVISIBLE);
		}
		
		return vg;
	}
	
	private void loadBookmark() {
		mBookmark = new Bookmark(this);
		List<Bookmark.Item> items = mBookmark.getChildren(null);
		
		Log.d("Bookmark", String.format("bookmarks count: %d", items.size()));
		
		BookmarkArrayAdapter adapter = new BookmarkArrayAdapter(this, items);
				
		ListView listView = (ListView)
			mTabHost.findViewById(R.id.bookmarkhistory_bookmark);
		listView.setAdapter(adapter);
	}
}

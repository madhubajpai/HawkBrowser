package com.hawkbrowser.app;

import com.hawkbrowser.R;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

public class BookmarkActivity extends Activity {
	
	private final String BOOKMARK_TAB_ID = "bookmark";
	private final String HISTORY_TAB_ID = "history";
	
	private TabHost	mTabHost;
	private TabWidget mTabWidget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			
		setContentView(R.layout.bookmark_history);
		
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabWidget = mTabHost.getTabWidget();
		
		Resources rc = getResources();
		
		TabSpec tabBookmark = mTabHost.newTabSpec(BOOKMARK_TAB_ID);
		tabBookmark.setContent(R.id.bookmarkhistory_bookmark);
		tabBookmark.setIndicator(rc.getString(R.string.bookmark));
		mTabHost.addTab(tabBookmark);

		TabSpec tabHistory = mTabHost.newTabSpec(HISTORY_TAB_ID);
		tabHistory.setContent(R.id.bookmarkhistory_history);
		tabHistory.setIndicator(rc.getString(R.string.history));
		mTabHost.addTab(tabHistory);
	}
}

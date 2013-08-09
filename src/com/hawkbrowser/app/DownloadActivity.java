package com.hawkbrowser.app;

import com.hawkbrowser.R;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class DownloadActivity extends Activity {
	
	private static final String DOWNLOAD_TAB_ID = "download";
	private static final String FILE_TAB_ID = "file";
	
	private TabHost	mTabHost;
	private TabWidget mTabWidget;
	private boolean mIsDownloadItemsLoaded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.download);
		initLayout();
		setupListeners();
	}
	
	private void initLayout() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		mTabWidget = mTabHost.getTabWidget();
				
		TabSpec tabDownload = mTabHost.newTabSpec(DOWNLOAD_TAB_ID);
		tabDownload.setContent(R.id.download_download);
		Resources rc = getResources();
		View vb = getCustomTab(
			rc.getString(R.string.download_management), true);
		tabDownload.setIndicator(vb);
		mTabHost.addTab(tabDownload);

		TabSpec tabFile = mTabHost.newTabSpec(FILE_TAB_ID);
		View vh = getCustomTab(
			rc.getString(R.string.file_management), false);
		tabFile.setContent(R.id.download_file);
		tabFile.setIndicator(vh);
		mTabHost.addTab(tabFile);
	}
	
	private void setupListeners() {
		
		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
								
				for(int i = 0; i < mTabWidget.getChildCount(); ++i) {
					ViewGroup twChild = (ViewGroup) mTabWidget.getChildAt(i);
					View lineView = twChild.findViewById(
						R.id.download_custom_tab_line);
					if(i != mTabHost.getCurrentTab()) {
						lineView.setVisibility(View.INVISIBLE);
					} else {
						lineView.setVisibility(View.VISIBLE);
					}
				}
				
				if(tabId.equals(DOWNLOAD_TAB_ID) && !mIsDownloadItemsLoaded) {
					mIsDownloadItemsLoaded = true;
				}
			}
		});
		
		View back = mTabHost.findViewById(R.id.download_return);
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
			R.layout.download_custom_tab, null);
		
		TextView titleView = (TextView)
			vg.findViewById(R.id.download_custom_tab_title);
		titleView.setText(title);
		
		if(!isSelectable) {
			View lineView = vg.findViewById(R.id.download_custom_tab_line);
			lineView.setVisibility(View.INVISIBLE);
		}
		
		return vg;
	}

}

package com.hawkbrowser.app;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hawkbrowser.R;
import com.hawkbrowser.base.AlertDialogCheck;
import com.hawkbrowser.shell.HawkBrowser;
import com.hawkbrowser.util.CommonUtil;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class DownloadActivity extends Activity 
	implements DownloadManager.Listener,
		FileListAdapter.Listener, View.OnClickListener {
	
	private static final String DOWNLOAD_TAB_ID = "download";
	private static final String FILE_TAB_ID = "file";
	
	private TabHost	mTabHost;
	private TabWidget mTabWidget;
	private File mCurrentFolder;
	private DownloadExpListAdapter mDownloadListAdapter;
	private FileListAdapter mFileListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.download);
		initLayout();
		setupListeners();
		loadDownloadItems();
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
								
				if(tabId.equals(FILE_TAB_ID) && (null == mCurrentFolder)) {
					loadFileSystem();
				}
				
				boolean toolbarViewEditable = false;
				if(tabId.equals(DOWNLOAD_TAB_ID)) {
					toolbarViewEditable = mDownloadListAdapter.getEditable();
				} else {
					toolbarViewEditable = mFileListAdapter.getEditable();
				}
				
				refreshToolbarView(toolbarViewEditable);
			}
		});
		
		View back = mTabHost.findViewById(R.id.download_return);
		back.setOnClickListener(this);
		
		View management = mTabHost.findViewById(R.id.download_management);
		management.setOnClickListener(this);
		
		View finish = mTabHost.findViewById(R.id.download_finish);
		finish.setOnClickListener(this);
		
		View delete = mTabHost.findViewById(R.id.download_deletetask);
		delete.setOnClickListener(this);
	}
	
	// View.onClickListener
	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
		case R.id.download_return:
			finish();
			return;
			
		case R.id.download_management:
			refreshToolbarView(true);
			refreshAdapterView(true);
			return;
			
		case R.id.download_finish:
			refreshToolbarView(false);
			refreshAdapterView(false);
			return;
			
		case R.id.download_deletetask:
			deleteItems();
			return;
		}
	}
	
	private void deleteItems() {
		
		if(mTabHost.getCurrentTabTag().equals(DOWNLOAD_TAB_ID)) {
			
			AlertDialogCheck dlg = new AlertDialogCheck(
				R.string.confirm_delete_task, R.string.ok, R.string.cancel);
			dlg.show(dlg.getFragmentManager(), "DeleteDownloadTask");
			
			if(dlg.isConfirmed()) {
				List<DownloadItem> items = 
					mDownloadListAdapter.getCheckedItems();
				for(DownloadItem item : items) {
					HawkBrowser.getDownloadMgr(this).deleteDownloadItem(item);
					
					if(dlg.isChecked()) {
						File f = new File(item.localFilePath());
						f.delete();
						loadFileSystem();
					}
				}
				
				loadDownloadItems();
			}
			
		} else {
			
			boolean bConfirm = CommonUtil.showDialog(this, 
				R.string.confirm_delet_file, R.string.ok, R.string.cancel);
			
			if(bConfirm) {
				List<File> files = mFileListAdapter.getCheckedItems();
				for(File f : files) {
					CommonUtil.deleteFile(f);
				}
				
				loadFileSystem();
			}
		}
	}
	
	private void refreshToolbarView(boolean bEditable) {
		
		View toolBarShow = mTabHost.findViewById(R.id.download_show_view);
		toolBarShow.setVisibility(bEditable ? View.GONE : View.VISIBLE);
		
		View toolBar = mTabHost.findViewById(R.id.download_management_view);
		toolBar.setVisibility(bEditable ? View.VISIBLE : View.GONE);
		
		Button delete = (Button) 
			mTabHost.findViewById(R.id.download_deletetask);
		View reDownload = mTabHost.findViewById(R.id.download_redownload);
				
		if(mTabHost.getCurrentTabTag().equals(DOWNLOAD_TAB_ID)) {
			reDownload.setVisibility(View.VISIBLE);
			delete.setEnabled(
				mDownloadListAdapter.getCheckedItems().size() > 0);
			
		} else {
			reDownload.setVisibility(View.INVISIBLE);
			delete.setEnabled(
				mFileListAdapter.getCheckedItems().size() > 0);
		}
	}
	
	private void refreshAdapterView(boolean bEditable) {
		
		if(mTabHost.getCurrentTabTag().equals(DOWNLOAD_TAB_ID)) {
			mDownloadListAdapter.setEditable(bEditable);
			mDownloadListAdapter.notifyDataSetChanged();
		} else {
			mFileListAdapter.setEditable(bEditable);
			mFileListAdapter.notifyDataSetChanged();
		}
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
	
	private void loadDownloadItems() {
		
		List<DownloadItem> items = 
			HawkBrowser.getDownloadMgr(this).getItems();
		
		mDownloadListAdapter = new DownloadExpListAdapter(this, items);
			
		ExpandableListView listView = (ExpandableListView)
			mTabHost.findViewById(R.id.download_download);
		listView.setGroupIndicator(null);
		listView.setAdapter(mDownloadListAdapter);
		listView.setOnChildClickListener(mDownloadListAdapter);
		
		if(mDownloadListAdapter.getChildrenCount(0) > 0) {
			listView.expandGroup(0);
		}
	}
	
	private void loadFileSystem() {
		
		if(null == mCurrentFolder) {
			mCurrentFolder = new File(CommonUtil.getOurExtDataDir());
		}
		
		if(null == mFileListAdapter) {
			mFileListAdapter = new FileListAdapter(this, mCurrentFolder);
			mFileListAdapter.setListener(this);
			ListView listView = (ListView)
					mTabHost.findViewById(R.id.download_file);
			listView.setAdapter(mFileListAdapter);
			listView.setOnItemClickListener(mFileListAdapter);
		} else {
			mFileListAdapter.setData(mCurrentFolder);
			mFileListAdapter.notifyDataSetChanged();
		}		
	}

	@Override
	public void onProgressUpdate(DownloadItem item) {
		mDownloadListAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onDownloadFinished(DownloadItem item) {
		List<DownloadItem> items = 
				HawkBrowser.getDownloadMgr(this).getItems();
		mDownloadListAdapter.setData(items);
		mDownloadListAdapter.notifyDataSetChanged();
		
		// Expand the last group
		ExpandableListView listView = (ExpandableListView)
				mTabHost.findViewById(R.id.download_download);
		listView.expandGroup(mDownloadListAdapter.getGroupCount() - 1);
	}
	
	@Override
	public void onDownloadStoped(DownloadItem item) {
		mDownloadListAdapter.notifyDataSetChanged();
	}
	
	public void onDownloadItemClick(DownloadItem item) {
		
		Log.d("download", String.format("onDownloadItemClick: %s, %s", 
			item.name(), item.status()));
		
		switch(item.status()) {
		case FINISHED:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("file://" + item.localFilePath()), 
				"application/vnd.android.package-archive");
			startActivity(intent);
			break;
			
		case ONPROGRESS:
			HawkBrowser.getDownloadMgr(this).stop(item);
			break;
			
		case PAUSED:
			HawkBrowser.getDownloadMgr(this).continueDownload(item);
			break;
		}
	}
	
	@Override
	public void onFileItemClick(File file) {
		
		if(null == file) {
			// return to parent folder is click
			mCurrentFolder = mCurrentFolder.getParentFile();
			loadFileSystem();
		} else {
			
			if(file.isDirectory()) {
				mCurrentFolder = file;
				loadFileSystem();
			} else {
				
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(
						Uri.parse("file://" + file.getPath()),
						CommonUtil.getMimeType(file.getPath()));
					startActivity(intent);
				} catch(ActivityNotFoundException e) {
					e.printStackTrace();
					CommonUtil.showTips(this, R.string.open_file_no_app);
				}
			}
		}
	}
	
	@Override
	public void onFileItemChecked(File file) {
		refreshToolbarView(true);
	}
	
	public void onDownloadItemChecked(DownloadItem item) {
		refreshToolbarView(true);
	}
	
	protected void onStart() {
		super.onStart();
		
		HawkBrowser.getDownloadMgr(this).setEventListener(this);
	}
	
    @Override
    protected void onResume() {
        super.onResume();
        
		HawkBrowser.getDownloadMgr(this).setEventListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        HawkBrowser.getDownloadMgr(this).setEventListener(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        
        HawkBrowser.getDownloadMgr(this).setEventListener(null);
    }

}

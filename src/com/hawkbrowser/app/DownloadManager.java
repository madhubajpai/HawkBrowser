package com.hawkbrowser.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.hawkbrowser.R;
import com.hawkbrowser.util.CommonUtil;

import android.content.Context;
import android.os.Environment;

public final class DownloadManager {
	
	public static final String FILE_NAME = "download.dat";
	
	private int mItemId = 0;
	private Context mContext;
	private List<DownloadItem> mItems;
	
	public DownloadManager(Context context) {
		mContext = context;
		load();
	}
	
	public void Download(String url, String userAgent, 
		String contentDisposition, String mimetype, long contentLength) {
		
		String externalStorageState = Environment.getExternalStorageState(); 
		if(!externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
			CommonUtil.showTips(mContext, R.string.nosdcard);
			return;
		}
		
		if(CommonUtil.getExternalStorageFreeSpace() < contentLength) {
			CommonUtil.showTips(mContext, R.string.diskspacenotengouth);
			return;
		}
		
		DownloadItem item = new DownloadItem(++mItemId, contentLength, url);
		mItems.add(item);
		new DownloadAsyncTask(this).execute(item);
	}
	
	public void onProgressUpdate(DownloadItem item) {
		
	}
	
	public void onDownloadFinished(DownloadItem item) {
		
	}
	
	private void load() {
		
		File file = new File(mContext.getFilesDir(), FILE_NAME);
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		try {
			
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			mItems = new ArrayList<DownloadItem>();
			
			do {
				DownloadItem item = (DownloadItem) ois.readObject();
				
				if(null != item) {
					mItems.add(item);
				} else {
					break;
				}
				
			} while(true);
						
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if(null != ois) {
					ois.close();
					ois = null;
					fis = null;
				} else if(null != fis) {
					fis.close();
					fis = null;
				}
				
			} catch(Exception e) {
				
			}
		}
	}
	
	public void save() {
		
		File file = new File(mContext.getFilesDir(), FILE_NAME);
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		
		try {
			
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			
			if(null != mItems) {
				for(DownloadItem item : mItems) {
					oos.writeObject(item);
				}
			}
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			try {
				if(null != oos) {
					oos.close();
					oos = null;
					fos = null;
				} else if(null != fos) {
					fos.close();
					fos = null;
				}
			} catch(Exception e) {
				
			}
		}
	}
}

package com.hawkbrowser.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;

import com.hawkbrowser.util.CommonUtil;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class DownloadAsyncTask extends 
	AsyncTask<DownloadItem, Integer, DownloadItem> {
	
	private static int CONNECT_TIMEOUT = 10000; // ms
	private static int READ_TIMEOUT = 10000; // ms

	private DownloadManager mDownloadMgr;
	
	public DownloadAsyncTask(DownloadManager downloadMgr) {
		mDownloadMgr = downloadMgr;
	}
	
	protected DownloadItem doInBackground(DownloadItem... params) {
		
		InputStream httpStream = null;
		
		try {
			
			DownloadItem item = params[0];
			item.setStatus(DownloadItem.Status.ONPROGRESS);
			
			URL url = new URL(item.url());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(READ_TIMEOUT);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			
			conn.connect();
			int responseCode = conn.getResponseCode();
			Log.d("Download", String.format("Response code: %d, url %s", 
					responseCode, item.url()));
			
			if(HttpStatus.SC_OK != responseCode) {
				httpStream = conn.getInputStream();
				saveToFile(url.getFile(), httpStream);
			} else {
				item.setStatus(DownloadItem.Status.FAILED);
			}
			
		} catch(Exception e) {
			
			Log.e("Download", CommonUtil.getExceptionStackTrace(e));
			
		} finally {
			
			if(null != httpStream) {
				
				try {
					httpStream.close();
					httpStream = null;
				} catch(Exception e) { 
					
				}
			}
		}
		
		return params[0];
	}
	
	protected void onPostExecute(DownloadItem item) {
		item.setStatus(DownloadItem.Status.FINISHED);
		mDownloadMgr.onDownloadFinished(item);
	}
	
	protected void onProgressUpdate(DownloadItem item, 
		Integer... progress) {
		
		item.setProgress(progress[0].longValue());
		mDownloadMgr.onProgressUpdate(item);
	}
	
	private void saveToFile(String fileName, InputStream inputStream) {
		
		Log.d("Download", String.format("save to file: %s", fileName));
		String externalStorageState = Environment.getExternalStorageState();
		if(!externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
			return;
		}
		
		
		FileOutputStream fos = null;
		File file = CommonUtil.getUniqueFile(
			Environment.getExternalStorageDirectory().getPath(), fileName);
		
		try {
			fos = new FileOutputStream(file);
			byte[] buffer = new byte[4096];
			int readLen = 0;
			int totalReadLen = 0;
			
			while((readLen = inputStream.read()) != -1) {
				fos.write(buffer, 0, readLen);
				totalReadLen += readLen;
				publishProgress(totalReadLen);
			}
			
			fos.flush();
			
		} catch(Exception e) {
			
			e.printStackTrace();
			
		} finally {
			
			if(null != fos) {
				
				try {
					fos.close();
					fos = null;
				} catch(Exception e) {
					
				}
			}
		}
		
	}
}

package com.hawkbrowser.app;

import java.io.Serializable;

public class DownloadItem implements Serializable {
	
	enum Status {
		NOTSTARTED,
		ONPROGRESS,
		PAUSED,
		FINISHED,
		FAILED
	}
	
	private static final long serialVersionUID = 1L;
	private int mId;
	private long mProgress;
	private long mLength;
	private String mUrl;
	private Status mStatus;

	public DownloadItem(int id, long length, String url) {
		mId = id;
		mProgress = 0;
		mLength = length;
		mUrl = url;
		mStatus = Status.NOTSTARTED;
	}
	
	public void setProgress(long progress) {
		mProgress = progress;
		
		if(mProgress >= mLength) {
			mProgress = mLength;
		}
	}
	
	public void setStatus(Status status) {
		mStatus = status;
	}
	
	public String url() {
		return mUrl;
	}
	
	public int hashCode() {
		return mId;
	}
}

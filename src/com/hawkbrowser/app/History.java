package com.hawkbrowser.app;

import java.util.List;

import android.content.Context;
import android.text.format.Time;

public class History implements HistoryStorageListener {

	Context mContext;
	HistoryStorage mStorage;
	
	public static class Item {
		String mTitle;
		String mUrl;
		Time mTime;
		
		Item(String title, String url, Time time) {
			mTitle = title;
			mUrl = url;
			mTime = time;
		}
		
		public String title() {
			return mTitle;
		}
		
		public String url() {
			return mUrl;
		}
		
		public Time time() {
			return mTime;
		}
	}
			
	public History(Context context) {
		mContext = context;
		mStorage = new HistorySQLStorage(context, this);
	}
	
	public void Add(String title, String url) {
		Time now = new Time();
		now.setToNow();
		Add(new Item(title, url, now));
	}
	
	public void Add(Item item) {
		mStorage.saveItem(item);
	}
	
	public List<Item> getHistoryToday(Time from, Time to) {
		return mStorage.getItem(from, to);
	}
	
	@Override
	public void onSaveComplete(History.Item item) {
		// nothing to do now
	}
	
	@Override
	public void onGetComplete(List<History.Item> items) {
		// nothing to do now
	}
}

package com.hawkbrowser.app;

import java.util.List;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

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
		
		@Override
		public String toString() {
			return String.format("%s;%s;%s", mTime.toString(), mTitle, mUrl);
		}
	}
				
	public History(Context context) {
		mContext = context;
		mStorage = new HistorySQLStorage(context, this);
	}
	
	public void add(String title, String url) {
		Time now = new Time();
		now.setToNow();
		add(new Item(title, url, now));
	}
	
	public void add(Item item) {
		mStorage.saveItem(item);
	}
	
	public List<Item> getHistoryToday() {
		
		Time now = new Time();
		now.setToNow();
		
		Time from = new Time();
		from.set(now.monthDay, now.month, now.year);
		Time to = new Time();
		to.set(now.monthDay + 1, now.month, now.year);
		
		Log.d("History", String.format("from: %d, %s, to: %d, %s", 
			from.toMillis(false), from.toString(), 
			to.toMillis(false), to.toString()));
		
		return getHistory(from, to);
	}
	
	public List<Item> getHistoryYesterday() {
		Time now = new Time();
		now.setToNow();
		
		Time from = new Time();
		from.set(now.monthDay - 1, now.month, now.year);
		Time to = new Time();
		to.set(now.monthDay, now.month, now.year);
		
		Log.d("History", String.format("from: %d, %s, to: %d, %s", 
			from.toMillis(false), from.toString(), 
			to.toMillis(false), to.toString()));
		
		return getHistory(from, to);
	}
	
	public List<Item> getHistoryRecentWeek() {
		Time now = new Time();
		now.setToNow();
		
		Time from = new Time();
		from.set(now.monthDay - 6, now.month, now.year);
		Time to = new Time();
		to.set(now.monthDay + 1, now.month, now.year);
		
		Log.d("History", String.format("from: %d, %s, to: %d, %s", 
			from.toMillis(false), from.toString(), 
			to.toMillis(false), to.toString()));
		
		return getHistory(from, to);
	}
	
	public List<Item> getHistoryRecentMonth() {
		Time now = new Time();
		now.setToNow();
		
		Time from = new Time();
		from.set(0, now.month, now.year);
		Time to = new Time();
		to.set(now.monthDay + 1, now.month, now.year);
		
		Log.d("History", String.format("from: %d, %s, to: %d, %s", 
			from.toMillis(false), from.toString(), 
			to.toMillis(false), to.toString()));
		
		return getHistory(from, to);
	}
	
	
	public List<Item> getHistory(Time from, Time to) {
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

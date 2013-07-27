package com.hawkbrowser.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.hawkbrowser.app.History.Item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.text.format.Time;

public class HistorySQLStorage implements HistoryStorage {

	HistoryDBHelper mDBHelper;
	SQLiteDatabase mDB;
	AtomicBoolean mIsDBOpened;
	HistoryStorageListener mListener;
	History.Item mPendingSaveItem;
	Time mPendingGetTimeFrom;
	Time mPendingGetTimeTo;
	
	class OpenDBAsyncTask extends 
		AsyncTask<HistorySQLStorage, Void, HistorySQLStorage> {
			
		protected void onPostExecute(HistorySQLStorage storage) {
			mIsDBOpened.set(true);
			storage.runPendingTasks();
		}

		@Override
		protected HistorySQLStorage doInBackground(HistorySQLStorage... params) {
			
			params[0].mDB = params[0].mDBHelper.getWritableDatabase();
			return params[0];
		}
	}
		
	public HistorySQLStorage(Context context, 
		HistoryStorageListener listener) {
		
		mIsDBOpened = new AtomicBoolean(false);
		mDBHelper = new HistoryDBHelper(context);
		mListener = listener;
		new OpenDBAsyncTask().execute(this);
	}
	
	@Override
	public void setListener(HistoryStorageListener listener) {
		mListener = listener;
	}
	
	@Override
	public void saveItem(History.Item item) {
		
		if(mIsDBOpened.get()) {
			internalSaveItem(item);
		} else {
			mPendingSaveItem = item;
		}
	}
	
	@Override
	public List<History.Item> getItem(Time from, Time to) {
		
		if(mIsDBOpened.get()) {
			return internalGetItem(from, to);
		} else {
			mPendingGetTimeFrom = from;
			mPendingGetTimeTo = to;
			return null;
		}
		
	}
	
	@Override
	public void close() {
		if(mIsDBOpened.get()) {
			mIsDBOpened.set(false);
			mDB.close();
			mDB = null;
		}
	}
	
	private void runPendingTasks() {
		
		if(null != mPendingSaveItem) {
			internalSaveItem(mPendingSaveItem);
			
			if(null != mListener) {
				mListener.onSaveComplete(mPendingSaveItem);
				mPendingSaveItem = null;
			}
		}
		
		if((null != mPendingGetTimeFrom) && (null != mPendingGetTimeTo)) {
			List<History.Item> items = 
				internalGetItem(mPendingGetTimeFrom, mPendingGetTimeTo);
			
			if(null != mListener) {
				mListener.onGetComplete(items);
			}
		}
	}
	
	private void internalSaveItem(History.Item item) {
		
		if(null != item) {
			return;
		}
		
		ContentValues values = new ContentValues();
		values.put(HistoryDBHelper.COLUMN_NAME_TIME, 
				item.time().toMillis(false));
		values.put(HistoryDBHelper.COLUMN_NAME_TITLE, item.title());
		values.put(HistoryDBHelper.COLUMN_NAME_URL, item.url());
		mDB.insert(HistoryDBHelper.TABLE_NAME_HISTORY, null, values);
	}
	
	private List<History.Item> internalGetItem(Time from, Time to) {
		
		if(null == from) {
			from = new Time();
			from.set(0);
		}
		
		if(null == to) {
			to = new Time();
			to.setToNow();
			to.year += 10;
		}
		
		String[] projection = {
			HistoryDBHelper.COLUMN_NAME_TIME,
			HistoryDBHelper.COLUMN_NAME_TITLE,
			HistoryDBHelper.COLUMN_NAME_URL
		};
		
		String where = HistoryDBHelper.COLUMN_NAME_TIME + " >= ? and " 
				+ HistoryDBHelper.COLUMN_NAME_TIME + " <= ?";
		String[] whereArgs = { String.format("%ld", from.toMillis(false)), 
				String.format("%ld", to.toMillis(false)) };
		String sortOrder = HistoryDBHelper.COLUMN_NAME_TIME + "DESC";
		
		Cursor c = mDB.query(HistoryDBHelper.TABLE_NAME_HISTORY, projection, 
				where, whereArgs, null, null, sortOrder);	
		c.moveToFirst();
		
		ArrayList<History.Item> results = new ArrayList<History.Item>();
		
		do {
			long ms = c.getLong(
				c.getColumnIndex(HistoryDBHelper.COLUMN_NAME_TIME));
			String title = c.getString(
				c.getColumnIndex(HistoryDBHelper.COLUMN_NAME_TITLE));
			String url = c.getString(
				c.getColumnIndex(HistoryDBHelper.COLUMN_NAME_URL));
			Time time = new Time();
			time.set(ms);
			
			History.Item item = new History.Item(title, url, time);
			results.add(new History.Item(title, url, time));
			
		} while(c.moveToNext());
		
		c.close();
		
		return results;
	}
}

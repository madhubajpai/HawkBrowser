package com.hawkbrowser.app;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class HistoryDBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "history.db";
	public static final String TABLE_NAME_HISTORY = "history";
	public static final String COLUMN_NAME_TITLE = "title";
	public static final String COLUMN_NAME_URL = "url";
	public static final String COLUMN_NAME_TIME = "time";
	
	public static final String SQL_CREATE_TABLE_HISTORY = 
		"CREATE TABLE " + TABLE_NAME_HISTORY + " (" + COLUMN_NAME_TIME + 
		" INTEGER PRIMARY KEY," + COLUMN_NAME_TITLE + " TEXT," + 
		COLUMN_NAME_URL + " TEXT" + ")";
	
	
	public static final String SQL_DELETE_TABLE_HISTORY = 
		"DROP TABLE IF EXISTS " + TABLE_NAME_HISTORY;
	
	public HistoryDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_TABLE_HISTORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_DELETE_TABLE_HISTORY);
		onCreate(db);
	}

}

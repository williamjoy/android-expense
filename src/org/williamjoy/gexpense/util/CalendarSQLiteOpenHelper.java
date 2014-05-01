package org.williamjoy.gexpense.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CalendarSQLiteOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "CALENDAR_STATISTICS";
	public static final String DATABASE_NAME = "CALENDAR_STATISTICS";
	public static final int DATABASE_VERSION = 1;

	public CalendarSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	CalendarSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +" ( DATE TEXT, MONEY INTEGER, PAYMENT TEXT, CATEGORY TEXT );");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}

package com.asb.smsbeta.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "asbsms.db";
	private static final int DATABASE_VERSION = 40;

	MyDatabaseHelper(Context context) {
		// calls the super constructor, requesting the default cursor factory.
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase database) {
		database.execSQL(MessageTable.DATABASE_CREATE);
		database.execSQL(InboxTable.DATABASE_CREATE);
		database.execSQL(MessageTable.CREATE_CONTACT_INDEX);
		database.execSQL(MessageTable.CREATE_INBOX_INDEX);
		database.execSQL(InboxTable.CREATE_CONTACT_INDEX);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		//Change in the future so info isn't lost
		database.execSQL("DROP TABLE IF EXISTS " + MessageTable.TABLE_NAME + ";");
		database.execSQL("DROP TABLE IF EXISTS " + InboxTable.TABLE_NAME + ";");
		database.execSQL(MessageTable.DROP_INBOX_INDEX);
		database.execSQL(MessageTable.DROP_CONTACT_INDEX);
		onCreate(database);
	}

}

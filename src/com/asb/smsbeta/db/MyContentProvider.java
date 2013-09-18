package com.asb.smsbeta.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider{
	//provider's authority
	public static final String AUTH = "com.asb.smsbeta.provider";
	public static final String MESSAGE_TABLE = "msg_table";
	public static final String INBOX_TABLE = "inbox_table";
	private static final int INBOX = 0;
	private static final int MESSAGE = 1;
	private static final int CHAT_ID = 2;
	private static final int ALL_MESSAGES = 3;
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static
    {
        sURIMatcher.addURI(AUTH, INBOX_TABLE, INBOX);
        sURIMatcher.addURI(AUTH, MESSAGE_TABLE, ALL_MESSAGES);
        sURIMatcher.addURI(AUTH, MESSAGE_TABLE+"/message/#", MESSAGE);
        sURIMatcher.addURI(AUTH, MESSAGE_TABLE+"/chat/#", CHAT_ID);//Note: CHAT_ID is inbox_id
    }
	
	public static final Uri CONTENT_MESSAGE_URI = Uri.parse("content://" + AUTH
			+ "/" + MESSAGE_TABLE);
	public static final Uri CONTENT_INBOX_URI = Uri.parse("content://" + AUTH
			+ "/" + INBOX_TABLE);
	public static final Uri CONTENT_CHAT_URI = Uri.parse("content://" + AUTH
			+ "/" + MESSAGE_TABLE+"/chat");
	//Thread Locks
	private static boolean updateLockChatTable;
	private static boolean updateLockInboxTable;
	private static boolean insertLockChatTable;
	private static boolean insertLockInboxTable;
	private static boolean deleteLockChatTable;
	private static boolean deleteLockInboxTable;
	//dbhelper
	private MyDatabaseHelper dbHelper;
	
	@Override
	public boolean onCreate() {
		dbHelper = new MyDatabaseHelper(getContext());
		return true;
	}
	
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		//Log.d("Query", "called");
		Log.d("Query", "Query called, URI: "+ uri.toString());
		int match = sURIMatcher.match(uri);
		//SQLiteQueryBuilder qb = new SQLiteQueryBuilder(); for rawQuery qb.setTables(MessageTable.TABLE_NAME);
		SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
		Cursor c;
		String id;
		switch (match) {
		case ALL_MESSAGES:
			Log.d("Query", "Query case: All messages");
			c = sqlDB.query(MessageTable.TABLE_NAME, 
					projection, 
					selection, 
					selectionArgs, 
					null, null, null);
			break;
		case INBOX:
			Log.d("Query", "Query case: inbox");
			c = sqlDB.query(InboxTable.TABLE_NAME, 
					projection, 
					selection, 
					selectionArgs, 
					null, null, null);
			break;
		case CHAT_ID:
			id = uri.getLastPathSegment();
			Log.d("Query", "Query case: Chat_Id, "+ id);
			selection = MessageTable.INBOX_ID + "=?";
			selectionArgs = new String[]{id};
			c = sqlDB.query(MessageTable.TABLE_NAME, 
					projection, 
					selection, 
					selectionArgs, 
					null, null, null);
			//Log.d("Query", "Chat_Id, "+ id + ", "+ c.);
			
			
		break;
		case MESSAGE:
			id = uri.getLastPathSegment();
			Log.d("Query", "Query case: Chat_Id, "+ id);
			selection = MessageTable._ID + "=?";
			selectionArgs = new String[]{id};
			c = sqlDB.query(MessageTable.TABLE_NAME, 
					projection, 
					selection, 
					selectionArgs, 
					null, null, null);
			//Log.d("Query", "Chat_Id, "+ id + ", "+ c.);
		break;
		default:
			throw new IllegalArgumentException("Unknown INSERT URI: " + uri);
		}
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		//Log.d("INSERT", "Insert called");
		int match = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		long id = 0;
		switch (match) {
		case ALL_MESSAGES:
			Log.d("INSERT", "Insert case: All messages");
			id = sqlDB.insert(MessageTable.TABLE_NAME, null, values);
			break;
		case INBOX:
			Log.d("INSERT", "Insert case: inbox");
			id = sqlDB.insert(InboxTable.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown INSERT URI: " + uri);
		}
		if(id == -1){
			Log.d("INSERT", "Failed returned -1");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		//TODO FIX THIS FOR ALL CASES
		return Uri.parse("content://" + AUTH + "/" + MESSAGE_TABLE + "/message/" + id);
	}
		
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int match = sURIMatcher.match(uri);
		String id;
		Log.d("UPDATE", "UPDATE CALLED");
		switch(match){
			case INBOX:
				Log.d("UPDATE", "Update index called");
				sqlDB.update(InboxTable.TABLE_NAME, 
							values,  
							selection,
							selectionArgs);
			break;
			case MESSAGE:
				id = uri.getLastPathSegment();
				Log.d("UPDATE", "UPDATE case: Chat_Id, "+ id);
				selection = MessageTable._ID + "=?";
				selectionArgs = new String[]{id};
				sqlDB.update(MessageTable.TABLE_NAME, values, selection, selectionArgs);
			break;
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return 0;
	}

}

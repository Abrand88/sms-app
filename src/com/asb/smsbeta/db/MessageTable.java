package com.asb.smsbeta.db;

public abstract class MessageTable {
	public final static String ME = "-20";
	public final static String TABLE_NAME = "msgtable";
	public final static String _ID = "_id"; // _ID needed to bind data from a provider to a ListView 
	public final static String INBOX_ID = "inbox_id";
	public final static String CONTACT_ID = "contact_id";
	public final static String BODY = "body";
	public final static String DATE = "date";
	public final static String SMS_TIME = "smstime";
	public final static String ADDRESS = "address";
	public final static String UNSEEN = "unseed";
	public final static String UNREAD = "unread";
	public final static String TYPE = "type";
	public final static String ERROR = "error_msg";
	public final static String OTHER_TEXT = "other_text";
	public final static String OTHER_NUM = "other_num";
	public final static String NAME = "name";
	public final static String DATABASE_CREATE = "create table " + 
			TABLE_NAME + " ( " +
			_ID + " integer primary key autoincrement, " +
			CONTACT_ID + " integer, " +
			INBOX_ID + " integer, " +
			SMS_TIME + " integer, " +
			DATE + " INTEGER NOT NULL DEFAULT (strftime('%s','now')), " +
			ADDRESS + " text, " +
			BODY + " text, " +
			NAME + " text, " +
			ERROR + " text, " +
			UNSEEN + " integer, " + // seen 0, unseen 1;
			UNREAD + " integer, " + // read 0, unread 1;
			TYPE + " integer, " + 
			OTHER_TEXT + " text, " +
			OTHER_NUM + " integer " +
			");";
	public final static String CREATE_CONTACT_INDEX = 
			"CREATE INDEX IF NOT EXISTS contact_id_index_msg ON " + TABLE_NAME +" ( "+CONTACT_ID+" );";
	public final static String CREATE_INBOX_INDEX = 
			"CREATE INDEX IF NOT EXISTS inbox_id_index_msg ON " + TABLE_NAME +" ( "+INBOX_ID+" );";
	public final static String DROP_INBOX_INDEX = "DROP INDEX IF EXISTS inbox_id_index_msg";
	public final static String DROP_CONTACT_INDEX = "DROP INDEX IF EXISTS contact_id_index_msg"; 
}

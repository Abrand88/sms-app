package com.asb.smsbeta.db;

public class InboxTable {
	public final static String TABLE_NAME = "inboxtable";
	public final static String _ID = "_id";
	public final static String CONTACT_ID = "contact_id";
	public final static String SNIPPET = "snippet";
	public final static String DATE = "date";
	public final static String ADDRESS = "address";
	public final static String UNSEEN = "unseen";
	public final static String UNREAD = "unread";
	public final static String TYPE = "type";
	public final static String NAME = "contact_name";
	public final static String ERROR = "error";
	public final static String DATABASE_CREATE = "create table " + 
			TABLE_NAME + " (" +
			_ID + " integer primary key autoincrement, " +
			CONTACT_ID + " integer," +
			NAME + " text," +
			SNIPPET + " text, " +
			DATE + " INTEGER NOT NULL DEFAULT (strftime('%s','now')), " +
			ADDRESS + " text, " +
			UNSEEN + " integer, " + 
			UNREAD + " integer, " +  
			TYPE + " integer, " +
			ERROR + " text" +
           ");";
	public final static String CREATE_CONTACT_INDEX = 
			"CREATE UNIQUE INDEX IF NOT EXISTS contact_id_index_inbox ON " + TABLE_NAME +" ( "+CONTACT_ID+" )";
}

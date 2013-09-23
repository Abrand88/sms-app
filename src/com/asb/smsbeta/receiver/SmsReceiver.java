package com.asb.smsbeta.receiver;

import java.util.Iterator;

import com.asb.smsbeta.db.InboxTable;
import com.asb.smsbeta.db.MessageTable;
import com.asb.smsbeta.db.MyContentProvider;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
	private String contactName;
	private Long contactID;
	private String body, address;
	private static final String TAG = "onReceive";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "CALLED");
		Bundle extras = intent.getExtras();
		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		if(extras.get( "pdus" ) != null){
			// Get received SMS array
			Object[] smsExtra = (Object[]) extras.get( "pdus" );
			ContentResolver resolver = context.getContentResolver();
			for ( int i = 0; i < smsExtra.length; ++i ) {
				SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
				body = sms.getDisplayMessageBody(); //sms.getMessageBody();
				address = sms.getDisplayOriginatingAddress();//has email check in it //sms.getOriginatingAddress();
				Log.d(TAG, "SMS body: "+body);
				Log.d(TAG, "SMS address: "+address);
				Cursor cursor;
				if(sms.isEmail()){
					Uri uri = Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, Uri.encode(address));
					cursor = resolver.query(uri,
							new String[]{Email.CONTACT_ID, Email.DISPLAY_NAME /*, Email.DATA*/ },
							null, null, null);
				}else{ //sms
					Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(address));
					cursor = resolver.query(lookupUri, 
							new String[]{PhoneLookup._ID,PhoneLookup.DISPLAY_NAME},
							null,null,null);
				}
				if(cursor != null && cursor.getCount() > 0){
					cursor.moveToFirst();
					if(sms.isEmail()){
						contactID = cursor.getLong(cursor.getColumnIndex(Email.CONTACT_ID));
						contactName = cursor.getString(cursor.getColumnIndex(Email.DISPLAY_NAME));
					}else{
						contactID = cursor.getLong(cursor.getColumnIndex(PhoneLookup._ID));
						contactName = cursor.getString(cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME));
					}
				}
				cursor.close();
				cursor = null;
				ContentValues values = new ContentValues();
				boolean hasContact = false;
				//might use this Uri indexRow;
				if(contactName != null && contactID!= null){
					hasContact = true;
					values.put( MessageTable.CONTACT_ID, contactID);
					values.put( MessageTable.NAME, contactName);
				}
				String mSelectContactIDArgs[] = null;
				String mSelectAddressArgs[] = null;
				if(hasContact){
					Cursor cursor2;
					//Add Contact_ID, Name, Address, and snippet
					mSelectContactIDArgs = new String[]{Long.toString(contactID)};
					cursor2 = resolver.query(MyContentProvider.CONTENT_INBOX_URI,
							new String[]{InboxTable._ID,InboxTable.CONTACT_ID},
							InboxTable.CONTACT_ID + " =?",
							mSelectContactIDArgs,null);
					ContentValues inboxValues = new ContentValues();
					if(cursor2.getCount() <= 0){
						inboxValues.put(InboxTable.CONTACT_ID, contactID);
						inboxValues.put(InboxTable.NAME, contactName);
						inboxValues.put(InboxTable.ADDRESS, address);
						inboxValues.put(InboxTable.SNIPPET, body);
						resolver.insert(MyContentProvider.CONTENT_INBOX_URI, inboxValues);
					}else {
						Log.d("onReceive", "update: snippet, c_id:"+ mSelectContactIDArgs[0]);
						inboxValues.put(InboxTable.SNIPPET, body);
						resolver.update(MyContentProvider.CONTENT_INBOX_URI, 
								inboxValues, 
								InboxTable.CONTACT_ID + " =?",
								mSelectContactIDArgs);
					}
					cursor2.close();
					cursor2 = null;
				}else{ // No Contact
					Cursor cursor3;
					//Add Address and snippet 
					mSelectAddressArgs = new String[]{address};
					cursor3 = resolver.query(MyContentProvider.CONTENT_INBOX_URI,
							new String[]{InboxTable.ADDRESS},
							InboxTable.ADDRESS + " =?",
							mSelectAddressArgs, null);
					if(cursor3.getCount() <= 0){
						ContentValues inboxValues = new ContentValues();
						inboxValues.put(InboxTable.ADDRESS, address);
						inboxValues.put(InboxTable.SNIPPET, body);
						resolver.insert(MyContentProvider.CONTENT_INBOX_URI, inboxValues);
					}
					cursor3.close();
					cursor3 = null;
				}
				values.put(MessageTable.ADDRESS, address);
				values.put(MessageTable.BODY, body);
				values.put(MessageTable.UNREAD, 1); 
				values.put(MessageTable.UNSEEN, 1);
				values.put(MessageTable.SMS_TIME, sms.getTimestampMillis());
				Cursor cursor4;
				if(hasContact){
					cursor4 = resolver.query(MyContentProvider.CONTENT_INBOX_URI,
							new String[]{InboxTable._ID},
							InboxTable.CONTACT_ID + " =?",
							mSelectContactIDArgs, null);
				}else{
					cursor4 = resolver.query(MyContentProvider.CONTENT_INBOX_URI,
							new String[]{InboxTable._ID},
							InboxTable.ADDRESS + " =?",
							mSelectAddressArgs, null);
				}
				//Add chat_id aka inbox_id to message 
				if(cursor4.moveToFirst()){
					values.put(MessageTable.INBOX_ID, 
							cursor4.getLong(cursor4.getColumnIndexOrThrow(InboxTable._ID)));
				}else{
					Log.d(TAG, "Get chat id ERROR");
				}
				Log.d(TAG, "Before msg insert");
				resolver.insert(MyContentProvider.CONTENT_MESSAGE_URI, values);
				Log.d(TAG, "After msg insert");
			}
		} else {
			//MMS
			Log.d(TAG, "Intent getAction"+ intent.getAction());
			Iterator<String> it = extras.keySet().iterator();
			String key ="";
			while (it.hasNext()){
				Log.d(TAG, "Key: "+ (key = it.next()));
				Log.d(TAG, "Value: "+ extras.get(key).toString());
			}
		}
	}
	/*boolean show =prefs.getBoolean("pref_key_top_bar",false);
	if(show){
		showAppNotification(context,name,message,prefs);
	}*/	
}

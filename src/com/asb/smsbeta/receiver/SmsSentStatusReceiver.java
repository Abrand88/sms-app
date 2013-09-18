package com.asb.smsbeta.receiver;

import com.asb.smsbeta.db.MessageTable;

import android.content.BroadcastReceiver;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsSentStatusReceiver extends BroadcastReceiver {
	//public static final String ACTION_SMS_SENT = "com.asb.smsbeta.chat.SMS_SENT_ACTION";
	//public static final String ACTION_SMS_DELIVERED = "com.asb.smsbeta.chat.SMS_DELIVERED";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("SentStatus", "SentStatus CALLED");
		String message = "no case";
		boolean sent = false;
		switch (getResultCode()){
		case Activity.RESULT_OK:
			message = "sent";
			sent = true;
			break;
		case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
			message = "Error.";
			break;
		case SmsManager.RESULT_ERROR_NO_SERVICE:
			message = "Error: No service.";
			break;
		case SmsManager.RESULT_ERROR_NULL_PDU:
			message = "Error: Null PDU.";
			break;
		case SmsManager.RESULT_ERROR_RADIO_OFF:
			message = "Error: Radio off.";
			break;
		}
		Log.d("SentStatus", "SentStatus msg: "+ message);
		//mSendIntent.getDataString()
		Log.d("SentStatus", "getAction() is: "+ intent.getAction());
		//Log.d("SentStatus", "get____ is: "+ intent.getPackage());
		//Log.d("SentStatus", "get____ is: "+ intent.getDataString());
		if(intent.getExtras() == null){
			Log.d("SentStatus", "getExtras() is null");		
		}else{
			Log.d("SentStatus", "getExtras() NOT null");
			Log.d("SentStatus", "Extra row_id " +intent.getExtras().getString("row_id"));
		}
		if(!sent){
			Uri uri = intent.getExtras().getParcelable("uri");
			Log.d("SentStatus", "Uri is: "+ uri.toString());
			ContentResolver resolver = context.getContentResolver();
			ContentValues values = new ContentValues();
			values.put(MessageTable.ERROR, "Not Sent, " + message);
			resolver.update(uri, values, null, null);	
		}
		/*if(message != null)
			Toast.makeText(getApplicationContext(), message,
					Toast.LENGTH_SHORT).show();
		*/
	}

}

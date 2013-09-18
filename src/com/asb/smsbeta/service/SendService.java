package com.asb.smsbeta.service;

import java.util.ArrayList;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class SendService extends IntentService {
	public static final String ACTION_SMS_SENT = "com.asb.smsbeta.chat.SMS_SENT_ACTION";
	
	public SendService() {
		super("SendService");
	}

	@Override
    protected void onHandleIntent(Intent intent) {
		Log.d("SendService", "SendService onHandleCalled");
		Bundle extras = intent.getExtras();
		if(extras == null){
			//Log.d("SendService", "SendService Extras equals NULL");
		}
		String address = extras.getString("address");
		String body = extras.getString("body");
		Uri msgUri = extras.getParcelable("uri");
		Log.d("SendService", "SendService Body: " + body);
		if(address != null){
			SmsManager sms = SmsManager.getDefault();
			ArrayList<String> parts = sms.divideMessage(body);
			int numParts = parts.size();
			ArrayList<PendingIntent> pendingSendIntents = new ArrayList<PendingIntent>();
			Intent sendIntent;
			for (int i = 0; i < numParts; i++) {
				sendIntent = new Intent(ACTION_SMS_SENT);
				String rowId = msgUri.getLastPathSegment();
				int requestCode = Integer.parseInt(rowId);
				sendIntent.putExtra("row_id", rowId);
				sendIntent.putExtra("uri", msgUri);
				pendingSendIntents.add(PendingIntent.getBroadcast(this, requestCode, sendIntent, 0));
			}
			sms.sendMultipartTextMessage(address,null ,parts, pendingSendIntents, null);
		}else{
			Log.d("SendService", "SendService Address is NULL");
		}
    }
}

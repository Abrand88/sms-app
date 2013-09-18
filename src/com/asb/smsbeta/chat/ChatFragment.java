package com.asb.smsbeta.chat;

import java.util.ArrayList;

import com.asb.smsbeta.MainActivity;
import com.asb.smsbeta.R;
import com.asb.smsbeta.db.MessageTable;
import com.asb.smsbeta.db.MyContentProvider;
import com.asb.smsbeta.inbox.InboxFragment.OnChatSelectedListener;
import com.asb.smsbeta.service.SendService;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.LoaderManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
//import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ChatFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	public static ChatFragment newInstance(Uri uri,String address) {
		final ChatFragment cf = new ChatFragment();
		Bundle args = new Bundle();
		//TODO set Chat class
		Chat chat = new Chat();
		chat.setUri(uri);
		chat.setAddress(address);
		chat.setBundleArgs(args);
		cf.setArguments(args);
		return cf;
	}

	public String getChatUriString() {
		if(getArguments() != null){
			return getArguments().getString("uri", null);
		}else{
			return	null;
		}

	}
	public Uri getChatUri() {
		if(getArguments() != null){
			return Uri.parse(getArguments().getString("uri", ""));
		}else{
			return null;
		}
	}

	public EditText getEditTextView(){
		return editText;
	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	ChatCursorAdapter chatAdapter;
	ListView lv;
	Button btnSend;
	EditText editText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.chat_list_content, container, false);
		lv = (ListView)view.findViewById(android.R.id.list);
		btnSend = (Button) view.findViewById(R.id.buttonSend);
		editText = (EditText) view.findViewById(R.id.editTextChatView);
		return view;
	}
	
	@Override 
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		chatAdapter = new ChatCursorAdapter(getActivity(),null,0);
		setListAdapter(chatAdapter);
		lv.setStackFromBottom(true);
		lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		getLoaderManager().initLoader(0, null, (LoaderCallbacks<Cursor>) this);
		final Context context = getActivity();
		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (TextUtils.isEmpty(editText.getText())) {
					Toast.makeText(getActivity(), "Please enter a message",
							Toast.LENGTH_SHORT).show();
					return;
				}
				final String body = editText.getText().toString();
				editText.setText("");
				//Insert message into the database
				ContentResolver resolver = context.getContentResolver();
				ContentValues values = new ContentValues();
				String inbox_id = getChatUri().getLastPathSegment();
				values.put(MessageTable.INBOX_ID,inbox_id);
				values.put(MessageTable.CONTACT_ID, MessageTable.ME);
				values.put(MessageTable.BODY, body);
				Uri messageUri = resolver.insert(MyContentProvider.CONTENT_MESSAGE_URI, values);
				//Make intent and start service 
				Intent mServiceIntent = new Intent(getActivity(), SendService.class);
				//mServiceIntent.setData(messageUri); Doesn't really do anything since getData isn't set up 
				mServiceIntent.putExtra("uri", messageUri);
				mServiceIntent.putExtra("address", getArguments().getString("address", null));
				mServiceIntent.putExtra("body", body);
				getActivity().startService(mServiceIntent);
				((MainActivity)getActivity()).hideKeyBoard();
			}
		});
	}

	static final String[] MESSAGES_PROJECTION = new String[] {
		MessageTable._ID,
		MessageTable.DATE,
		MessageTable.CONTACT_ID,
		MessageTable.BODY,
		MessageTable.INBOX_ID,
		MessageTable.SMS_TIME,
		MessageTable.NAME,
		MessageTable.ERROR
	};
	String mCurFilter;
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d("Loader Create", "Loader Create was called");
		Uri baseUri;
		//baseUri = MyContentProvider.CONTENT_MESSAGE_URI;
		//Faster than getChatUri()
		if(getChatUriString() != null){
			baseUri = getChatUri();
		} else {
			baseUri = MyContentProvider.CONTENT_MESSAGE_URI;
		}
		// Now create and return a CursorLoader that will take care of
		// creating a Cursor for the data being displayed.
		String select = null; /*"((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + Contacts.DISPLAY_NAME + " != '' ))";*/
		select = MessageTable.INBOX_ID + "=?";
		return new CursorLoader(getActivity(), baseUri,
				MESSAGES_PROJECTION, null, null,
				MessageTable.DATE + " COLLATE LOCALIZED ASC");
	}
	@Override
	public void onLoadFinished(Loader<Cursor> loader,
			Cursor data) {
		chatAdapter.swapCursor(data);

		// The list should now be shown.
		//if (isResumed()) {
		//  setListShown(true);
		//} else {
		//   setListShownNoAnimation(true);
		//}
	}
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		chatAdapter.swapCursor(null);
	}
}

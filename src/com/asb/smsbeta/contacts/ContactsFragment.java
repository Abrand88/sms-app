package com.asb.smsbeta.contacts;

import com.asb.smsbeta.R;
import android.widget.AdapterView;
import com.asb.smsbeta.db.InboxTable;
import com.asb.smsbeta.db.MyContentProvider;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ContactsFragment extends ListFragment implements 
	LoaderManager.LoaderCallbacks<Cursor> {
	private EditText et;
	private ContactsCursorAdapter contactAdapter;
	// The column index for the _ID column
	private static final int CONTACT_ID_INDEX = 0;
	// The column index for the LOOKUP_KEY column
	private static final int LOOKUP_KEY_INDEX = 1;
	
	public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contactAdapter = new ContactsCursorAdapter(getActivity(),null,0);
        setListAdapter(contactAdapter);
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.chat_list_content, container, false);
		et = (EditText) view.findViewById(R.id.editTextContactSearch);
		return view;
	}
	static final String[] CONTACT_PROJECTION = new String[] {
		Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ? Contacts.DISPLAY_NAME_PRIMARY :
        Contacts.DISPLAY_NAME,
        Contacts._ID,
        Contacts.LOOKUP_KEY
    };
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		//Log.d("Loader Create", "ContactLoader Create was called");
        Uri baseUri;
        baseUri = MyContentProvider.CONTENT_INBOX_URI; 
        String select = null;
        return new CursorLoader(getActivity(), baseUri,
        		CONTACT_PROJECTION, select, null,
                InboxTable.DATE + " COLLATE LOCALIZED ASC");
	}
	@Override
	public void onLoadFinished(Loader<Cursor> loader,
			Cursor data) {
		contactAdapter.swapCursor(data);
	}
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		contactAdapter.swapCursor(null);
	}

	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		final Cursor cursor = contactAdapter.getCursor();
		final Uri uri = Contacts.getLookupUri(
                cursor.getLong(CONTACT_ID_INDEX),
                cursor.getString(LOOKUP_KEY_INDEX));
	}
}

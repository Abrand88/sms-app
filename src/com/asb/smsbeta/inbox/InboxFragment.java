package com.asb.smsbeta.inbox;
import com.asb.smsbeta.R;
import com.asb.smsbeta.db.InboxTable;
import com.asb.smsbeta.db.MyContentProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
//import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class InboxFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	public interface OnChatSelectedListener {
        public void onChatSelected(Uri uri,String address);
    }
	OnChatSelectedListener mListener;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnChatSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Uri chatUri = ContentUris.withAppendedId(MyContentProvider.CONTENT_CHAT_URI, id);
        //MyContentProvider content = new MyContentProvider();
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor c = resolver.query(MyContentProvider.CONTENT_INBOX_URI, 
        		new String[] {InboxTable._ID, InboxTable.ADDRESS},
        		InboxTable._ID+" = ?", 
        		new String[]{Long.toString(id)},
        		null);
        if(c == null)
        	Log.d("Cursor", "Cursor is null");
        c.moveToFirst();
        mListener.onChatSelected(chatUri, c.getString(c.getColumnIndex(InboxTable.ADDRESS)));
    }
	
	SimpleCursorAdapter mAdapter;
	InboxCursorAdapter inboxAdapter;
	@Override 
	public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
        inboxAdapter = new InboxCursorAdapter(getActivity(),null,0);
        setListAdapter(inboxAdapter);
        getLoaderManager().initLoader(0, null, (LoaderCallbacks<Cursor>) this);
	}
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.inbox_list_content, container, false);
    }
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
		Log.d("Test","onCreateContextMenu");
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getActivity().getMenuInflater();
	    inflater.inflate(R.menu.inbox_context_menu, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.d("Test","onContextItemSelected");
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.delete:
	        	ContentResolver resolver = getActivity().getContentResolver();
	        	//TODO write delete
	        	//resolver.delete(url, where, selectionArgs);
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	static final String[] INBOX_PROJECTION = new String[] {
        InboxTable._ID,
        InboxTable.NAME,
        InboxTable.CONTACT_ID,
        InboxTable.SNIPPET,
        InboxTable.DATE
    };
	String mCurFilter;
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.d("Loader Create", "Inbox Loader Create was called");
        Uri baseUri;
        baseUri = MyContentProvider.CONTENT_INBOX_URI; 
        String select = null;
        return new CursorLoader(getActivity(), baseUri,
        		INBOX_PROJECTION, select, null,
                InboxTable.DATE + " COLLATE LOCALIZED ASC");
	}
	@Override
	public void onLoadFinished(Loader<Cursor> loader,
			Cursor data) {
		Log.d("inbox", "onLoaderfinished called");
		inboxAdapter.swapCursor(data);

        // The list should now be shown.
        //if (isResumed()) {
          //  setListShown(true);
        //} else {
         //   setListShownNoAnimation(true);
        //}
	}
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		inboxAdapter.swapCursor(null);
	}
}

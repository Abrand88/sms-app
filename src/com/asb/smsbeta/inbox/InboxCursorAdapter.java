package com.asb.smsbeta.inbox;

import com.asb.smsbeta.R;
import com.asb.smsbeta.db.InboxTable;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
public class InboxCursorAdapter extends CursorAdapter{
	//http://developer.android.com/training/contacts-provider/display-contact-badge.html
	private LayoutInflater mInflater;
	private int nameIndex;
	private int snippetIndex;
	public InboxCursorAdapter (Context context, Cursor c, int flags){
		super(context,c,flags);
		//Log.d("InboxCursorAdapter","ICA constutor called");
		mInflater = LayoutInflater.from(context);
	}
	private class ViewHolder {
        TextView displayname;
        TextView snippet;
    }
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		//Log.d("InboxCursorAdapter","ICA Bind called");
		final ViewHolder holder = (ViewHolder) view.getTag();
		//final String name =  cursor.getString(nameIndex);
        //final String snippet = cursor.getString(snippetIndex);
		//final String photoData = cursor.getString(mPhotoDataIndex);
        //final String displayName = cursor.getString(mDisplayNameIndex);
        //...
        // Sets the display name in the layout
		nameIndex = cursor.getColumnIndex(InboxTable.NAME);
		snippetIndex = cursor.getColumnIndex(InboxTable.SNIPPET);
		//Log.d("InboxCursorAdapter","ICA cursor index found");
		holder.displayname.setText(cursor.getString(nameIndex));
        holder.snippet.setText(cursor.getString(snippetIndex));
        //Log.d("InboxCursorAdapter","ICA holders set");
        //...
        /*
         * Generates a contact URI for the QuickContactBadge.
         
        final Uri contactUri = Contacts.getLookupUri(
                cursor.getLong(mIdIndex),
                cursor.getString(mLookupKeyIndex));
        holder.quickcontact.assignContactUri(contactUri);
        String photoData = cursor.getString(mPhotoDataIndex);
        *//*
         * Decodes the thumbnail file to a Bitmap.
         * The method loadContactPhotoThumbnail() is defined
         * in the section "Set the Contact URI and Thumbnail"
         */
        /*Bitmap thumbnailBitmap =
                loadContactPhotoThumbnail(photoData);
         */
        /*
         * Sets the image in the QuickContactBadge
         * QuickContactBadge inherits from ImageView
         */
        //holder.quickcontact.setImageBitmap(thumbnailBitmap);
		
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		Log.d("InboxCursorAdapter","ICA NEWVIEW called");
		final View itemView =
                mInflater.inflate(
                        R.layout.inbox_list_item,
                        parent,
                        false
                );
        final ViewHolder holder = new ViewHolder();
        holder.displayname = (TextView) itemView.findViewById(R.id.textViewDisplayName);
        holder.snippet = (TextView) itemView.findViewById(R.id.textViewSnippet);
        itemView.setTag(holder);
        return itemView;
	}
	
	
	

}

package com.asb.smsbeta.contacts;

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

public class ContactsCursorAdapter extends CursorAdapter{

	private LayoutInflater mInflater;
	public ContactsCursorAdapter (Context context, Cursor c, int flags){
		super(context,c,flags);
		mInflater = LayoutInflater.from(context);
	}
	
	private class ViewHolder {
        TextView displayname;
    }
	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		final View itemView =
                mInflater.inflate(
                        R.layout.contact_list_item,
                        parent,
                        false
                );
        final ViewHolder holder = new ViewHolder();
        holder.displayname = (TextView) itemView.findViewById(R.id.textViewDisplayName);
        itemView.setTag(holder);
        return itemView;
	}
	public void bindView(View view, Context context, Cursor cursor) {
		
		final ViewHolder holder = (ViewHolder) view.getTag();
		
	}
}

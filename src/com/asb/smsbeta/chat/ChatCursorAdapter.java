package com.asb.smsbeta.chat;

import com.asb.smsbeta.R;
import com.asb.smsbeta.db.MessageTable;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ChatCursorAdapter extends CursorAdapter {
	private LayoutInflater mInflater;
	private int nameIndex;
	private int bodyIndex;
	private int errorIndex;
	private int moreMargin = -1;
	private int lessMargin = -1;
	
	private double dp = 0.0;
	private class ViewHolder {
        TextView displayname;
        TextView textBody;
        TextView error;
        FrameLayout frame;
    }
	
	public ChatCursorAdapter(Context context, Cursor cursor, int flags){
		super(context,cursor,flags);
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		nameIndex = cursor.getColumnIndex(MessageTable.NAME);
		//Log.v("bindview", "nameIndex is "+ nameIndex);
		bodyIndex = cursor.getColumnIndex(MessageTable.BODY);
		//Log.v("bindview", "bodyIndex is "+ bodyIndex);
		errorIndex = cursor.getColumnIndex(MessageTable.ERROR);
		//Log.v("bindview", "errorIndex is "+ errorIndex);
		final ViewHolder holder = (ViewHolder) view.getTag();
		holder.displayname.setText(cursor.getString(nameIndex));
        holder.textBody.setText(cursor.getString(bodyIndex));
        LayoutParams lp = (LayoutParams) holder.frame.getLayoutParams();
        if(dp == 0.0 && moreMargin == -1 ){
        	dp = lp.rightMargin / 50.0;// was set in the xml file
        	moreMargin = lp.rightMargin;
        	lessMargin = lp.leftMargin;
        }
        if(cursor.getInt(cursor.getColumnIndex(MessageTable.CONTACT_ID)) == -20){
        	holder.frame.setBackgroundResource(R.drawable.chat_shadow_right);
        	lp.leftMargin = moreMargin;
        	lp.rightMargin = lessMargin;
        	holder.frame.setLayoutParams(lp);
        	//holder.displayname.setVisibility(View.GONE);
        	//lp.setMargins(left, lp.topMargin, lp.rightMargin, lp.bottomMargin);
        	String err = cursor.getString(errorIndex);
            if(err != null && err.length() > 1){
            	holder.error.setText(err);
            	holder.error.setVisibility(View.VISIBLE);
            }else{
            	holder.error.setVisibility(View.GONE);
            }
        }else if (lp.leftMargin == moreMargin){
        	holder.frame.setBackgroundResource(R.drawable.chat_shadow_left);
        	lp.leftMargin = lessMargin;
        	lp.rightMargin = moreMargin;
        	holder.frame.setLayoutParams(lp);
        	holder.error.setVisibility(View.GONE);
        }
        
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		final View itemView =
                mInflater.inflate(
                        R.layout.chat_list_item,
                        parent,
                        false
                );
        final ViewHolder holder = new ViewHolder();
        holder.displayname = (TextView) itemView.findViewById(R.id.textViewChatDisplayName);
        holder.textBody = (TextView) itemView.findViewById(R.id.textViewChatBody);
        holder.frame = (FrameLayout) itemView.findViewById(R.id.frameLayoutChat);
        holder.error = (TextView) itemView.findViewById(R.id.textViewChatError);
        itemView.setTag(holder);
        return itemView;
	}

}

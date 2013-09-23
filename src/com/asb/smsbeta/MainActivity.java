package com.asb.smsbeta;

import com.asb.smsbeta.R;
import com.asb.smsbeta.chat.ChatFragment;
import com.asb.smsbeta.db.MyContentProvider;
import com.asb.smsbeta.inbox.InboxFragment;
import com.asb.smsbeta.inbox.InboxFragment.OnChatSelectedListener;
//import com.asb.smsbeta.R.layout;
//import com.asb.smsbeta.R.menu;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends FragmentActivity implements OnChatSelectedListener {

	private SlidingPaneLayout mSlidingPaneLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSlidingPaneLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane);
		mSlidingPaneLayout.openPane();
		mSlidingPaneLayout.setParallaxDistance(200);
		//mSlidingPaneLayout.setSliderFadeColor(0);

		/*<fragment android:name="com.asb.smsbeta.chat.ChatFragment"
	        android:id="@+id/chat"
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	         />*/

		if(savedInstanceState == null){
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			ChatFragment fragment = ChatFragment.newInstance(MyContentProvider.CONTENT_MESSAGE_URI, null);
			fragmentTransaction.add(R.id.chatContainer, fragment,"start");
			fragmentTransaction.commit();
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void hideKeyBoard(){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow (
				mSlidingPaneLayout.getWindowToken(),
				0);
	}


	@Override
	public void onChatSelected(Uri uri, String address) {
		mSlidingPaneLayout.closePane();
		ChatFragment chat = (ChatFragment)
				getSupportFragmentManager().findFragmentByTag(uri.toString());
		if (chat == null || chat.getChatUri().compareTo(uri) != 0) {
			hideKeyBoard();
			ChatFragment chat2 = ChatFragment.newInstance(uri,address);
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.chatContainer, chat2, uri.toString());
			//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.commit();
		}
	}
}

package com.asb.smsbeta.chat;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Chat {
	private String uri;
	private String name;
	private String address;
	private Long id;
	private Long contact_id;
		
	public Chat(){	
	}
	
	public Chat(Uri uri, String name, String address, Long id, Long contact_id){
		this.uri = uri.toString();
		this.name = name;
		this.address =  address;
		this.id = id;
		this.contact_id = contact_id;
	}

	public void setBundleArgs(Bundle args){
		args.putString("uri", uri);
		//args.putString("name", name);
		args.putString("address", address);
		//args.putLong("chat_id", id);
		//args.putLong("contact_id", contact_id);
	}
	
	public String getStringUri() {
		return uri;
	}

	public void setUri(Uri uri) {
		this.uri = uri.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getContact_id() {
		return contact_id;
	}

	public void setContact_id(Long contact_id) {
		this.contact_id = contact_id;
	}
}

package com.filper.helpers;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class HelpersFilper {
	private Context context; 
	
	public HelpersFilper(Context context){
		this.context = context;
	}
	
	public void openCall(String numberPhone){
		String uri = String.format(Locale.ENGLISH, "tel:%s", numberPhone );
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		context.startActivity(intent);	
	}
	
}

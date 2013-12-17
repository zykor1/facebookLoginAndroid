package com.filper.notifications;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.filper.notifications.WebSocketService;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
	
	private static final String TAG = "com.filper.notifications";
	
 @Override
 public void onReceive(Context context, Intent intent) {
	 if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) { 
	  Log.d(TAG, "websocket: booting.");
      Intent serviceIntent = new Intent(context, WebSocketService.class);
      context.startService(serviceIntent);
	 }
 }
}

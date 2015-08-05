package com.cceo.miyoideal.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
	
	private DialyNotificationReceiver dialyNotification;
	@Override
	public void onReceive(Context context, Intent intent){
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			dialyNotification = new DialyNotificationReceiver();
			dialyNotification.setAlarm(context);	
        } 
	}
}

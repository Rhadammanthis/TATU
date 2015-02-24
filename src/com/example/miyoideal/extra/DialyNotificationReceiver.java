package com.example.miyoideal.extra;

import java.util.Calendar;
import java.util.Random;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.example.miyoideal.MainActivity;
import com.example.miyoideal.R;

public class DialyNotificationReceiver extends BroadcastReceiver {

	private AlarmManager alarmManager;
	private PendingIntent alarmIntent;
	
	@Override
	public void onReceive(Context context, Intent intent){
		Log.d("DialyNotification", "Entro notification");
		NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(context)
			.setSmallIcon(R.drawable.notification_icon)
			.setVibrate(new long[] {0, 1000, 50, 2000})
			.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.takeme))
			.setContentTitle("Notificacion Saludable")
			.setContentText("Recuerda tomar dos litros de agua al dia");
		//Implicit intent
		Intent resultIntent = new Intent(context, MainActivity.class);
		//Add to the top of the stack
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotificationBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// An Id allows you to update the notification later on. 
		mNotificationManager.notify(001, mNotificationBuilder.build());		
	}
	
	public void setAlarm(Context context){
		// Setting the alarm. 
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DialyNotificationReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger
        Random mRandom = new Random();
        int iRandom = (mRandom.nextInt(6)+1)+10;
        //calendar.set(Calendar.HOUR_OF_DAY, iRandom);
        //calendar.set(Calendar.MINUTE, 0);
        
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 20);
        
        // Set the intervals for every day 
		alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
		        AlarmManager.INTERVAL_DAY, alarmIntent);
	}
}

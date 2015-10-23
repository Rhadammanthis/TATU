package com.cceo.miyoideal.extra;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
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

import com.cceo.miyoideal.HomeActivity;
import com.cceo.miyoideal.MainActivity;
import com.cceo.miyoideal.R;

public class DialyNotificationReceiver extends BroadcastReceiver {

	private AlarmManager alarmManager;
	private PendingIntent alarmIntent;

	@Override
	public void onReceive(Context context, Intent intent){
		Log.d("DialyNotification", "Entro notification");
		if (new API(context).shouldSendNotif() && isTimetoSendNotif()) 
		{
			String message = getRandomNotification();

			NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(
					context)
			.setSmallIcon(R.drawable.notification_icon)
			.setVibrate(new long[] { 0, 1000, 50, 2000 })
			.setSound(
					Uri.parse("android.resource://"
							+ context.getPackageName() + "/"
							+ R.raw.takeme))
							.setContentTitle("Notificacion Saludable")
							.setContentText(message);
			//Implicit intent
			Intent resultIntent = new Intent(context, HomeActivity.class);
			resultIntent.putExtra("title", "Notificacion Saludable");
			resultIntent.putExtra("message", message);
			//Add to the top of the stack
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addParentStack(MainActivity.class);
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);
			mNotificationBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// An Id allows you to update the notification later on. 
			mNotificationManager.notify(001, mNotificationBuilder.build());
		}		
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
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				60*1000, alarmIntent);
	}

	public String getRandomNotification()
	{
		List<String> strings = new ArrayList<String>();

		strings.add("Recuerda tomar dos litros de agua al dia");
		strings.add("Come frutas y verduras");
		strings.add("Camina dos kilometros al dia");
		strings.add("Consulta a tu medico");
		strings.add("Fumar es causa de cancer");
		strings.add("No comas gluten");
		strings.add("El verde es vida");

		Collections.shuffle(strings);

		Random rad = new Random();
		rad.nextInt(strings.size() - 1);

		return strings.get(rad.nextInt(strings.size() - 1));
	}

	private boolean isTimetoSendNotif()
	{
		Calendar c = Calendar.getInstance();
		if(c.get(Calendar.HOUR_OF_DAY) == 13 && (c.get(Calendar.MINUTE) == 21))
			return true;
		else
			return false;

	}
}

package com.cceo.miyoideal.extra;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.cceo.DAO.DAO_Dieta;
import com.cceo.DB.SQLiteControl;
import com.cceo.DTO.DTO_Dieta;
import com.cceo.miyoideal.HomeActivity;
import com.cceo.miyoideal.MainActivity;
import com.cceo.miyoideal.R;

public class DietaCompleteNotification extends BroadcastReceiver {

	private AlarmManager alarmManager;
	private PendingIntent alarmIntent;
	private Context con;
	private int duracion;
	private String init_day;


	@Override
	public void onReceive(Context context, Intent intent){
		Log.d("FinishedDietaNotif", "Entro notification terminar dieta");

		if (new API(context).IsDietaSet()) 
		{
			//Gather selected dieta info
			DTO_Dieta dieta = new DAO_Dieta(context).getDieta(new API(context).getID_Dieta());
			init_day = new API(context).getDia();
			duracion = Integer.valueOf(dieta.getDuracion());

			Log.d("service", String.valueOf("Brodcast recieved"));
			Log.d("service", init_day);
			Log.d("service", "Get Dia " + new API(context).getDia());

			//generates date object with the date the dieta was originally set
			Calendar c = Calendar.getInstance();
			SimpleDateFormat dfDate = new SimpleDateFormat("dd-MMM-yy");

			//int dia_inicial = parseDay(init_day);
			//int dia_actual = Integer.parseInt((String) android.text.format.DateFormat.format("dd", c.getTime()));

			java.util.Date d = null;
			java.util.Date d1 = null;
			Calendar cal = Calendar.getInstance();
			try {
				d = dfDate.parse(new API(context).getDia());
				d1 = dfDate.parse(dfDate.format(cal.getTime()));//Returns 15/10/2012
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}

			int diffInDays = (int) ((d1.getTime() - d.getTime())/ (1000 * 60 * 60 * 24));

			Log.d("service", "checking if should terminate");	
			Log.d("service", "Duracion = " + String.valueOf(duracion));
			Log.d("service", "Dia actual = " + String.valueOf(d));
			Log.d("service", "Dia inicial = " + String.valueOf(d1));
			Log.d("service", "(dia_actual - dia_inicial) >= (duracion - 1) => " + String.valueOf((diffInDays) >= (duracion - 1)));

			if((diffInDays) >= (duracion - 1))
			{
				Calendar currentDate = Calendar.getInstance();
				Calendar fixedDate = Calendar.getInstance();
				fixedDate.set(Calendar.HOUR_OF_DAY, 9);
				int hour = c.get(Calendar.HOUR_OF_DAY);

				if(currentDate.get(Calendar.HOUR_OF_DAY) >= fixedDate.get(Calendar.HOUR_OF_DAY) && new API(context).IsDietaSet())
				{
					Log.d("service", "should terminate");

					//Set flag in the Control DB to validate that a dieta program has been finished
					ContentValues cv = new ContentValues();
					cv.put("dietaTerminada", "1"); 
					SQLiteControl db = new SQLiteControl(context);
					db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
					db.close();

					NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(context)
					.setSmallIcon(R.drawable.notification_icon)
					.setContentTitle("Yo Ideal")
					.setContentText("Has completado la dieta!");
					//Implicit intent
					Intent resultIntent = new Intent(context, MainActivity.class);
					//resultIntent.putExtra("response", true);
					//Add to the top of the stack
					TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
					stackBuilder.addParentStack(MainActivity.class);
					stackBuilder.addNextIntent(resultIntent);
					PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
					mNotificationBuilder.setContentIntent(resultPendingIntent);
					NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
					// An Id allows you to update the notification later on. 
					mNotificationManager.notify(002, mNotificationBuilder.build());	

				}
			}			
		} 
	}

	public void setAlarm(Context context, String duracion, String init_day){
		con = context;
		this.duracion = Integer.valueOf(duracion);
		this.init_day = init_day;
		// Setting the alarm. 
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, DietaCompleteNotification.class);
		alarmIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

		// Set the intervals for every 5 hours
		Calendar cal = Calendar.getInstance();
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 60*1000, alarmIntent);
	}

	private int parseDay(String date)
	{
		int day = 0;
		String dia;

		char[] dia_array = date.toCharArray();
		dia = String.valueOf(dia_array[0]);
		dia += String.valueOf(dia_array[0+1]);

		day = Integer.parseInt(dia);

		return day;
	}
}

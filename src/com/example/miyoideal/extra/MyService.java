package com.example.miyoideal.extra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.DAO.DAO_Dieta;
import com.example.DB.SQLiteControl;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service{
	private Looper mServiceLooper;
	  private ServiceHandler mServiceHandler;
	  private String init_day;
	  private int duracion;
	  private boolean shouldLog = false;
	  private Context con;
	  public static final String BROADCAST_ACTION = "com.example.miyoideal.extra.displayevent";
	  private Intent intent;
	  int counter = 0;
	  private final Handler handler = new Handler();

	  // Handler that receives messages from the thread
	  private final class ServiceHandler extends Handler {
	      public ServiceHandler(Looper looper) {
	          //.super(looper);
	      }
	      @Override
	      public void handleMessage(Message msg) {
	          // Normally we would do some work here, like download a file.
	          // For our sample, we just sleep for 5 seconds.
	    	  if(shouldLog)
	    	  {
	    		  new API(con).clearDieta();
	    		  
	    		 /* Log.d("service", String.valueOf(msg.arg1));
	    		  intent.putExtra("exit", String.valueOf(1));
		    		intent.setAction("com.example.miyoideal.extra");
		    		sendBroadcast(intent);*/
		    		
		    		
		    		//stopSelf();
		    		
		    		
	    		  Log.d("service-1", String.valueOf(duracion));
	    	  
		    	  //generates date object with the date the dieta was originally set
		    	  Calendar c = Calendar.getInstance();
			  	  SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
			  	
			  	  int dia_inicial = parseDay(init_day);
			  	  int dia_actual = Integer.parseInt((String) android.text.format.DateFormat.format("dd", c.getTime()));
			  
			  	 // Log.d("dif", String.valueOf(dia_actual - dia_inicial));			  	  
			  	  
			  	  if((dia_actual - dia_inicial) == (duracion - 1))
			  	  {
			  		int hour = c.get(Calendar.HOUR_OF_DAY);
			  		if(hour >= 9)
			  		{

			  			//clean set dieta information
			  			new API(con).clearDieta();
			  			
			  			//prompt "share" dialog
			  			intent.putExtra("exit", String.valueOf(1));
			    		intent.setAction("com.example.miyoideal.extra");
			    		
			    		sendBroadcast(intent);
			  					  			
			    		stopSelf();
			  		}
			  	  }

	    	  }
	          // Stop the service using the startId, so that we don't stop
	          // the service in the middle of handling another job
	    	  stopSelf(msg.arg1);
	      }
	  }

	  @Override
	  public void onCreate() {
	    // Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
	      
	      HandlerThread thread = new HandlerThread("ServiceStartArguments",
		            Process.THREAD_PRIORITY_BACKGROUND);
		    //thread.start();
		    
		   intent = new Intent("com.example.miyoideal.extra");

		    // Get the HandlerThread's Looper and use it for our Handler
		    //mServiceLooper = thread.getLooper();
		    mServiceHandler = new ServiceHandler(mServiceLooper);
	  }

	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
	      Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

	      // For each start request, send a message to start a job and deliver the
	      // start ID so we know which request we're stopping when we finish the job
	      Message msg = mServiceHandler.obtainMessage();
	      msg.arg1 = startId;
	      mServiceHandler.sendMessage(msg);
	      
	     // handler.removeCallbacks(sendUpdatesToUI);
	     //   handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
	        
	        init_day = intent.getStringExtra("dia_inicial");
		      duracion = Integer.parseInt(intent.getStringExtra("duracion"));
	      
	      
	      con = this;
	      shouldLog = true;
	      	

	      // If we get killed, after returning from here, restart
	      return START_STICKY;
	  }

	  @Override
	  public IBinder onBind(Intent intent) {
	      // We don't provide binding, so return null
	      return null;
	  }

	  @Override
	  public void onDestroy() {
	    Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
	    //handler.removeCallbacks(sendUpdatesToUI);		
		super.onDestroy();
	  }
	  
	  /*private Runnable sendUpdatesToUI = new Runnable() {
	    	public void run() {
	    		DisplayLoggingInfo();    		
	    	    handler.postDelayed(this, 1000); // 10 seconds
	    	}
	    };
	    
	    private void DisplayLoggingInfo() {

	    	
	    	intent.putExtra("exit", String.valueOf(1));
    		intent.setAction("com.example.miyoideal.extra");
	    	sendBroadcast(intent);
	    }
	  */
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

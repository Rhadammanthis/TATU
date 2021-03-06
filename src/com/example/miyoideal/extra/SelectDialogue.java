package com.example.miyoideal.extra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.DAO.DAO_Dieta;
import com.example.DAO.DAO_Ejercicio;
import com.example.DB.SQLiteControl;
import com.example.DB.SQLiteEjercicioDB;
import com.example.DTO.DTO_Dieta;
import com.example.DTO.DTO_Ejercicio;
import com.example.miyoideal.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class SelectDialogue extends DialogFragment {
	
	private Context con;
	private String id_Dieta;
	private boolean accept;
	private broadcastReceiver messageReceiver = new broadcastReceiver();
	private AlarmManager alarm;
	private Intent intent;
	private PendingIntent pintent;
	private String todaysDate;
	
	public SelectDialogue(Context con, String id_Dieta, boolean acc)
	{
		this.con = con;
		this.id_Dieta = id_Dieta;
		this.accept = acc;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	con.registerReceiver(messageReceiver, new IntentFilter("com.example.miyoideal.extra"));
    	
        // Use the Builder class for convenient dialog construction
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    	if(accept)
        {
            builder.setMessage("�Deseas tomar esta dieta?")
                   .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	   
                    	   //Update Control DB (so we can know a dieta has been selected)
                    	   Calendar c = Calendar.getInstance();
                   			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
                   			todaysDate = df.format(c.getTime());
                    	   ContentValues cv = new ContentValues();
                    	   cv.put("id_dieta", id_Dieta); //These Fields should be your String values of actual column names
                    	   cv.put("dia",todaysDate);
                    	   
                    	   SQLiteControl db = new SQLiteControl(con);
                    	   db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
                    	   db.close();
                    	   
                    	   DTO_Dieta dieta = new DAO_Dieta(con).getDieta(new API(con).getID_Dieta());
                    	   
                    	   String actividad = "";
                    	   for(int i = 0; i < Integer.parseInt(dieta.getDuracion()); i++)
                    		   actividad += "0";
                    	   
                    	   /*Add new insert to DB Ejercicio. This helps us keep track of the days in
                    	    *which physical activity was performed when undertaking the diet program*/
                    	   DTO_Ejercicio ejercicio = new DTO_Ejercicio(id_Dieta, todaysDate, actividad);
                    	   new DAO_Ejercicio(con).newEjercicio(ejercicio);                  	   
                    	   
                    	   //service
                    	   Calendar cal = Calendar.getInstance();
                    	   
                    	   String tex = new API(con).getDia();

                    	   intent = new Intent(con, MyService.class);
                    	   intent.putExtra("dia_inicial", tex);
                    	   
                    	   
                    	   intent.putExtra("duracion", dieta.getDuracion());
                    	   
                    	   
                    	   pintent = PendingIntent.getService(con, 1234, intent, 0);

                    	   alarm = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);                   	   
                    	   
                    	   // schedule for every 5 hours
                    	   alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 20*1000, pintent); 
                    	   
                    	   
                       }
                   })
                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           // User cancelled the dialog
                       }
                   });
        }
        else
        {
            builder.setMessage("Ya tienes un programa de dieta �Deseas cambiar?")
                   .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	   Calendar c = Calendar.getInstance();
                    	   SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
                    	   todaysDate = df.format(c.getTime());
                    	   ContentValues cv = new ContentValues();
                    	   cv.put("id_dieta", id_Dieta); //These Fields should be your String values of actual column names
                    	   cv.put("dia",todaysDate);
                    	   
                    	   SQLiteControl db = new SQLiteControl(con);
                    	   db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
                    	   db.close();
                    	   
                    	   /*Add new insert to DB Ejercicio. This helps us keep track of the days in
                    	    *which physical activity was performed when undertaking the diet program*/
                    	   DTO_Ejercicio ejercicio = new DTO_Ejercicio(id_Dieta, todaysDate, "0");
                    	   new DAO_Ejercicio(con).newEjercicio(ejercicio);
                    	   
                    	   //To get the time reference when the script is initially run
                    	   Calendar cal = Calendar.getInstance();

                    	   Intent intent = new Intent(con, MyService.class);
                    	   
                    	   //to gather extras
                    	   String dia = new API(con).getDia();
                    	   intent.putExtra("dia_inicial", new API(con).getDia());
                    	   
                    	   DTO_Dieta dieta = new DAO_Dieta(con).getDieta(new API(con).getID_Dieta());
                    	   intent.putExtra("duracion", dieta.getDuracion());
                    	   
                    	   pintent = PendingIntent.getService(con, 1234, intent, 0);

                    	   alarm = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
                    	   
                    	   // schedule for every 5 hours
                    	   alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 18000*1000, pintent); 
                    	   
                    	   
                       }
                   })
                   .setNegativeButton("No", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                           // User cancelled the dialog
                       }
                   });
        }
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
    private class broadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent inten) {
        	//do something
        	
        	Log.d("service", "stopping...");
        	
        	if(intent!= null && context!=null)
        	{
        		String exit = inten.getStringExtra("exit"); 
            	if(exit.equals("1"))
            	{
            		Log.d("service", "stopping...");
            		Toast.makeText(con, "should stop", Toast.LENGTH_SHORT).show();
            		alarm.cancel(pintent);
            		con.stopService(intent);
            		//Intent inte = new Intent(con, MyService.class); 
            		Log.d("tag", "exit");

             	   
	             	Dialog dialog = new Dialog(con);
	
	                dialog.setContentView(R.layout.baseline_share);
	                dialog.setTitle("Compartir");
	
	                Button save=(Button)dialog.findViewById(R.id.buttonGuardar);
	                dialog.show();
	                
	                intent = null;
	                pintent = null;
	             	Log.d("tag", "exit2");
            	}
        	}
        	
        }
    }; 
    
    private void initEjercicioDB()
    {
    	
    }
    
   /* public interface SelectDialogueListener
    {
    	public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    
    SelectDialogue mListener;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SelectDialogue) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }*/
    
}

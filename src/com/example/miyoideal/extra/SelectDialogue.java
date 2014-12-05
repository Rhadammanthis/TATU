package com.example.miyoideal.extra;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.example.DB.SQLiteControl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class SelectDialogue extends DialogFragment {
	
	private Context con;
	private String id_Dieta;
	private boolean accept;
	
	public SelectDialogue(Context con, String id_Dieta, boolean acc)
	{
		this.con = con;
		this.id_Dieta = id_Dieta;
		this.accept = acc;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    	if(accept)
        {
            builder.setMessage("¿Deseas tomar esta dieta?")
                   .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	   Calendar c = Calendar.getInstance();
                   			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
                   			String todayDate = df.format(c.getTime());
                    	   ContentValues cv = new ContentValues();
                    	   cv.put("id_dieta", id_Dieta); //These Fields should be your String values of actual column names
                    	   cv.put("dia",todayDate);
                    	   
                    	   SQLiteControl db = new SQLiteControl(con);
                    	   db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
                    	   
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
            builder.setMessage("Ya tienes un programa de dieta ¿Deseas cambiar?")
                   .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int id) {
                    	   Calendar c = Calendar.getInstance();
                    	   SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
                    	   String todayDate = df.format(c.getTime());
                    	   ContentValues cv = new ContentValues();
                    	   cv.put("id_dieta", id_Dieta); //These Fields should be your String values of actual column names
                    	   cv.put("dia",todayDate);
                    	   
                    	   SQLiteControl db = new SQLiteControl(con);
                    	   db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
                    	   
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

package com.cceo.miyoideal.extra;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.cceo.DAO.DAO_Componente;
import com.cceo.DAO.DAO_Dieta;
import com.cceo.DAO.DAO_Ejercicio;
import com.cceo.DB.SQLiteControl;
import com.cceo.DTO.DTO_Dieta;
import com.cceo.DTO.DTO_Ejercicio;
import com.cceo.miyoideal.DietaActivity;
import com.cceo.miyoideal.HomeActivity;
import com.cceo.miyoideal.MiPerfilActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class CancelDietaDialog extends DialogFragment{

	private Context con;
	private String id_Dieta;
	private boolean accept;
	//private broadcastReceiver messageReceiver = new broadcastReceiver();
	private AlarmManager alarm;
	private Intent intent;
	private PendingIntent pintent;
	private String todaysDate;

	public CancelDietaDialog(Context con)
	{
		this.con = con;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		//con.registerReceiver(messageReceiver, new IntentFilter("com.example.miyoideal.extra"));

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMessage("¿Estás seguro que deseas cancelar la dieta actual?")
		.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
				//reset all dieta checkboxes to not selected
				new DAO_Componente(con).setAllToNo(new API(con).getID_Dieta());

				//clears dieta information;
				new API(con).clearDieta();
				
				Intent intent = new Intent(con, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// User cancelled the dialog
			}
		});

		return builder.create();
	}

}

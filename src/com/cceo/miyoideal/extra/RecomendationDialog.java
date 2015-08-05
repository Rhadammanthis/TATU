package com.cceo.miyoideal.extra;

import java.util.zip.Inflater;

import com.cceo.miyoideal.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class RecomendationDialog extends DialogFragment{
	
	public Context con;
	
	public RecomendationDialog(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.recomendation_fragment, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(con);
		CharSequence[] items = new CharSequence[1];
		items[0] = "Hola";
		builder.setMessage("Ahora prueba una de estas!")
		.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
		.setTitle("Seleccion")
		.setView(v);
		
		return builder.create();
	}
}

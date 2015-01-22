package com.example.DAO;

import java.util.ArrayList;
import java.util.List;

import com.example.DB.SQLiteComponenteDB;
import com.example.DB.SQLiteControl;
import com.example.DB.SQLiteEjercicioDB;
import com.example.DTO.DTO_Componente;
import com.example.DTO.DTO_Ejercicio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DAO_Ejercicio {
	
	private Context con;
	
	public DAO_Ejercicio(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}
	
	public void newEjercicio(DTO_Ejercicio ejercicio)
	{
		SQLiteEjercicioDB db = new SQLiteEjercicioDB(con);
    	int id = 0;
    	
    	db.getReadableDatabase();
		String query = "Select * FROM " + "ejercicio";
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		while(cursor.moveToNext())
		{
			id++;
		}
    	
		db.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id_ejercicio", String.valueOf(id));
		values.put("id_dieta", ejercicio.getIdDIeta());
		values.put("fecha", ejercicio.getFecha());
		values.put("diasActividad", ejercicio.getDiasActividad());
		db.getWritableDatabase().insert("ejercicio", null, values);
		
		db.close();	
	}
	
	public DTO_Ejercicio getEjercicio(String idDieta, String fecha)
	{
		DTO_Ejercicio ejercicio = new DTO_Ejercicio();
		SQLiteEjercicioDB db = new SQLiteEjercicioDB(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "ejercicio" + " WHERE " + "id_dieta" + " =  \"" + idDieta 
				+ "\" AND fecha =" + " \"" + fecha + "\"";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			ejercicio.setIdEjercicio(cursor.getString(0));
			ejercicio.setIdDIeta(cursor.getString(1));
			ejercicio.setFecha(cursor.getString(2));
			ejercicio.setDiasActividad(cursor.getString(3));			
		}	
		
		db.close();
		
		return ejercicio;
	}
	
	public void updateEjercicio(DTO_Ejercicio ejercicio)
	{		
		ContentValues cv = new ContentValues();
 	   	cv.put("diasActividad", ejercicio.getDiasActividad());
 	   
 	   SQLiteEjercicioDB db = new SQLiteEjercicioDB(con);
 	   	db.getWritableDatabase().update("ejercicio", cv, "id_ejercicio "+"="+ejercicio.getIdEjercicio(), null);
 	   	db.close();
	}

}

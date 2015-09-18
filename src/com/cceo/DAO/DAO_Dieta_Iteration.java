package com.cceo.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.cceo.DB.SQLiteDietaDB;
import com.cceo.DB.SQLiteDietaIteration;
import com.cceo.DB.SQLiteEjercicioDB;
import com.cceo.DTO.DTO_Dieta;
import com.cceo.DTO.DTO_DietaIteration;
import com.cceo.DTO.DTO_Ejercicio;

public class DAO_Dieta_Iteration {
	
	private Context con;
	
	public DAO_Dieta_Iteration(Context context)
	{
		con = context;
	}
	
	public void newDietaIteration(DTO_DietaIteration dieta_it)
	{
		SQLiteDietaIteration db = new SQLiteDietaIteration(con);
		db.getWritableDatabase();
		
		Log.d("db", "Dieta " + db.getDatabaseName());

		ContentValues values = new ContentValues();
		values.put("id_dieta_iteration", dieta_it.getId_dieta_iteration());
		values.put("id_dieta", dieta_it.getId_dieta());
		values.put("actividad", dieta_it.getActividad());

		db.getWritableDatabase().insert("dieta_iteration", null, values);
		
		db.close();
	}
	 	
	public DTO_DietaIteration getDietaIteration(String id)
	{
		DTO_DietaIteration dieta = null;
		
		SQLiteDietaIteration db = new SQLiteDietaIteration(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "dieta_iteration WHERE " + "id_dieta_iteration " + " =  \"" + id + "\"";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			dieta = new DTO_DietaIteration(cursor.getString(0),cursor.getString(1),cursor.getString(2));
		}
		
		db.close();
		return dieta;
	}
	
	public void updateEjercicio(DTO_DietaIteration dIt)
	{		
		ContentValues cv = new ContentValues();
 	   	cv.put("actividad", dIt.getActividad());
 	   
 	   SQLiteDietaIteration db = new SQLiteDietaIteration(con);
 	   	db.getWritableDatabase().update("dieta_iteration", cv, "id_dieta_iteration "+"="+dIt.getId_dieta_iteration(), null);
 	   	db.close();
	}

}
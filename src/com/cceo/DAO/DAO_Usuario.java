package com.cceo.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cceo.DB.SQLiteControl;
import com.cceo.DB.SQLiteUserDB;
import com.cceo.DTO.DTO_Usuario;

public class DAO_Usuario {
	
	private Context con;
	
	public DAO_Usuario(Context con)
	{
		this.con = con;
	}
	
	public DTO_Usuario getUsuario()
	{
		DTO_Usuario usuario = null;
		
		SQLiteUserDB db = new SQLiteUserDB(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "usuario";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			usuario = new DTO_Usuario(cursor.getString(0), cursor.getString(1), 
					cursor.getString(2), cursor.getString(3), cursor.getString(4), 
					cursor.getString(5), cursor.getString(6), cursor.getString(7));
		
		}
		
		return usuario;
	}
	
	public void updateNivel(String nivel)
	{
		ContentValues cv = new ContentValues();
		cv.put("nivel", nivel);

		SQLiteUserDB db = new SQLiteUserDB(con);
		db.getWritableDatabase().update("usuario", cv, "id_usuario "+"="+1, null);
		db.close();
	}
	
	public void updatePesoActual(String pesoActual)
	{
		ContentValues cv = new ContentValues();
		cv.put("peso", pesoActual);

		SQLiteUserDB db = new SQLiteUserDB(con);
		db.getWritableDatabase().update("usuario", cv, "id_usuario "+"="+1, null);
		db.close();
	}
	
	public void updateTalla(String talla)
	{
		ContentValues cv = new ContentValues();
		cv.put("talla", talla);

		SQLiteUserDB db = new SQLiteUserDB(con);
		db.getWritableDatabase().update("usuario", cv, "id_usuario "+"="+1, null);
		db.close();
	}

}

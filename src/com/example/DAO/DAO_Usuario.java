package com.example.DAO;

import android.content.Context;
import android.database.Cursor;

import com.example.DB.SQLiteUserDB;
import com.example.DTO.DTO_Usuario;

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
					cursor.getString(5), cursor.getString(6));
		
		}
		
		return usuario;
	}

}

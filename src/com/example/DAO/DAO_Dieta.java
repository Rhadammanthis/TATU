package com.example.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.example.DB.SQLiteDietaDB;
import com.example.DTO.DTO_Dieta;

public class DAO_Dieta {
	
	private Context con;
	
	public DAO_Dieta(Context context)
	{
		con = context;
	}
	
	public DTO_Dieta getDieta(String id)
	{
		DTO_Dieta dieta = new DTO_Dieta();
		
		SQLiteDietaDB db = new SQLiteDietaDB(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "dieta WHERE " + "id_dieta" + " =  \"" + id + "\"";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			dieta.setId_dieta(cursor.getString(0));
			dieta.setNombre(cursor.getString(1));
			dieta.setTipo(cursor.getString(2));
			dieta.setDuracion(cursor.getString(3));			
		}
		
		return dieta;
	}
	
	public List<DTO_Dieta> getAllDieta()
	{
		List<DTO_Dieta> list_dieta = new ArrayList<DTO_Dieta>();
		SQLiteDietaDB db = new SQLiteDietaDB(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "dieta";// + " WHERE " + "id_usuario" + " =  \"" + "1" + "\"";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				DTO_Dieta temp = new DTO_Dieta();
				temp.setId_dieta(cursor.getString(0));
				temp.setNombre(cursor.getString(1));
				temp.setTipo(cursor.getString(2));
				temp.setDuracion(cursor.getString(3));
				
				list_dieta.add(temp);
			}
			while((cursor.moveToNext()));			
		}	
		
		db.close();
		return list_dieta;
	}

}

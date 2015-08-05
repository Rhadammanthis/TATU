package com.cceo.DAO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.cceo.DB.SQLiteComponenteDB;
import com.cceo.DB.SQLiteDietaDB;
import com.cceo.DTO.DTO_Componente;

public class DAO_Componente {
	
	private Context con;
	
	public DAO_Componente(Context context)
	{
		con = context;
	}
	
	public List<DTO_Componente> getAllComponente(String id_dieta, String dia)
	{
		List<DTO_Componente> list_dieta = new ArrayList<DTO_Componente>();
		SQLiteComponenteDB db = new SQLiteComponenteDB(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "componente" + " WHERE " + "id_dieta" + " =  \"" + id_dieta 
				+ "\" AND dia =" + " \"" + dia + "\"";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				DTO_Componente temp = new DTO_Componente();
				temp.setId_componente(cursor.getString(0));
				temp.setId_dieta(cursor.getString(1));
				temp.setDia(cursor.getString(2));
				temp.setAlimento(cursor.getString(3));
				temp.setActivo(cursor.getString(4));
				temp.setDescripcion(cursor.getString(5));
				
				list_dieta.add(temp);
			}
			while((cursor.moveToNext()));			
		}	
		
		db.close();
		return list_dieta;
	}
	
	public void updateComponenteYes(DTO_Componente temp)
	{
		SQLiteComponenteDB db = new SQLiteComponenteDB(con);
		db.getReadableDatabase();
		
		ContentValues values=new ContentValues();
		values.put("activo", "yes");
		
		int id = db.getWritableDatabase().update("componente", values, "id_componente =  \"" + temp.getId_componente() +"\"", null);
		
		db.close();
	}
	
	public void updateComponenteNo(DTO_Componente temp)
	{
		SQLiteComponenteDB db = new SQLiteComponenteDB(con);
		db.getReadableDatabase();
		
		ContentValues values=new ContentValues();
		values.put("activo", "no");
		
		int id = db.getWritableDatabase().update("componente", values, "id_componente =  \"" + temp.getId_componente() +"\"", null);
		
		db.close();
	}
}

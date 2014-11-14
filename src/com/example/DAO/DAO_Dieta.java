package com.example.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.DTO.*;
import com.example.miyoideal.HomeActivity;
import com.example.miyoideal.MainActivity;
import com.example.miyoideal.widget.SQLiteDietaDB;

public class DAO_Dieta {
	
	private Context con;
	
	public DAO_Dieta(Context context)
	{
		con = context;
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

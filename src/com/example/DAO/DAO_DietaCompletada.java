package com.example.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.DB.SQLiteDietaCompletadaDB;
import com.example.DTO.DTO_DietaCompletada;
import com.example.DTO.DTO_Usuario;

public class DAO_DietaCompletada {
	
	private Context con;
	
	public DAO_DietaCompletada(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}
	
	public void newDietaCompletada(DTO_DietaCompletada temp)
	{
		SQLiteDietaCompletadaDB db = new SQLiteDietaCompletadaDB(con);
    	int id = 0;
    	
    	db.getReadableDatabase();
		String query = "Select * FROM " + "dieta_completada";
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		while(cursor.moveToNext())
		{
			id++;
		}
    	
		db.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id_dieta_completada", String.valueOf(id));
		values.put("id_dieta", temp.getId_dieta());
		values.put("peso", temp.getPeso());
		values.put("talla", temp.getTalla());
		values.put("fecha", temp.getFecha());
		db.getWritableDatabase().insert("dieta_completada", null, values);
		
		db.close();	
	}
	
	//return last five insert in 'dieta completada' table. It includes the usuer's data as the first item in the list
	public List<DTO_DietaCompletada> getLastFiveDietaCompleta()
	{
		List<DTO_DietaCompletada> list = new ArrayList<DTO_DietaCompletada>();
		
		SQLiteDietaCompletadaDB db = new SQLiteDietaCompletadaDB(con);
		db.getReadableDatabase();

		String query = "select * from (select * from dieta_completada order by id_dieta_completada DESC limit 4) order by id_dieta_completada ASC;";
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		Log.d("grafica", "retrieve usuario");
		
		//first object is always the user's data
		DTO_Usuario datosUsuario = new DAO_Usuario(con).getUsuario();
		DTO_DietaCompletada first = new DTO_DietaCompletada("-", 
				"-", datosUsuario.getPeso(), datosUsuario.getTalla(), "-");
		list.add(first);
		
		if(cursor.moveToFirst())
		{
			do
			{
				DTO_DietaCompletada temp = new DTO_DietaCompletada();
				temp.setId_dieta_completada(cursor.getString(0));
				temp.setId_dieta(cursor.getString(1));
				temp.setPeso(cursor.getString(2));
				temp.setTalla(cursor.getString(3));
				temp.setFecha(cursor.getString(4));
				
				list.add(temp);
			}
			while((cursor.moveToNext() && list.size() < 5));			
		}	
		
		db.close();
		Log.d("grafica", "Ya tenemos lista con " + String.valueOf(list.size()) + " items");
		return list;
	}

}

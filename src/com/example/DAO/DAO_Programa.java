package com.example.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.example.DB.SQLiteProgramaDB;
import com.example.DTO.DTO_Programa;

public class DAO_Programa {
	
	private Context con;
	
	public DAO_Programa(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}
	
	public List<DTO_Programa> getAllPrograma(String nivel)
	{
		List<DTO_Programa> list = new ArrayList<DTO_Programa>();
		SQLiteProgramaDB db = new SQLiteProgramaDB(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "programa" + " WHERE " + "nivel" + " =  \"" + nivel + "\"";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				DTO_Programa temp = new DTO_Programa();
				temp.setId_programa(cursor.getString(0));
				temp.setNivel(cursor.getString(1));
				temp.setTitulo(cursor.getString(2));
				temp.setDescripcion(cursor.getString(3));
				temp.setHora(cursor.getString(4));
				
				list.add(temp);
			}
			while((cursor.moveToNext()));			
		}	
		
		db.close();
		
		
		return list;
	}

}

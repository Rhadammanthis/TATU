package com.cceo.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.cceo.DB.SQLiteDietaDB;
import com.cceo.DB.SQLiteRecommendation;
import com.cceo.DTO.DTO_Dieta;
import com.cceo.DTO.DTO_Recommendation;

public class DAO_Recommendation {
	
	private Context con;
	
	public DAO_Recommendation(Context context)
	{
		con = context;
	}
	 	
	public DTO_Recommendation getRecommendation(String key)
	{
		DTO_Recommendation dieta = null;
		
		SQLiteRecommendation db = new SQLiteRecommendation(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "recommendation WHERE " + "id_dieta" + " =  \"" + key + "\"";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			dieta = new DTO_Recommendation(cursor.getString(0), cursor.getString(1), cursor.getString(2), 
					cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));

		}
		
		return dieta;
	}

}
package com.example.miyoideal.extra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.DB.SQLiteControl;

public class API {
	
	private Context con;
	private Cursor cursor;
	private SQLiteControl db;
	
	public API(Context con)
	{
		this.con = con;
		db = new SQLiteControl(con);
		String query = "Select * FROM " + "control" + " WHERE " + "id_control" + " =  \"" + "1" + "\"";
		cursor = db.getReadableDatabase().rawQuery(query, null);
	}
	
	public void clearDieta()
	{
		ContentValues cv = new ContentValues();
		cv.put("id_dieta", 0); //These Fields should be your String values of actual column names
		cv.put("dia","");
 	   
		SQLiteControl db = new SQLiteControl(con);
		db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
		db.close();
	}
	
	public boolean IsDietaSet()
	{ 		
		try
		{
			if(cursor.moveToFirst())
			{
				if(!cursor.getString(1).equals("0"))
				{
					db.close();
					return true;
				}
				else
				{
					db.close();
					return false;
				}
			}
		}
		catch(Exception ex)
		{
			db.close();
			return false;
		}
		//in case everything fails
		db.close();
		return false;
	}
	
	public String getID_Dieta()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				return cursor.getString(1);
			}
			else
			{
				return null;
			}
		}
		catch(Exception ex)
		{
			db.close();
			return null;
		}
	}	

	public String getDia()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				return cursor.getString(2);
			}
			else
			{
				return null;
			}
		}
		catch(Exception ex)
		{
			db.close();
			return null;
		}
	}
	
	public String getPesoIdeal()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				Log.d("chino", "0 " + cursor.getString(0));
				Log.d("chino", "1 " + cursor.getString(1));
				Log.d("chino", "2 " + cursor.getString(2));
				Log.d("chino", "3 " + cursor.getString(3));
				return cursor.getString(3);
			}
			else
			{
				return null;
			}
		}
		catch(Exception ex)
		{
			db.close();
			return null;
		}
	}
}

package com.example.miyoideal.extra;

import android.content.Context;
import android.database.Cursor;

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
}

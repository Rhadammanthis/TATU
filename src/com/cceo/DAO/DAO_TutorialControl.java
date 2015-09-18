package com.cceo.DAO;

import com.cceo.DB.SQLiteControl;
import com.cceo.DB.SQLiteTutorialControl;

import android.content.Context;
import android.database.Cursor;

public class DAO_TutorialControl 
{

	private Context con;
	private Cursor cursor;
	private SQLiteTutorialControl db;
	
	public DAO_TutorialControl(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
		db = new SQLiteTutorialControl(con);
		String query = "Select * FROM " + "tutorialcontrol" + " WHERE " + "id_tutorialcontrol" + " =  \"" + "1" + "\"";
		cursor = db.getReadableDatabase().rawQuery(query, null);
	}
	
	public boolean tutorialShown()
	{ 		
		try
		{
			if(cursor.moveToFirst())
			{
				if(cursor.getString(1).equals("0"))
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
	
	public String buttonTutorialShown()
	{ 		
		try
		{
			if(cursor.moveToFirst())
			{
				if(cursor.getString(2).equals("0"))
				{
					db.close();
					return cursor.getString(2);
				}
				else
				{
					db.close();
					return cursor.getString(2);
				}
			}
		}
		catch(Exception ex)
		{
			db.close();
			return "";
		}
		//in case everything fails
		db.close();
		return "";
	}
}

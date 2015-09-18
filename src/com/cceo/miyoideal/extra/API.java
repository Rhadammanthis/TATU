package com.cceo.miyoideal.extra;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

import com.cceo.DB.SQLiteControl;

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
	
	//no dieta selected
	public void clearDieta()
	{
		ContentValues cv = new ContentValues();
		cv.put("id_dieta", 0); //These Fields should be your String values of actual column names
		cv.put("dia","");
 	   
		SQLiteControl db = new SQLiteControl(con);
		db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
		db.close();
	}
	
	public void setMotivacion(String motivacion)
	{
		ContentValues cv = new ContentValues();
		cv.put("motivacion", motivacion);
 	   
		SQLiteControl db = new SQLiteControl(con);
		db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
		db.close();
	}
	
	public void setNotifTrue()
	{
		ContentValues cv = new ContentValues();
		cv.put("notif", "1");
 	   
		SQLiteControl db = new SQLiteControl(con);
		db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
		db.close();
	}
	
	public void setNotifFalse()
	{
		ContentValues cv = new ContentValues();
		cv.put("notif", "0");
 	   
		SQLiteControl db = new SQLiteControl(con);
		db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
		db.close();
	}
	
	public void incDietaIteration()
	{
		String dIterator = this.getDietaIteration();
		int inc = Integer.valueOf(dIterator) + 1;
		
		ContentValues cv = new ContentValues();
		cv.put("dieta_iteration", String.valueOf(inc));
 	   
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
	
	public boolean HasDietaBeenCompleted()
	{ 		
		try
		{
			if(cursor.moveToFirst())
			{
				if(Integer.valueOf(cursor.getString(4)) == 0)
				{
					db.close();
					return false;
				}
				else
				{
					db.close();
					return true;
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
	
	public String getStyle()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				return cursor.getString(5);
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
	
	public String getMotivacion()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				return cursor.getString(6);
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
	
	public Boolean shouldSendNotif()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				String res = cursor.getString(7);
				
				//Log.d("ale", "Notif: " + res);
				
				if(res.equals("0"))
					return false;
				else
					return true;
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
	
	public String getFacebookID()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				return cursor.getString(8);
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
	
	public String getFacebookName()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				return cursor.getString(9);
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
	
	public String getPesoInicial()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				return cursor.getString(10);
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
	
	public String getDietaIteration()
	{
		try
		{
			if(cursor.moveToFirst())
			{
				cursor.moveToFirst();
				return cursor.getString(11);
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
	
	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
	    int targetWidth = 400;
	    int targetHeight = 400;
	    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
	                        targetHeight,Bitmap.Config.ARGB_8888);

	    Canvas canvas = new Canvas(targetBitmap);
	    Path path = new Path();
	    path.addCircle(((float) targetWidth - 1) / 2,
	        ((float) targetHeight - 1) / 2,
	        (Math.min(((float) targetWidth), 
	        ((float) targetHeight)) / 2),
	        Path.Direction.CCW);

	    canvas.clipPath(path);
	    Bitmap sourceBitmap = scaleBitmapImage;
	    canvas.drawBitmap(sourceBitmap, 
	        new Rect(0, 0, sourceBitmap.getWidth(),
	        sourceBitmap.getHeight()), 
	        new Rect(0, 0, targetWidth, targetHeight), null);
	    return targetBitmap;
	}
}

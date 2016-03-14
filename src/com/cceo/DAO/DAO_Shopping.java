package com.cceo.DAO;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import com.cceo.DB.SQLiteComponenteDB;
import com.cceo.DB.SQLiteProgramaDB;
import com.cceo.DB.SQLiteShoppingDB;
import com.cceo.DB.SQLiteUserDB;
import com.cceo.DTO.DTO_Componente;
import com.cceo.DTO.DTO_Programa;
import com.cceo.DTO.ShoppingItem;

public class DAO_Shopping {
	
	private Context con;
	
	public DAO_Shopping(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}
	
	public ArrayList<ShoppingItem> getAllShoppingListItems(String id_dieta)
	{
		ArrayList<ShoppingItem> list = new ArrayList<ShoppingItem>();
		SQLiteShoppingDB db = new SQLiteShoppingDB(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "shopping" + " WHERE " + "id_dieta" + " =  \"" + id_dieta + "\"";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				ShoppingItem temp = new ShoppingItem();
				temp.setIDShopping(cursor.getShort(0));
				temp.setItem(cursor.getString(2));
				temp.setChecked(setChecked(cursor.getString(3)));
				
				list.add(temp);
			}
			while((cursor.moveToNext()));			
		}	
		
		db.close();
		
		
		return list;
	}
	
	public void setStatus(boolean check, int id)
	{
		SQLiteShoppingDB db = new SQLiteShoppingDB(con);
		db.getReadableDatabase();
		
		ContentValues values=new ContentValues();
		values.put("status", formatStatus(check));
		
		int id_new = db.getWritableDatabase().update("shopping", values, "id_shopping =  \"" + id +"\"", null);
		
		db.close();
	}
	
	public void resetChecks()
	{
		List<ShoppingItem> list_dieta = new ArrayList<ShoppingItem>();
		SQLiteShoppingDB db = new SQLiteShoppingDB(con);
		db.getReadableDatabase();
		
		String query = "SELECT * FROM " + "shopping";		
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				ShoppingItem temp = new ShoppingItem();
				temp.setIDShopping(cursor.getShort(0));
				temp.setItem(cursor.getString(2));
				temp.setChecked(setChecked(cursor.getString(3)));
				
				list_dieta.add(temp);
			}
			while((cursor.moveToNext()));			
		}	
		
		db.close();
		
		SQLiteShoppingDB bd = new SQLiteShoppingDB(con);
		bd.getReadableDatabase();
		
		for(ShoppingItem temp : list_dieta)
		{		
			
			ContentValues values=new ContentValues();
			values.put("status", "off");
			
			int id = bd.getWritableDatabase().update("shopping", values, "id_shopping =  \"" + temp.getIDShopping() +"\"", null);		
			
		}
		
		bd.close();
	}
	
	public boolean setChecked(String status)
	{
		if(status.equals("off"))
			return false;
		else
			return true;
	}
	
	public String formatStatus(boolean status)
	{
		if(status)
			return "on";
		else
			return "off";
	}

}

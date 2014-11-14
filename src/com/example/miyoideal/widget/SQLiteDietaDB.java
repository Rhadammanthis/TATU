package com.example.miyoideal.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDietaDB extends SQLiteOpenHelper{
	
	private static final int VERSION = 2;
	private static final String TABLE_NAME = "dieta";
	private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            "id_dieta" + " TEXT, " +
            "nombre" + " TEXT, " +
            "tipo" + " TEXT, " +
            "duracion" + " TEXT);";

	public SQLiteDietaDB(Context context) {
		super(context, TABLE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}

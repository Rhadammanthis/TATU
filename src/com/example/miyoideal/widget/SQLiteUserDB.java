package com.example.miyoideal.widget;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteUserDB extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 2;
	private static final String USER_TABLE_NAME = "usuario";
	private static final String USER_TABLE_CREATE =
            "CREATE TABLE " + USER_TABLE_NAME + " (" +
            "id_usuario" + " TEXT, " +
            "nombre" + " TEXT, " +
            "peso" + " TEXT);";

	public SQLiteUserDB(Context context) {
		super(context, USER_TABLE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(USER_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}

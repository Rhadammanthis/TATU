package com.example.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteComponenteDB extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 2;
	private static final String TABLE_NAME = "componente";
	private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            "id_componente" + " TEXT, " +
            "id_dieta" + " TEXT, " +
            "dia" + " TEXT, " +
            "alimento" + " TEXT, " +
            "descripcion" + " TEXT);";

	public SQLiteComponenteDB(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
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

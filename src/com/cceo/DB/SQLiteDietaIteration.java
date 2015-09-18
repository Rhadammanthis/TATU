package com.cceo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDietaIteration extends SQLiteOpenHelper{
	
	private static final int VERSION = 2;
	private static final String TABLE_NAME = "dieta_iteration";
	private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            "id_dieta_iteration" + " TEXT, " +
            "id_dieta" + " TEXT, " +
            "actividad" + " TEXT);";

	public SQLiteDietaIteration(Context context) {
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

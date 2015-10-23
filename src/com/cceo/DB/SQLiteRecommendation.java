package com.cceo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteRecommendation extends SQLiteOpenHelper{
	
	private static final int VERSION = 2;
	private static final String TABLE_NAME = "recommendation";
	private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            "id_dieta" + " TEXT, " +
            "recommendation_more_first" + " TEXT, " +
            "recommendation_more_second" + " TEXT, " +
            "recommendation_same_first" + " TEXT, " +
            "recommendation_same_second" + " TEXT, " +
            "recommendation_less_first" + " TEXT, " +
            "recommendation_less_second" + " TEXT);";

	public SQLiteRecommendation(Context context) {
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

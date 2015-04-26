package com.example.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteEstadisticas extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 2;
	private static final String TABLE_NAME = "estadisticas";
	private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            "id_estadisticas" + " INTEGER, " +
            "talla_maxima" + " REAL, " +
            "talla_minima" + " REAL, " +
            "talla_actual" + " REAL, " +
            "peso_maximo" + " REAL, " +
            "peso_minimo" + " REAL, " +
            "peso_actual" + " REAL, " +
            "imc" + " REAL, " +
            "record_peso_perdido" + " REAL, " +
            "peso_perdido_tratamiento" + " REAL, " +
            "dias_consecutivos_dieta" + " INTEGER, " +
            "dias_consecutivos_ejercicio" + " INTEGER);";
	
	
	public SQLiteEstadisticas(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}

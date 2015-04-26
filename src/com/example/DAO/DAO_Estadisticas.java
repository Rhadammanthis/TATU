package com.example.DAO;

import com.example.DB.SQLiteDietaCompletadaDB;
import com.example.DB.SQLiteEstadisticas;
import com.example.DTO.DTO_Estadisticas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class DAO_Estadisticas {

	private Context con;
	
	public DAO_Estadisticas(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}
	
	public void updateEstadisticas(DTO_Estadisticas estadisticas){
		final String query = "UPDATE estadisticas ";
		SQLiteEstadisticas db = new SQLiteEstadisticas(con);
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		cursor.moveToFirst();
	}
	
	public DTO_Estadisticas getEstadisticas(){
		
		final String query = "Select * from estadisticas";
		SQLiteEstadisticas db = new SQLiteEstadisticas(con);
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		cursor.moveToFirst();
		int id_estadisticas = cursor.getInt(cursor.getColumnIndex("id_estadisticas"));
		float talla_maxima = cursor.getFloat(cursor.getColumnIndex("talla_maxima"));
		float talla_minima = cursor.getFloat(cursor.getColumnIndex("talla_minima"));
		float talla_actual = cursor.getFloat(cursor.getColumnIndex("talla_actual"));
		float peso_maximo = cursor.getFloat(cursor.getColumnIndex("peso_maximo"));
		float peso_minimo = cursor.getFloat(cursor.getColumnIndex("peso_minimo"));
		float peso_actual = cursor.getFloat(cursor.getColumnIndex("peso_actual"));
		float imc = cursor.getFloat(cursor.getColumnIndex("imc"));
		float record_peso_perdido = cursor.getFloat(cursor.getColumnIndex("record_peso_perdido"));
		float peso_perdido_tratamiento = cursor.getFloat(cursor.getColumnIndex("peso_perdido_tratamiento"));
		int dias_consecutivos_dieta = cursor.getInt(cursor.getColumnIndex("dias_consecutivos_dieta"));
		int dias_consecutivos_ejercicio = cursor.getInt(cursor.getColumnIndex("dias_consecutivos_ejercicio"));
		db.close();
		return new DTO_Estadisticas(id_estadisticas, talla_maxima, talla_minima, talla_actual, peso_maximo, peso_minimo, peso_actual,
				imc, record_peso_perdido, peso_perdido_tratamiento, dias_consecutivos_dieta, dias_consecutivos_ejercicio);
	}
}

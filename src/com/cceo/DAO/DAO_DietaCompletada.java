package com.cceo.DAO;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.cceo.DB.SQLiteDietaCompletadaDB;
import com.cceo.DB.SQLiteDietaDB;
import com.cceo.DB.SQLiteEstadisticas;
import com.cceo.DTO.DTO_DietaCompletada;
import com.cceo.DTO.DTO_Estadisticas;
import com.cceo.DTO.DTO_Usuario;
import com.cceo.miyoideal.extra.API;


public class DAO_DietaCompletada {
	
	private Context con;
	
	public DAO_DietaCompletada(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}
	
	public void newDietaCompletada(DTO_DietaCompletada temp)
	{
		SQLiteDietaCompletadaDB db = new SQLiteDietaCompletadaDB(con);
    	int id = 0;
    	
    	db.getReadableDatabase();
		String query = "Select * FROM dieta_completada";
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		while(cursor.moveToNext())
		{
			id++;
		}
    	
		db.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id_dieta_completada", String.valueOf(id));
		values.put("id_dieta", temp.getId_dieta());
		values.put("peso", temp.getPeso());
		values.put("talla", temp.getTalla());
		values.put("fecha", temp.getFecha());
		db.getWritableDatabase().insert("dieta_completada", null, values);
		db.close();
		
		//Update Estadisticas 
		DAO_Estadisticas daoEstadisticas = new DAO_Estadisticas(con);
		DTO_Estadisticas dtoEstadisticas = daoEstadisticas.getEstadisticas();
		SQLiteEstadisticas dbEstadisticas = new SQLiteEstadisticas(con);
		dbEstadisticas.getReadableDatabase();
			
		values = new ContentValues();
		values.put("id_estadisticas", 0);
		if(Float.parseFloat(dtoEstadisticas.getPesoMaximo()) < Float.parseFloat(temp.getPeso())){
			//Update the maximum and actual weight
			values.put("peso_maximo", Float.parseFloat(temp.getPeso()));
		}
		else if(Float.parseFloat(dtoEstadisticas.getPesoMinimo()) > Float.parseFloat(temp.getPeso())){
			//Update the minimum and actual weight 
			values.put("peso_minimo", Float.parseFloat(temp.getPeso()));
		}
		
		if(Float.parseFloat(temp.getPeso()) < Float.parseFloat(dtoEstadisticas.getPesoActual())){
			values.put("record_peso_perdido", (Float.parseFloat(dtoEstadisticas.getPesoActual()) - Float.parseFloat(temp.getPeso())) + Float.parseFloat(dtoEstadisticas.getRecordPesoPerdido()));
			values.put("peso_perdido_tratamiento", Float.parseFloat(dtoEstadisticas.getPesoActual()) - Float.parseFloat(temp.getPeso()));
		}
		else{
			values.put("peso_perdido_tratamiento", 0);
		}
		
		values.put("peso_actual", Float.parseFloat(temp.getPeso()));
		values.put("imc", Float.parseFloat(temp.getPeso())/1);
		
		//Update the size
		if(Float.parseFloat(dtoEstadisticas.getTallaMaxima()) < Float.parseFloat(temp.getTalla())){
			//Update the maximum size
			values.put("talla_maxima", Float.parseFloat(temp.getTalla()));
		}
		else if(Float.parseFloat(dtoEstadisticas.getTallaMinima()) > Float.parseFloat(temp.getPeso())){
			//Update the minimum size
			values.put("talla_minima", Float.parseFloat(temp.getTalla()));
		}
		values.put("talla_actual", Float.parseFloat(temp.getTalla()));
		
		dbEstadisticas.getReadableDatabase().update("estadisticas", values, null, null);
		dbEstadisticas.close();	
	}
	
	//return last five insert in 'dieta completada' table. It includes the usuer's data as the first item in the list
	public List<DTO_DietaCompletada> getLastFiveDietaCompleta()
	{
		List<DTO_DietaCompletada> list = new ArrayList<DTO_DietaCompletada>();
		
		SQLiteDietaCompletadaDB db = new SQLiteDietaCompletadaDB(con);
		db.getReadableDatabase();

		String query = "select * from (select * from dieta_completada order by id_dieta_completada DESC limit 4) order by id_dieta_completada DESC;";
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		
		Log.d("grafica", "retrieve usuario");
		
		//first object is always the user's data
		DTO_Usuario datosUsuario = new DAO_Usuario(con).getUsuario();
		DTO_DietaCompletada first = new DTO_DietaCompletada("-", 
				"-", new API(con).getPesoInicial(), datosUsuario.getTalla(), "-");
		list.add(first);
		
		String query1 = "select * from dieta_completada;";
		Cursor cursor1 = db.getReadableDatabase().rawQuery(query1, null);

		while(cursor1.moveToNext())
		{
			Log.d("dietas", "ID = " + cursor1.getString(0) + ", Peso = " + cursor1.getString(2));
		}
		
		if(cursor.moveToFirst())
		{
			do
			{
				DTO_DietaCompletada temp = new DTO_DietaCompletada();
				temp.setId_dieta_completada(cursor.getString(0));
				temp.setId_dieta(cursor.getString(1));
				temp.setPeso(cursor.getString(2));
				temp.setTalla(cursor.getString(3));
				temp.setFecha(cursor.getString(4));
				
				list.add(temp);
			}
			while((cursor.moveToNext() && list.size() < 5));			
		}	
		
		db.close();
		Log.d("grafica", "Ya tenemos lista con " + String.valueOf(list.size()) + " items");
		return list;
	}
	
	public void InsertCSVFile(String FilePath, String filename, String TableName) {
		
		/*ContentValues values = new ContentValues();
		SQLiteDietaCompletadaDB db = new SQLiteDietaCompletadaDB(con);
		try { 
			String state = Environment.getExternalStorageState();
			con.getResources().openRawResource(R.raw.programas);
			if(state.contentEquals(Environment.MEDIA_MOUNTED) || state.contentEquals(Environment.MEDIA_MOUNTED_READ_ONLY)){
				
				Reader inputStream = new InputStreamReader(con.getResources().openRawResource(R.raw.programas));
				
				//File file1 = Environment.getExternalStorageDirectory();
				//File csv = new File(file1.getAbsolutePath().concat("/programas.csv"));
				//FileReader fr = new FileReader(csv); 
	            BufferedReader br = new BufferedReader(inputStream);
	            String data = "";
	            String tableName = "dieta_completada";
	            String columns = "id_dieta_completada,id_dieta,peso,talla,fecha";
	            //String InsertString1 = "INSERT INTO " + tableName + " (" + columns
	             //       + ") values("; 
	            //String InsertString2 = ");";
	            while ((data = br.readLine()) != null) {
	                //StringBuilder sb = new StringBuilder(InsertString1);
	                String[] sarray = data.split(",");
	                values.put("id_dieta_completada", sarray[0]);
	                values.put("id_dieta", sarray[1]);
	                values.put("peso", sarray[2]);
	                values.put("talla", sarray[3]);
	                values.put("fecha", sarray[4]);
	                db.getWritableDatabase().insert("dieta_completada", null, values);
					db.close();
					
					String query = "select * from dieta_completada";
					Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
					
					if(cursor.moveToFirst())
					{
						do
						{
							Log.d("DAO", cursor.getString(0)+" "+cursor.getString(1)+" "+cursor.getString(2)+" "+cursor.getString(3)+" "+cursor.getString(4));
						}
						while(cursor.moveToNext());			
					}	
					db.close();
	            }//mainDatabase.execSQL(sb.toString()); 
            }  
        }catch(IOException ex){
        	ex.printStackTrace();
        }
		finally{
			//db.close();
		}
		
		*/
	}

}

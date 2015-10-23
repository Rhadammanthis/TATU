package com.cceo.miyoideal.extra;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.Inflater;

import com.cceo.DAO.DAO_Dieta;
import com.cceo.DAO.DAO_Dieta_Iteration;
import com.cceo.DAO.DAO_Ejercicio;
import com.cceo.DAO.DAO_Recommendation;
import com.cceo.DB.SQLiteControl;
import com.cceo.DTO.DTO_Dieta;
import com.cceo.DTO.DTO_DietaIteration;
import com.cceo.DTO.DTO_Ejercicio;
import com.cceo.DTO.DTO_Recommendation;
import com.cceo.miyoideal.HomeActivity;
import com.cceo.miyoideal.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class RecomendationDialog extends DialogFragment{

	public Context con;
	public int recomend;
	public String dietaKey;

	private Button bFirst;
	private Button bSecond;

	private String first;
	private String second;

	public RecomendationDialog(Context con, int recomend, String id_dieta) {
		// TODO Auto-generated constructor stub
		this.con = con;
		this.recomend = recomend;
		this.dietaKey = id_dieta;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.recomendation_fragment, null);

		bFirst = (Button) v.findViewById(R.id.recomen_bFirst);
		bSecond = (Button) v.findViewById(R.id.recomen_bSecond);

		DTO_Recommendation temp = getRecommendation();

		AlertDialog.Builder builder = new AlertDialog.Builder(con);
		CharSequence[] items = new CharSequence[1];
		items[0] = "Hola";
		switch (recomend) {
		case 0:
			//same
			builder.setMessage("Ni arriba ni abajo");
			first = temp.getRecommendationSameFirst();
			second = temp.getRecommendationSameSecond();
			break;
		case 1:
			//more
			builder.setMessage("Parece que hay que trabajar mas");
			first = temp.getRecommendationMoreFirst();
			second = temp.getRecommendationMoreSecond();
			break;
		case 2:
			//less
			builder.setMessage("Bien hecho");
			first = temp.getRecommendationLessFirst();
			second = temp.getRecommendationLessSecond();
			break;

		default:
			break;
		}
		builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		})
		.setTitle("Seleccion")
		.setView(v);
		
		//buttons select dieta. It is searched by the key provided by the recommendations object;
		bFirst.setText(first);
		bFirst.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectNewDieta(new DAO_Dieta(con).getDietaByKey(first).getId_dieta());
			}
		});
		
		bSecond.setText(first);
		bSecond.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectNewDieta(new DAO_Dieta(con).getDietaByKey(second).getId_dieta());
			}
		});

		return builder.create();
	}

	public DTO_Recommendation getRecommendation()
	{
		DTO_Recommendation recommendation = new DAO_Recommendation(con).getRecommendation(dietaKey);

		return recommendation;
	}
	
	public void selectNewDieta(String id_Dieta)
	{
		//Update Control DB (so we can know a dieta has been selected)
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
		String todaysDate = df.format(c.getTime());
		ContentValues cv = new ContentValues();
		cv.put("id_dieta", id_Dieta); //These Fields should be your String values of actual column names
		cv.put("dia",todaysDate);

		SQLiteControl db = new SQLiteControl(con);
		db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
		db.close();
		
		new API(con).incDietaIteration();

		DTO_Dieta dieta = new DAO_Dieta(con).getDieta(new API(con).getID_Dieta());

		String actividad = "";
		for(int i = 0; i < Integer.parseInt(dieta.getDuracion()); i++)
			actividad += "0";

		/*Add new insert to DB Ejercicio. This helps us keep track of the days in
		 *which physical activity was performed when undertaking the diet program*/
		DTO_Ejercicio ejercicio = new DTO_Ejercicio(id_Dieta, todaysDate, actividad);
		new DAO_Ejercicio(con).newEjercicio(ejercicio);   
		
		Log.d("sopor", "Dieta Iteration number: "  + new API(con).getDietaIteration());
		Log.d("sopor", "Dieta id assigned: "  + String.valueOf(id_Dieta));
		Log.d("sopor", "Activity: "  + actividad);
		
		//create new dieta iteration
		DTO_DietaIteration dieta_it = new DTO_DietaIteration(new API(con).getDietaIteration(), id_Dieta, actividad);
		new DAO_Dieta_Iteration(con).newDietaIteration(dieta_it);

		//service
		Calendar cal = Calendar.getInstance();

		String dia = new API(con).getDia();

		Intent intent = new Intent(con, MyService.class);
		intent.putExtra("dia_inicial", dia);                   	                       	   
		intent.putExtra("duracion", dieta.getDuracion());                    	   

		PendingIntent pintent = PendingIntent.getService(con, 1234, intent, 0);
		//con.startService(intent);
                   	   

		// schedule for every 5 hours
		//alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 20*1000, pintent); 
		DietaCompleteNotification notification = new DietaCompleteNotification();
		notification.setAlarm(con,dieta.getDuracion(),dia);

		
		Intent intentHome = new Intent(con, HomeActivity.class);
		intentHome.putExtra("intent_origin", "select".toString());
		startActivity(intentHome);
	}
}

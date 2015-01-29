package com.example.miyoideal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import shared.ui.actionscontentview.ActionsContentView;

import com.example.DAO.DAO_Ejercicio;
import com.example.DAO.DAO_Programa;
import com.example.DTO.*;
import com.example.miyoideal.R;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.EjercicioChildFactory;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class EjercicioActivity extends Activity {
	
	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	private LinearLayout mainLayout;
	private CheckBox checkB;
	private Context con;
	private RelativeLayout topBar;
	
	private Calendar c;
	private DTO_Ejercicio ejercicio;
	private long distance;
	private char[] dia;
	
	private EjercicioChildFactory ejercicioFactory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline_ejercicio);
		con = this;
		
		viewActionsContentView = (ActionsContentView) findViewById(R.id.ejercicio_actionsContentView);
		
		viewActionsList = (ListView) findViewById(R.id.actions);
		setUpMenu();
		
		mainLayout = (LinearLayout) findViewById(R.id.ejercicio_linearLayout);
		checkB = (CheckBox) findViewById(R.id.ejercicio_checkB);
		topBar = (RelativeLayout) findViewById(R.id.ejercicioTopBar);
		
		
		//Ejercicio Factory Example
		ejercicioFactory = new EjercicioChildFactory();
		mainLayout.addView(ejercicioFactory.GenerateChildEjercicio(this, "Sentadillas", "Hacer de 10 a 20", "Mañana"));
		mainLayout.addView(ejercicioFactory.GenerateChildEjercicio(this, "Lagartijas", "Hacer 3 repeticiones de 8", "Tarde"));
		mainLayout.addView(ejercicioFactory.GenerateChildEjercicio(this, "Correr", "Correr 3 KM a ritmo medio", "Noche"));
		initProgramaComponentes();
		
		//distance variable getes the difference between today's date and that of the first day of the diet program
		c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
		java.util.Date initalDate = null;
		String lol = new API(con).getDia();
		try {
			initalDate = df.parse(new API(con).getDia());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String todayDate = df.format(c.getTime());	
		distance = getDateDiffString(initalDate,c.getTime());
		
		//Obtain current ejercicio info from BD
		ejercicio = new DTO_Ejercicio();
		ejercicio = new DAO_Ejercicio(con).getEjercicio(new API(con).getID_Dieta(), new API(con).getDia());
		
		if(distance > ejercicio.getDiasActividad().length())
		{
			topBar.setVisibility(View.GONE);
		}
		else
		{
			//Check the state of todays physical activity
			dia = ejercicio.getDiasActividad().toCharArray();
			if(dia[(int) distance] == '0')
			{
				checkB.setChecked(false);
			}
			else
			{
				checkB.setChecked(true);
			}
			
			//if distance > dias que dura la dieta, esconder barra
			
			checkB.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(checkB.isChecked())
					{
						dia[(int) distance] = '1';
					}
					else
					{
						dia[(int) distance] = '0';
					}	
					
					String l = "";
					for(int i = 0; i<=ejercicio.getDiasActividad().length()-1;i++)
						l += String.valueOf(dia[i]);
					Log.d("dias", l);
					ejercicio.setDiasActividad(l);
					Log.d("dias", ejercicio.getDiasActividad());
					new DAO_Ejercicio(con).updateEjercicio(ejercicio);
				}
			});
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Add the menu layout to the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.base_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//If the Logo clicked
		Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}
	
	private void setUpMenu()
	{
		final String[] values = new String[] { "Mi Perfil", "Mi Dieta", "Mi Ejercicio", 
	    		"Mas Dietas", "Calendario", "Estadisticas", "Preguntanos",
	    		"Comparte", "Tips y Sujerencias", "Recordatorios"};
	    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, android.R.id.text1, values);

	    viewActionsList.setAdapter(adapter);
	    viewActionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	      @Override
	      public void onItemClick(AdapterView<?> adapter, View v, int position,
	          long flags) {
	    	  showActivity(position);

	      }
	    });
	}
	
	private void showActivity(int position) 
	{
	    final Intent intent;
	    switch (position) {
	    case 0:
	    	intent = new Intent(EjercicioActivity.this, MiPerfilActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	      break;
	    case 1:
	    	intent = new Intent(EjercicioActivity.this, DietaActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		  break;
	    case 4:
	    	intent = new Intent(EjercicioActivity.this, CalendarioActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	      break;

	    default:
	      return;
	    }

	}
	
	//returns distance between days of  two Date objects
	public long getDateDiffString(java.util.Date date, java.util.Date date2)
	{
	    long timeOne = date.getTime();
	    long timeTwo = date2.getTime();
	    long oneDay = 1000 * 60 * 60 * 24;
	    long delta = (timeTwo - timeOne) / oneDay;
	    
	    int dia_Date1 = Integer.valueOf((String)android.text.format.DateFormat.format("dd", date)), 
	    		dia_Date2 = Integer.valueOf((String)android.text.format.DateFormat.format("dd", date2));

	    String lol = String.valueOf(timeOne/oneDay);
	    if(dia_Date2 < dia_Date1)
	    	return -1;
	    else
	    	return delta;
	}
	
	private void initProgramaComponentes()
	{
		//the hardcoded string at the end must be take from the user profile
		List<DTO_Programa> lista = new DAO_Programa(con).getAllPrograma("medio");
		
		for(DTO_Programa temp : lista)
		{
			mainLayout.addView(ejercicioFactory.GenerateChildEjercicio(con, temp.getTitulo(), temp.getDescripcion(), temp.getHora()));
		}
	}
}

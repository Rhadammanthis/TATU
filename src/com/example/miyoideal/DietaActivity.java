package com.example.miyoideal;

import java.sql.Date;
import java.text.DateFormat.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import shared.ui.actionscontentview.ActionsContentView;

import com.example.DAO.DAO_Componente;
import com.example.DTO.DTO_Componente;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.DietaChild;
import com.example.miyoideal.extra.DietaChildFactory;
import com.example.miyoideal.extra.EjercicioChildFactory;

import android.R.bool;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.TabListener;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class DietaActivity extends Activity {
	
	private LinearLayout mainLayout;
	private ListView viewActionsList;
	private TextView currentDate;
	private DietaChildFactory dietaChildFactory;
	private Context con;
	private List<DietaChild> dietaChildList;
	
	private List<DTO_Componente> componentes;
	private ImageButton nextDay;
	private ImageButton previousDay;
	
	private boolean checkBoxInitialState[];
	
	private Calendar c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.baseline_dieta);
		
		con = this;
		dietaChildList = new ArrayList<DietaChild>();
		viewActionsList = (ListView) findViewById(R.id.actions);	
		mainLayout = (LinearLayout) findViewById(R.id.dieta_linearLayout);
		currentDate = (TextView) findViewById(R.id.fechaDieta);
		dietaChildFactory = new DietaChildFactory();
		nextDay = (ImageButton)findViewById(R.id.nextDay);
		previousDay = (ImageButton)findViewById(R.id.previousDay);
		
		setUpMenu();
		
		//if a "dieta" is currently set, the corresponding components are loaded
		
		c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
		java.util.Date initalDate = null;
		String lol = new API(this).getDia();
		try {
			initalDate = df.parse(new API(this).getDia());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String todayDate = df.format(c.getTime());	
		long distance = getDateDiffString(initalDate,c.getTime());
		if(new API(this).IsDietaSet())
		{
			componentes = new DAO_Componente(con).getAllComponente(new API(con).getID_Dieta(), String.valueOf(distance+1));
			if(componentes.size()>0)
				initDietaLayout(componentes);
			else
				mainLayout.addView(new DietaChild(con).getDefaultLayout());
		}
		currentDate.setText(todayDate);
		
		nextDay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//on a second call, the initial date is still the one set on the previous page.
				Log.d("dieta", "nextDay");
				mainLayout.removeAllViews();
				c.add(Calendar.DATE, 1);
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
				long distance = getDateDiffString(initalDate,c.getTime());
				if(new API(con).IsDietaSet())
				{
					componentes = new DAO_Componente(con).getAllComponente(new API(con).getID_Dieta(), String.valueOf(distance+1));
					if(componentes.size()>0)
						initDietaLayout(componentes);
					else
						mainLayout.addView(new DietaChild(con).getDefaultLayout());
				}
				
				currentDate.setText(todayDate);
			}
		});
		
		previousDay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//on a second call, the initial date is still the one set on the previous page.
				Log.d("dieta", "previousDay");
				mainLayout.removeAllViews();
				c.add(Calendar.DATE, -1);
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
				long distance = getDateDiffString(initalDate,c.getTime());
				if(new API(con).IsDietaSet())
				{
					componentes = new DAO_Componente(con).getAllComponente(new API(con).getID_Dieta(), String.valueOf(distance+1));
					if(componentes.size()>0)
						initDietaLayout(componentes);
					else
						mainLayout.addView(new DietaChild(con).getDefaultLayout());
				}
				
				currentDate.setText(todayDate);
			}
		});
		
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
	    	intent = new Intent(DietaActivity.this, MiPerfilActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	      break;
	    case 2:
	    	intent = new Intent(DietaActivity.this, EjercicioActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	      break;
	    case 4:
	    	intent = new Intent(DietaActivity.this, CalendarioActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	      break;

	    default:
	      return;
	    }

	  }
	
	public void initDietaLayout(List<DTO_Componente> lista)
	{
		//list to hold all same Comida entries in 'lista'
		final List<DTO_Componente> content = new ArrayList<DTO_Componente>();
		for(int i=1; i < lista.size(); i++)
		{
			//checks if the Alimento of a given insert in 'lista' is the same as the last one. If so, it is then added to 'content'
			if(lista.get(i).getAlimento().equals(lista.get(i-1).getAlimento()))
			{
				content.add(lista.get(i-1));
			}
			else
			{
				//when a different Alimento occurs, the parameters to create the DietaChild are set up
				String title = lista.get(i-1).getAlimento();
				boolean activo = false;
				
				//the last checked insert in 'lista' is added to
				content.add(lista.get(i-1));
				
				//Cheks wheter one of the inserts has a negative value in Activo to initialize the checkbox as checked
				for(DTO_Componente temp : content)
				{
					if(temp.getActivo().equals("yes"))
						activo = true;
				}
				
				//A new DietaChild is created from the dietaChildFactory
				final DietaChild newChild = dietaChildFactory.GenerateChild(con, title, content, "10 am", activo);
				
				//A click listener is added to the checkbox the handle checked and unchecked behavior
				newChild.getCheckBox().setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//when the status of checkbox changes, its current state is saved in the DB
						if(newChild.getCheckBox().isChecked())
						{
							new DAO_Componente(con).updateComponenteYes(getModifiedChild().getDTO_Componente());
						}	
						else
						{
							new DAO_Componente(con).updateComponenteNo(getModifiedChild().getDTO_Componente());
						}		
						
						//update inital checkbox status's
						for(int i=0;i< dietaChildList.size();i++)
						{
							checkBoxInitialState[i] = dietaChildList.get(i).getCheckBox().isChecked();
						}
					}
				});
				
				//the dietachild is added to the main layout
				mainLayout.addView(newChild.getChild());
				
				//we keep track of children views in a list
				dietaChildList.add(newChild);
				
				//the content list is cleared 
				content.clear();
			}
		}
		
		//same behavior as last, but this handles when an Alimento only carries one insert in the content list and is the last Alimento instance
		String title = lista.get(lista.size()-1).getAlimento();
		boolean activo = false;
		content.add(lista.get(lista.size()-1));

		for(DTO_Componente temp : content)
		{
			if(temp.getActivo().equals("yes"))
				activo = true;
		}
		
		final DietaChild newChild = dietaChildFactory.GenerateChild(con, title, content, "10 am", activo);
		newChild.getCheckBox().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(newChild.getCheckBox().isChecked())
				{
					new DAO_Componente(con).updateComponenteYes(getModifiedChild().getDTO_Componente());
				}	
				else
				{
					new DAO_Componente(con).updateComponenteNo(getModifiedChild().getDTO_Componente());
				}
				
				for(int i=0;i< dietaChildList.size();i++)
				{
					checkBoxInitialState[i] = dietaChildList.get(i).getCheckBox().isChecked();
				}
			}
		});
		mainLayout.addView(newChild.getChild());
		dietaChildList.add(newChild);

		content.clear();
		
		checkBoxInitialState = new boolean[dietaChildList.size()];
		
		for(int i=0;i< dietaChildList.size();i++)
		{
			checkBoxInitialState[i] = dietaChildList.get(i).getCheckBox().isChecked();
		}
	}
	
	public DietaChild getModifiedChild()
	{
		DietaChild res = new DietaChild(con);
		
		boolean checkBoxActualState[] = new boolean[dietaChildList.size()];
		int size = dietaChildList.size();
		
		for(int i=0;i<size;i++)
		{
			checkBoxActualState[i] = dietaChildList.get(i).getCheckBox().isChecked();
		}
		
		for(int i=0;i<size;i++)
		{
			if(checkBoxInitialState[i] != checkBoxActualState[i])
			{
				return dietaChildList.get(i);
			}
		}
		
		return res;
	}
	
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
}

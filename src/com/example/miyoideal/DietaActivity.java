package com.example.miyoideal;

import java.sql.Date;
import java.text.DateFormat.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import shared.ui.actionscontentview.ActionsContentView;

import com.example.miyoideal.extra.DietaChildFactory;
import com.example.miyoideal.extra.EjercicioChildFactory;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DietaActivity extends ActionBarActivity {
	
	private LinearLayout mainLayout;
	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	
	private DietaChildFactory dietaChildFactory;
	private EjercicioChildFactory ejercicioChildFactory;
	private Context con;
	private android.support.v7.app.ActionBar.Tab tab11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.baseline_dieta);
		
		con = this;
		
		viewActionsContentView = (ActionsContentView) findViewById(R.id.ejercicio_actionsContentView);
		viewActionsList = (ListView) findViewById(R.id.actions);	
		setUpMenu();
		
		mainLayout = (LinearLayout) findViewById(R.id.dieta_linearLayout);
		
		dietaChildFactory = new DietaChildFactory();
		ejercicioChildFactory = new EjercicioChildFactory();
		
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.setDisplayShowTitleEnabled(false);

	    android.support.v7.app.ActionBar.Tab tab = actionBar.newTab()
	                       .setText("<-")
	                       .setTabListener(new TabListener() {
							
							@Override
							public void onTabUnselected(android.support.v7.app.ActionBar.Tab arg0,
									FragmentTransaction arg1) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onTabSelected(android.support.v7.app.ActionBar.Tab arg0,
									FragmentTransaction arg1) {
								// TODO Auto-generated method stub
								DisplayMetrics metrics = getResources().getDisplayMetrics();
								int densityDpi = (int)(metrics.density);
								
								mainLayout.addView(ejercicioChildFactory.GenerateChildEjercicio(con, "LOL", "w", densityDpi));
								mainLayout.addView(ejercicioChildFactory.GenerateChildEjercicio(con, "ROFL", "w", densityDpi));
								mainLayout.addView(ejercicioChildFactory.GenerateChildEjercicio(con, "LMFAO", "w", densityDpi));
								
							}
							
							@Override
							public void onTabReselected(android.support.v7.app.ActionBar.Tab arg0,
									FragmentTransaction arg1) {
								// TODO Auto-generated method stub
								
							}
						});
	    actionBar.addTab(tab);

	    tab11 = actionBar.newTab()
                .setText("FECHA")
                .setTabListener(new TabListener() {
					
					@Override
					public void onTabUnselected(android.support.v7.app.ActionBar.Tab arg0,
							FragmentTransaction arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onTabSelected(android.support.v7.app.ActionBar.Tab arg0,
							FragmentTransaction arg1) {
						// TODO Auto-generated method stub
						String lol[] = new String[2];
						lol[0] = "Huevo";
						lol[1] = "Pollo";
						
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Desayuno", lol, "10 am"));
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Comida", lol = new String[]{"Carne","Brocoli"}, "2 pm"));
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Cena", lol = new String[]{"Cereal","Leche"}, "2 pm"));
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Correr 5km", "8 am"));
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Subir 100 escalones", "8 am"));
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Desayuno", lol, "10 am"));
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Comida", lol = new String[]{"Carne","Brocoli"}, "2 pm"));
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Cena", lol = new String[]{"Cereal","Leche"}, "2 pm"));
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Correr 5km", "8 am"));
						mainLayout.addView(dietaChildFactory.GenerateChild(con, "Subir 100 escalones", "8 am"));
						
					}
					
					@Override
					public void onTabReselected(android.support.v7.app.ActionBar.Tab arg0,
							FragmentTransaction arg1) {
						// TODO Auto-generated method stub
						
					}
				});
	    actionBar.addTab(tab11);
	    
	    android.support.v7.app.ActionBar.Tab tab2 = actionBar.newTab()
                .setText("->")
                .setTabListener(new TabListener() {
					
					@Override
					public void onTabUnselected(android.support.v7.app.ActionBar.Tab arg0,
							FragmentTransaction arg1) {
						// TODO Auto-generated method stub
					
						
					}
					
					@Override
					public void onTabSelected(android.support.v7.app.ActionBar.Tab arg0,
							FragmentTransaction arg1) {
						// TODO Auto-generated method stub
						DisplayMetrics metrics = getResources().getDisplayMetrics();
						int densityDpi = (int)(metrics.density);
						
						
					}
					
					@Override
					public void onTabReselected(android.support.v7.app.ActionBar.Tab arg0,
							FragmentTransaction arg1) {
						// TODO Auto-generated method stub
						
					}
				});
	    actionBar.addTab(tab2);
		
		
		
		
		
		
		/*mainLayout.addView(factory.GenerateChild(this, "Desayuno", lol, "10 am"));
		mainLayout.addView(factory.GenerateChild(this, "Comida", lol = new String[]{"Carne","Brocoli"}, "2 pm"));
		mainLayout.addView(factory.GenerateChild(this, "Cena", lol = new String[]{"Cereal","Leche"}, "2 pm"));
		mainLayout.addView(factory.GenerateChild(this, "Correr 5km", "8 am"));
		mainLayout.addView(factory.GenerateChild(this, "Subir 100 escalones", "8 am"));
		mainLayout.addView(factory.GenerateChild(this, "Desayuno", lol, "10 am"));
		mainLayout.addView(factory.GenerateChild(this, "Comida", lol = new String[]{"Carne","Brocoli"}, "2 pm"));
		mainLayout.addView(factory.GenerateChild(this, "Cena", lol = new String[]{"Cereal","Leche"}, "2 pm"));
		mainLayout.addView(factory.GenerateChild(this, "Correr 5km", "8 am"));
		mainLayout.addView(factory.GenerateChild(this, "Subir 100 escalones", "8 am"));*/
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dieta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
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
}

package com.example.miyoideal;

import shared.ui.actionscontentview.ActionsContentView;

import com.example.miyoideal.widget.DietaChildFactory;
import com.example.miyoideal.widget.EjercicioChildFactory;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline_dieta);
		
		viewActionsContentView = (ActionsContentView) findViewById(R.id.ejercicio_actionsContentView);
		viewActionsList = (ListView) findViewById(R.id.actions);	
		setUpMenu();
		
		mainLayout = (LinearLayout) findViewById(R.id.dieta_linearLayout);
		
		String lol[] = new String[2];
		lol[0] = "Huevo";
		lol[1] = "Pollo";
		
		DietaChildFactory factory = new DietaChildFactory();
		EjercicioChildFactory fac = new EjercicioChildFactory();
		
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int densityDpi = (int)(metrics.density);
		
		mainLayout.addView(fac.GenerateChildEjercicio(this, "LOL", "w", densityDpi));
		mainLayout.addView(fac.GenerateChildEjercicio(this, "ROFL", "w", densityDpi));
		mainLayout.addView(fac.GenerateChildEjercicio(this, "LMFAO", "w", densityDpi));
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

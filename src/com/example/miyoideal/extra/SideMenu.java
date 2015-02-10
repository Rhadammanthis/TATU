package com.example.miyoideal.extra;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SideMenu {
	
	private ListView viewActionsList;
	
	public SideMenu(ListView finalMenu, Context context)
	{
		viewActionsList = finalMenu;
		
		final String[] values = new String[] { "Mi Perfil", "Mi Dieta", "Mi Ejercicio", 
	    		"Mas Dietas", "Calendario", "Estadisticas", "Preguntanos",
	    		"Comparte", "Tips y Sujerencias", "Recordatorios"};
	    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
	        android.R.layout.simple_list_item_1, android.R.id.text1, values);

	    viewActionsList.setAdapter(adapter);
	    viewActionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	      @Override
	      public void onItemClick(AdapterView<?> adapter, View v, int position,
	          long flags) {


	      }
	    });
	}
	

	
	public ListView getListView()
	{
		return viewActionsList;
	}

}

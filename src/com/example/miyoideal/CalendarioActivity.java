package com.example.miyoideal;

import java.text.SimpleDateFormat;
import java.util.Locale;

import shared.ui.actionscontentview.ActionsContentView;

import com.example.miyoideal.widget.CalendarioChildFactory;
import com.example.miyoideal.widget.SideMenu;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.wt.calendarcard.*;

public class CalendarioActivity extends ActionBarActivity {
	
	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	private Button b;
	private CalendarCardPager mCalendarCard;
	private LinearLayout linearLayout;
	private int width, height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline3);
		
    	Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;		
		
		viewActionsContentView = (ActionsContentView) findViewById(R.id.calendario_actionsContentView);
		linearLayout = (LinearLayout) findViewById(R.id.calendarLinearLayout);		
		
		viewActionsList = (ListView) findViewById(R.id.actions);
		setUpMenu();
		
		mCalendarCard = (CalendarCardPager)findViewById(R.id.calendarCard6);
		mCalendarCard.getLayoutParams().height = height/2;
		mCalendarCard.setOnCellItemClick(new OnCellItemClick() {
			@Override
			public void onCellClick(View v, CardGridItem item) {
				//b.setText(getResources().getString(R.string.sel_date, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(item.getDate().getTime())));
				item.setEnabled(false);
			}
		});
		
		CalendarioChildFactory factory = new CalendarioChildFactory(this);
		
		linearLayout.addView(factory.GenerateChildEjercicio("Uno 1", "asjhgdlgd ladjgajaldal bd lsjdblsbdlsnv dbasv dv sd"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendario, menu);
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
	    	intent = new Intent(CalendarioActivity.this, MiPerfilActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	      break;
	    case 2:
	    	intent = new Intent(CalendarioActivity.this, EjercicioActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
	      break;

	    default:
	      return;
	    }

	  }
}

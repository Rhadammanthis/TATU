package com.example.miyoideal;

import com.example.DAO.DAO_Usuario;
import com.example.DTO.DTO_Usuario;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.PerfilNumberPicker;

import shared.ui.actionscontentview.ActionsContentView;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class MiPerfilActivity extends FragmentActivity implements DialogInterface.OnDismissListener{

	//layout components
	private Spinner spinnerNivel;
	private RelativeLayout pesoDeseadoRL;
	private TextView pesoIdeal;

	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline2);
		con = this;

		spinnerNivel = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.nivel_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerNivel.setAdapter(adapter);	
		
		pesoIdeal = (TextView) findViewById(R.id.pesoIdeal);
		
		setPesoIdealValue();
		
		final FragmentManager manager = this.getFragmentManager();
		
		pesoDeseadoRL = (RelativeLayout) findViewById(R.id.pesoDeseadoRL);
		pesoDeseadoRL.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PerfilNumberPicker dialog = new PerfilNumberPicker(con);
				dialog.show(manager, "Picker");

			}
		});
		
		DTO_Usuario usuario = new DAO_Usuario(this).getUsuario();
		
		TextView peso = (TextView) findViewById(R.id.pesoActual_);
		peso.setText(usuario.getPeso());
		TextView talla = (TextView) findViewById(R.id.tallaActual_);	
		talla.setText(usuario.getTalla());
		

		setUpMenu();
	}

	private void setPesoIdealValue() {
		// TODO Auto-generated method stub
		if(new API(con).getPesoIdeal().equals(null)
				|| new API(con).getPesoIdeal().equals(""))
		{
			pesoIdeal.setText("0");
		}
		else 
		{
			pesoIdeal.setText(new API(con).getPesoIdeal());
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
		viewActionsContentView = (ActionsContentView) findViewById(R.id.miPerfil_actionsContentView);

		viewActionsList = (ListView) findViewById(R.id.actions);
		
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
		case 2:
			intent = new Intent(MiPerfilActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(MiPerfilActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		default:
			return;
		}

	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		pesoIdeal.setText(new API(con).getPesoIdeal());
	}

	public void onUserSelectValue(String value) {
		// TODO Auto-generated method stub
		Log.d("chino", "number 2" + value);
	}
}

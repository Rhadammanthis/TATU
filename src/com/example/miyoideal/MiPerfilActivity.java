package com.example.miyoideal;

import com.example.DAO.DAO_DietaCompletada;
import com.example.DAO.DAO_Usuario;
import com.example.DB.SQLiteControl;
import com.example.DTO.DTO_Usuario;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.PerfilNumberPicker;

import shared.ui.actionscontentview.ActionsContentView;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MiPerfilActivity extends FragmentActivity implements DialogInterface.OnDismissListener, OnItemSelectedListener{

	//layout components
	private Spinner spinnerNivel;
	private RelativeLayout pesoDeseadoRL;
	private TextView pesoIdeal;
	private LinearLayout mainLayout;
	private ScrollView scroll;
	private View bar;

	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	private Context con;
	
	//Style variables
	private int styleMain;
	private int styleDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline2);
		con = this;

		spinnerNivel = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.nivel_array, R.layout.spinnercustomitem);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerNivel.setAdapter(adapter);	
		DTO_Usuario userData = new DAO_Usuario(con).getUsuario();
		int pos = 0;
		Log.d("blood", userData.getNivel());
		if(userData.getNivel().equals("Bajo")) pos = 0;
		if(userData.getNivel().equals("Medio")) pos = 1;
		if(userData.getNivel().equals("Alto")) pos = 2;
		spinnerNivel.setSelection(pos, true);
		
		spinnerNivel.setOnItemSelectedListener(this);
		
		pesoIdeal = (TextView) findViewById(R.id.pesoIdeal);
		mainLayout = (LinearLayout) findViewById(R.id.miPerfilMainLayout);
		scroll = (ScrollView) findViewById(R.id.miPerfilScroll);
		bar = (View) findViewById(R.id.miPerfilBar);
		
		//update view's style
		updateStyle();
		
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
		
		EditText motivacion = (EditText) findViewById(R.id.miPerfilMotivaciones);
		motivacion.setText(new API(con).getMotivacion());

		setUpMenu();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		EditText motivacion = (EditText) findViewById(R.id.miPerfilMotivaciones);
		new API(con).setMotivacion(motivacion.getText().toString());
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
		
		View color1 = (View) findViewById(R.id.col1);
		color1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//asigns style value to data base
				ContentValues cv = new ContentValues();
				cv.put("estilo","femenino");

				SQLiteControl db = new SQLiteControl(con);
				db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
				db.close();

				updateStyle();
			}
		});

		View color2 = (View) findViewById(R.id.col2);
		color2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//asigns style value to data base
				ContentValues cv = new ContentValues();
				cv.put("estilo","masculino");

				SQLiteControl db = new SQLiteControl(con);
				db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
				db.close();

				updateStyle();
			}
		});

		View color3 = (View) findViewById(R.id.col3);
		color3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//asigns style value to data base
				ContentValues cv = new ContentValues();
				cv.put("estilo","neutral");

				SQLiteControl db = new SQLiteControl(con);
				db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
				db.close();

				updateStyle();
			}
		});

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
	
	//Saves selected style colors to local variables
	private void getStyle()
	{
		Log.d("color", "en perfil" + new API(con).getStyle());
		//get selected style from database
		String style = new API(con).getStyle();
		//get system resources
		Resources res = getResources();

		if(style.equals("masculino"))
		{
			styleMain = res.getColor(R.color.MASCULINO_MAIN);
			styleDetail = res.getColor(R.color.MASCULINO_DETAIL);
		}
		if(style.equals("femenino"))
		{
			styleMain = res.getColor(R.color.FEMENINO_MAIN);
			styleDetail = res.getColor(R.color.FEMENINO_DETAIL);
		}
		if(style.equals("neutral"))
		{
			styleMain = res.getColor(R.color.NEUTRAL_MAIN);
			styleDetail = res.getColor(R.color.NEUTRAL_DETAIL);
		}

	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{
		//sets local style variables
		getStyle();
		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
		scroll.setBackgroundColor(styleMain);
		bar.setBackgroundColor(styleDetail);
		
		EditText mot = (EditText) findViewById(R.id.miPerfilMotivaciones);
		mot.setHintTextColor(Color.WHITE);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Toast.makeText(parent.getContext(), 
		        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
		        Toast.LENGTH_SHORT).show();
		ContentValues cv = new ContentValues();
		new DAO_Usuario(con).updateNivel(parent.getItemAtPosition(position).toString());

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	
}

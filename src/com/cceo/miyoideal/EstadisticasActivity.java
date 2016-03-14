package com.cceo.miyoideal;

import java.text.DecimalFormat;

import shared.ui.actionscontentview.ActionsContentView;

import com.cceo.DAO.DAO_Estadisticas;
import com.cceo.DAO.DAO_Usuario;
import com.cceo.DTO.DTO_Estadisticas;
import com.cceo.DTO.DTO_Usuario;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.MenuFragment;
import com.cceo.miyoideal.extra.MyArrayAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class EstadisticasActivity extends Activity{
	
	TextView textPesoActual;
	TextView textRecordPesoPerdido;
	TextView textPesoPerdidoTratamiento;
	TextView textPesoMinimo;
	TextView textPesoMaximo;
	TextView textTallaActual;
	TextView textTallaMaxima;
	TextView textTallaMinima;
	TextView textIMC;
	TextView textDiasConsecutivosDieta;
	TextView textDiasConsecutivosEjercicio;
	TextView textClasificacionIMC;
	
	//Style variables
	private int styleMain;
	private int styleDetail;
	
	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	
	private Context con;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline_estadisticas);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.menu_white);
		this.setTitle("		Estadísticas");
		
		con = this;
		
		setUpMenu();
		updateStyle();
		
		DAO_Estadisticas daoEstadisticas = new DAO_Estadisticas(this);
		DTO_Estadisticas dtoEstadisticas = daoEstadisticas.getEstadisticas();
		
		textPesoActual = (TextView) findViewById(R.id.textPesoActual);
		textRecordPesoPerdido = (TextView) findViewById(R.id.textRecordPesoPerdido);
		textPesoPerdidoTratamiento = (TextView) findViewById(R.id.textPesoPerdidoTratamiento);
		textPesoMinimo = (TextView) findViewById(R.id.textPesoMinimo);
		textPesoMaximo = (TextView) findViewById(R.id.textPesoMaximo);
		textTallaActual = (TextView) findViewById(R.id.textTallaActual);
		textTallaMaxima = (TextView) findViewById(R.id.textTallaMaximo);
		textTallaMinima = (TextView) findViewById(R.id.textTallaMinima);
		textIMC = (TextView) findViewById(R.id.textIMC);
		textDiasConsecutivosDieta = (TextView) findViewById(R.id.textDiasConsecutivosDieta);
		textDiasConsecutivosEjercicio = (TextView) findViewById(R.id.textDiasConsecutivosEjercicio);
		textClasificacionIMC = (TextView) findViewById(R.id.textClasificacionIMC);
		
		//Set the data to TextViews
		textPesoActual.setText(dtoEstadisticas.getPesoActual());
		textRecordPesoPerdido.setText(dtoEstadisticas.getRecordPesoPerdido());
		if(Float.parseFloat(dtoEstadisticas.getPesoPerdidoTratamiento()) == 0){
			textPesoPerdidoTratamiento.setText("--.-");
		}else{
			textPesoPerdidoTratamiento.setText(dtoEstadisticas.getPesoPerdidoTratamiento());
		}
		textPesoMinimo.setText(dtoEstadisticas.getPesoMinimo());
		textPesoMaximo.setText(dtoEstadisticas.getPesoMaximo());
		textTallaActual.setText(dtoEstadisticas.getTallaActual());
		textTallaMaxima.setText(dtoEstadisticas.getTallaMaxima());
		textTallaMinima.setText(dtoEstadisticas.getTallaMinima());
		textIMC.setText(getIMC());
		textDiasConsecutivosDieta.setText(dtoEstadisticas.getDiasConsecutivosDieta());
		textDiasConsecutivosEjercicio.setText(dtoEstadisticas.getDiasConsecutivosEjercicio());
		textClasificacionIMC.setText(getClasificacionIMC(getIMC()));
	}
	
	private String getClasificacionIMC(String imcClas) {
		// TODO Auto-generated method stub
		float imc = Float.valueOf(imcClas);
		
		if(imc <= 18)
			return "Peso bajo";
		if(imc > 18 && imc < 24.9)
			return "Normal";
		if(imc >= 25 && imc < 26.9)
			return "Sobrepeso";
		if(imc >= 27 && imc < 29.9)
			return "Obesidad grado I";
		if(imc >= 30 && imc < 39.9)
			return "Obesidad grado II";
		if(imc >= 40)
			return "Obesidad grado III";
		
		return "XX";
	}

	public String getIMC()
	{
		DTO_Usuario user = new DAO_Usuario(con).getUsuario();
		
		float imc = (float) (Integer.valueOf(user.getPeso())/Math.pow(Integer.valueOf(user.getEestatura()),2)) * (10000);
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		return String.valueOf(df.format(imc));
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
	
	private void setUpMenu()
	{
		viewActionsContentView = (ActionsContentView) findViewById(R.id.estadisticas_actionsContentView);

		viewActionsList = (ListView) findViewById(R.id.actions_simple);

		final String[] values = new String[] { 
				"Mi Perfil", 	//0 
				"Mi Dieta", 	//1
				"Mi Ejercicio", //2
				"Calendario", 	//3
				"Lista de Compras",	//4
				"Seleccionar Dieta",	//5
				"Antes y Despues", 	//6
				"Comparte",	//7 
				"Tip del Día", 	//8
				"Pregúntanos", 	//9
				"Tutorial",	//10
		"Disclaimer"};	//11

		final MyArrayAdapter adapter = new MyArrayAdapter(this,
				android.R.layout.simple_list_item_1, values);

		viewActionsList.setAdapter(adapter);
		viewActionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,
					long flags) {
				showActivity(position);

			}
		});

		if(!new API(con).getFacebookID().equals(""))
		{
			MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.simple_menu_fragment);
			fragment.setFacebookId(new API(con).getFacebookID());
			fragment.runImageAsyncTask();

			TextView facebook_name = (TextView) findViewById(R.id.tvLOL_simple);
			facebook_name.setText(new API(con).getFacebookName());

			Font f = new Font();					
			f.changeFontRaleway(con, facebook_name);
		}
		else
		{
			MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.simple_menu_fragment);
			fragment.setContext(con);
			fragment.setDefaultProfilePic();
		}
	}

	private void showActivity(int position) 
	{
		final Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(EstadisticasActivity.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(EstadisticasActivity.this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(EstadisticasActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(EstadisticasActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(EstadisticasActivity.this, ShoppingActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(EstadisticasActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 6:
			intent = new Intent(EstadisticasActivity.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;
		case 7:
			intent = new Intent(EstadisticasActivity.this, ShareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 8:
			//tip del dia (blog)
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(browserIntent);
			break;
		case 9:
			//preguntanos
			intent = getOpenFacebookIntent(con);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 10:
			intent = new Intent(EstadisticasActivity.this, TutorialActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);			
			break;
		case 11:
			intent = new Intent(EstadisticasActivity.this, DisclaimerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		default:
			return;
		}
	}
	
	public static Intent getOpenFacebookIntent(Context context) {

		try {
			context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1398816470330009"));
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/miyoideal"));
		}
	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{
		ScrollView mainLayout = (ScrollView) findViewById(R.id.estadisticasBody);
		
		//sets local style variables
		getStyle();
		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
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
		switch (item.getItemId()) 
		{
			case android.R.id.home:
				if(viewActionsContentView.isContentShown())
					viewActionsContentView.showActions();
				else
					viewActionsContentView.showContent();
			return true;

			default:
				Intent intent = new Intent(this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}

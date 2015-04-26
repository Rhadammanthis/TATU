package com.example.miyoideal;

import com.example.DAO.DAO_Estadisticas;
import com.example.DTO.DTO_Estadisticas;
import com.example.miyoideal.extra.API;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
	
	//Style variables
	private int styleMain;
	private int styleDetail;
	
	private Context con;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_estadisticas);
		con = this;
		
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
		textIMC.setText(dtoEstadisticas.getIMC());
		textDiasConsecutivosDieta.setText(dtoEstadisticas.getDiasConsecutivosDieta());
		textDiasConsecutivosEjercicio.setText(dtoEstadisticas.getDiasConsecutivosEjercicio());	
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
		ScrollView mainLayout = (ScrollView) findViewById(R.id.estadisticasBody);
		
		//sets local style variables
		getStyle();
		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
	}
}

package com.example.miyoideal;

import com.example.DAO.DAO_Estadisticas;
import com.example.DTO.DTO_Estadisticas;

import android.app.Activity;
import android.os.Bundle;
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
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_estadisticas);
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
}

package com.cceo.DTO;

public class DTO_Estadisticas {

	private int idEstadisticas;
	private String tallaMaxima;
	private String tallaMinima;
	private String tallaActual;
	private String pesoMaximo;
	private String pesoMinimo;
	private String pesoActual;
	private String imc;
	private String recordPesoPerdido;
	private String pesoPerdidoTratamiento;
	private String diasConsecutivosDieta;
	private String diasConsecutivosEjercicio;
	
	public DTO_Estadisticas(int id_estadisticas, float talla_maxima, float talla_minima, float talla_actual, float peso_maximo, float peso_minimo, 
			float peso_actual, float imc, float record_peso_perdido, float peso_perdido_tratamiento, int dias_consecutivos_dieta, int dias_consecutivos_ejercicio){
		this.idEstadisticas = id_estadisticas;
		this.tallaMaxima = Float.toString(talla_maxima);
		this.tallaMinima = Float.toString(talla_minima);
		this.tallaActual = Float.toString(talla_actual);
		this.pesoMaximo = Float.toString(peso_maximo);
		this.pesoMinimo = Float.toString(peso_minimo);
		this.pesoActual = Float.toString(peso_actual);
		this.imc = Float.toString(imc);
		this.recordPesoPerdido = Float.toString(record_peso_perdido);
		this.pesoPerdidoTratamiento = Float.toString(peso_perdido_tratamiento);
		this.diasConsecutivosDieta = Integer.toString(dias_consecutivos_dieta);
		this.diasConsecutivosEjercicio = Integer.toString(dias_consecutivos_ejercicio);
	}
	
	public int getIdEstadisticas(){
		return idEstadisticas;
	}
	
	public void setIdEstadisticas(int idEstadisticas){
		this.idEstadisticas = idEstadisticas;
	}
	public String getTallaMaxima(){
		return tallaMaxima;
	}
	
	public void setTallaMaxima(float tallaMaxima){
		this.tallaMaxima = Float.toString(tallaMaxima);
	}
	
	public String getTallaMinima(){
		return tallaMinima;
	}
	
	public void setTallaMinima(float tallaMinima){
		this.tallaMinima = Float.toString(tallaMinima);
	}
	
	public String getTallaActual(){
		return tallaActual;
	}
	
	public void setTallaActual(float tallaActual){
		this.tallaActual = Float.toString(tallaActual);
	}
	
	public String getPesoMaximo(){
		return pesoMaximo;
	}
	
	public void setPesoMaximo(float pesoMaximo){
		this.pesoMaximo = Float.toString(pesoMaximo);
	}
	
	public String getPesoMinimo(){
		return pesoMinimo;
	}
	
	public void setPesoMinimo(float pesoMinimo){
		this.pesoMinimo = Float.toString(pesoMinimo);
	}
	
	public String getPesoActual(){
		return pesoActual;
	}
	
	public void setPesoActual(float pesoActual){
		this.pesoActual = Float.toString(pesoActual);
	}
	
	public String getIMC(){
		return imc;
	}
	
	public void setIMC(float imc){
		this.imc = Float.toString(imc);
	}
	
	public String getRecordPesoPerdido(){
		return recordPesoPerdido;
	}
	
	public void setRecordPesoPerdido(float recordPesoPerdido){
		this.recordPesoPerdido = Float.toString(recordPesoPerdido);
	}
	
	public String getPesoPerdidoTratamiento(){
		return pesoPerdidoTratamiento;
	}
	
	public void setPesoPerdidoTratamiento(float pesoPerdidoTratamiento){
		this.pesoPerdidoTratamiento = Float.toString(pesoPerdidoTratamiento);
	}	
	
	public String getDiasConsecutivosDieta(){
		return diasConsecutivosDieta;
	}
	
	public void setDiasConsecutivosDieta(float diasConsecutivosDieta){
		this.diasConsecutivosDieta = Float.toString(diasConsecutivosDieta);
	}	
	
	public String getDiasConsecutivosEjercicio(){
		return diasConsecutivosEjercicio;
	}
	
	public void setDiasConsecutivosEjercicio(float diasConsecutivosEjercicio){
		this.diasConsecutivosEjercicio = Float.toString(diasConsecutivosEjercicio);
	}
}

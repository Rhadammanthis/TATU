package com.cceo.DTO;

public class DTO_Ejercicio {
	
	private String idEjercicio;
	private String idDIeta;
	private String fecha;
	private String diasActividad;

	public DTO_Ejercicio(String idDieta, String fecha, String diasActividad) {
		// TODO Auto-generated constructor stub
		this.idDIeta = idDieta;
		this.fecha = fecha;
		this.diasActividad = diasActividad;
	}
	
	public DTO_Ejercicio() {
		// TODO Auto-generated constructor stub
	}

	public String getIdEjercicio() {
		return idEjercicio;
	}
	public void setIdEjercicio(String idEjercicio) {
		this.idEjercicio = idEjercicio;
	}
	public String getIdDIeta() {
		return idDIeta;
	}
	public void setIdDIeta(String idDIeta) {
		this.idDIeta = idDIeta;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public String getDiasActividad() {
		return diasActividad;
	}
	public void setDiasActividad(String diasActividad) {
		this.diasActividad = diasActividad;
	}
}

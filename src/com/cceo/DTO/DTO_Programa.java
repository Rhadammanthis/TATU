package com.cceo.DTO;

public class DTO_Programa {
	
	private String id_programa;
	private String nivel;
	private String titulo;
	private String descripcion;
	private String hora;

	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	public String getId_programa() {
		return id_programa;
	}
	public void setId_programa(String id_programa) {
		this.id_programa = id_programa;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}

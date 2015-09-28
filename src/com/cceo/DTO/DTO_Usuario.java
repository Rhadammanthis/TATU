package com.cceo.DTO;

public class DTO_Usuario {
	
	private String id_usuario;
    private String nombre;
    private String peso;
    private String talla;
    private String nivel;
    private String sexo;
    private String edad;
    private String estatura;

	public DTO_Usuario(String id_usuario, String nombre, String peso,
			String talla, String nivel, String sexo, String edad, String estatura) {
		this.id_usuario = id_usuario;
		this.nombre = nombre;
		this.peso = peso;
		this.talla = talla;
		this.nivel = nivel;
		this.sexo = sexo;
		this.edad = edad;
		this.estatura = estatura;
	}
    
    public String getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPeso() {
		return peso;
	}
	public void setPeso(String peso) {
		this.peso = peso;
	}
	public String getTalla() {
		return talla;
	}
	public void setTalla(String talla) {
		this.talla = talla;
	}
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	
	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}
	
	public String getEestatura() {
		return estatura;
	}

	public void setEstatura(String eestatura) {
		this.edad = eestatura;
	}
}

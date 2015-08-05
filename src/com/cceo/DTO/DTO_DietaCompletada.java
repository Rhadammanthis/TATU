package com.cceo.DTO;

public class DTO_DietaCompletada {
	
    public DTO_DietaCompletada(String id_dieta_completada, String id_dieta,
			String peso, String talla, String fecha) {
		this.id_dieta_completada = id_dieta_completada;
		this.id_dieta = id_dieta;
		this.peso = peso;
		this.talla = talla;
		this.fecha = fecha;
	}
    
    public DTO_DietaCompletada()
    {
    	
    }
	
	private String id_dieta_completada;
	private String id_dieta;
    private String peso;
    private String talla;
    private String fecha;

    public String getId_dieta_completada() {
		return id_dieta_completada;
	}
	public void setId_dieta_completada(String id_dieta_completada) {
		this.id_dieta_completada = id_dieta_completada;
	}
	public String getId_dieta() {
		return id_dieta;
	}
	public void setId_dieta(String id_dieta) {
		this.id_dieta = id_dieta;
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
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


}

package com.cceo.DTO;

public class DTO_DietaIteration {
	
	public String getId_dieta_iteration() {
		return id_dieta_iteration;
	}
	public void setId_dieta_iteration(String id_dieta_iteration) {
		this.id_dieta_iteration = id_dieta_iteration;
	}
	public String getId_dieta() {
		return id_dieta;
	}
	public void setId_dieta(String id_dieta) {
		this.id_dieta = id_dieta;
	}
	public String getActividad() {
		return actividad;
	}
	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	private String id_dieta_iteration;
	private String id_dieta;
	private String actividad;

	public DTO_DietaIteration(String id_dieta_iteration, String id_dieta,
			String actividad) {
		super();
		this.id_dieta_iteration = id_dieta_iteration;
		this.id_dieta = id_dieta;
		this.actividad = actividad;
	}
}

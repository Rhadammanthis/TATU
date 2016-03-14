package com.cceo.DB;


public interface SQLiteFactory {
	
	public void initDietaDB();

	public void initComponenteDB();
	
	public void initControlDB();
	
	public void initProgramaDB();
	
	public void initTutorialControl();
	
	public void initRecommendationDB();
	
	public void initShoppingDB();
	
	public void initEstadisticasDB(String pesoActual, String tallaActual);
	
}

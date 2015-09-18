package com.cceo.miyoideal.extra;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Esta clase Font cambia el estilo de fuente
 * @author Triolabs
 * @Developer Raul Quintero Esparza
 * @Designer Ivan Padilla
 * @version 1.0
 */
public class Font {
	
	/** Se crea una instancia de Font 
 	 */
	public Font(){
		
	}
	
	/** Metodo changeFontIntro cambia la funete aun estlio de INTRO
	 * @param context contexto de la actividad
	 * @param textView textView que se cambiara la fuente
 	 */
	public void changeFontRaleway(Context context,TextView textView){
		Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Medium.ttf"); 
		textView.setTypeface(type);
	}
	
	/** Metodo changeFontIntro cambia la funete aun estlio de RALEWAY hEAVY
	 * @param context contexto de la actividad
	 * @param textView textView que se cambiara la fuente
 	 */
	public void changeFontRalewayHeavy(Context context,TextView textView){
		Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Raleway-Heavy_0.ttf"); 
		textView.setTypeface(type);
	}
	
	/** Metodo changeFontHelvetica cambia la funete aun estlio de Helvetica
	 * @param context contexto de la actividad 
	 * @param textView textView que se cambiara la fuente
 	 */
//	public void changeFontHelvetica(Context context,TextView textView){
//		Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Lato-Reg.ttf"); 
//		textView.setTypeface(type);
//	}
	
}

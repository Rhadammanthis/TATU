package com.example.miyoideal.extra;

import com.facebook.model.OpenGraphObject;

import android.net.Uri;

public class ShareContentManager {
	
	Uri photoUri;
	public ShareContentManager(Uri photoUri){
		this.photoUri = photoUri;
	}
	
	public OpenGraphObject loadContent(){
		OpenGraphObject diet = OpenGraphObject.Factory.createForPost("yo-ideal:diet");
		diet.setProperty("title", "Progreso de la Dieta");
		diet.setProperty("image", photoUri.toString());
		diet.setProperty("url", "https://facebook.com");
		diet.setProperty("description", "Mi dieta esta funcionando estupendamente");
		return diet;
	}
	
	public String getUri(){
		return photoUri.toString();
	}
}

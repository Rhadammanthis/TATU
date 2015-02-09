package com.example.miyoideal.extra;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.net.Uri;
import android.os.Environment;

public class PhotoManager{
	
	private File photoFile;
	private Uri photoUri;
	
	public File createFile() throws IOException{
		//Create an image file name 
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.US).format(new Date());
		File storageDir = Environment.getExternalStoragePublicDirectory(
	          Environment.DIRECTORY_PICTURES+"/YoIdeal");
		if(!storageDir.exists()){
			storageDir.mkdirs();
		}
		File image = new File(storageDir, timeStamp + ".jpg");
		photoUri = Uri.fromFile(image);
		return image;
	}
	
	public void setFile(File photoFile){
		this.photoFile = photoFile;
	}
	
	public File getFile(){
		return photoFile;
	}
	
	public void setUri(Uri photoUri){
		this.photoUri = photoUri;
	}
	public Uri getUri(){
		return photoUri;
	}
}
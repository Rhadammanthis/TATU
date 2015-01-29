package com.example.miyoideal.extra;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Environment;

public class PhotoManager{
	
	private File photoFile;
	private Uri photoUri;
	
	public File createFile() throws IOException{
		//Create an image file name 
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
	          Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */ 
				storageDir      /* directory */
		);
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
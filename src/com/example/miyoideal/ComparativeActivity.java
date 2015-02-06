package com.example.miyoideal;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class ComparativeActivity extends Activity{
	ImageView initialImage;
	ImageView finalImage;
	private File[] imageFiles;
	private Matrix matrix;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comparative);
		initialImage = (ImageView) findViewById(R.id.imageViewInicial);
		finalImage = (ImageView) findViewById(R.id.imageViewFinal);
		try {
			imageFiles = getTheTwoImages();
			if(imageFiles != null){
				//get screen size
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				Bitmap firstBitmap = BitmapFactory.decodeFile(imageFiles[0].getAbsolutePath());
				Bitmap lastBitmap = BitmapFactory.decodeFile(imageFiles[1].getAbsolutePath());
				//Verify the rotation
				matrix = verifyOrientation(imageFiles[0].getAbsolutePath());
				Bitmap rotatedBitmapFirst = Bitmap.createBitmap(firstBitmap, 0, 0,
                         firstBitmap.getWidth(), firstBitmap.getHeight(), matrix, true);
				matrix = verifyOrientation(imageFiles[1].getAbsolutePath());
				Bitmap rotatedBitmapLast = Bitmap.createBitmap(lastBitmap, 0, 0,
                        lastBitmap.getWidth(), lastBitmap.getHeight(), matrix, true);
				initialImage.setImageBitmap(rotatedBitmapFirst);
				initialImage.setScaleType(ScaleType.FIT_XY);
				finalImage.setImageBitmap(rotatedBitmapLast);
				finalImage.setScaleType(ScaleType.FIT_XY);				
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Get the two images
	private File[] getTheTwoImages() throws ParseException{
		int last, first;
		String[] namesList;
		File[] arrayImages;
		String state = Environment.getExternalStorageState();
		if(state.contentEquals(Environment.MEDIA_MOUNTED) || state.contentEquals(Environment.MEDIA_MOUNTED_READ_ONLY)){
			File yoIdealDirectory = Environment.getExternalStorageDirectory();
			File yoIdealDir = new File(yoIdealDirectory.getAbsolutePath()+"/Pictures/YoIdeal/");
			first = 0; 
			last = 0;
			namesList = yoIdealDir.list();
			if(namesList.length > 1){
				Date dateFirst = new SimpleDateFormat("yyyy-MM-dd_HH-mm").parse(namesList[first]);
				Date dateLast = new SimpleDateFormat("yyyy-MM-dd_HH-mm").parse(namesList[last]);
				for(int i = 0; i < namesList.length; i++){
					Date dateCompare = new SimpleDateFormat("yyyy-MM-dd_HH-mm").parse(namesList[i]);
					if(dateLast.compareTo(dateCompare) < 0){
						last = i;
						dateLast = dateCompare;
					}
					else if(dateFirst.compareTo(dateCompare) > 0){
						first = i;
						dateFirst = dateCompare;
					}
				}
				File firstImage = new File(yoIdealDir + "/" + namesList[first]);
				File lastImage = new File(yoIdealDir + "/" + namesList[last]);
				arrayImages = new File[] {firstImage, lastImage};
			}
			else{
				Toast.makeText(this, "No se encontraron fotos para hacer la comparacion", Toast.LENGTH_LONG).show();
				arrayImages = null;
			}
		}
		else{
			arrayImages = null;
		}
		return arrayImages;
	}
	
	private Matrix verifyOrientation(String path) throws IOException{
				 
		ExifInterface exif = new ExifInterface(path);
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
		Matrix matrix = new Matrix(); 
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
		    matrix.postRotate(90); 
		    break; 
		case ExifInterface.ORIENTATION_ROTATE_180:
		    matrix.postRotate(180); 
		    break; 
		case ExifInterface.ORIENTATION_ROTATE_270:
		    matrix.postRotate(270); 
		    break; 
		default: 
		    break; 
		}
		return matrix;
	}
}

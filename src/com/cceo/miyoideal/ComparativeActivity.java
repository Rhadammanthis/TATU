package com.cceo.miyoideal;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import shared.ui.actionscontentview.ActionsContentView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.MenuFragment;
import com.cceo.miyoideal.extra.MyArrayAdapter;

public class ComparativeActivity extends Activity{
	ImageView initialImage;
	ImageView finalImage;
	private File[] imageFiles;
	private Matrix matrix;
	Context con;
	
	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline_comparar);
		con = this;
		
		setUpMenu();
		
		initialImage = (ImageView) findViewById(R.id.imageViewInicial);
		finalImage = (ImageView) findViewById(R.id.imageViewFinal);
		
		try {
			imageFiles = getTheTwoImages();
			if(imageFiles != null){
				//Layout Parameters for modify
				LayoutParams paramsInitialImage = (LayoutParams) initialImage.getLayoutParams();
				LayoutParams paramsFinalImage = (LayoutParams) finalImage.getLayoutParams();
				DisplayMetrics displaymetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				int heightScreen = displaymetrics.heightPixels;
				paramsInitialImage.height = heightScreen/2;
				paramsFinalImage.height = heightScreen/2;
				initialImage.setLayoutParams(paramsInitialImage);
				finalImage.setLayoutParams(paramsFinalImage);
				
				//get screen size
				Display display = getWindowManager().getDefaultDisplay();
				Point size = new Point();
				display.getSize(size);
				//Get the two images on Bitmap object
				Bitmap firstBitmap = BitmapFactory.decodeFile(imageFiles[0].getAbsolutePath());
				Bitmap lastBitmap = BitmapFactory.decodeFile(imageFiles[1].getAbsolutePath());
				//Verify the orientation of the image
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Add the menu layout to the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.base_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//If the Logo clicked
		Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		return super.onOptionsItemSelected(item);
	}
	
	private void setUpMenu()
	{
		viewActionsContentView = (ActionsContentView) findViewById(R.id.comparative_actionsContentView);

		viewActionsList = (ListView) findViewById(R.id.actions_simple);
		
		final String[] values = new String[] { 
				"Mi Perfil", 	//0 
				"Mi Dieta", 	//1
				"Mi Ejercicio", //2
				"Calendario", 	//3
				"Preguntanos",	//4
				"Comparte", 	//5
				"Tips y Sujerencias",	//6 
				"Seleccionar Dieta", 	//7
				//"Comparativa", 	//
				"Disclaimer",	//8
				"Tutorial"};	//9

		final MyArrayAdapter adapter = new MyArrayAdapter(this,
				android.R.layout.simple_list_item_1, values);

		viewActionsList.setAdapter(adapter);
		viewActionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int position,
					long flags) {
				showActivity(position);

			}
		});
		
		if(!new API(con).getFacebookID().equals(""))
		{
			MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.simple_menu_fragment);
			fragment.setFacebookId(new API(con).getFacebookID());
			fragment.runImageAsyncTask();
			
			TextView facebook_name = (TextView) findViewById(R.id.tvLOL_simple);
			facebook_name.setText(new API(con).getFacebookName());
		}
	}
	
	private void showActivity(int position) 
	{
		final Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(ComparativeActivity.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(ComparativeActivity.this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(ComparativeActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(ComparativeActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(browserIntent);
			break;
		case 5:
			intent = new Intent(ComparativeActivity.this, ShareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 6:
			intent = getOpenFacebookIntent(con);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 7:
			intent = new Intent(ComparativeActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 8:
			intent = new Intent(ComparativeActivity.this, DisclaimerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 9:
			intent = new Intent(ComparativeActivity.this, TutorialActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;


		default:
			return;
		}
	}
	
	public static Intent getOpenFacebookIntent(Context context) {

		try {
			context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1398816470330009"));
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/miyoideal"));
		}
	}
	
	//Get the two images
	private File[] getTheTwoImages() throws ParseException, IOException{
		int last, first;
		String[] namesList;
		File[] arrayImages = null;
		String state = Environment.getExternalStorageState();
		if(state.contentEquals(Environment.MEDIA_MOUNTED) || state.contentEquals(Environment.MEDIA_MOUNTED_READ_ONLY)){
			File yoIdealDirectory = Environment.getExternalStorageDirectory();
			File yoIdealDir = new File(yoIdealDirectory.getAbsolutePath()+"/Pictures/YoIdeal/");
			//If the directory doesn't exists
			if(!yoIdealDir.exists()){
				yoIdealDir.mkdirs();
			}
			first = 0; 
			last = 0;
			namesList = yoIdealDir.list();
			if(namesList != null){
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
					//Toast.makeText(this, "No se encontraron fotos para hacer la comparacion", Toast.LENGTH_LONG).show();
					new AlertDialog.Builder(this) 
				    	.setTitle("Alerta") 
				    	.setMessage("No existen fotos, ¡Tomate algunas para observar tu progreso!") 
				    	.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() { 
				    		public void onClick(DialogInterface dialog, int which) { 
				    			// continue with delete
				    			
				    		} 
				    	}).setIcon(android.R.drawable.ic_dialog_alert) 
				        .show(); 
					arrayImages = null;
				}
			}else{
				new AlertDialog.Builder(this) 
		    	.setTitle("Alerta") 
		    	.setMessage("No existen fotos, ¡Tomate algunas para observar tu progreso!") 
		    	.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() { 
		    		public void onClick(DialogInterface dialog, int which) { 
		    			// continue with delete
		    			
		    		} 
		    	}).setIcon(android.R.drawable.ic_dialog_alert) 
		        .show(); 
			}
		}else{
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

package com.example.miyoideal;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import shared.ui.actionscontentview.ActionsContentView;


import com.example.DB.SQLiteUserDB;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.DialyNotificationReceiver;
import com.example.miyoideal.extra.MyService;
import com.example.miyoideal.extra.SideMenu;

import android.R.menu;
import android.support.v4.app.Fragment;
import android.support.v4.view.ActionProvider;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends Activity implements View.OnClickListener{
	
	//global variables;
	private int width;
	private int height;
	
	//layout components
	private LinearLayout linearLayout1;
	private Button button_MiPerfil;
	private Button button_MiEjercicio;
	private Button button_Calendario;
	private ActionsContentView viewActionsContentView;
	
	private Uri fileUri;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private Context cont;
	
	
	
	private ListView viewActionsList;
	
	private Uri mImageCaptureUri; // This needs to be initialized.
    static final int CAMERA_PIC_REQUEST = 1337; 
    private String filePath;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private File mediaFile;
    private CameraActivity activity;
    
    //REQUEST IMAGE CODE
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageThumbnail;
    private String mCurrentPhotoPath;

	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline4);
		cont = this;
		
		//Clear the notification
		cancelNotification(this, 001);
				
		//Adapt the layout depends screen
    	Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		linearLayout1.getLayoutParams().width = (width/2);
		button_MiPerfil = (Button) findViewById(R.id.buttonHome);
		button_MiPerfil.setOnClickListener(this);
		
		//
		viewActionsContentView = (ActionsContentView) findViewById(R.id.home_actionsContentView);
		viewActionsList = (ListView) findViewById(R.id.actions);
		setUpMenu();	
		Intent[] mSharedIntents = new Intent[]{getEmailIntent()};
		
		SQLiteUserDB dbUser = new SQLiteUserDB(this);
		dbUser.getReadableDatabase();
		String query = "Select * FROM " + "usuario" + " WHERE " + "id_usuario" + " =  \"" + "1" + "\"";
		Cursor cursor = dbUser.getReadableDatabase().rawQuery(query, null);
		if(cursor.moveToFirst()){
			cursor.moveToFirst();
			this.setTitle(cursor.getString(1));
		}
}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
    	super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			if(imageBitmap != null){
				//Set the image thumbnail on the Dialog
				//imageThumbnail.setImageBitmap(imageBitmap);
			}
		} 
		else { 
			Toast.makeText(this, "Image Capture Failed", Toast.LENGTH_SHORT) .show(); 
		}
		Log.d("HomeActivity", "Termino");
	}
	
	
	public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setUpMenu()
	{
		final String[] values = new String[] { "Mi Perfil", "Mi Dieta", "Mi Ejercicio", 
	    		"Mas Dietas", "Calendario", "Estadisticas", "Preguntanos",
	    		"Comparte", "Tips y Sujerencias", "Recordatorios", "Seleccionar Dieta"};
	    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	        android.R.layout.simple_list_item_1, android.R.id.text1, values);

	    viewActionsList.setAdapter(adapter);
	    viewActionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		      @Override
		      public void onItemClick(AdapterView<?> adapter, View v, int position,
		          long flags) {
		    	  showActivity(position);
		
		      }
	    });
	}
	
	private void showActivity(int position) 
	{
	    final Intent intent;
	    switch (position) {
		    case 0:
		    	intent = new Intent(HomeActivity.this, MiPerfilActivity.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
		      break;
		    case 1:
		    	intent = new Intent(HomeActivity.this, DietaActivity.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
		      break;
		    case 2:
		    	intent = new Intent(HomeActivity.this, EjercicioActivity.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
		      break;
		    case 4:
		    	intent = new Intent(HomeActivity.this, CalendarioActivity.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
		      break;
		    case 10:
		    	intent = new Intent(HomeActivity.this, SelecDieta.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
		      break;
		
		    default:
		      return;
	    }
	}

	private Intent getEmailIntent() {
		
		String to = "foo@bar.com";
		String subject = "yo dude";
		String body = "Here's an email body";
	
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		String[] toArr = new String[] { to };
		intent.putExtra(Intent.EXTRA_EMAIL, toArr);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, body);
		return intent;
	}
	
	//When the user clicks on the back-key 
	@Override
	public void onBackPressed(){
		moveTaskToBack(true);
	}
	
	//When click on the button for take the photo
	@Override
	public void onClick(View v){
		Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
		Dialog dialog = new Dialog(cont);
        dialog.setContentView(R.layout.baseline_share);
        dialog.setTitle("Compartir");

        Button save = (Button)dialog.findViewById(R.id.buttonGuardar);
        dialog.show();
        imageThumbnail = (ImageView) dialog.findViewById(R.id.foto);
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	// Camera exists? Then proceed... 
            	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            	if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            		File photoFile = null;
            		try{
            			//Create the Image File
            			photoFile = createImageFile();
            		}catch(IOException e){
            			e.printStackTrace();
            		}
            		//Continue only when the ImageFile was created
            		if(photoFile != null){
            			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            		}
            	}
            }
        });
	}
	
	private File createImageFile() throws IOException{
		 // Create an image file name 
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */ 
	        storageDir      /* directory */
	    ); 
	 
	    // Save a file: path for use with ACTION_VIEW intents 
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
	    return image;
	}
	
	//Clear the Notification
	private void cancelNotification(Context context, int notifyId) {
	    String notificationService = Context.NOTIFICATION_SERVICE;
	    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(notificationService);
	    mNotificationManager.cancel(notifyId);
	} 
}

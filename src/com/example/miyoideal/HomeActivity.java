package com.example.miyoideal;

import java.io.File;
import java.io.IOException;

import shared.ui.actionscontentview.ActionsContentView;


import com.example.DB.SQLiteUserDB;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.MyService;
import com.example.miyoideal.extra.SideMenu;

import android.R.menu;
import android.support.v4.app.Fragment;
import android.support.v4.view.ActionProvider;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.app.Activity;
import android.app.Dialog;
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

public class HomeActivity extends Activity {
	
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
	
	private ImageView dialogIV;
	
	private ListView viewActionsList;
	
	private Uri mImageCaptureUri; // This needs to be initialized.
    static final int CAMERA_PIC_REQUEST = 1337; 
    private String filePath;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private File mediaFile;
    private CameraActivity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline4);
		
		cont = this;
				
		mediaFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Temp1.jpg");
		filePath = mediaFile.getAbsolutePath();
		
		//Calculates Screen size
    	Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		
		dialogIV = (ImageView) findViewById(R.id.foto);
		
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		
		linearLayout1.getLayoutParams().width = (width/2);

		button_MiPerfil = (Button) findViewById(R.id.buttonHome);
		button_MiPerfil.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Dialog dialog = new Dialog(cont);

                dialog.setContentView(R.layout.baseline_share);
                dialog.setTitle("Compartir");

                Button save=(Button)dialog.findViewById(R.id.buttonGuardar);
                dialog.show();
                
                save.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                    	
                    	/*// Camera exists? Then proceed... 
                    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
                    	// Ensure that there's a camera activity to handle the intent 
                    	activity = new CameraActivity();

                    		// Create the File where the photo should go. 
                    		// If you don't do this, you may get a crash in some devices. 
                    		File photoFile = null; 
                    		try 
                    		{ 
								photoFile = new File(Environment.getExternalStorageDirectory() + File.separator + "share_image.png");
								photoFile.createNewFile();
                    		} catch (IOException ex) { 
                    			// Error occurred while creating the File 
                    			Toast toast = Toast.makeText(activity, "There was a problem saving the photo...", Toast.LENGTH_SHORT); toast.show(); 
                    			} 
                    		// Continue only if the File was successfully created 
                    		if (photoFile != null) 
                    		{ 
                    			Uri fileUri = Uri.fromFile(photoFile); 
                    			activity.setCapturedImageURI(fileUri); 
                    			activity.setCurrentPhotoPath(fileUri.getPath()); 
                    			//takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, activity.getCapturedImageURI()); 
                    			startActivityForResult(takePictureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE); 
                    			} */
                    		}
                    		
                
            
                    	/*String path = Environment.getExternalStorageDirectory() + "/photo1.jpg";
                    	File file = new File(path);
                        fileUri = Uri.fromFile(file);
                        Intent intent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);*/
                    
                });
				
			}
		});

		
		viewActionsContentView = (ActionsContentView) findViewById(R.id.home_actionsContentView);
				
		viewActionsList = (ListView) findViewById(R.id.actions);
		
		setUpMenu();	
		
		Intent[] mSharedIntents = new Intent[]{getEmailIntent()};
		
		SQLiteUserDB dbUser = new SQLiteUserDB(this);
		dbUser.getReadableDatabase();
		String query = "Select * FROM " + "usuario" + " WHERE " + "id_usuario" + " =  \"" + "1" + "\"";
		Cursor cursor = dbUser.getReadableDatabase().rawQuery(query, null);
		if(cursor.moveToFirst())
		{
			cursor.moveToFirst();
			this.setTitle(cursor.getString(1));
		}
		
		//checks if the final dieta day has arrived
		
		/*Handler handler = new Handler() {
		    @Override
		    public void handleMessage(Message msg) {
		            Bundle reply = msg.getData();
		            reply.putInt("dia", 2);
		                            // do whatever with the bundle here
		            }
		};*/
		
		/*Intent intent = new Intent(this, MyService.class);
		intent.putExtra("messenger", new Messenger(handler));
		startService(intent);*/
		
		//stopService(intent);

		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) { 
			//CameraActivity activity = (CameraActivity)getActivity(); 
			// Show the full sized image. 
			//setFullImageFromFilePath(activity.getCurrentPhotoPath(), mImageView); 
			//setFullImageFromFilePath(activity.getCurrentPhotoPath(), mThumbnailImageView); 
			dialogIV.setImageBitmap((Bitmap) data.getExtras().get("data"));
			//dialogIV.setImageURI(activity.getCapturedImageURI());
			} 
		else { 
			Toast.makeText(this, "Image Capture Failed", Toast.LENGTH_SHORT) .show(); 
			}
		
		/*try {
			if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
		        if (resultCode == RESULT_OK) {
		        	//Bitmap photo = (Bitmap) data.getExtras().get("data"); 
		        	//dialogIV.setImageBitmap(photo);
		        	dialogIV.setImageURI(Uri.parse((fileUri).toString()));
		            // Image captured and saved to fileUri specified in the Intent
		        	//Toast.makeText(this, "Image saved to:\n" +
		            //         data.getData(), Toast.LENGTH_LONG).show();
		        } else if (resultCode == RESULT_CANCELED) {
		            // User cancelled the image capture
		        } else {
		            // Image capture failed, advise user
		        }
		    }

		    if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
		        if (resultCode == RESULT_OK) {
		            // Video captured and saved to fileUri specified in the Intent
		           
		        } else if (resultCode == RESULT_CANCELED) {
		            // User cancelled the video capture
		        } else {
		            // Video capture failed, advise user
		        }
		    }

        } catch (Exception e) {
            Toast.makeText(this, "Picture Not taken",
                            Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }*/
	    
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
}

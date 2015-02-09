package com.example.miyoideal;

import java.io.File;
import java.io.IOException;

import com.example.miyoideal.extra.PhotoManager;
import com.example.miyoideal.extra.ShareContentManager;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.scrumptious.ScrumptiousApplication;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.OpenGraphActionDialogBuilder;

//import com.facebook.scrumptious.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;

public class ShareActivity extends Activity implements View.OnClickListener {
	PhotoManager photoManager;
	
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    private final int PHOTO_TAKED = 1;
    private final int GALLERY_OPEN = 2;
    
    private File photoFile;
    
    //Facebook callback variable
    private UiLifecycleHelper uiHelper;
    
    private Button facebookButton;
    private Uri tempUri;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            Log.d("ShareActivity", "Callback");
        }
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_share);
    	uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
		facebookButton = (Button) findViewById(R.id.shareFacebook);
		facebookButton.setOnClickListener(this);
    	//Uri uri = (Uri) bundle.get("URI");
    }
    
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == GALLERY_OPEN){
	    	if(data != null){
	    		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	    	        @Override
	    	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	    	            Log.d("Activity", String.format("Error: %s", error.toString()));
	    	        }
	    	        @Override
	    	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	    	            Log.d("Activity", "Success!");
	    	        }
	    	    });
	    		//Instance the object with the picture
	    		ShareContentManager shareContentManager = new ShareContentManager(data.getData());
	    		createFacebookDialog(shareContentManager);
	    	}
	    	else{
	    		Toast.makeText(this, "Se produjo un error al cargar la imagen", Toast.LENGTH_LONG).show();
	    	}
    	}
    	else if(requestCode == PHOTO_TAKED){
    		if(resultCode == RESULT_OK) {
	    		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
	    	        @Override
	    	        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
	    	            Log.d("Activity", String.format("Error: %s", error.toString()));
	    	        }
	    	        @Override
	    	        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
	    	            Log.d("Activity", "Success!");
	    	        }
	    	    });
	    		//Instance the object with the picture
	    		ShareContentManager shareContentManager = new ShareContentManager(photoManager.getUri());
	    		createFacebookDialog(shareContentManager);
	    		//Toast.makeText(this, "Foto Almacenada en la galeria", Toast.LENGTH_SHORT).show();
			} 
			else { 
				Toast.makeText(this, "No se logro almacenar la imagen", Toast.LENGTH_SHORT).show(); 
			}
			Log.d("HomeActivity", "Termino");
    	}
	}
    
    @Override
	public void onClick(View v) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);
        CharSequence camera = getResources().getString(R.string.action_photo_camera);
        CharSequence gallery = getResources().getString(R.string.action_photo_gallery);
        builder.setCancelable(true).
                setItems(new CharSequence[] {camera, gallery}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == CAMERA) {
                            try {
								startCamera();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        } else if (i == GALLERY) {
                        	startGallery();
                        } 
                    }
                });
        builder.show();	
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
    
    
    
    public void createFacebookDialog(ShareContentManager shareContentManager){
    	/*if (FacebookDialog.canPresentOpenGraphActionDialog(this,FacebookDialog.OpenGraphActionDialogFeature.OG_ACTION_DIALOG)){
		    OpenGraphObject diet = shareContentManager.loadContent(); 
			OpenGraphAction action = GraphObject.Factory.create(OpenGraphAction.class);
			action.setProperty("diet", diet);
			action.setType("yo-ideal:diet");
			FacebookDialog.OpenGraphActionDialogBuilder shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(this, action, "diet");
			shareDialog.build().present();
			//uiHelper.trackPendingDialogCall(shareDialog.present());
    	}else { 
            Toast.makeText(this, "Facebook not available", Toast.LENGTH_SHORT).show();
        }
        */
    	FacebookDialog.ShareDialogBuilder builder = new FacebookDialog.ShareDialogBuilder(this)
        .setLink("http://facebook.com")
        .setName("Yo Ideal")
        .setPicture(shareContentManager.getUri());
    	
        if (builder.canPresent()) {
                builder.build().present();
            }
    }
        
    //Set the extras for ShareActivity and start the camera
  	public void startCamera() throws IOException{
  		photoManager = new PhotoManager();
  		File photoFile = photoManager.createFile();
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if(takePictureIntent.resolveActivity(getPackageManager()) != null){
			//photoFile = createImageFile();
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoManager.getUri());
	    	//Save the image uri for later use
	  		startActivityForResult(takePictureIntent, PHOTO_TAKED);
	    }	
  	}
  	
    //Set the extras for ShareActivity and start the gallery
  	public void startGallery(){
  		tempUri = null;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        String selectPicture = getResources().getString(R.string.select_picture);
        startActivityForResult(Intent.createChooser(intent, selectPicture), GALLERY_OPEN);
  	}	
}

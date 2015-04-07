package com.example.miyoideal;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;


import java.io.File;
import java.io.IOException;

import com.example.miyoideal.extra.PhotoManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.provider.MediaStore;

public class ShareActivity extends Activity implements View.OnClickListener {
	PhotoManager photoManager;
	
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    private final int PHOTO_TAKED = 1;
    private final int GALLERY_OPEN = 2;
    
    private File photoFile;
    
    private Button facebookButton;
    private Uri tempUri;
    private Uri photoUri;
    private ShareDialog shareDialog;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_share);
    	
    	shareDialog = new ShareDialog(this);
    	facebookButton = (Button) findViewById(R.id.shareFacebook);
    	facebookButton.setOnClickListener(this);
    	//Uri uri = (Uri) bundle.get("URI");
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == GALLERY_OPEN){
	    	if(data != null){
	    		//Instance the object with the picture
		    	SharePhoto photo = new SharePhoto.Builder()
		    		.setImageUrl(data.getData())
					.setUserGenerated(false)
					.build();
				SharePhotoContent content = new SharePhotoContent.Builder()
					.addPhoto(photo)
					
					//.setContentUrl(Uri.parse("http://cceo.com.mx"))
					.setRef("http://cceo.com.mx")
					.build();
	    		
//	    		ShareLinkContent content = new ShareLinkContent.Builder()
//	    			.setContentTitle("Yo ideal")
//	    			.setContentDescription("Con esta aplicacion puedo adelgazar")
//	    			.setImageUrl(data.getData())
//	    			.setContentUrl(Uri.parse("http://cceo.com.mx"))
//	    			.build();
//	    			
				createFacebookDialog(content);
	    	}
	    	else{
	    		Toast.makeText(this, "Fallo", Toast.LENGTH_LONG).show();
	    	}
    	}
    	else if(requestCode == PHOTO_TAKED){
    		if(resultCode == RESULT_OK) {
    			SharePhoto photo = new SharePhoto.Builder()
    				.setImageUrl(photoUri)
    				.setUserGenerated(true)
    				.build();
    			SharePhotoContent content = new SharePhotoContent.Builder()
    				.addPhoto(photo)
    				.setRef("MiYoIdeal")
    				.build();
    			createFacebookDialog(content);
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
    
    //Set the extras for ShareActivity and start the camera
  	public void startCamera() throws IOException{
  		photoManager = new PhotoManager();
  		photoFile = photoManager.createFile();
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if(takePictureIntent.resolveActivity(getPackageManager()) != null){
			//photoFile = createImageFile();
			photoUri = photoManager.getUri();
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
  	
  	public void createFacebookDialog(ShareContent content){
    	if(ShareDialog.canShow(ShareLinkContent.class)){
    		Toast.makeText(this, "Entro", Toast.LENGTH_LONG).show();
    		shareDialog.show(content);
    	}
    }
}
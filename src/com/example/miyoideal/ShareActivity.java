package com.example.miyoideal;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusOneButton;
import com.google.android.gms.plus.PlusShare;


import java.io.File;
import java.io.IOException;
import java.net.URL;

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
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

public class ShareActivity extends Activity implements View.OnClickListener {
	PhotoManager photoManager;
	
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    
    private static final String GOOGLE_PLUS_URL = "https://plus.google.com/+AmauryEsparza/posts";
    
    private static final int TWITTER_BUTTON = R.id.shareTwitter;
    private static final int FACEBOOK_BUTTON = R.id.shareFacebook;
    private static final int GOOGLEPLUS_BUTTON = R.id.shareGPlus;
    private final int FACEBOOK_CAMERA_CODE = 1;
    private final int FACEBOOK_GALLERY_CODE = 2;
    private final int TWITTER_CAMERA_CODE = 3;
    private final int TWITTER_GALLERY_CODE = 4;
    private final int GOOGLEPLUS_GALLERY_CODE = 5;
    private final int GOOGLEPLUS_CAMERA_CODE = 6;
        
    private File photoFile;
    
    private Button facebookButton;
    private Button twitterButton;
    private Button googlePlusButton;
    private PlusOneButton mPlusOneButton;
    
    private Uri tempUri;
    private Uri photoUri;
    private ShareDialog shareDialog;
    //Facebook Share Button
    ShareButton shareFacebookButton;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_share);
    	
    	Fabric.with(this, new TweetComposer());
    	shareDialog = new ShareDialog(this);
    	shareFacebookButton = (ShareButton) findViewById(FACEBOOK_BUTTON);
    	shareFacebookButton.setOnClickListener(this);
    	
    	twitterButton = (Button) findViewById(TWITTER_BUTTON);
    	twitterButton.setOnClickListener(this);
    	
    	googlePlusButton = (Button) findViewById(GOOGLEPLUS_BUTTON);
    	googlePlusButton.setOnClickListener(this);
    	    
    	mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);
    	//AQUII
    	LikeView likeView = (LikeView) findViewById(R.id.like_facebook);
    	likeView.setObjectIdAndType(
    		    "https://www.facebook.com/miyoideal",
    		    LikeView.ObjectType.PAGE);
    	//Uri uri = (Uri) bundle.get("URI");
    }
    
    protected void onResume() {
        super.onResume();
        // Actualiza el estado del botón +1 cada vez que se enfoque la actividad.
        mPlusOneButton.initialize(GOOGLE_PLUS_URL, 0);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode == FACEBOOK_GALLERY_CODE && data != null){
    		//Gallery
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
			createFacebookDialog(content);	
    	}
    	else if(requestCode == FACEBOOK_CAMERA_CODE && resultCode == RESULT_OK){
			//Photo taked
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
		else if(requestCode == TWITTER_CAMERA_CODE && resultCode == RESULT_OK){
			TweetComposer.Builder builder = new TweetComposer.Builder(this)
   	     		.text("Me siento fantastico con #YoIdeal")
   	     		.image(photoUri);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
    		builder.show();
    	}
    	else if(requestCode == TWITTER_GALLERY_CODE){
    		TweetComposer.Builder builder = new TweetComposer.Builder(this)
	     		.text("Me siento fantastico con #YoIdeal")
	     		.image(data.getData());
	     	builder.show();
    	}
    	else if(requestCode == GOOGLEPLUS_CAMERA_CODE && resultCode == RESULT_OK){
    		Intent shareIntent = new PlusShare.Builder(this)
		        .setType("text/plain")
		        .setContentUrl(Uri.parse("http://cceo.com.mx"))
		        .setText("Me siento genial con #YoIdeal")
		        .setStream(photoUri)
		        .getIntent();
    		startActivityForResult(shareIntent, 0);
    	}
    	else if(requestCode == GOOGLEPLUS_GALLERY_CODE){
    		Intent shareIntent = new PlusShare.Builder(this)
		        .setType("text/plain")
		        .setContentUrl(Uri.parse("http://cceo.com.mx"))
		        .setText("Me siento genial gracias a #YoIdeal")
		        .setStream(data.getData())
		        .getIntent();
    		startActivityForResult(shareIntent, 0);
    	}
	}
    
    @Override
	public void onClick(View v) {
		//For Twitter, Facebook or Google+ share buttons
    	final int buttonId = v.getId();
    	AlertDialog.Builder builder = new AlertDialog.Builder(ShareActivity.this);
	    CharSequence camera = getResources().getString(R.string.action_photo_camera);
        CharSequence gallery = getResources().getString(R.string.action_photo_gallery);
        builder.setCancelable(true).
                setItems(new CharSequence[] {camera, gallery}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == CAMERA) {
                            try {
								if(buttonId == FACEBOOK_BUTTON){
									startCamera(FACEBOOK_CAMERA_CODE);
								}
								else if(buttonId == TWITTER_BUTTON){
									startCamera(TWITTER_CAMERA_CODE);
								}
								else if(buttonId == GOOGLEPLUS_BUTTON){
									startCamera(GOOGLEPLUS_CAMERA_CODE);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        } else if (i == GALLERY) {
                        	if(buttonId == FACEBOOK_BUTTON){
                        		startGallery(FACEBOOK_GALLERY_CODE);
                        	}else if(buttonId == TWITTER_BUTTON){
                        		startGallery(TWITTER_GALLERY_CODE);
                        	}else if(buttonId == GOOGLEPLUS_BUTTON){
                        		startGallery(GOOGLEPLUS_GALLERY_CODE);
                        	}	
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
  	public void startCamera(int requestCode) throws IOException{
  		photoManager = new PhotoManager();
  		photoFile = photoManager.createFile();
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if(takePictureIntent.resolveActivity(getPackageManager()) != null){
			//photoFile = createImageFile();
			photoUri = photoManager.getUri();
	    	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoManager.getUri());
	    	//Save the image uri for later use
	  		startActivityForResult(takePictureIntent, requestCode);
	    }	
  	}
  	
    //Set the extras for ShareActivity and start the gallery
  	public void startGallery(int requestCode){
  		tempUri = null;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        String selectPicture = getResources().getString(R.string.select_picture);
        startActivityForResult(Intent.createChooser(intent, selectPicture), requestCode);
  	}	
  	
  	public void createFacebookDialog(ShareContent content){
    	shareDialog.show(content);
  		/*if(ShareDialog.canShow(ShareContent.class)){
    		shareDialog.show(content);
    	}
    	else{
    		Toast.makeText(this, "Imposible publicar en este momento", Toast.LENGTH_LONG).show();
    	}*/
    }
}
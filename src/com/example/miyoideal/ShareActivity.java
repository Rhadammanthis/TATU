package com.example.miyoideal;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.PhotoManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.provider.MediaStore;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

public class ShareActivity extends Activity implements View.OnClickListener {
	PhotoManager photoManager;
	
    private static final int CAMERA = 0;
    private static final int GALLERY = 1;
    
    
    private static final int TWITTER_BUTTON = R.id.shareTwitter;
    private static final int FACEBOOK_BUTTON = R.id.shareFacebook;
    private final int FACEBOOK_CAMERA_CODE = 1;
    private final int FACEBOOK_GALLERY_CODE = 2;
    private final int TWITTER_CAMERA_CODE = 3;
    private final int TWITTER_GALLERY_CODE = 4;
        
    private File photoFile;
    
    private Button facebookButton;
    private Button twitterButton;
    private Uri tempUri;
    private Uri photoUri;
    private ShareDialog shareDialog;
    //Facebook Share Button
    ShareButton shareFacebookButton;
    
	//Style variables
	private int styleMain;
	private int styleDetail;
	
	private Context con;
    
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_share);
    	
    	con = this;
    	updateStyle();
    	
    	Fabric.with(this, new TweetComposer());
    	shareDialog = new ShareDialog(this);
    	shareFacebookButton = (ShareButton) findViewById(FACEBOOK_BUTTON);
    	shareFacebookButton.setOnClickListener(this);
    	
    	twitterButton = (Button) findViewById(TWITTER_BUTTON);
    	twitterButton.setOnClickListener(this);
    	
    	//AQUII
    	LikeView likeView = (LikeView) findViewById(R.id.like_facebook);
    	likeView.setObjectIdAndType(
    		    "https://www.facebook.com/miyoideal",
    		    LikeView.ObjectType.PAGE);
    	//Uri uri = (Uri) bundle.get("URI");
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
								else{
									startCamera(TWITTER_CAMERA_CODE);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        } else if (i == GALLERY) {
                        	if(buttonId == FACEBOOK_BUTTON){
                        		startGallery(FACEBOOK_GALLERY_CODE);
                        	}else{
                        		startGallery(TWITTER_GALLERY_CODE);
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
  	
	//Saves selected style colors to local variables
	private void getStyle()
	{
		Log.d("color", "en perfil" + new API(con).getStyle());
		//get selected style from database
		String style = new API(con).getStyle();
		//get system resources
		Resources res = getResources();

		if(style.equals("masculino"))
		{
			styleMain = res.getColor(R.color.MASCULINO_MAIN);
			styleDetail = res.getColor(R.color.MASCULINO_DETAIL);
		}
		if(style.equals("femenino"))
		{
			styleMain = res.getColor(R.color.FEMENINO_MAIN);
			styleDetail = res.getColor(R.color.FEMENINO_DETAIL);
		}
		if(style.equals("neutral"))
		{
			styleMain = res.getColor(R.color.NEUTRAL_MAIN);
			styleDetail = res.getColor(R.color.NEUTRAL_DETAIL);
		}

	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{
		RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.shareMain);
		
		//sets local style variables
		getStyle();
		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
	}
}
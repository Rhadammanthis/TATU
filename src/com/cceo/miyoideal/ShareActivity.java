package com.cceo.miyoideal;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.json.JSONObject;

import shared.ui.actionscontentview.ActionsContentView;

import com.cceo.DB.SQLiteControl;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.ImageAsyncTask;
import com.cceo.miyoideal.extra.MenuFragment;
import com.cceo.miyoideal.extra.MyArrayAdapter;
import com.cceo.miyoideal.extra.PhotoManager;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;

import android.content.ActivityNotFoundException;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
	private ImageButton twitterButton;
	private ImageButton googlePlusButton;
	//private PlusOneButton mPlusOneButton;
	private Button twitterFollowButton;
	private Uri tempUri;
	private Uri photoUri;
	private ShareDialog shareDialog;
	//Facebook Share Button
	ShareButton shareFacebookButton;
	
	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;


	//Style variables
	private int styleMain;
	private int styleDetail;

	private Context con;
	private CallbackManager callbackManager;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline_compartir);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.actionbar_icon_white);

		con = this;
		setUpMenu();
		updateStyle();

		FacebookSdk.sdkInitialize(getApplicationContext());

		//Fabric.with(this, new TweetComposer());
		shareDialog = new ShareDialog(this);
		shareFacebookButton = (ShareButton) findViewById(FACEBOOK_BUTTON);
		shareFacebookButton.setOnClickListener(this);

		twitterButton = (ImageButton) findViewById(TWITTER_BUTTON);
		twitterButton.setOnClickListener(this);

		twitterFollowButton = (Button) findViewById(R.id.follow_button);
		twitterFollowButton.setOnClickListener(this);

		googlePlusButton = (ImageButton) findViewById(GOOGLEPLUS_BUTTON);
		googlePlusButton.setOnClickListener(this);

		//mPlusOneButton = (PlusOneButton) findViewById(R.id.plus_one_button);

		//AQUII
		LikeView likeView = (LikeView) findViewById(R.id.like_facebook);
		likeView.setObjectIdAndType(
				"https://www.facebook.com/miyoideal",
				LikeView.ObjectType.PAGE);


		callbackManager = CallbackManager.Factory.create();
		// Callback registration

		LoginButton loginButton = (LoginButton) findViewById(R.id.share_facebook_login);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!new API(con).getFacebookID().equals(""))
				{
					Log.d("maneling","eliminar datos");

					//lo elimino
					ContentValues cv = new ContentValues();
					cv.put("fb_id", "");
					cv.put("fb_name", "");

					SQLiteControl db = new SQLiteControl(con);
					db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
					db.close();

					Log.d("maneling","ID " + (new API(con).getFacebookID()));
					Log.d("maneling","NAme " + (new API(con).getFacebookName()));
				}

				//				if(new API(con).getFacebookID().equals(""))
				//					LoginManager.getInstance().logInWithReadPermissions(ShareActivity.this, (Arrays.asList("public_profile", "user_friends")));//,"user_birthday","user_about_me","email")));
			}
		});

		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				// App code
				final AccessToken accessToken = loginResult.getAccessToken();
				Log.d("maneling","boton clicked");
				GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject user, GraphResponse graphResponse) {
						Log.d("CONTRA", "Mail" + user.optString("email"));
						Log.d("CONTRA", "Name " + user.optString("name"));
						Log.d("CONTRA", "ID " + user.optString("id"));
						Log.d("CONTRA", "First name " + user.optString("first_name"));

						//si no tengo id de Facebook
						if(new API(con).getFacebookID().equals(""))
						{
							Log.d("maneling","guardar datos");
							//lo agrego
							String[] name = user.optString("name").split(" ");

							ContentValues cv = new ContentValues();
							cv.put("fb_id", user.optString("id"));
							cv.put("fb_name", name[0]);

							SQLiteControl db = new SQLiteControl(con);
							db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
							db.close();

							Log.d("maneling","ID " + (new API(con).getFacebookID()));
							Log.d("maneling","NAme " + (new API(con).getFacebookName()));
						}
						//si tengo id de facebook
						//						else
						//						{
						//							Log.d("maneling","eliminar datos");
						//
						//							//lo elimino
						//							ContentValues cv = new ContentValues();
						//							cv.put("fb_id", "");
						//							cv.put("fb_name", "");
						//
						//							SQLiteControl db = new SQLiteControl(con);
						//							db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
						//							db.close();
						//
						//							Log.d("maneling","ID " + (new API(con).getFacebookID()));
						//							Log.d("maneling","NAme " + (new API(con).getFacebookName()));
						//
						//							//							Intent intent = new Intent(ShareActivity.this, MainActivity.class);
						//							//							startActivity(intent);
						//						}

						//cal Aynk
						//						ImageAsyncTask programming = new ImageAsyncTask();
						//						programming.setId(user.optString("id"));
						//						programming.execute();

						//						SQLiteControl db = new SQLiteControl(con);
						//						
						//						db.getWritableDatabase();
						//						
						//						Log.d("db", "Control " + db.getDatabaseName());
						//						
						//						String[] name = user.optString("name").split(" ");
						//
						//						ContentValues values = new ContentValues();
						//						values.put("id_control", "1");
						//						values.put("id_dieta", "0");
						//						values.put("dia", "0");
						//						values.put("peso_ideal", "0");
						//						values.put("dietaTerminada", "0");
						//						values.put("estilo", "neutral");
						//						values.put("motivacion", "");
						//						values.put("notif", "0");
						//						values.put("fb_id", user.optString("id"));
						//						values.put("fb_name", name[0]);
						//						values.put("peso_inicial", "0");
						//						db.getWritableDatabase().insert("control", null, values);
						//
						//						db.close();
						//						
						//						if(!new API(con).getFacebookID().equals(""))
						//						{
						//							Intent intent = new Intent(ShareActivity.this, MainActivity.class);
						//							startActivity(intent);
						//						}


						Toast.makeText(con, "Log In Succes", Toast.LENGTH_SHORT).show();
					}

				}).executeAsync();
			}

			@Override
			public void onCancel() {
				// App code
			}

			@Override
			public void onError(FacebookException exception) {
				// App code
			}
		}); 
	}

	protected void onResume() {
		super.onResume();
		// Actualiza el estado del botón +1 cada vez que se enfoque la actividad.
		// mPlusOneButton.initialize(GOOGLE_PLUS_URL, 0);
	}
	
	private void setUpMenu()
	{
		viewActionsContentView = (ActionsContentView) findViewById(R.id.compartir_actionsContentView);

		viewActionsList = (ListView) findViewById(R.id.actions_simple);

		final String[] values = new String[] { 
				"Mi Perfil", 	//0 
				"Mi Dieta", 	//1
				"Mi Ejercicio", //2
				"Calendario", 	//3
				"Preguntanos",	//4
				//"Comparte", 	
				"Tips y Sujerencias",	//5 
				"Seleccionar Dieta", 	//6
				"Comparativa", 	//7
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

			Font f = new Font();					
			f.changeFontRaleway(con, facebook_name);
		}
	}

	private void showActivity(int position) 
	{
		final Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(ShareActivity.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(ShareActivity.this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(ShareActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(ShareActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(browserIntent);
			break;
		case 5:
			intent = getOpenFacebookIntent(con);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 6:
			intent = new Intent(ShareActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 7:
			intent = new Intent(ShareActivity.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 8:
			intent = new Intent(ShareActivity.this, DisclaimerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 9:
			intent = new Intent(ShareActivity.this, TutorialActivity.class);
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
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		//For Twitter, Facebook or Google+ share buttons
		final int buttonId = v.getId();
		if(buttonId == R.id.follow_button){
			// Twitter follow button
			try { 
				//user_id get from http://gettwitterid.com
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://follow?user_id=613887285"))); 
			} catch (ActivityNotFoundException e) { 
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/follow?user_id=613887285"))); 
			}
		}
		else{
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

	public Bitmap getPhotoFacebook(final String id) {

		Bitmap bitmap=null;
		final String nomimg = "https://graph.facebook.com/"+id+"/picture?type=large";
		URL imageURL = null;

		try {
			imageURL = new URL(nomimg);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			InputStream in = (InputStream) imageURL.getContent();
			bitmap = BitmapFactory.decodeStream(in);

		} catch (IOException e) {

			e.printStackTrace();
		}
		return bitmap;

	}


	//	public boolean isLoggedIn() {
	//	    //Session session = Session.getActiveSession();
	//	    //return (session != null && session.isOpened());
	//	}
}
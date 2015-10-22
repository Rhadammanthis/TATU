package com.cceo.miyoideal;

import java.util.Arrays;

import org.json.JSONObject;

import com.cceo.DB.SQLiteControl;
import com.cceo.DB.SQLiteUserDB;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.ImageAsyncTask;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class LogInActivity extends Activity {

	public CallbackManager callbackManager;
	public SQLiteControl db;
	private Context con;
	private ImageButton continuar;
	private ProgressDialog progressDialog;
	private LoginButton loginButton;
	private ImageView logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		con = this;

		logo = (ImageView) findViewById(R.id.splash_logo);
		loginButton = (LoginButton) findViewById(R.id.login_facebook_login);
		continuar = (ImageButton) findViewById(R.id.login_continuar);

		continuar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LogInActivity.this, MainActivity.class);
				Log.d("grafica", "ya hay usuario");
				startActivity(intent);
			}
		});

		SQLiteUserDB userDB = new SQLiteUserDB(this);
		userDB.getReadableDatabase();
		String query = "Select * FROM " + "usuario" + " WHERE " + "id_usuario" + " =  \"" + "1" + "\"";
		Cursor cursor = userDB.getReadableDatabase().rawQuery(query, null);

		if(cursor.moveToFirst())
		{
			//hide buttons and wait 3 seconds to start
			continuar.setVisibility(View.GONE);
			loginButton.setVisibility(View.GONE);
			
			//set splash background color according to theme
			RelativeLayout splash_view = (RelativeLayout) findViewById(R.id.splash_view);
			
			String style = new API(con).getStyle();
			//get system resources
			Resources res = getResources();

			if(style.equals("masculino"))
			{
				splash_view.setBackgroundColor(Color.parseColor("#13294B"));
			}
			if(style.equals("femenino"))
			{
				splash_view.setBackgroundColor(Color.parseColor("#FB637E"));
			}
			if(style.equals("neutral"))
			{
				splash_view.setBackgroundColor(Color.parseColor("#00A499"));
			}
			

			Thread logoTimer = new Thread() {

				public void run() {
					try {
						sleep(3000);


					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						Intent splashIntent = new Intent(con.getApplicationContext(), HomeActivity.class);
						startActivity(splashIntent);
						finish();
					}
				}

			};
			logoTimer.start();
		}
		else
		{
			//logo.setVisibility(View.GONE);

		}


		db = new SQLiteControl(this);

		//Facebook LogIn


		//loginButton.setReadPermissions("user_friends");

		callbackManager = CallbackManager.Factory.create();

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginManager.getInstance().logInWithReadPermissions(LogInActivity.this, (Arrays.asList("public_profile", "user_friends")));//,"user_birthday","user_about_me","email")));
			}
		});

		loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				// App code
				final AccessToken accessToken = loginResult.getAccessToken();

				GraphRequestAsyncTask request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject user, GraphResponse graphResponse) {
						Log.d("CONTRA", "Mail" + user.optString("email"));
						Log.d("CONTRA", "Name " + user.optString("name"));
						Log.d("CONTRA", "ID " + user.optString("id"));
						Log.d("CONTRA", "First name " + user.optString("first_name"));

						//cal Aynk
						//						ImageAsyncTask programming = new ImageAsyncTask();
						//						programming.setId(user.optString("id"));
						//						programming.execute();


						db.getWritableDatabase();

						Log.d("db", "Control " + db.getDatabaseName());

						String[] name = user.optString("name").split(" ");

						ContentValues values = new ContentValues();
						values.put("id_control", "1");
						values.put("id_dieta", "0");
						values.put("dia", "0");
						values.put("peso_ideal", "0");
						values.put("dietaTerminada", "0");
						values.put("estilo", "neutral");
						values.put("motivacion", "");
						values.put("notif", "0");
						values.put("fb_id", user.optString("id"));
						values.put("fb_name", name[0]);
						values.put("peso_inicial", "0");
						values.put("dieta_iteration", "0");
						values.put("talla_ideal", "0");
						values.put("goal_date", "0");
						db.getWritableDatabase().insert("control", null, values);

						db.close();

						if(!new API(con).getFacebookID().equals(""))
						{
							Intent intent = new Intent(LogInActivity.this, MainActivity.class);
							startActivity(intent);
						}


						//Toast.makeText(con, "Log In Succes", Toast.LENGTH_SHORT).show();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
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
}

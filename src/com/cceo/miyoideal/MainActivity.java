package com.cceo.miyoideal;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cceo.DAO.DAO_Usuario;
import com.cceo.DB.SQLiteComponenteDB;
import com.cceo.DB.SQLiteControl;
import com.cceo.DB.SQLiteDietaDB;
import com.cceo.DB.SQLiteEstadisticas;
import com.cceo.DB.SQLiteFactory;
import com.cceo.DB.SQLiteProgramaDB;
import com.cceo.DB.SQLiteRecommendation;
import com.cceo.DB.SQLiteShoppingDB;
import com.cceo.DB.SQLiteTutorialControl;
import com.cceo.DB.SQLiteUserDB;
import com.cceo.DTO.DTO_Usuario;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.DialyNotificationReceiver;
import com.cceo.miyoideal.extra.DietaCompleteNotification;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.ImageAsyncTask;
import com.facebook.FacebookSdk;
//import com.google.android.gms.plus.Plus;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends Activity implements SQLiteFactory{

	// Note: Your consumer key and secret should be obfuscated in your source code before shipping.
	public static final String TWITTER_KEY = "W0MKS891WG6RThT1rXA0ZXrk2";
	public static final String TWITTER_SECRET = "qtSPokpJDNzJlIoOKq4rftqW0ibXRXlJYy8vZGjINcSm3yveBe";
	
	//layout components
	private ImageButton button;
	private Spinner spinnerNivel;
	private Spinner spinnerSexo;

	//global variables;
	private int width;
	private int height;
	private Context con;

	//database
	private SQLiteUserDB userDB;
	private ContentValues values;
	

	//Style variables
	private int styleMain;
	private int styleDetail;
	
	private static ImageView profilePic;
	private Bitmap bit;


	@Override
	public void onResume(){
		super.onResume();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.clear);
		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new Twitter(authConfig));
		FacebookSdk.sdkInitialize(getApplicationContext());
		bit = null;
		
		
		setContentView(R.layout.activity_main);
		this.setTitle("Registro");
		con = this;
		
		initControlDB();

		//Calculates Screen size
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;	
		
		updateStyle();
		setFont();

		profilePic = (ImageView) findViewById(R.id.main_image);
		button = (ImageButton) findViewById(R.id.button1);

		spinnerNivel = (Spinner) findViewById(R.id.spinnerMain);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.nivel_array, R.layout.spinnercustomitem);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerNivel.setAdapter(adapter);

		spinnerSexo = (Spinner) findViewById(R.id.spinnerSexo);
		ArrayAdapter<CharSequence> adapterSexo = ArrayAdapter.createFromResource(this,
				R.array.perfil_sexo, R.layout.spinnercustomitem);
		adapterSexo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerSexo.setAdapter(adapterSexo);

		userDB = new SQLiteUserDB(this);

		userDB.getReadableDatabase();
		String query = "Select * FROM " + "usuario" + " WHERE " + "id_usuario" + " =  \"" + "1" + "\"";
		values = new ContentValues();
		Cursor cursor = userDB.getReadableDatabase().rawQuery(query, null);

		if(cursor.moveToFirst())
		{
			Log.d("main", "entre");

			Intent intent = new Intent(MainActivity.this, HomeActivity.class);
			startActivity(intent);
		}

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LinearLayout main = (LinearLayout) findViewById(R.id.mainLinerLayout);
				boolean allFiledsComplete = true;
				for(int i = 0; i < main.getChildCount(); i++)
				{
					if(main.getChildAt(i).getClass() == EditText.class)
					{
						TextView text = (TextView) main.getChildAt(i);
						String t = text.getText().toString();
						if(text.getText().toString().equals("") || text.getText().toString().equals(null))
							allFiledsComplete = false;
					}
				}

				if (allFiledsComplete) 
				{				
					initComponenteDB();
					initDietaDB();
					initProgramaDB();
					initTutorialControl();
					initRecommendationDB();
					initShoppingDB();
										
					Spinner sexo = (Spinner) main.getChildAt(1);
					EditText edad = (EditText) main.getChildAt(2);
					EditText estatura = (EditText) main.getChildAt(3);
					EditText peso = (EditText) main.getChildAt(5);
					EditText talla = (EditText) main.getChildAt(4);
					
					initEstadisticasDB(peso.getText().toString(), estatura.getText().toString());
					userDB.getWritableDatabase();
					Log.d("db", userDB.getDatabaseName());
					values.put("id_usuario", "1");
					values.put("nombre", "Hugo");
					values.put("peso", peso.getText().toString());
					values.put("talla", talla.getText().toString());
					values.put("estatura", estatura.getText().toString());
					values.put("nivel", spinnerNivel.getSelectedItem().toString());
					values.put("sexo", sexo.getSelectedItem().toString());
					values.put("edad", edad.getText().toString());
					
					userDB.getWritableDatabase().insert("usuario", null, values);
					userDB.close();
					
					//Set initial weigth. Save value to control DB
					ContentValues cv = new ContentValues();
					cv.put("peso_inicial", peso.getText().toString());
			 	   
					SQLiteControl db = new SQLiteControl(con);
					db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
					db.close();
					
					DialyNotificationReceiver dialyNotification = new DialyNotificationReceiver();
					dialyNotification.setAlarm(con);
					
					Intent intent = new Intent(MainActivity.this, HomeActivity.class);
					Log.d("grafica", "ya hay usuario");
					startActivity(intent);
				}
				else
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(con);
					builder.setMessage("No se han llenado todos los campos")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//do things
						}
					});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});	
		

		
		if(!new API(con).getFacebookID().equals(""))
		{

			//new thead to update UI
			Thread logoTimer = new Thread() {

				public void run() {
					try {
						bit = getUserPic(new API(con).getFacebookID());
						

					} finally {
						//special thead to update UI
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								profilePic.setImageBitmap(API.getRoundedShape(bit));
							}
						});
						
					}
				}

			};
			logoTimer.start();
			
		}
		//if User has yet to sign to Facebook, we use the Facebook profile pic placeholder
		else
		{
			Bitmap icon = BitmapFactory.decodeResource(con.getResources(),
                    R.drawable.com_facebook_profile_picture_blank_square);
			profilePic.setImageBitmap(API.getRoundedShape(icon));
			
			
			//profilePic.setBackgroundResource(R.drawable.com_facebook_profile_picture_blank_square);
		}
		
	}
	
	private void setFont() {
		// TODO Auto-generated method stub
		Font f = new Font();
		LinearLayout main = (LinearLayout) findViewById(R.id.mainLinerLayout);
		TextView textView = (TextView) findViewById(R.id.diagnostico_textView);
		Spinner sexo = (Spinner) main.getChildAt(1);
		EditText edad = (EditText) main.getChildAt(2);
		EditText talla = (EditText) main.getChildAt(3);
		EditText peso = (EditText) main.getChildAt(4);
		
		f.changeFontRaleway(con, peso);
		f.changeFontRaleway(con, talla);
		f.changeFontRaleway(con, edad);
		f.changeFontRaleway(con, textView);
	}


	public static void setFacebokProfilePic(Bitmap bitMap)
	{		
		//if(bitMap != null && profilePic!=null)
			//profilePic.setImageResource(R.drawable.abc_ic_cab_done_holo_light);
	}
	
	public Bitmap getUserPic(String userID) {
		Bitmap bitmap = null;
	    final String nomimg = "https://graph.facebook.com/"+userID+"/picture?type=large";
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

	@Override
	public void onPause(){
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Add the menu layout to the action bar
		getMenuInflater().inflate(R.menu.base_action_bar, menu);
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


	@Override
	public void initDietaDB() {
		// TODO Auto-generated method stub
		SQLiteDietaDB db = new SQLiteDietaDB(this);
		db.getWritableDatabase();
		
		Log.d("db", "Dieta " + db.getDatabaseName());

		ContentValues values = new ContentValues();
		values.put("id_dieta", "1");
		values.put("nombre", "Dieta 1");
		values.put("tipo", "Perdida de Peso");
		values.put("duracion", "7");
		values.put("etiqueta", "DP1");
		values.put("descripcion", "Descripcion de la Dieta Numero 1");
		db.getWritableDatabase().insert("dieta", null, values);

		values = new ContentValues();
		values.put("id_dieta", "2");
		values.put("nombre", "Dieta 2");
		values.put("tipo", "Perdida de Peso");
		values.put("duracion", "3");
		values.put("etiqueta", "DP2");
		values.put("descripcion", "Descripcion de la Dieta Numero 2");
		db.getWritableDatabase().insert("dieta", null, values);
		
		values = new ContentValues();
		values.put("id_dieta", "3");
		values.put("nombre", "Dieta 3");
		values.put("tipo", "Perdida de Peso");
		values.put("duracion", "2");
		values.put("etiqueta", "DP3");
		values.put("descripcion", "Descripcion de la Dieta Numero 3");
		db.getWritableDatabase().insert("dieta", null, values);
		db.close();
		
		Log.d("selec", "se agregaron dietas");

	}

	@Override
	public void initComponenteDB() {
		// TODO Auto-generated method stub
		SQLiteComponenteDB db = new SQLiteComponenteDB(this);
		db.getWritableDatabase();
		
		Log.d("db", "Componente " + db.getDatabaseName());

		//1
		ContentValues values = new ContentValues();
		values.put("id_componente", "1");
		values.put("id_dieta", "1");
		values.put("dia", "1");
		values.put("alimento", "Desayuno");
		values.put("activo", "no");
		values.put("descripcion", "Desayuno DIETA UNO D?a 1 Alimento 1");
		db.getWritableDatabase().insert("componente", null, values);

		//2
		values = new ContentValues();
		values.put("id_componente", "2");
		values.put("id_dieta", "1");
		values.put("dia", "1");
		values.put("alimento", "Desayuno");
		values.put("activo", "no");
		values.put("descripcion", "Desayuno DIETA UNO D?a 1 Alimento 2");
		db.getWritableDatabase().insert("componente", null, values);

		//3
		values = new ContentValues();
		values.put("id_componente", "3");
		values.put("id_dieta", "1");
		values.put("dia", "1");
		values.put("alimento", "Desayuno");
		values.put("activo", "no");
		values.put("descripcion", "Desayuno DIETA UNO D?a 1 Alimento 3");
		db.getWritableDatabase().insert("componente", null, values);

		//4
		values = new ContentValues();
		values.put("id_componente", "4");
		values.put("id_dieta", "1");
		values.put("dia", "1");
		values.put("alimento", "Colacion");
		values.put("activo", "no");
		values.put("descripcion", "Colacion DIETA UNO D?a 1 Alimento 1");
		db.getWritableDatabase().insert("componente", null, values);

		//5
		values = new ContentValues();
		values.put("id_componente", "5");
		values.put("id_dieta", "1");
		values.put("dia", "1");
		values.put("alimento", "Colacion");
		values.put("activo", "no");
		values.put("descripcion", "Colacion DIETA UNO D?a 1 Alimento 2");
		db.getWritableDatabase().insert("componente", null, values);

		//6
		values = new ContentValues();
		values.put("id_componente", "6");
		values.put("id_dieta", "1");
		values.put("dia", "1");
		values.put("alimento", "Colacion");
		values.put("activo", "no");
		values.put("descripcion", "Colacion DIETA UNO D?a 1 Alimento 3");
		db.getWritableDatabase().insert("componente", null, values);
		
		//6-1
				values = new ContentValues();
				values.put("id_componente", "61");
				values.put("id_dieta", "1");
				values.put("dia", "1");
				values.put("alimento", "Comida");
				values.put("activo", "no");
				values.put("descripcion", "Pollo");
				db.getWritableDatabase().insert("componente", null, values);
				
				//6-2
				values = new ContentValues();
				values.put("id_componente", "62");
				values.put("id_dieta", "1");
				values.put("dia", "1");
				values.put("alimento", "Comida");
				values.put("activo", "no");
				values.put("descripcion", "Brocoli");
				db.getWritableDatabase().insert("componente", null, values);

		///////////////////////////////////////////////////////////////////////////////////////////////
		//segundo dia

		//7
		values = new ContentValues();
		values.put("id_componente", "7");
		values.put("id_dieta", "1");
		values.put("dia", "2");
		values.put("alimento", "Desayuno");
		values.put("activo", "no");
		values.put("descripcion", "Desayuno DIETA UNO D?a 2 Alimento 3");
		db.getWritableDatabase().insert("componente", null, values);

		//88
		values = new ContentValues();
		values.put("id_componente", "8");
		values.put("id_dieta", "1");
		values.put("dia", "2");
		values.put("alimento", "Colacion");
		values.put("activo", "no");
		values.put("descripcion", "Colacion DIETA UNO D?a 2 Alimento 1");
		db.getWritableDatabase().insert("componente", null, values);

		//9
		values = new ContentValues();
		values.put("id_componente", "9");
		values.put("id_dieta", "1");
		values.put("dia", "2");
		values.put("alimento", "Colacion");
		values.put("activo", "no");
		values.put("descripcion", "Colacion DIETA UNO D?a 2 Alimento 2");
		db.getWritableDatabase().insert("componente", null, values);

		//10
		values = new ContentValues();
		values.put("id_componente", "10");
		values.put("id_dieta", "1");
		values.put("dia", "2");
		values.put("alimento", "Colacion");
		values.put("activo", "no");
		values.put("descripcion", "Colacion DIETA UNO D?a 2 Alimento 3");
		db.getWritableDatabase().insert("componente", null, values);

		db.close();
	}


	@Override
	public void initControlDB() {
		// TODO Auto-generated method stub
		SQLiteControl db = new SQLiteControl(this);
		db.getWritableDatabase();
		
		Log.d("db", "Control " + db.getDatabaseName());

		ContentValues values = new ContentValues();
		values.put("id_control", "1");
		values.put("id_dieta", "0");
		values.put("dia", "0");
		values.put("peso_ideal", "0");
		values.put("dietaTerminada", "0");
		values.put("estilo", "neutral");
		values.put("motivacion", "");
		values.put("notif", "0");
		values.put("fb_id", "");
		values.put("fb_name", "");
		values.put("peso_inicial", "0");
		values.put("dieta_iteration", "0");
		values.put("talla_ideal", "0");
		values.put("goal_date", "0");
		db.getWritableDatabase().insert("control", null, values);

		db.close();
	}


	@Override
	public void initProgramaDB() {
		// TODO Auto-generated method stub
		SQLiteProgramaDB db = new SQLiteProgramaDB(this);
		db.getWritableDatabase();
		
		Log.d("db", "Programa " + db.getDatabaseName());

		//bajo
		ContentValues values = new ContentValues();
		values.put("id_programa", "1");
		values.put("nivel", "bajo");
		values.put("titulo", "Lagartijas");
		values.put("descripcion", "Hacer 1 series de 8 repeticiones");
		values.put("hora", "10 am");
		db.getWritableDatabase().insert("programa", null, values);
		
		//bajo
				values = new ContentValues();
				values.put("id_programa", "4");
				values.put("nivel", "bajo");
				values.put("titulo", "Bajo 1");
				values.put("descripcion", "Actividad bajo 1");
				values.put("hora", "10 am");
				db.getWritableDatabase().insert("programa", null, values);
				
				//bajo
				 values = new ContentValues();
				values.put("id_programa", "5");
				values.put("nivel", "bajo");
				values.put("titulo", "Bajo 2");
				values.put("descripcion", "Actividad bajo 2");
				values.put("hora", "10 am");
				db.getWritableDatabase().insert("programa", null, values);
				
				//bajo
				 values = new ContentValues();
				values.put("id_programa", "6");
				values.put("nivel", "bajo");
				values.put("titulo", "Bajo 3");
				values.put("descripcion", "Actividad bajo 3");
				values.put("hora", "10 am");
				db.getWritableDatabase().insert("programa", null, values);

		//medio
		values = new ContentValues();
		values.put("id_programa", "2");
		values.put("nivel", "medio");
		values.put("titulo", "Abdominales");
		values.put("descripcion", "Hacer 3 series de 8 repeticiones");
		values.put("hora", "3 pm");
		db.getWritableDatabase().insert("programa", null, values);
		
		values = new ContentValues();
		values.put("id_programa", "7");
		values.put("nivel", "medio");
		values.put("titulo", "Medio 1");
		values.put("descripcion", "Actividad medio 1");
		values.put("hora", "3 pm");
		db.getWritableDatabase().insert("programa", null, values);
		
		values = new ContentValues();
		values.put("id_programa", "8");
		values.put("nivel", "medio");
		values.put("titulo", "Medio 2");
		values.put("descripcion", "Actividad medio 2");
		values.put("hora", "3 pm");
		db.getWritableDatabase().insert("programa", null, values);
		
		values = new ContentValues();
		values.put("id_programa", "9");
		values.put("nivel", "medio");
		values.put("titulo", "Medio 3");
		values.put("descripcion", "Actividad medio 3");
		values.put("hora", "3 pm");
		db.getWritableDatabase().insert("programa", null, values);

		//alto
		new ContentValues();
		values.put("id_programa", "3");
		values.put("nivel", "alto");
		values.put("titulo", "Carrera");
		values.put("descripcion", "Correr 5 km a un ritmo medio");
		values.put("hora", "7 pm");
		db.getWritableDatabase().insert("programa", null, values);
		
		new ContentValues();
		values.put("id_programa", "10");
		values.put("nivel", "alto");
		values.put("titulo", "Alto 1");
		values.put("descripcion", "Actividad alto 1");
		values.put("hora", "7 pm");
		db.getWritableDatabase().insert("programa", null, values);
		new ContentValues();
		values.put("id_programa", "11");
		values.put("nivel", "alto");
		values.put("titulo", "Alto 2");
		values.put("descripcion", "Actividad alto 2");
		values.put("hora", "7 pm");
		db.getWritableDatabase().insert("programa", null, values);
		new ContentValues();
		values.put("id_programa", "12");
		values.put("nivel", "alto");
		values.put("titulo", "Alto 3");
		values.put("descripcion", "Actividad alto 3");
		values.put("hora", "7 pm");
		db.getWritableDatabase().insert("programa", null, values);
		
	}		
	
	//Saves selected style colors to local variables
	private void getStyle()
	{	
		styleMain = Color.parseColor("#00A499");
		styleDetail = Color.parseColor("#FFA300");
	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{
		//sets local style variables
		getStyle();
		
		ScrollView mainLayout = (ScrollView) findViewById(R.id.mainScrollView);
		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
	}

	//Create the Estaditicas Database 
	@Override
	public void initEstadisticasDB(String pesoActual, String tallaActual){
		SQLiteEstadisticas db = new SQLiteEstadisticas(this);
		ContentValues values = new ContentValues();
		values.put("id_estadisticas", 0);
		values.put("talla_maxima", Float.valueOf(tallaActual));
		values.put("talla_minima", Float.valueOf(tallaActual));
		values.put("talla_actual", Float.valueOf(tallaActual));
		values.put("peso_maximo", Float.valueOf(pesoActual));
		values.put("peso_minimo", Float.valueOf(pesoActual));
		values.put("peso_actual", Float.valueOf(pesoActual));
		values.put("imc", (Float.valueOf(pesoActual) / Float.valueOf(tallaActual)));
		values.put("record_peso_perdido", 0);
		values.put("peso_perdido_tratamiento", 0);
		values.put("dias_consecutivos_dieta", 0);
		values.put("dias_consecutivos_ejercicio", 0);
		long i = db.getWritableDatabase().insert("estadisticas", null, values);
		Log.d("Main Activity Result insert", i+" ");
		
	}


	@Override
	public void initTutorialControl() {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		SQLiteTutorialControl db = new SQLiteTutorialControl(this);
		db.getWritableDatabase();
		
		Log.d("db", "Control " + db.getDatabaseName());

		ContentValues values = new ContentValues();
		values.put("id_tutorialcontrol", "1");
		values.put("tutorial", "0");
		values.put("button_tutorial", "0");

		db.getWritableDatabase().insert("tutorialcontrol", null, values);

		db.close();
	}


	@Override
	public void initRecommendationDB() {
		// TODO Auto-generated method stub
		SQLiteRecommendation db = new SQLiteRecommendation(this);
		db.getWritableDatabase();
		
		Log.d("db", "Control " + db.getDatabaseName());

		ContentValues values = new ContentValues();
		values.put("id_dieta", "DP1");
		values.put("recommendation_more_first", "DP2");
		values.put("recommendation_more_second", "DP3");
		values.put("recommendation_same_first", "0");
		values.put("recommendation_same_second", "0");
		values.put("recommendation_less_first", "0");
		values.put("recommendation_less_second", "0");

		db.getWritableDatabase().insert("recommendation", null, values);

		db.close();
	}


	@Override
	public void initShoppingDB() {
		// TODO Auto-generated method stub
		SQLiteShoppingDB db = new SQLiteShoppingDB(this);
		db.getWritableDatabase();
		
		Log.d("db", "Control " + db.getDatabaseName());

		ContentValues values = new ContentValues();
		values.put("id_shopping", "1");
		values.put("id_dieta", "1");
		values.put("item", "Manzana");
		values.put("status", "off");
		db.getWritableDatabase().insert("shopping", null, values);
		
		values = new ContentValues();
		values.put("id_shopping", "2");
		values.put("id_dieta", "1");
		values.put("item", "Pascado");
		values.put("status", "off");
		db.getWritableDatabase().insert("shopping", null, values);
		
		values = new ContentValues();
		values.put("id_shopping", "3");
		values.put("id_dieta", "1");
		values.put("item", "Arroz");
		values.put("status", "off");
		db.getWritableDatabase().insert("shopping", null, values);
		
		values = new ContentValues();
		values.put("id_shopping", "4");
		values.put("id_dieta", "2");
		values.put("item", "Yogurth");
		values.put("status", "off");
		db.getWritableDatabase().insert("shopping", null, values);
		
		values = new ContentValues();
		values.put("id_shopping", "5");
		values.put("id_dieta", "2");
		values.put("item", "Brocoli");
		values.put("status", "off");
		db.getWritableDatabase().insert("shopping", null, values);

		db.close();
	}
}

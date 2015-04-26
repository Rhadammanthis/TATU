package com.example.miyoideal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.DB.SQLiteComponenteDB;
import com.example.DB.SQLiteControl;
import com.example.DB.SQLiteDietaDB;
import com.example.DB.SQLiteEstadisticas;
import com.example.DB.SQLiteFactory;
import com.example.DB.SQLiteProgramaDB;
import com.example.DB.SQLiteUserDB;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.DialyNotificationReceiver;
import com.facebook.FacebookSdk;
//import com.google.android.gms.plus.Plus;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;


public class MainActivity extends Activity implements SQLiteFactory{

	// Note: Your consumer key and secret should be obfuscated in your source code before shipping.
	private static final String TWITTER_KEY = "W0MKS891WG6RThT1rXA0ZXrk2";
	private static final String TWITTER_SECRET = "qtSPokpJDNzJlIoOKq4rftqW0ibXRXlJYy8vZGjINcSm3yveBe";
	
	//layout components
	private Button button;
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


	@Override
	public void onResume(){
		super.onResume();
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new Twitter(authConfig));
		FacebookSdk.sdkInitialize(getApplicationContext());
		
		setContentView(R.layout.activity_main);
		this.setTitle("Diagnostico");
		con = this;

		//Calculates Screen size
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;	
		
		updateStyle();

		button = (Button) findViewById(R.id.button1);

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
					initControlDB();
					initProgramaDB();
										
					Spinner sexo = (Spinner) main.getChildAt(1);
					EditText edad = (EditText) main.getChildAt(2);
					EditText talla = (EditText) main.getChildAt(3);
					EditText peso = (EditText) main.getChildAt(4);
					Spinner nivel = (Spinner) main.getChildAt(5);
					
					initEstadisticasDB(peso.getText().toString(), talla.getText().toString());
					userDB.getWritableDatabase();
					Log.d("db", userDB.getDatabaseName());
					values.put("id_usuario", "1");
					values.put("nombre", "Hugo");
					values.put("peso", peso.getText().toString());
					values.put("talla", talla.getText().toString());
					values.put("nivel", nivel.getSelectedItem().toString());
					values.put("sexo", sexo.getSelectedItem().toString());
					values.put("edad", edad.getText().toString());
					
					userDB.getWritableDatabase().insert("usuario", null, values);
					userDB.close();
					
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
		db.getWritableDatabase().insert("dieta", null, values);

		values = new ContentValues();
		values.put("id_dieta", "2");
		values.put("nombre", "Dieta 2");
		values.put("tipo", "Perdida de Peso");
		values.put("duracion", "3");
		values.put("etiqueta", "DP2");
		db.getWritableDatabase().insert("dieta", null, values);
		
		values = new ContentValues();
		values.put("id_dieta", "3");
		values.put("nombre", "Dieta 3");
		values.put("tipo", "Perdida de Peso");
		values.put("duracion", "2");
		values.put("etiqueta", "DP3");
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
		db.getWritableDatabase().insert("control", null, values);

		//Set the notification receiver 
		DialyNotificationReceiver dialyNotification = new DialyNotificationReceiver();
		dialyNotification.setAlarm(this);

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

		//medio
		values = new ContentValues();
		values.put("id_programa", "2");
		values.put("nivel", "medio");
		values.put("titulo", "Abdominales");
		values.put("descripcion", "Hacer 3 series de 8 repeticiones");
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
}

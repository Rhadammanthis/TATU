package com.example.miyoideal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import shared.ui.actionscontentview.ActionsContentView;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.example.DAO.DAO_Ejercicio;
import com.example.DAO.DAO_Estadisticas;
import com.example.DAO.DAO_Programa;
import com.example.DAO.DAO_Usuario;
import com.example.DB.SQLiteEstadisticas;
import com.example.DTO.DTO_Ejercicio;
import com.example.DTO.DTO_Estadisticas;
import com.example.DTO.DTO_Programa;
import com.example.DTO.DTO_Usuario;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.DietaChildFactory;
import com.example.miyoideal.extra.EjercicioChildFactory;
import com.wt.calendarcard.CheckableLayout;

public class EjercicioActivity extends Activity {

	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	private LinearLayout mainLayout;
	private CheckBox checkB;
	private Context con;
	private RelativeLayout topBar;

	private Calendar c;
	private DTO_Ejercicio ejercicio;
	private long distance;
	private char[] dia;

	int styleMain;
	int styleDetail;
	int styleDarkest;
	int styleDark;
	int styleBright;
	int styleBrightest;

	private EjercicioChildFactory ejercicioFactory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline_ejercicio);
		con = this;

		viewActionsContentView = (ActionsContentView) findViewById(R.id.ejercicio_actionsContentView);

		viewActionsList = (ListView) findViewById(R.id.actions);
		setUpMenu();



		mainLayout = (LinearLayout) findViewById(R.id.ejercicio_linearLayout);
		checkB = (CheckBox) findViewById(R.id.ejercicio_checkB);
		topBar = (RelativeLayout) findViewById(R.id.ejercicioTopBar);

		updateStyle();

		//Ejercicio Factory Example
		ejercicioFactory = new EjercicioChildFactory();
		initProgramaComponentes();

		if(new API(con).IsDietaSet())
		{
			//distance variable getes the difference between today's date and that of the first day of the diet program
			c = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
			java.util.Date initalDate = null;
			String lol = new API(con).getDia();
			try {
				initalDate = df.parse(new API(con).getDia());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String todayDate = df.format(c.getTime());	
			distance = getDateDiffString(initalDate,c.getTime());

			//Obtain current ejercicio info from BD
			ejercicio = new DTO_Ejercicio();
			ejercicio = new DAO_Ejercicio(con).getEjercicio(new API(con).getID_Dieta(), new API(con).getDia());

			if(distance > ejercicio.getDiasActividad().length())
			{
				topBar.setVisibility(View.GONE);
			}
			else
			{
				//Check the state of todays physical activity
				dia = ejercicio.getDiasActividad().toCharArray();
				if(dia[(int) distance] == '0')
				{
					checkB.setChecked(false);
				}
				else
				{
					checkB.setChecked(true);
				}

				//if distance > dias que dura la dieta, esconder barra

				checkB.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(checkB.isChecked())
						{
							dia[(int) distance] = '1';
						}
						else
						{
							dia[(int) distance] = '0';
						}	

						String l = "";
						for(int i = 0; i<=ejercicio.getDiasActividad().length()-1;i++)
							l += String.valueOf(dia[i]);
						Log.d("dias", l);
						ejercicio.setDiasActividad(l);
						Log.d("dias", ejercicio.getDiasActividad());
						new DAO_Ejercicio(con).updateEjercicio(ejercicio);
					}
				});
			}
		}
		else
		{
			RelativeLayout header = (RelativeLayout) findViewById(R.id.ejercicioTopBar);
			header.setVisibility(View.GONE);
			checkB.setChecked(true);
		}

		//if distance > dias que dura la dieta, esconder barra

		//			checkB.setOnClickListener(new View.OnClickListener() {
		//				
		//				@Override
		//				public void onClick(View v) {
		//					// TODO Auto-generated method stub
		//					if(checkB.isChecked())
		//					{
		//						dia[(int) distance] = '1';
		//					}
		//					else
		//					{
		//						dia[(int) distance] = '0';
		//					}		
		//					String l = "";
		//					for(int i = 0; i<=ejercicio.getDiasActividad().length()-1;i++)
		//						l += String.valueOf(dia[i]);
		//					Log.d("dias", l);
		//					ejercicio.setDiasActividad(l);
		//					Log.d("dias", ejercicio.getDiasActividad());
		//					new DAO_Ejercicio(con).updateEjercicio(ejercicio);
		//				}
		//			});

	}


	@Override
	public void onStop(){
		super.onStop();

		if(new API(con).IsDietaSet())
		{
			DAO_Estadisticas daoEstadisticas = new DAO_Estadisticas(con);
			DTO_Estadisticas dtoEstadisticas = daoEstadisticas.getEstadisticas();
			SQLiteEstadisticas dbEstadisticas = new SQLiteEstadisticas(con);
			if(dia[(int) distance] == 1){
				dbEstadisticas.getReadableDatabase();
				ContentValues values = new ContentValues();
				values.put("dias_consecutivos_ejercicio", Integer.valueOf(dtoEstadisticas.getDiasConsecutivosEjercicio()) + 1);
				/*if(distance > 0 && dia[((int)(distance))-1] == 0){
					values.put("dias_consecutivos_ejercicio", Integer.valueOf(dtoEstadisticas.getDiasConsecutivosEjercicio()) + 1);
				}
				else{
					values.put("dias_consecutivos_ejercicio", 0);
				}*/
				dbEstadisticas.close();
				dbEstadisticas.getWritableDatabase().update("estadisticas", values, null, null);
				dbEstadisticas.close();
			}
			else if(dia[(int) distance] == 0){
				dbEstadisticas.getReadableDatabase();
				ContentValues values = new ContentValues();
				values.put("dias_consecutivos_ejercicio", 0);
				/*if(distance > 0 && dia[((int) (distance))-1] == 0){
					values.put("dias_consecutivos_ejercicio", Integer.valueOf(dtoEstadisticas.getDiasConsecutivosEjercicio()) - 1);
				}else{
					values.put("dias_consecutivos_ejercicio", 0);
				}*/
				dbEstadisticas.close();
				dbEstadisticas.getWritableDatabase().update("estadisticas", values, null, null);
				dbEstadisticas.close();
			}
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

	private void setUpMenu()
	{
		final String[] values = new String[] { "Mi Perfil", "Mi Dieta", "Mi Ejercicio", 
				"Mas Dietas", "Calendario", "Estadisticas", "Preguntanos",
				"Comparte", "Tips y Sujerencias", "Recordatorios"};
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
			intent = new Intent(EjercicioActivity.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(EjercicioActivity.this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(EjercicioActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		default:
			return;
		}

	}

	//returns distance between days of  two Date objects
	public long getDateDiffString(java.util.Date date, java.util.Date date2)
	{
		long timeOne = date.getTime();
		long timeTwo = date2.getTime();
		long oneDay = 1000 * 60 * 60 * 24;
		long delta = (timeTwo - timeOne) / oneDay;

		int dia_Date1 = Integer.valueOf((String)android.text.format.DateFormat.format("dd", date)), 
				dia_Date2 = Integer.valueOf((String)android.text.format.DateFormat.format("dd", date2));

		String lol = String.valueOf(timeOne/oneDay);

		if(date2.before(date))
			return -1;
		else
			return delta;
		//		if(dia_Date2 < dia_Date1)
		//			return -1;
		//		else
		//			return delta;
	}

	private void initProgramaComponentes()
	{
		//the hardcoded string at the end must be take from the user profile
		DTO_Usuario userData = new DAO_Usuario(con).getUsuario();
		List<DTO_Programa> lista = new DAO_Programa(con).getAllPrograma(userData.getNivel().toLowerCase());

		for(DTO_Programa temp : lista)
		{
			DietaChildFactory childFactory = new DietaChildFactory();
			mainLayout.addView(childFactory.GenerateChilEjercicio(con, temp.getTitulo(), temp.getDescripcion()));
			//mainLayout.addView(genEjercicioChild(con, temp.getTitulo(), temp.getDescripcion()));
			//to generate row setoff shadow
			mainLayout.addView(getDarkest());
			mainLayout.addView(getDarker());
			mainLayout.addView(getBrighter());
			mainLayout.addView(getBrightest());
			//mainLayout.addView(ejercicioFactory.GenerateChildEjercicio(con, temp.getTitulo(), temp.getDescripcion(), temp.getHora()));
		}
	}

	public RelativeLayout genEjercicioChild(Context context, String title, String content)
	{
		RelativeLayout child = new RelativeLayout(context);
		final float scale = context.getResources().getDisplayMetrics().density;
		int pixels = (int) (60 * scale + 0.5f);

		//child main layout
		child = new RelativeLayout(context);
		child.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		child.setPadding(pixels, (int) ((int) pixels * 0.2), pixels, (int) ((int) pixels * 0.2));

		//LinearLayout to wrap content 
		LinearLayout linear = new LinearLayout(context);
		LinearLayout.LayoutParams linear1_LP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(linear1_LP);

		//FrameLayout to wrap 'title' and 'time'
		FrameLayout header = new FrameLayout(context);
		header.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//header.setBackgroundColor(Color.RED);
		//header.setPadding(pixels, 0, pixels, 0);

		//Time TextView. Color, size and text adjusted
		TextView tv_title = new TextView(context);
		tv_title.setTextSize(20);
		tv_title.setTextColor(Color.WHITE);
		tv_title.setTypeface(null, Typeface.BOLD);
		tv_title.setText(title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		tv_title.setId(tv_title.hashCode());
		//tv_title.setPadding(0, 0, pixels, 0);

		//		//Title TextView. Color, size and text adjusted
		//		TextView tv_time = new TextView(context);
		//		tv_time.setTextSize(25);
		//		tv_time.setTextColor(Color.WHITE);
		//		tv_time.setTypeface(null, Typeface.BOLD);
		//		tv_time.setText(title);
		//		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		//		//tv_title.setPadding(pixels, 0, 0, 0);

		//title and time added to header
		header.addView(tv_title);

		//header added to main container
		linear.addView(header);

		//RelativeLayout to wrap 'content' and checkbox
		FrameLayout body = new FrameLayout(context);
		body.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));//(int) (120 * scale + 0.5f)));
		//body.setBackgroundColor(Color.BLUE);
		//body.setPadding(12, 12, 12, 12);

		//Content multiline textview
		TextView tv_content = new TextView(context);
		//tv_content.setPadding((int) (15 * scale + 0.5f), 0, 0, 0);
		tv_content.setTextSize(17);
		tv_content.setTextColor(Color.WHITE);
		tv_content.setText(content);
		tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);


		//content and checkbox added to body
		body.addView(tv_content);

		//body added toi main container
		linear.addView(body);

		//main container added to view body
		child.addView(linear);		

		return child;
	}

	public View getDarkest()
	{
		final float scale = con.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(con);

		v.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, pixels));
		v.setBackgroundColor(styleDarkest);

		return v;
	}
	public View getDarker()
	{
		final float scale = con.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(con);

		v.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, pixels));
		v.setBackgroundColor(styleDark);

		return v;
	}
	public View getBrighter()
	{
		final float scale = con.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(con);

		v.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, pixels));
		v.setBackgroundColor(styleBright);

		return v;
	}
	public View getBrightest()
	{
		final float scale = con.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(con);

		v.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, pixels));
		v.setBackgroundColor(styleBrightest);

		return v;
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
			styleDarkest = res.getColor(R.color.MASCULINO_DARKEST);
			styleDark = res.getColor(R.color.MASCULINO_DARKER);
			styleBright = res.getColor(R.color.MASCULINO_BRIGHTER);
			styleBrightest = res.getColor(R.color.MASCULINO_BRIGHTEST);
		}
		if(style.equals("femenino"))
		{
			styleMain = res.getColor(R.color.FEMENINO_MAIN);
			styleDetail = res.getColor(R.color.FEMENINO_DETAIL);
			styleDarkest = res.getColor(R.color.FEMENINO_DARKEST);
			styleDark = res.getColor(R.color.FEMENINO_DARKER);
			styleBright = res.getColor(R.color.FEMENINO_BRIGHTER);
			styleBrightest = res.getColor(R.color.FEMENINO_BRIGHTEST);
		}
		if(style.equals("neutral"))
		{
			styleMain = res.getColor(R.color.NEUTRAL_MAIN);
			styleDetail = res.getColor(R.color.NEUTRAL_DETAIL);
			styleDarkest = res.getColor(R.color.NEUTRAL_DARKEST);
			styleDark = res.getColor(R.color.NEUTRAL_DARKER);
			styleBright = res.getColor(R.color.NEUTRAL_BRIGHTER);
			styleBrightest = res.getColor(R.color.NEUTRAL_BRIGHTEST);
		}

	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{	
		//header setoff bar
		View bar1 = (View) findViewById(R.id.ejerBar1);
		View bar2 = (View) findViewById(R.id.ejerBar2);
		View bar3 = (View) findViewById(R.id.ejerBar3);
		View bar4 = (View) findViewById(R.id.ejerBar4);

		//sets local style variables
		getStyle();

		ScrollView scroll = (ScrollView) findViewById(R.id.ejercicioScroll);
		RelativeLayout header = (RelativeLayout) findViewById(R.id.ejercicioTopBar);

		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
		header.setBackgroundColor(styleBright);
		scroll.setBackgroundColor(styleMain);

		bar1.setBackgroundColor(styleDarkest);
		bar2.setBackgroundColor(styleDark);
		bar3.setBackgroundColor(styleBright);
		bar4.setBackgroundColor(styleBrightest);
	}
}

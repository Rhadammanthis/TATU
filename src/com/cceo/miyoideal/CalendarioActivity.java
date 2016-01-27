package com.cceo.miyoideal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import shared.ui.actionscontentview.ActionsContentView;

import com.cceo.DAO.DAO_Componente;
import com.cceo.DAO.DAO_Dieta;
import com.cceo.DTO.*;
import com.cceo.calendarcard.*;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.CalendarioChildFactory;
import com.cceo.miyoideal.extra.DietaChild;
import com.cceo.miyoideal.extra.DietaChildFactory;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.ImageAsyncTask;
import com.cceo.miyoideal.extra.MenuFragment;
import com.cceo.miyoideal.extra.MyArrayAdapter;
import com.cceo.miyoideal.extra.SideMenu;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;


public class CalendarioActivity extends Activity {

	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	private Button b;
	private CalendarCard mCalendarCard;
	private LinearLayout linearLayout;
	private LinearLayout bodyLayout;
	private int width, height;
	private Context ctx;
	int size;

	private ImageButton rightButton;
	private ImageButton leftButton;
	
	int styleMain;
	int styleDetail;
	int styleDarkest;
	int styleDark;
	int styleBright;
	int styleBrightest;

	Calendar cal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline3);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.menu_white);
		this.setTitle("		Calendario");

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;	
		ctx = this;

		cal = Calendar.getInstance();

		View bar = (View) findViewById(R.id.calBar1);
		this.size = bar.getLayoutParams().width;


		viewActionsContentView = (ActionsContentView) findViewById(R.id.calendario_actionsContentView);
		linearLayout = (LinearLayout) findViewById(R.id.calendarLinearLayout);		
		bodyLayout = (LinearLayout) findViewById(R.id.calendarioBody);
		rightButton = (ImageButton) findViewById(R.id.calendarRightB);
		leftButton = (ImageButton) findViewById(R.id.calendarLeftB);
		ScrollView calendarScroll = (ScrollView) findViewById(R.id.calendarScrollView);

		viewActionsList = (ListView) findViewById(R.id.actions_simple);
		setUpMenu();

		//updates style according to user selected theme
				updateStyle();

		//init calendar widget
		mCalendarCard = (CalendarCard)findViewById(R.id.calendarCard6);
		mCalendarCard.getLayoutParams().height = height/2;
		
		

		//load today's dieta components, if dieta is set
		if(new API(ctx).IsDietaSet())
		{
			initTodayDieta();
			
			//colors dieta duration
			mCalendarCard.drawDietaDays(String.valueOf(getDietaBegining()), String.valueOf(getDietaEnd()),styleDetail, String.valueOf(getDietaMonth()));
			Log.d("Fever", "Begining " + String.valueOf(getDietaBegining()));
			Log.d("Fever", "End" + String.valueOf(getDietaEnd()));
		}

		linearLayout.setOnTouchListener(new DrawerCloseListener());
		bodyLayout.setOnTouchListener(new DrawerCloseListener());
		mCalendarCard.cardGrid.setOnTouchListener(new DrawerCloseListener());
		mCalendarCard.all.setOnTouchListener(new DrawerCloseListener());
		calendarScroll.setOnTouchListener(new DrawerCloseListener());


		mCalendarCard.setOnCellItemClick(new OnCellItemClick() {
			@Override
			public void onCellClick(View v, CardGridItem item) {

				if (!viewActionsContentView.isContentShown())
				{
					viewActionsContentView.showContent();
				}
				else
				{

					ScrollView mainScrollView = (ScrollView) findViewById(R.id.calendarScrollView);
					mainScrollView.fullScroll(ScrollView.FOCUS_UP);

					((CheckableLayout)v).setChecked(true);
					if(new API(ctx).IsDietaSet())
					{
						populateDietaList(item.getDayOfMonth());

					}
					else
					{
						//updates selected cell and current cell style
						String dayOfMonth = item.getDayOfMonth().toString();
						mCalendarCard.updateSelectedCellStyle(dayOfMonth);
					}

				}

			}
		});

		//linearLayout.addView(factory.GenerateChildEjercicio("Uno 1", "asjhgdlgd ladjgajaldal bd lsjdblsbdlsnv dbasv dv sd"));

		
		rightButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int month = cal.get(Calendar.MONTH);

				cal.set(Calendar.MONTH, month + 1);

				if(cal.get(Calendar.MONTH) > month + 1)
					cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);


				mCalendarCard.cleanCellStyle();
				mCalendarCard.iniMonth(ctx, cal);


				mCalendarCard.updateSelectedCellStyle("1");

				if(cal.get(Calendar.MONTH) != month + 1)
					cal.set(Calendar.MONTH, month + 1);

				if(new API(ctx).IsDietaSet())
				{
					if(Calendar.getInstance().get(Calendar.MONTH) == cal.get(Calendar.MONTH))
					{
						initTodayDieta();
					}
					else
					{
						linearLayout.removeAllViewsInLayout();
						Calendar newDate = Calendar.getInstance();
						newDate.set(Calendar.MONTH, month + 1);
						newDate.set(Calendar.DAY_OF_MONTH, 1);

						populateDietaList(1, newDate);
					}
				}
				else
				{
					if(Calendar.getInstance().get(Calendar.MONTH) == cal.get(Calendar.MONTH))
					{
						Calendar cal = Calendar.getInstance();
						int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
						mCalendarCard.updateSelectedCellStyle(String.valueOf(dayOfMonth));
					}
					
				}

			}
		});

		
		leftButton.setOnTouchListener(new DrawerCloseListener());
		leftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub	
				int month = cal.get(Calendar.MONTH);

				cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);

				if(cal.get(Calendar.MONTH) != month - 1)
					cal.set(Calendar.MONTH, month - 1);

				mCalendarCard.cleanCellStyle();
				mCalendarCard.iniMonth(ctx, cal);

				mCalendarCard.updateSelectedCellStyle("1");

				if(cal.get(Calendar.MONTH) != month - 1)
					cal.set(Calendar.MONTH, month - 1);

				if(new API(ctx).IsDietaSet())
				{
					if(Calendar.getInstance().get(Calendar.MONTH) == cal.get(Calendar.MONTH))
					{
						initTodayDieta();
					}
					else
					{
						linearLayout.removeAllViewsInLayout();

						Calendar newDate = Calendar.getInstance();
						newDate.set(Calendar.MONTH, month - 1);
						newDate.set(Calendar.DAY_OF_MONTH, 1);

						populateDietaList(1, newDate);
					}
				}
				else
				{
					if(Calendar.getInstance().get(Calendar.MONTH) == cal.get(Calendar.MONTH))
					{
						Calendar cal = Calendar.getInstance();
						int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
						mCalendarCard.updateSelectedCellStyle(String.valueOf(dayOfMonth));
					}
					
				}
			}
		});

		
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
		switch (item.getItemId()) 
		{
			case android.R.id.home:
				if(viewActionsContentView.isContentShown())
					viewActionsContentView.showActions();
				else
					viewActionsContentView.showContent();
			return true;

			default:
				Intent intent = new Intent(this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void setUpMenu()
	{
		viewActionsContentView = (ActionsContentView) findViewById(R.id.calendario_actionsContentView);

		viewActionsList = (ListView) findViewById(R.id.actions_simple);

		final String[] values = new String[] { 
				"Mi Perfil", 	//0 
				"Mi Dieta", 	//1
				"Mi Ejercicio", //2
				//"Calendario", 	//
				"Seleccionar Dieta",	//3
				"Antes y Despues", 	//4
				"Comparte",	//5 
				"Tip del Día", 	//6
				"Pregúntanos", 	//7
				"Tutorial",	//8
		"Disclaimer"};	//9

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
		
		//Facebbok Components
		if(!new API(ctx).getFacebookID().equals(""))
		{
			MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.simple_menu_fragment);
			fragment.setFacebookId(new API(ctx).getFacebookID());
			fragment.runImageAsyncTask();
			
			TextView facebook_name = (TextView) findViewById(R.id.tvLOL_simple);
			facebook_name.setText(new API(ctx).getFacebookName());
			
			Font f = new Font();					
			f.changeFontRaleway(ctx, facebook_name);

		}
		else
		{
			MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.simple_menu_fragment);
			fragment.setContext(ctx);
			fragment.setDefaultProfilePic();
		}
	}

	private void showActivity(int position) 
	{
		final Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(CalendarioActivity.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(CalendarioActivity.this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(CalendarioActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(CalendarioActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(CalendarioActivity.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;
		case 5:
			intent = new Intent(CalendarioActivity.this, ShareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 6:
			//tip del dia (blog)
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(browserIntent);
			break;
		case 7:
			//preguntanos
			intent = getOpenFacebookIntent(ctx);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 8:
			intent = new Intent(CalendarioActivity.this, TutorialActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);			
			break;
		case 9:
			intent = new Intent(CalendarioActivity.this, DisclaimerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		default:
			return;
		}
	}

	public List<RelativeLayout> initDietaComponents(List<DTO_Componente> lista)
	{
		if(lista.size() == 0)
		{
			Toast.makeText(ctx, "No hay datos para este dia", Toast.LENGTH_SHORT).show();
			return null;
		}

		List<RelativeLayout> children = new ArrayList<RelativeLayout>();
		DietaChildFactory factory = new DietaChildFactory();

		//list to hold all same Comida entries in 'lista'
		final List<DTO_Componente> content = new ArrayList<DTO_Componente>();
		for(int i=1; i < lista.size(); i++)
		{
			//checks if the Alimento of a given insert in 'lista' is the same as the last one. If so, it is then added to 'content'
			if(lista.get(i).getAlimento().equals(lista.get(i-1).getAlimento()))
			{
				content.add(lista.get(i-1));
			}
			else
			{
				//when a different Alimento occurs, the parameters to create the DietaChild are set up
				String title = lista.get(i-1).getAlimento();
				boolean activo = false;

				//the last checked insert in 'lista' is added to
				content.add(lista.get(i-1));

				//use same function to generate children!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!				
				children.add(factory.GenerateChildCalendar(ctx, title, content));



				//the content list is cleared 
				content.clear();
			}

		}

		//same behavior as last, but this is handled when an Alimento only carries one insert in content and the last Alimento instance
		String title = lista.get(lista.size()-1).getAlimento();
		content.add(lista.get(lista.size()-1));

		children.add(factory.GenerateChildCalendar(ctx, title, content));



		content.clear();

		return children;
	}

	//Saves selected style colors to local variables
	private void getStyle()
	{
		Log.d("color", "en perfil" + new API(ctx).getStyle());
		//get selected style from database
		String style = new API(ctx).getStyle();
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
			
			rightButton.setImageResource(R.drawable.flechader_mas);
			leftButton.setImageResource(R.drawable.flechaizq_mas);
		}
		if(style.equals("femenino"))
		{
			styleMain = res.getColor(R.color.FEMENINO_MAIN);
			styleDetail = res.getColor(R.color.FEMENINO_DETAIL);
			styleDarkest = res.getColor(R.color.FEMENINO_DARKEST);
			styleDark = res.getColor(R.color.FEMENINO_DARKER);
			styleBright = res.getColor(R.color.FEMENINO_BRIGHTER);
			styleBrightest = res.getColor(R.color.FEMENINO_BRIGHTEST);
			
			rightButton.setImageResource(R.drawable.flechader_fem);
			leftButton.setImageResource(R.drawable.flechaizq_fem);
		}
		if(style.equals("neutral"))
		{
			styleMain = res.getColor(R.color.NEUTRAL_MAIN);
			styleDetail = res.getColor(R.color.NEUTRAL_DETAIL);
			styleDarkest = res.getColor(R.color.NEUTRAL_DARKEST);
			styleDark = res.getColor(R.color.NEUTRAL_DARKER);
			styleBright = res.getColor(R.color.NEUTRAL_BRIGHTER);
			styleBrightest = res.getColor(R.color.NEUTRAL_BRIGHTEST);
			
			rightButton.setImageResource(R.drawable.flechader_neu);
			leftButton.setImageResource(R.drawable.flechaizq_neut);
		}

	}

	public View getDarkest()
	{
		final float scale = ctx.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(ctx);

		v.setLayoutParams(new LayoutParams(size, pixels));
		v.setBackgroundColor(styleDarkest);

		return v;
	}
	public View getDarker()
	{
		final float scale = ctx.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(ctx);

		v.setLayoutParams(new LayoutParams(size, pixels));
		v.setBackgroundColor(styleDark);

		return v;
	}
	public View getBrighter()
	{
		final float scale = ctx.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(ctx);

		v.setLayoutParams(new LayoutParams(size, pixels));
		v.setBackgroundColor(styleBright);

		return v;
	}
	public View getBrightest()
	{
		final float scale = ctx.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(ctx);

		v.setLayoutParams(new LayoutParams(size, pixels));
		v.setBackgroundColor(styleBrightest);

		return v;
	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{
		//header setoff bar
		View bar1 = (View) findViewById(R.id.calBar1);
		View bar2 = (View) findViewById(R.id.calBar2);
		View bar3 = (View) findViewById(R.id.calBar3);
		View bar4 = (View) findViewById(R.id.calBar4);

		//save bar size to a global variable
		//size = bar1.getLayoutParams().width;

		//sets local style variables
		getStyle();

		bodyLayout.setBackgroundColor(styleMain);
		linearLayout.setBackgroundColor(styleMain);

		bar1.setBackgroundColor(styleDarkest);
		bar2.setBackgroundColor(styleDark);
		bar3.setBackgroundColor(styleBright);
		bar4.setBackgroundColor(styleBrightest);

	}

	//loads tod
	private void initTodayDieta()
	{
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

		populateDietaList(dayOfMonth);

		mCalendarCard.updateSelectedCellStyle(String.valueOf(dayOfMonth));
	}

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
	}

	public void populateDietaList(int day)
	{
		if(mCalendarCard.getFirstDay()<=day)
		{
			java.util.Date initialDate = mCalendarCard.getInitialDate();
			cal.set(Calendar.DAY_OF_MONTH, day);
			java.util.Date selectedDate =  cal.getTime();

			Log.d("actor", "Fecha de inicio: " + initialDate);
			Log.d("actor", "Fecha seleccionada: " + selectedDate);

			long dayDifference = getDateDiffString(initialDate, selectedDate);

			List<DTO_Componente> componentes = new DAO_Componente(ctx).getAllComponente(
					new API(ctx).getID_Dieta(),String.valueOf(dayDifference + 1));
			int t = Math.abs(mCalendarCard.getFirstDay()-day) + 1;
			List<RelativeLayout> childrenLayout = initDietaComponents(componentes);
			linearLayout.removeAllViewsInLayout();

			if (childrenLayout != null) 
			{
				for (RelativeLayout temp : childrenLayout) 
				{
					linearLayout.addView(temp);
				}

				//updates selected cell and current cell style
				String dayOfMonth = String.valueOf(day);
				mCalendarCard.updateSelectedCellStyle(dayOfMonth);
			}
		}
		else
		{
			Toast.makeText(ctx, "No hay datos para este dia", Toast.LENGTH_SHORT).show();
		}
	}

	public void populateDietaList(int day, Calendar newD)
	{

		java.util.Date initialDate = mCalendarCard.getInitialDate();
		cal.set(Calendar.DAY_OF_MONTH, day);
		java.util.Date selectedDate =  newD.getTime();

		Log.d("actor", "Fecha de inicio1: " + initialDate);
		Log.d("actor", "Fecha seleccionada1: " + selectedDate);

		long dayDifference = getDateDiffString(initialDate, selectedDate);

		List<DTO_Componente> componentes = new DAO_Componente(ctx).getAllComponente(
				new API(ctx).getID_Dieta(),String.valueOf(dayDifference + 1));
		int t = Math.abs(mCalendarCard.getFirstDay()-day) + 1;
		List<RelativeLayout> childrenLayout = initDietaComponents(componentes);
		linearLayout.removeAllViewsInLayout();

		if (childrenLayout != null) 
		{
			for (RelativeLayout temp : childrenLayout) 
			{
				linearLayout.addView(temp);
			}

			//updates selected cell and current cell style
			String dayOfMonth = String.valueOf(day);
			mCalendarCard.updateSelectedCellStyle(dayOfMonth);
		}
		else
		{
			mCalendarCard.updateSelectedCellStyle("1");
			Toast.makeText(ctx, "No hay datos para este dia", Toast.LENGTH_SHORT).show();
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
	public void onStop(){
		super.onStop();

		if(viewActionsContentView != null)
			viewActionsContentView.showContent();
	}

	private final class DrawerCloseListener implements View.OnTouchListener
	{

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (!viewActionsContentView.isContentShown())
			{
				viewActionsContentView.showContent();
				return true;
			}
			return false;
		}

	}
	
	private int getDietaBegining()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
		java.util.Date initalDate = null;
		String lol = new API(ctx).getDia();
		try {
			initalDate = df.parse(new API(ctx).getDia());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.setTime(initalDate);
		return c.get(Calendar.DATE);
	}
	
	private int getDietaMonth()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
		java.util.Date initalDate = null;
		String lol = new API(ctx).getDia();
		try {
			initalDate = df.parse(new API(ctx).getDia());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.setTime(initalDate);
		return c.get(Calendar.MONTH);
	}
	
	private int getDietaEnd()
	{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
		java.util.Date initalDate = null;
		String lol = new API(ctx).getDia();
		try {
			initalDate = df.parse(new API(ctx).getDia());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.setTime(initalDate);
		String dietaDuration = new DAO_Dieta(ctx).getDieta(new API(ctx).getID_Dieta()).getDuracion();
		c.add(Calendar.DATE, Integer.valueOf(dietaDuration));
		String todayDate = df.format(c.getTime());
		
		return c.get(Calendar.DATE);
	}

}
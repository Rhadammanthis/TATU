package com.example.miyoideal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import shared.ui.actionscontentview.ActionsContentView;

import com.example.DAO.DAO_Componente;
import com.example.DTO.*;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.CalendarioChildFactory;
import com.example.miyoideal.extra.DietaChild;
import com.example.miyoideal.extra.DietaChildFactory;
import com.example.miyoideal.extra.SideMenu;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

import com.wt.calendarcard.*;

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

	int styleMain;
	int styleDetail;
	int styleDarkest;
	int styleDark;
	int styleBright;
	int styleBrightest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline3);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;	
		ctx = this;

		View bar = (View) findViewById(R.id.calBar1);
		this.size = bar.getLayoutParams().width;


		viewActionsContentView = (ActionsContentView) findViewById(R.id.calendario_actionsContentView);
		linearLayout = (LinearLayout) findViewById(R.id.calendarLinearLayout);		
		bodyLayout = (LinearLayout) findViewById(R.id.calendarioBody);

		viewActionsList = (ListView) findViewById(R.id.actions);
		setUpMenu();

		//updates style according to user selected theme
		updateStyle();

		//init calendar widget
		mCalendarCard = (CalendarCard)findViewById(R.id.calendarCard6);
		mCalendarCard.getLayoutParams().height = height/2;

		//load today's dieta components, if dieta is set
		if(new API(ctx).IsDietaSet())
		{
			Calendar cal = Calendar.getInstance();
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			int dayOfMonthInt = Integer.parseInt(String.valueOf(dayOfMonth));

			List<DTO_Componente> componentes = new DAO_Componente(ctx).getAllComponente(
					new API(ctx).getID_Dieta(), String.valueOf((Math.abs(mCalendarCard.getFirstDay()-dayOfMonthInt) + 1)));
			int p = Math.abs(mCalendarCard.getFirstDay()-dayOfMonthInt) + 1;
			List<RelativeLayout> childrenLayout = initDietaComponents(componentes);
			linearLayout.removeAllViewsInLayout();
			for(RelativeLayout temp : childrenLayout)
			{
				linearLayout.addView(temp);
				//to generate row setoff shadow
//				linearLayout.addView(getDarkest());
//				linearLayout.addView(getDarker());
//				linearLayout.addView(getBrighter());
//				linearLayout.addView(getBrightest());
			}
		}

		mCalendarCard.setOnCellItemClick(new OnCellItemClick() {
			@Override
			public void onCellClick(View v, CardGridItem item) {

				ScrollView mainScrollView = (ScrollView) findViewById(R.id.calendarScrollView);
				mainScrollView.fullScroll(ScrollView.FOCUS_UP);

				((CheckableLayout)v).setChecked(true);
				if(new API(ctx).IsDietaSet())
				{
					List<DTO_Componente> componentes = new DAO_Componente(ctx).getAllComponente(
							new API(ctx).getID_Dieta(), String.valueOf((Math.abs(mCalendarCard.getFirstDay()-item.getDayOfMonth()) + 1)));
					int t = Math.abs(mCalendarCard.getFirstDay()-item.getDayOfMonth()) + 1;
					List<RelativeLayout> childrenLayout = initDietaComponents(componentes);
					linearLayout.removeAllViewsInLayout();
					for(RelativeLayout temp : childrenLayout)
					{
						linearLayout.addView(temp);
						//to generate row setoff shadow
//						linearLayout.addView(getDarkest());
//						linearLayout.addView(getDarker());
//						linearLayout.addView(getBrighter());
//						linearLayout.addView(getBrightest());
					}

					//updates selected cell and current cell style
					String dayOfMonth = item.getDayOfMonth().toString();
					mCalendarCard.updateSelectedCellStyle(dayOfMonth);

				}
				
				//updates selected cell and current cell style
				String dayOfMonth = item.getDayOfMonth().toString();
				mCalendarCard.updateSelectedCellStyle(dayOfMonth);
			}
		});

		CalendarioChildFactory factory = new CalendarioChildFactory(this);

		//linearLayout.addView(factory.GenerateChildEjercicio("Uno 1", "asjhgdlgd ladjgajaldal bd lsjdblsbdlsnv dbasv dv sd"));
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
			intent = new Intent(CalendarioActivity.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(CalendarioActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		default:
			return;
		}

	}

	public List<RelativeLayout> initDietaComponents(List<DTO_Componente> lista)
	{
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

}
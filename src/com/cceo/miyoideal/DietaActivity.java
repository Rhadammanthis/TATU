package com.cceo.miyoideal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import shared.ui.actionscontentview.ActionsContentView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.cceo.DAO.DAO_Componente;
import com.cceo.DTO.DTO_Componente;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.HomeActivity.*;
import com.cceo.miyoideal.extra.*;
import com.github.johnpersano.supertoasts.SuperActivityToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.github.johnpersano.supertoasts.util.OnClickWrapper;

public class DietaActivity extends Activity {

	private LinearLayout mainLayout;
	private ListView viewActionsList;
	private TextView currentDate;
	private DietaChildFactory dietaChildFactory;
	private Context con;
	private List<DietaChild> dietaChildList;

	private List<DTO_Componente> componentes;
	private ImageButton nextDay;
	private ImageButton previousDay;
	private RelativeLayout dietaBody;
	private RelativeLayout dietaHeader;

	private ImageButton cancelButton;
	private ActionsContentView viewActionsContentView;

	int size;

	int styleMain;
	int styleDetail;
	int styleDarkest;
	int styleDark;
	int styleBright;
	int styleBrightest;

	private boolean checkBoxInitialState[];

	private Calendar c;
	private Font font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.baseline_dieta);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.actionbar_icon_white);
		
		con = this;
		font = new Font();
		
		dietaChildList = new ArrayList<DietaChild>();
		viewActionsList = (ListView) findViewById(R.id.actions_simple);	
		mainLayout = (LinearLayout) findViewById(R.id.dieta_linearLayout);
		currentDate = (TextView) findViewById(R.id.fechaDieta);
		dietaBody = (RelativeLayout) findViewById(R.id.dietaBody);
		dietaChildFactory = new DietaChildFactory();
		nextDay = (ImageButton)findViewById(R.id.nextDay);
		previousDay = (ImageButton)findViewById(R.id.previousDay);
		cancelButton = (ImageButton) findViewById(R.id.dieta_cancel);
		dietaHeader = (RelativeLayout) findViewById(R.id.dietaHeader);
		
		mainLayout.setOnTouchListener(new DrawerCloseListener());
		previousDay.setOnTouchListener(new DrawerCloseListener());
		dietaBody.setOnTouchListener(new DrawerCloseListener());
		
		font.changeFontRaleway(con, currentDate);
		//font.changeFontRaleway(con, cancelButton);
		
		LinearLayout ln = (LinearLayout) findViewById(R.id.dietaBodyFrame);
		ln.setOnTouchListener(new DrawerCloseListener());
		//initializes side menu
		setUpMenu();
		
		//updates style
		updateStyle();

		//if a "dieta" is currently set, the corresponding components are loaded
		if(!new API(con).IsDietaSet())
		{
			OnClickWrapper onClickWrapper;
			
			SuperActivityToast superActivityToast = new SuperActivityToast(DietaActivity.this, SuperToast.Type.BUTTON);
			superActivityToast.setDuration(SuperToast.Duration.LONG);
			superActivityToast.setBackground(SuperToast.Background.BLACK);
			superActivityToast.setTextColor(Color.WHITE);
			superActivityToast.setText("¡Selecciona una Dieta!");
			superActivityToast.setButtonIcon(SuperToast.Icon.Dark.UNDO, " ");
			
			superActivityToast.show();

			/**
			 * The OnClickWrapper is needed to reattach SuperToast.OnClickListeners on orientation changes. 
			 * It does this via a unique String tag defined in the first parameter so each OnClickWrapper's tag 
			 * should be unique.
			 */
			onClickWrapper = new OnClickWrapper("superactivitytoast", new SuperToast.OnClickListener() {

				@Override
				public void onClick(View view, Parcelable token) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(DietaActivity.this, SelecDieta.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}

			});
			
			superActivityToast.setOnClickWrapper(onClickWrapper);
		}
		else 
		{
			//loads data to main Linear Layout and binds buttons with respective behaviour
			populateDietaView();
		}

		/*check if Cancelar Dieta button should be displayed
		 * button is active by default
		 */
		if(!new API(con).IsDietaSet())
		{			
			cancelButton.setVisibility(View.GONE);
		}
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CancelDietaDialog dialog = new CancelDietaDialog(con);
				dialog.show(getFragmentManager(), "cancel");
			}
		});

	}

	private void populateDietaView() {
		// TODO Auto-generated method stub
		c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
		java.util.Date initalDate = null;
		String lol = new API(this).getDia();
		try {
			initalDate = df.parse(new API(this).getDia());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String todayDate = df.format(c.getTime());	
		long distance = getDateDiffString(initalDate,c.getTime());
		if(new API(this).IsDietaSet())
		{
			componentes = new DAO_Componente(con).getAllComponente(new API(con).getID_Dieta(), String.valueOf(distance+1));
			if(componentes.size()>0)
				initDietaLayout(componentes);
			else
			{
				mainLayout.addView(new DietaChild(con).getDefaultLayout());

			}
		}
		currentDate.setText(todayDate);

		nextDay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//on a second call, the initial date is still the one set on the previous page.
				Log.d("dieta", "nextDay");
				mainLayout.removeAllViews();
				c.add(Calendar.DATE, 1);
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
				long distance = getDateDiffString(initalDate,c.getTime());
				Log.d("311", "Diferencia " + String.valueOf(distance));
				if(new API(con).IsDietaSet())
				{
					componentes = new DAO_Componente(con).getAllComponente(new API(con).getID_Dieta(), String.valueOf(distance+1));
					if(componentes.size()>0)
					{
						initDietaLayout(componentes);
						cancelButton.setVisibility(View.VISIBLE);
					}
					else
					{
						RelativeLayout dafault = new DietaChild(con).getDefaultLayout();
						//dafault.getLayoutParams().height = dietaBody.getLayoutParams().height;
						mainLayout.addView(dafault);
						cancelButton.setVisibility(View.INVISIBLE);
					}
				}

				currentDate.setText(todayDate);
			}
		});

		previousDay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//on a second call, the initial date is still the one set on the previous page.
				Log.d("dieta", "previousDay");
				mainLayout.removeAllViews();
				c.add(Calendar.DATE, -1);
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
				long distance = getDateDiffString(initalDate,c.getTime());
				if(new API(con).IsDietaSet())
				{
					componentes = new DAO_Componente(con).getAllComponente(new API(con).getID_Dieta(), String.valueOf(distance+1));
					if(componentes.size()>0)
					{
						initDietaLayout(componentes);
						cancelButton.setVisibility(View.VISIBLE);
					}
					else
					{
						RelativeLayout dafault = new DietaChild(con).getDefaultLayout();
						//dafault.getLayoutParams().height = dietaBody.getLayoutParams().height;
						mainLayout.addView(dafault);
						cancelButton.setVisibility(View.INVISIBLE);
					}
				}

				currentDate.setText(todayDate);
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
		viewActionsContentView = (ActionsContentView) findViewById(R.id.dieta_actionsContentView);

		viewActionsList = (ListView) findViewById(R.id.actions_simple);
		
		final String[] values = new String[] { 
				"Mi Perfil", 	//0 
				"Mi Ejercicio", //1
				"Calendario", 	//2
				"Seleccionar Dieta",	//3
				"Antes y Despues", 	//4
				"Comparte",	//5 
				"Tip del Día", 	//6
				"Preguntanos", 	//7
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
			intent = new Intent(DietaActivity.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(DietaActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(DietaActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(DietaActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(DietaActivity.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(DietaActivity.this, ShareActivity.class);
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
			intent = getOpenFacebookIntent(con);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 8:
			intent = new Intent(DietaActivity.this, TutorialActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);			
			break;
		case 9:
			intent = new Intent(DietaActivity.this, DisclaimerActivity.class);
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

	public void initDietaLayout(List<DTO_Componente> lista)
	{
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
				

				//Cheks wheter one of the inserts has a negative value in Activo to initialize the checkbox as checked
				for(DTO_Componente temp : content)
				{
					if(temp.getActivo().equals("yes"))
						activo = true;
				}

				//A new DietaChild is created from the dietaChildFactory
				final DietaChild newChild = dietaChildFactory.GenerateChildDieta(con, title, content, "10 am", activo);

				//A click listener is added to the checkbox the handle checked and unchecked behavior
				newChild.getCheckBox().setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						//when the status of checkbox changes, its current state is saved in the DB
						if(newChild.getCheckBox().isChecked())
						{
							new DAO_Componente(con).updateComponenteYes(getModifiedChild().getDTO_Componente());
						}	
						else
						{
							new DAO_Componente(con).updateComponenteNo(getModifiedChild().getDTO_Componente());
						}		

						//update inital checkbox status's
						for(int i=0;i< dietaChildList.size();i++)
						{
							checkBoxInitialState[i] = dietaChildList.get(i).getCheckBox().isChecked();
						}
					}
				});

				//the dietachild is added to the main layout
				mainLayout.addView(newChild.getChild());

				//to generate row setoff shadow
				mainLayout.addView(getDarkest());
				mainLayout.addView(getDarker());
				mainLayout.addView(getBrighter());
				mainLayout.addView(getBrightest());

				//we keep track of children views in a list
				dietaChildList.add(newChild);

				//the content list is cleared 
				content.clear();
			}
		}

		//same behavior as last, but this handles when an Alimento only carries one insert in the content list and is the last Alimento instance
		String title = lista.get(lista.size()-1).getAlimento();
		boolean activo = false;
		content.add(lista.get(lista.size()-1));

		for(DTO_Componente temp : content)
		{
			if(temp.getActivo().equals("yes"))
				activo = true;
		}

		final DietaChild newChild = dietaChildFactory.GenerateChildDieta(con, title, content, "10 am", activo);
		newChild.getCheckBox().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(newChild.getCheckBox().isChecked())
				{
					new DAO_Componente(con).updateComponenteYes(getModifiedChild().getDTO_Componente());
				}	
				else
				{
					new DAO_Componente(con).updateComponenteNo(getModifiedChild().getDTO_Componente());
				}

				for(int i=0;i< dietaChildList.size();i++)
				{
					checkBoxInitialState[i] = dietaChildList.get(i).getCheckBox().isChecked();
				}
			}
		});
		
		mainLayout.addView(newChild.getChild());
		
		//to generate row setoff shadow
		mainLayout.addView(getDarkest());
		mainLayout.addView(getDarker());
		mainLayout.addView(getBrighter());
		mainLayout.addView(getBrightest());
		
		dietaChildList.add(newChild);

		content.clear();

		checkBoxInitialState = new boolean[dietaChildList.size()];

		for(int i=0;i< dietaChildList.size();i++)
		{
			checkBoxInitialState[i] = dietaChildList.get(i).getCheckBox().isChecked();
		}
	}

	public DietaChild getModifiedChild()
	{
		DietaChild res = new DietaChild(con);

		boolean checkBoxActualState[] = new boolean[dietaChildList.size()];
		int size = dietaChildList.size();

		for(int i=0;i<size;i++)
		{
			checkBoxActualState[i] = dietaChildList.get(i).getCheckBox().isChecked();
		}

		for(int i=0;i<size;i++)
		{
			if(checkBoxInitialState[i] != checkBoxActualState[i])
			{
				return dietaChildList.get(i);
			}
		}

		return res;
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

	public View getDarkest()
	{
		final float scale = con.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(con);

		v.setLayoutParams(new LayoutParams(size, pixels));
		v.setBackgroundColor(styleDarkest);

		return v;
	}
	public View getDarker()
	{
		final float scale = con.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(con);

		v.setLayoutParams(new LayoutParams(size, pixels));
		v.setBackgroundColor(styleDark);

		return v;
	}
	public View getBrighter()
	{
		final float scale = con.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(con);

		v.setLayoutParams(new LayoutParams(size, pixels));
		v.setBackgroundColor(styleBright);

		return v;
	}
	public View getBrightest()
	{
		final float scale = con.getResources().getDisplayMetrics().density;
		int pixels = (int) (1 * scale + 0.5f);
		View v = new View(con);

		v.setLayoutParams(new LayoutParams(size, pixels));
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
			
			nextDay.setImageResource(R.drawable.flechader_mas);
			previousDay.setImageResource(R.drawable.flechaizq_mas);
		}
		if(style.equals("femenino"))
		{
			styleMain = res.getColor(R.color.FEMENINO_MAIN);
			styleDetail = res.getColor(R.color.FEMENINO_DETAIL);
			styleDarkest = res.getColor(R.color.FEMENINO_DARKEST);
			styleDark = res.getColor(R.color.FEMENINO_DARKER);
			styleBright = res.getColor(R.color.FEMENINO_BRIGHTER);
			styleBrightest = res.getColor(R.color.FEMENINO_BRIGHTEST);
			
			nextDay.setImageResource(R.drawable.flechader_fem);
			previousDay.setImageResource(R.drawable.flechaizq_fem);
		}
		if(style.equals("neutral"))
		{
			styleMain = res.getColor(R.color.NEUTRAL_MAIN);
			styleDetail = res.getColor(R.color.NEUTRAL_DETAIL);
			styleDarkest = res.getColor(R.color.NEUTRAL_DARKEST);
			styleDark = res.getColor(R.color.NEUTRAL_DARKER);
			styleBright = res.getColor(R.color.NEUTRAL_BRIGHTER);
			styleBrightest = res.getColor(R.color.NEUTRAL_BRIGHTEST);
			
			nextDay.setImageResource(R.drawable.flechader_neu);
			previousDay.setImageResource(R.drawable.flechaizq_neut);
		}

	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{
		//header setoff bar
		View bar1 = (View) findViewById(R.id.dietaBar1);
		View bar2 = (View) findViewById(R.id.dietaBar2);
		View bar3 = (View) findViewById(R.id.dietaBar3);
		View bar4 = (View) findViewById(R.id.dietaBar4);
		LinearLayout bodyFrame = (LinearLayout) findViewById(R.id.dietaBodyFrame);
		
		//save bar size to a global variable
		size = bar1.getLayoutParams().width;
		
		//sets local style variables
		getStyle();
		
		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
		bodyFrame.setBackgroundColor(styleMain);
		dietaBody.setBackgroundColor(styleMain);
		dietaHeader.setBackgroundColor(styleBright);
		
		bar1.setBackgroundColor(styleDarkest);
		bar2.setBackgroundColor(styleDark);
		bar3.setBackgroundColor(styleBright);
		bar4.setBackgroundColor(styleBrightest);
		

	}
	
	@Override
	public void onStop()
	{
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
}

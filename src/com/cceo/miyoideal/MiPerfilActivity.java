package com.cceo.miyoideal;

import java.util.Arrays;

import javax.crypto.spec.IvParameterSpec;

import org.json.JSONObject;

import com.cceo.DAO.DAO_DietaCompletada;
import com.cceo.DAO.DAO_Usuario;
import com.cceo.DB.SQLiteControl;
import com.cceo.DTO.DTO_Usuario;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.DatePickerFragment;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.ImageAsyncTask;
import com.cceo.miyoideal.extra.MenuFragment;
import com.cceo.miyoideal.extra.MyArrayAdapter;
import com.cceo.miyoideal.extra.OnDatePickerDismissListener;
import com.cceo.miyoideal.extra.PerfilNumberPicker;
import com.cceo.miyoideal.extra.SelectDialogue;
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

import shared.ui.actionscontentview.ActionsContentView;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MiPerfilActivity extends FragmentActivity implements DialogInterface.OnDismissListener, OnItemSelectedListener, OnDatePickerDismissListener{

	//layout components
	private Spinner spinnerNivel;
	private RelativeLayout pesoDeseadoRL;
	private RelativeLayout tallaDeseadoRL;
	private RelativeLayout goalDateRL;
	private TextView pesoIdeal;
	private TextView tallaIdeal;
	private TextView tvGoalDate;
	private LinearLayout mainLayout;
	private ScrollView scroll;
	private View bar;
	private EditText motivacion;

	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	private Context con;

	private Rect rect;

	//Style variables
	private int styleMain;
	private int styleDetail;
	private int styleDark;

	private static ImageView ivProfilePic;
	private TextView tvProfileName;

	//facebook login
	public CallbackManager callbackManager;
	private LoginButton loginButton;
	private SQLiteControl db;

	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline2);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.actionbar_icon_white);
		con = this;		

		tvGoalDate = (TextView) findViewById(R.id.goalDate);
		ivProfilePic = (ImageView) findViewById(R.id.ivPerfilProfilePic);
		tvProfileName = (TextView) findViewById(R.id.tvPerfilName);
		runImageAsyncTask();

		setUpMenu();

		FacebookSdk.sdkInitialize(getApplicationContext());

		db = new SQLiteControl(this);
		//DrawerCloseListener draweCloseListener = new DrawerCloseListener(viewActionsContentView);

		spinnerNivel = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.nivel_array, R.layout.spinnercustomitem);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerNivel.setAdapter(adapter);	
		DTO_Usuario userData = new DAO_Usuario(con).getUsuario();
		int pos = 0;
		Log.d("blood", userData.getNivel());
		if(userData.getNivel().equals("Bajo")) pos = 0;
		if(userData.getNivel().equals("Medio")) pos = 1;
		if(userData.getNivel().equals("Alto")) pos = 2;
		spinnerNivel.setSelection(pos, true);

		spinnerNivel.setOnItemSelectedListener(this);

		pesoIdeal = (TextView) findViewById(R.id.pesoIdeal);
		tallaIdeal = (TextView) findViewById(R.id.tallaIdeal);
		mainLayout = (LinearLayout) findViewById(R.id.miPerfilMainLayout);
		scroll = (ScrollView) findViewById(R.id.miPerfilScroll);
		scroll.setOnTouchListener(new DrawerCloseListener());
		bar = (View) findViewById(R.id.miPerfilBar);
		motivacion = (EditText) findViewById(R.id.miPerfilMotivaciones);

		//update view's style
		updateStyle();

		changeFont();

		setPesoIdealValue();

		final FragmentManager manager = this.getFragmentManager();

		tallaDeseadoRL = (RelativeLayout) findViewById(R.id.tallaDeseadoRL);
		tallaDeseadoRL.setOnTouchListener(new DrawerCloseListener());
		tallaDeseadoRL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//				PerfilNumberPicker dialog = new PerfilNumberPicker(con);
				//				dialog.show(manager, "Picker");

			}
		});

		tallaDeseadoRL.setOnTouchListener(new View.OnTouchListener() {

			private boolean shouldCancel = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub


				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					tallaDeseadoRL.setBackgroundColor(styleDark);

					rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

					shouldCancel = false;

					break;
				case MotionEvent.ACTION_MOVE:
					//					Toast.makeText(context, "lol", Toast.LENGTH_SHORT).show();
					if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
					{
						// User moved outside bounds
						shouldCancel = true;
						tallaDeseadoRL.setBackgroundColor(styleMain); 
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					tallaDeseadoRL.setBackgroundColor(styleMain); 
					break;
				case MotionEvent.ACTION_UP:
					//set color back to default
					tallaDeseadoRL.setBackgroundColor(styleMain); 

					if (!shouldCancel) 
					{
						SelectDialogue temp1;
						//Checks whether dieta is available
						if (!new API(con).IsDietaSet()) {
							PerfilNumberPicker dialog = new PerfilNumberPicker(con, 1);
							dialog.show(manager, "Picker");
						} else {
							PerfilNumberPicker dialog = new PerfilNumberPicker(con, 1);
							dialog.show(manager, "Picker");
						}

						shouldCancel = false;
					}
					break;
				}
				return false;
			}
		});
		
		goalDateRL = (RelativeLayout) findViewById(R.id.goalDateRL);
		goalDateRL.setOnTouchListener(new DrawerCloseListener());
		goalDateRL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//				PerfilNumberPicker dialog = new PerfilNumberPicker(con);
				//				dialog.show(manager, "Picker");

			}
		});
		goalDateRL.setOnTouchListener(new View.OnTouchListener() {

			private boolean shouldCancel = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub


				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					goalDateRL.setBackgroundColor(styleDark);

					rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

					shouldCancel = false;

					break;
				case MotionEvent.ACTION_MOVE:
					//					Toast.makeText(context, "lol", Toast.LENGTH_SHORT).show();
					if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
					{
						// User moved outside bounds
						shouldCancel = true;
						goalDateRL.setBackgroundColor(styleMain); 
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					goalDateRL.setBackgroundColor(styleMain); 
					break;
				case MotionEvent.ACTION_UP:
					//set color back to default
					goalDateRL.setBackgroundColor(styleMain); 

					if (!shouldCancel) 
					{
						SelectDialogue temp1;
						//Checks whether dieta is available
						DialogFragment newFragment = new DatePickerFragment(MiPerfilActivity.this);
						newFragment.show(getFragmentManager(), "datePicker");

						shouldCancel = false;
					}
					break;
				}
				return false;
			}
		});
		
		pesoDeseadoRL = (RelativeLayout) findViewById(R.id.pesoDeseadoRL);
		pesoDeseadoRL.setOnTouchListener(new DrawerCloseListener());
		pesoDeseadoRL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//				PerfilNumberPicker dialog = new PerfilNumberPicker(con);
				//				dialog.show(manager, "Picker");

			}
		});

		pesoDeseadoRL.setOnTouchListener(new View.OnTouchListener() {

			private boolean shouldCancel = false;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub


				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					pesoDeseadoRL.setBackgroundColor(styleDark);

					rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

					shouldCancel = false;

					break;
				case MotionEvent.ACTION_MOVE:
					//					Toast.makeText(context, "lol", Toast.LENGTH_SHORT).show();
					if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
					{
						// User moved outside bounds
						shouldCancel = true;
						pesoDeseadoRL.setBackgroundColor(styleMain); 
					}
					break;
				case MotionEvent.ACTION_CANCEL:
					pesoDeseadoRL.setBackgroundColor(styleMain); 
					break;
				case MotionEvent.ACTION_UP:
					//set color back to default
					pesoDeseadoRL.setBackgroundColor(styleMain); 

					if (!shouldCancel) 
					{
						SelectDialogue temp1;
						//Checks whether dieta is available
						if (!new API(con).IsDietaSet()) {
							PerfilNumberPicker dialog = new PerfilNumberPicker(con, 0);
							dialog.show(manager, "Picker");
						} else {
							PerfilNumberPicker dialog = new PerfilNumberPicker(con, 0);
							dialog.show(manager, "Picker");
						}

						shouldCancel = false;
					}
					break;
				}
				return false;
			}
		});

		DTO_Usuario usuario = new DAO_Usuario(this).getUsuario();

		TextView peso = (TextView) findViewById(R.id.pesoActual_);
		peso.setText(usuario.getPeso());
		TextView talla = (TextView) findViewById(R.id.tallaActual_);	
		talla.setText(usuario.getTalla());
		TextView estatura = (TextView) findViewById(R.id.estaturaActual_);	
		estatura.setText(usuario.getEestatura());

		Font font = new Font();
		font.changeFontRaleway(con, peso);
		font.changeFontRaleway(con, talla);
		font.changeFontRaleway(con, estatura);

		motivacion.setText(new API(con).getMotivacion());
		motivacion.setOnTouchListener(new DrawerCloseListener());

		motivacion.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (!hasFocus) {
					Toast.makeText(con, "Finished focus", Toast.LENGTH_SHORT).show();
				}
			}
		});
		//		
		motivacion.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					Toast.makeText(con, "Enter", Toast.LENGTH_SHORT).show();
					if (!event.isShiftPressed()) {
						// the user is done typing. 

						return true; // consume.
					}                
				}
				return false;
			}
		});

//		Button bDate = (Button)findViewById(R.id.miPerfil_bDate);
//		bDate.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				DialogFragment newFragment = new DatePickerFragment(MiPerfilActivity.this);
//				newFragment.show(getFragmentManager(), "datePicker");
//
//			}
//		});
	}

	private void changeFont() {
		// TODO Auto-generated method stub
		Font font = new Font();
		//TextView tv_1 = (TextView) findViewById(R.id.miPerfil_1);
		TextView tv_2 = (TextView) findViewById(R.id.miPerfil_2);
		TextView tv_3 = (TextView) findViewById(R.id.miPerfil_3);
		TextView tv_4 = (TextView) findViewById(R.id.miPerfil_4);
		TextView tv_44 = (TextView) findViewById(R.id.miPerfil_44);
		TextView tv_5 = (TextView) findViewById(R.id.miPerfil_5);
		TextView tv_6 = (TextView) findViewById(R.id.miPerfil_6);
		TextView tv_55 = (TextView) findViewById(R.id.miPerfil_55);
		TextView tv_mPT = (TextView) findViewById(R.id.miPerfilTalla);

		//font.changeFontRaleway(con, tv_1);
		font.changeFontRaleway(con, tv_2);
		font.changeFontRaleway(con, tv_3);
		font.changeFontRaleway(con, tv_4);
		font.changeFontRaleway(con, tv_5);
		font.changeFontRaleway(con, tv_6);
		font.changeFontRaleway(con, tv_44);
		font.changeFontRaleway(con, tv_55);
		font.changeFontRaleway(con, tv_mPT);
		font.changeFontRaleway(con, motivacion);
		font.changeFontRaleway(con, pesoIdeal);
		font.changeFontRaleway(con, tallaIdeal);
		font.changeFontRaleway(con, tvProfileName);
		font.changeFontRaleway(con, tvGoalDate);
	}

	@Override
	protected void onPause()
	{
		super.onPause();


	}

	private void setPesoIdealValue() {
		// TODO Auto-generated method stub

		pesoIdeal.setText(new API(con).getPesoIdeal());
		tallaIdeal.setText(new API(con).getTallaIdeal());
		tvGoalDate.setText(new API(con).getGoalDate());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Add the menu layout to the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.base_action_bar, menu);
		viewActionsList.setSelectionAfterHeaderView();
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
		viewActionsContentView = (ActionsContentView) findViewById(R.id.miPerfil_actionsContentView);

		viewActionsList = (ListView) findViewById(R.id.actions_simple);

		final String[] values = new String[] { 
				//	"Mi Perfil", 	//0 
				"Mi Dieta", 	//0
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
			intent = new Intent(MiPerfilActivity.this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(MiPerfilActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(MiPerfilActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(MiPerfilActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(MiPerfilActivity.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;
		case 5:
			intent = new Intent(MiPerfilActivity.this, ShareActivity.class);
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
			intent = new Intent(MiPerfilActivity.this, TutorialActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);			
			break;
		case 9:
			intent = new Intent(MiPerfilActivity.this, DisclaimerActivity.class);
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
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		//Toast.makeText(con, "Talla: "+new API(con).getTallaIdeal(), Toast.LENGTH_SHORT).show();
		if (type == 0)
			pesoIdeal.setText(new API(con).getPesoIdeal());
		else
			tallaIdeal.setText(new API(con).getTallaIdeal());
	}

	public void onUserSelectValue(int value) {
		// TODO Auto-generated method stub
		this.type = value;
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
			styleDark = res.getColor(R.color.MASCULINO_DARKER);
		}
		if(style.equals("femenino"))
		{
			styleMain = res.getColor(R.color.FEMENINO_MAIN);
			styleDetail = res.getColor(R.color.FEMENINO_DETAIL);
			styleDark = res.getColor(R.color.FEMENINO_DARKER);
		}
		if(style.equals("neutral"))
		{
			styleMain = res.getColor(R.color.NEUTRAL_MAIN);
			styleDetail = res.getColor(R.color.NEUTRAL_DETAIL);
			styleDark = res.getColor(R.color.NEUTRAL_DARKER);
		}

	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{
		//sets local style variables
		getStyle();
		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
		scroll.setBackgroundColor(styleMain);
		bar.setBackgroundColor(styleDetail);

		EditText mot = (EditText) findViewById(R.id.miPerfilMotivaciones);
		mot.setHintTextColor(Color.WHITE);

		scroll.fullScroll(ScrollView.FOCUS_UP);

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		//		Toast.makeText(parent.getContext(), 
		//		        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
		//		        Toast.LENGTH_SHORT).show();
		ContentValues cv = new ContentValues();
		new DAO_Usuario(con).updateNivel(parent.getItemAtPosition(position).toString());

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop()
	{
		super.onStop();
		if(viewActionsContentView != null)
			viewActionsContentView.showContent();

		new API(con).setMotivacion(motivacion.getText().toString());

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

	public void runImageAsyncTask()
	{
		//profile_pic.setBackgroundResource(R.drawable.com_facebook_profile_picture_blank_portrait);
		ImageAsyncTask programming = new ImageAsyncTask("profile", ivProfilePic);
		programming.setId(new API(con).getFacebookID());
		programming.execute();

		tvProfileName.setText(new API(con).getFacebookName());
	}

	public static void setFacebokProfilePic(Bitmap bitMap)
	{		
		if(bitMap != null)
			ivProfilePic.setImageBitmap(API.getRoundedShape(bitMap));
	}

	//listens for Date Picker Fragment dismissed
	@Override
	public void onDismissListener() {
		// TODO Auto-generated method stub
		
		//UPDATE DATE UI
		tvGoalDate.setText(new API(con).getGoalDate());
		
	}
}

package com.cceo.miyoideal;

import java.util.Arrays;

import org.json.JSONObject;

import com.cceo.DAO.DAO_DietaCompletada;
import com.cceo.DAO.DAO_Usuario;
import com.cceo.DB.SQLiteControl;
import com.cceo.DTO.DTO_Usuario;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.MenuFragment;
import com.cceo.miyoideal.extra.MyArrayAdapter;
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
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MiPerfilActivity extends FragmentActivity implements DialogInterface.OnDismissListener, OnItemSelectedListener{

	//layout components
	private Spinner spinnerNivel;
	private RelativeLayout pesoDeseadoRL;
	private TextView pesoIdeal;
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

	//facebook login
	public CallbackManager callbackManager;
	private LoginButton loginButton;
	private SQLiteControl db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline2);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.actionbar_icon_white);
		con = this;

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
							PerfilNumberPicker dialog = new PerfilNumberPicker(con);
							dialog.show(manager, "Picker");
						} else {
							PerfilNumberPicker dialog = new PerfilNumberPicker(con);
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

		//loginButton.setReadPermissions("user_friends","public_profile");


	}

	private void changeFont() {
		// TODO Auto-generated method stub
		Font font = new Font();
		TextView tv_1 = (TextView) findViewById(R.id.miPerfil_1);
		TextView tv_2 = (TextView) findViewById(R.id.miPerfil_2);
		TextView tv_3 = (TextView) findViewById(R.id.miPerfil_3);
		TextView tv_4 = (TextView) findViewById(R.id.miPerfil_4);
		TextView tv_5 = (TextView) findViewById(R.id.miPerfil_5);
		TextView tv_6 = (TextView) findViewById(R.id.miPerfil_6);

		font.changeFontRaleway(con, tv_1);
		font.changeFontRaleway(con, tv_2);
		font.changeFontRaleway(con, tv_3);
		font.changeFontRaleway(con, tv_4);
		font.changeFontRaleway(con, tv_5);
		font.changeFontRaleway(con, tv_6);
		font.changeFontRaleway(con, motivacion);
		font.changeFontRaleway(con, pesoIdeal);
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		
	}

	private void setPesoIdealValue() {
		// TODO Auto-generated method stub
		if(new API(con).getPesoIdeal().equals(null)
				|| new API(con).getPesoIdeal().equals(""))
		{
			pesoIdeal.setText("0");
		}
		else 
		{
			pesoIdeal.setText(new API(con).getPesoIdeal());
		}

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
				//"Mi Perfil", 	//0 
				"Mi Dieta", 	//1
				"Mi Ejercicio", //2
				"Calendario", 	//3
				"Preguntanos",	//4
				"Comparte", 	//5
				"Tips y Sujerencias",	//6 
				"Seleccionar Dieta", 	//7
				"Comparativa", 	//8
				"Disclaimer",	//9
		"Tutorial"};	//10

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
		case 4:
			intent = new Intent(MiPerfilActivity.this, ShareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 6:
			intent = new Intent(MiPerfilActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 7:
			intent = new Intent(MiPerfilActivity.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 8:
			intent = new Intent(MiPerfilActivity.this, DisclaimerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 9:
			intent = new Intent(MiPerfilActivity.this, TutorialActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;


		default:
			return;
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		pesoIdeal.setText(new API(con).getPesoIdeal());
	}

	public void onUserSelectValue(String value) {
		// TODO Auto-generated method stub
		Log.d("chino", "number 2" + value);
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

}

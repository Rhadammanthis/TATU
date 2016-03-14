package com.cceo.miyoideal;

import java.util.ArrayList;

import shared.ui.actionscontentview.ActionsContentView;

import com.cceo.DAO.DAO_Shopping;
import com.cceo.DTO.ShoppingItem;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.MenuFragment;
import com.cceo.miyoideal.extra.MyArrayAdapter;
import com.cceo.miyoideal.inappbilling.util.ShoppingAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShoppingActivity extends Activity {

	int styleMain;
	int styleDetail;
	int styleDarkest;
	int styleDark;
	int styleBright;
	int styleBrightest;

	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	
	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline_shopping);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.menu_white);
		this.setTitle("		Lista de Compras");

		TextView text = (TextView) findViewById(R.id.shoppingText);
		
		setUpMenu();

		if (new API(this).IsDietaSet()) 
		{
			text.setVisibility(View.GONE);

			mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
			RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
					this);
			mRecyclerView.setLayoutManager(layoutManager);
			mRecyclerView.setHasFixedSize(true);

			ArrayList<ShoppingItem> dataSet = new DAO_Shopping(this).getAllShoppingListItems(new API(this).getID_Dieta());

			mAdapter = new ShoppingAdapter(dataSet, this);
			mRecyclerView.setAdapter(mAdapter);
		}

		updateStyle();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.base_action_bar, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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

	private void getStyle() {
		// get selected style from database
		String style = new API(ShoppingActivity.this).getStyle();
		// get system resources
		Resources res = getResources();

		if (style.equals("masculino")) {
			styleMain = res.getColor(R.color.MASCULINO_MAIN);
			styleDetail = res.getColor(R.color.MASCULINO_DETAIL);
			styleDarkest = res.getColor(R.color.MASCULINO_DARKEST);
			styleDark = res.getColor(R.color.MASCULINO_DARKER);
			styleBright = res.getColor(R.color.MASCULINO_BRIGHTER);
			styleBrightest = res.getColor(R.color.MASCULINO_BRIGHTEST);
			// checkB.setBackgroundResource(R.drawable.selector_checkbox);
		}
		if (style.equals("femenino")) {
			styleMain = res.getColor(R.color.FEMENINO_MAIN);
			styleDetail = res.getColor(R.color.FEMENINO_DETAIL);
			styleDarkest = res.getColor(R.color.FEMENINO_DARKEST);
			styleDark = res.getColor(R.color.FEMENINO_DARKER);
			styleBright = res.getColor(R.color.FEMENINO_BRIGHTER);
			styleBrightest = res.getColor(R.color.FEMENINO_BRIGHTEST);
			// checkB.setBackgroundResource(R.drawable.selector_checkbox_alt);
		}
		if (style.equals("neutral")) {
			styleMain = res.getColor(R.color.NEUTRAL_MAIN);
			styleDetail = res.getColor(R.color.NEUTRAL_DETAIL);
			styleDarkest = res.getColor(R.color.NEUTRAL_DARKEST);
			styleDark = res.getColor(R.color.NEUTRAL_DARKER);
			styleBright = res.getColor(R.color.NEUTRAL_BRIGHTER);
			styleBrightest = res.getColor(R.color.NEUTRAL_BRIGHTEST);
			// checkB.setBackgroundResource(R.drawable.selector_checkbox);
		}

	}

	// set's specific color to certain components in the layout so it achieves
	// the desired style.
	private void updateStyle() {
		getStyle();

		LinearLayout header = (LinearLayout) findViewById(R.id.shoppingBodyFrame);

		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		header.setBackgroundColor(styleMain);
	}
	
	private void setUpMenu()
	{
		viewActionsContentView = (ActionsContentView) findViewById(R.id.shopping_actionsContentView);

		viewActionsList = (ListView) findViewById(R.id.actions_simple);

		final String[] values = new String[] { 
				"Mi Perfil", 	//0 
				"Mi Dieta", 	//1
				"Mi Ejercicio", //2
				"Calendario", 	//3
				"Seleccionar Dieta",	//4
				"Antes y Despues", 	//5
				"Comparte",	//6
				"Tip del Día", 	//7
				"Pregúntanos", 	//8
				"Tutorial",	//9
		"Disclaimer"};	//10

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
		
		if(!new API(this).getFacebookID().equals(""))
		{
			MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.simple_menu_fragment);
			fragment.setFacebookId(new API(this).getFacebookID());
			fragment.runImageAsyncTask();
			
			TextView facebook_name = (TextView) findViewById(R.id.tvLOL_simple);
			facebook_name.setText(new API(this).getFacebookName());
			
			Font f = new Font();					
			f.changeFontRaleway(this, facebook_name);
		}
		else
		{
			MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.simple_menu_fragment);
			fragment.setContext(this);
			fragment.setDefaultProfilePic();
		}
	}

	private void showActivity(int position) 
	{
		final Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;
		case 6:
			intent = new Intent(this, ShareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 7:
			//tip del dia (blog)
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
			browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(browserIntent);
			break;
		case 8:
			//preguntanos
			intent = getOpenFacebookIntent(this);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 9:
			intent = new Intent(this, TutorialActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);			
			break;
		case 10:
			intent = new Intent(this, DisclaimerActivity.class);
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
}

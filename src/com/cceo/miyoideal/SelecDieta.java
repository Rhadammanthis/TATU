package com.cceo.miyoideal;

import java.util.ArrayList;
import java.util.List;

import shared.ui.actionscontentview.ActionsContentView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.cceo.DAO.DAO_Componente;
import com.cceo.DAO.DAO_Dieta;
import com.cceo.DB.SQLiteControl;
import com.cceo.DTO.DTO_Componente;
import com.cceo.DTO.DTO_Dieta;
import com.cceo.miyoideal.inappbilling.util.*;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.DietaChild;
import com.cceo.miyoideal.extra.DietaChildFactory;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.MenuFragment;
import com.cceo.miyoideal.extra.MyArrayAdapter;
import com.cceo.miyoideal.extra.SelectDialogue;

public class SelecDieta extends Activity {

	private LinearLayout mainLayout;
	private Context con;

	DietaChildFactory dietaChildFactory;

	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;
	


	int styleMain;
	int styleDetail;
	int styleDarkest;
	int styleDark;
	int styleBright;
	int styleBrightest;

	private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener;
	private IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener;
	private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener;
	private IabHelper.QueryInventoryFinishedListener mQueryFinishedListener;
	private List<String> additionalSkuList;
	private static final String TAG = "com.cceo.inappbilling";
	IabHelper mHelper;
	static final String ITEM_SKU = "productSKU_1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline_selec_dieta);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.menu_white);
		this.setTitle("		Seleccionar Dieta");
		
		con = this;
		
		setUpMenu();

		mainLayout = (LinearLayout) findViewById(R.id.selecdieta_linearlayout);
		dietaChildFactory = new DietaChildFactory();
		mainLayout.setOnTouchListener(new DrawerCloseListener());

		updateStyle();

		DAO_Dieta dao_dieta = new DAO_Dieta(this);
		List<DTO_Dieta> list_Dieta = dao_dieta.getAllDieta();

		//loads available dietas to list
		initDietaLayout(list_Dieta);
		
		//fixListItems();

		//setups listener to perform in-app purchases
		//setUpInAppPurchase();

	}

	private void fixListItems() {
		// TODO Auto-generated method stub
	//	Log.d("amo", "Hijos Todos: " + String.valueOf(mainLayout.getChildCount()));
		RelativeLayout child = (RelativeLayout)mainLayout.getChildAt(0);
		child.setBackgroundColor(styleMain);
		LinearLayout linear = (LinearLayout) child.getChildAt(0);
		linear.setBackgroundColor(styleMain);
		RelativeLayout list = (RelativeLayout)linear.getChildAt(1);
		list.setBackgroundColor(styleMain);
		LinearLayout otherlist = (LinearLayout)list.getChildAt(0);
		otherlist.setBackgroundColor(styleMain);
		LinearLayout ff = (LinearLayout)(otherlist.getChildAt(0));
		Log.d("amo", "Hijos Todos: " + String.valueOf(ff.getChildCount()));
		Log.d("amo", "Hijos Todos: " + String.valueOf(ff.getClass()));
		
		for (int i = 0; i < otherlist.getChildCount(); i++) 
		{
//			LinearLayout ff = (LinearLayout)(otherlist.getChildAt(i));
//			ff.setBackgroundColor(styleMain);
//			Log.d("amo", "Hijos : " + String.valueOf(ff.getBackground()));
//			//Log.d("amo", "Hijos : " + String.valueOf(otherlist.getChildAt(i)));
		}
		
	}

	private void setUpMenu()
	{
		viewActionsContentView = (ActionsContentView) findViewById(R.id.selec_dieta_actionsContentView);

//		viewActionsContentView.on

		viewActionsList = (ListView) findViewById(R.id.actions_simple);

		final String[] values = new String[] { 
				"Mi Perfil", 	//0 
				"Mi Dieta", 	//1
				"Mi Ejercicio", //2
				"Calendario", 	//3
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
		else
		{
			MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.simple_menu_fragment);
			fragment.setContext(con);
			fragment.setDefaultProfilePic();
		}
	}

	private void showActivity(int position) 
	{
		final Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(SelecDieta.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(SelecDieta.this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(SelecDieta.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(SelecDieta.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(SelecDieta.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;
		case 5:
			intent = new Intent(SelecDieta.this, ShareActivity.class);
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
			intent = new Intent(SelecDieta.this, TutorialActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);			
			break;
		case 9:
			intent = new Intent(SelecDieta.this, DisclaimerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		default:
			return;
		}
	}

	private void setUpInAppPurchase() {
		// TODO Auto-generated method stub

		//STRING has not yet been accordingly generated
		String base64EncodedPublicKey = 
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo1mE9UQWj175IJsHtowkSuIGP6PR63fkV4oGb3khUZ0nTgQhyS9puZXMXUnH2U037V/wKqTzSu32z17El72oEN3JeXaax/3q5FMg2w6v1vbs4YhZPlcIskV5RNsdYF4AJY4IcNBxMKTAExYw2fqjCcMkktvdnmtrx3Gmv5tVDQ8R3PpfZwkrhGO/oQ6Txxi0lY/ytzzDzVEKmyGBrHwD3lhLLhfeVM9eFfXbx9ZDaCBaTo1M72zAp4KVahbFy+pNvX/+wqwtRK2PnPQL+H2zRw7gGck6vnwagx/Klf2ZxQqWrnIgEBd13YAKVQcNSK/P48FxOUGtm8Q30QRWY9AqGwIDAQAB";

		mHelper = new IabHelper(this, base64EncodedPublicKey);

		mHelper.startSetup(new 
				IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) 
			{
				if (!result.isSuccess()) {
					Log.d(TAG, "In-app Billing setup failed: " + 
							result);
				} else {             
					Log.d(TAG, "In-app Billing is set up OK");
				}
			}
		});

		mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
			public void onQueryInventoryFinished(IabResult result, Inventory inventory)   
			{
				if (result.isFailure()) {
					// handle error
					return;
				}

				String applePrice =
						inventory.getSkuDetails(ITEM_SKU).getPrice();

				TextView text = (TextView) findViewById(R.id.textView1);
				text.setText(applePrice);
				// update the UI 
			}
		};

		mPurchaseFinishedListener 
		= new IabHelper.OnIabPurchaseFinishedListener() {
			public void onIabPurchaseFinished(IabResult result, 
					Purchase purchase) 
			{
				if (result.isFailure()) {
					//Error
					return;
				}      
				else if (purchase.getSku().equals(ITEM_SKU)) {
					//WHEN WE PROCED TO BUY SOMETHING, THIS HAPPENS

					//consumeItem();
				}

			}
		};

		mReceivedInventoryListener 
		= new IabHelper.QueryInventoryFinishedListener() {
			public void onQueryInventoryFinished(IabResult result,
					Inventory inventory) {


				if (result.isFailure()) {
					// Handle failure
				} else {
					mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU), 
							mConsumeFinishedListener);
				}
			}
		};

		mConsumeFinishedListener =
				new IabHelper.OnConsumeFinishedListener() {
			public void onConsumeFinished(Purchase purchase, 
					IabResult result) {

				if (result.isSuccess()) {		    	 
					//WHEN THE ITEM HAS BEEN SUCCESFULY PURCHASED
				} else {
					// handle error
				}
			}
		};
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

	public RelativeLayout genDietaView(final DTO_Dieta dieta)
	{
		//Sets up a new Relative Layout with the desired text
		final RelativeLayout layout = new RelativeLayout(this);
		layout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		//Formats text
		TextView TV_titulo = new TextView(this);
		TV_titulo.setText(dieta.getNombre());
		TV_titulo.setTextSize(20);
		TV_titulo.setPadding(24, 16, 0, 16);
		TV_titulo.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

		layout.addView(TV_titulo);

		//Used to detect when view is clicked
		layout.setOnTouchListener( new View.OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch(event.getAction())
				{
				case MotionEvent.ACTION_DOWN:
					layout.setBackgroundColor(Color.LTGRAY);
					break;
				case MotionEvent.ACTION_UP:
					//set color back to default
					layout.setBackgroundColor(Color.WHITE); 

					SelectDialogue temp1;
					//Checks whether dieta is available
					if(!new API(con).IsDietaSet())
					{
						//If it is, prompts a dialogue to ask whether the user would like to select this one
						temp1 = new SelectDialogue(con, dieta.getId_dieta(), true);
						temp1.show(getFragmentManager(), "2");
					}
					else
					{
						temp1 = new SelectDialogue(con, dieta.getId_dieta(), false);
						temp1.show(getFragmentManager(), "2");
					}
					break;
				}
				return true;        
			}
		});

		return layout;
	}

	public void consumeItem() {
		mHelper.queryInventoryAsync(mReceivedInventoryListener);
		//		mHelper.queryInventoryAsync(true, additionalSkuList,
		//				mQueryFinishedListener);
	}

	public boolean IsControlAvailable()
	{
		SQLiteControl db = new SQLiteControl(this);
		db.getReadableDatabase();
		String query = "Select * FROM " + "control" + " WHERE " + "id_control" + " =  \"" + "1" + "\"";
		Cursor cursor = db.getReadableDatabase().rawQuery(query, null);
		try
		{
			if(cursor.moveToFirst())
			{
				//When the id_dieta value in the Control db is set to 0, it means that no dieta is currently set.
				if(cursor.getString(1).equals("0"))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		catch(Exception ex)
		{
			return false;
		}
		//in case everything fails
		return false;
	}

	public void initDietaLayout(List<DTO_Dieta> lista)
	{
		Log.d("selec", "size:" + String.valueOf(lista.size()));
		//list to hold all same dieta type entries in 'lista'
		final List<String> raw_content = new ArrayList<String>();

		//list to hold all same dieta type's tag entries in 'lista'
		final List<String> raw_tag = new ArrayList<String>();

		final List<String> id = new ArrayList<String>();

		for (int i = 0; i < lista.size(); i++) 
		{
			raw_content.add(lista.get(i).getNombre());
			raw_tag.add(lista.get(i).getEtiqueta());
		}

		final List<String> content = new ArrayList<String>();

		//list to hold all same dieta type's tag entries in 'lista'
		final List<String> tag = new ArrayList<String>();
		final List<String> desc = new ArrayList<String>();

		for(int i=1; i < lista.size(); i++)
		{
			//checks if the dieta type of a given insert in 'lista' is the same as the last one. If so, it is then added to 'content'
			if(lista.get(i).getTipo().equals(lista.get(i-1).getTipo()))
			{
				content.add(lista.get(i-1).getNombre());
				tag.add(lista.get(i-1).getEtiqueta());
				desc.add(lista.get(i-1).getDescripcion());
				id.add(lista.get(i-1).getId_dieta());
			}
			else
			{
				//when a different dieta type occurs, the parameters to create the DietaChild are set up
				String title = lista.get(i-1).getTipo();

				//the last dieta type in 'lista' is added to
				content.add(lista.get(i-1).getNombre());

				//so is it's tag
				tag.add(lista.get(i-1).getEtiqueta());
				desc.add(lista.get(i-1).getDescripcion());
				id.add(lista.get(i-1).getId_dieta());

				//A new DietaChild is created from the dietaChildFactory
				final RelativeLayout newChild = dietaChildFactory.generateChildSelectDieta(con, title, id, content, 
						tag, desc, styleDark,styleMain, getFragmentManager());

				//the dietachild is added to the main layout
				mainLayout.addView(newChild);

				//to generate row setoff shadow
				mainLayout.addView(getDarkest());
				mainLayout.addView(getDarker());
				mainLayout.addView(getBrighter());
				mainLayout.addView(getBrightest());

				//we keep track of children views in a list
				//				dietaChildList.add(newChild);

				//the content and tag list is cleared 
				content.clear();
				tag.clear();
				id.clear();
				desc.clear();
			}
		}

		//same behavior as before, but this handles when a dieta type only carries one insert in the content list and/or is the last dieta instance
		String title = lista.get(lista.size()-1).getTipo();
		content.add(lista.get(lista.size()-1).getNombre());
		tag.add(lista.get(lista.size()-1).getEtiqueta());
		desc.add(lista.get(lista.size()-1).getDescripcion());
		id.add(lista.get(lista.size()-1).getId_dieta());

		final RelativeLayout newChild = dietaChildFactory.generateChildSelectDieta(con, title, id, content, 
				tag, desc, styleDark,styleMain, getFragmentManager());

		mainLayout.addView(newChild);

		//to generate row setoff shadow
		mainLayout.addView(getDarkest());
		mainLayout.addView(getDarker());
		mainLayout.addView(getBrighter());
		mainLayout.addView(getBrightest());

		//		dietaChildList.add(newChild);

		content.clear();
		tag.clear();
		id.clear();
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
	
	public static Intent getOpenFacebookIntent(Context context) {

		try {
			context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/1398816470330009"));
		} catch (Exception e) {
			return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/miyoideal"));
		}
	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{
		//sets local style variables
		getStyle();

		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
		mainLayout.setBackgroundColor(styleMain);

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
				fixListItems();
				return true;
			}
			
			
			return false;
		}

	}

}

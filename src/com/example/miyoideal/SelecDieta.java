package com.example.miyoideal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.example.DAO.DAO_Componente;
import com.example.DAO.DAO_Dieta;
import com.example.DB.SQLiteControl;
import com.example.DTO.DTO_Componente;
import com.example.DTO.DTO_Dieta;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.DietaChild;
import com.example.miyoideal.extra.DietaChildFactory;
import com.example.miyoideal.extra.SelectDialogue;

public class SelecDieta extends Activity {

	private LinearLayout mainLayout;
	private Context con;

	DietaChildFactory dietaChildFactory;

	int styleMain;
	int styleDetail;
	int styleDarkest;
	int styleDark;
	int styleBright;
	int styleBrightest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selec_dieta);

		con = this;
		mainLayout = (LinearLayout) findViewById(R.id.selecdieta_linearlayout);
		dietaChildFactory = new DietaChildFactory();
		
		updateStyle();

		DAO_Dieta dao_dieta = new DAO_Dieta(this);
		List<DTO_Dieta> list_Dieta = dao_dieta.getAllDieta();
		
		initDietaLayout(list_Dieta);

//		for(DTO_Dieta temp : list_Dieta)
//		{
//			mainLayout.addView(this.genDietaView(temp));
//		}
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
		
		for(int i=1; i < lista.size(); i++)
		{
			//checks if the dieta type of a given insert in 'lista' is the same as the last one. If so, it is then added to 'content'
			if(lista.get(i).getTipo().equals(lista.get(i-1).getTipo()))
			{
				content.add(lista.get(i-1).getNombre());
				tag.add(lista.get(i-1).getEtiqueta());
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

				id.add(lista.get(i-1).getId_dieta());
				
				//A new DietaChild is created from the dietaChildFactory
				final RelativeLayout newChild = dietaChildFactory.generateChildSelectDieta(con, title, id, content, 
						tag, styleDark,styleMain, getFragmentManager());

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
			}
		}

		//same behavior as before, but this handles when a dieta type only carries one insert in the content list and/or is the last dieta instance
		String title = lista.get(lista.size()-1).getTipo();
		content.add(lista.get(lista.size()-1).getNombre());
		tag.add(lista.get(lista.size()-1).getEtiqueta());
		id.add(lista.get(lista.size()-1).getId_dieta());

		final RelativeLayout newChild = dietaChildFactory.generateChildSelectDieta(con, title, id, content, 
				tag, styleDark,styleMain, getFragmentManager());

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

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{
		//header setoff bar
//		View bar1 = (View) findViewById(R.id.dietaBar1);
//		View bar2 = (View) findViewById(R.id.dietaBar2);
//		View bar3 = (View) findViewById(R.id.dietaBar3);
//		View bar4 = (View) findViewById(R.id.dietaBar4);

		//save bar size to a global variable
		//size = bar1.getLayoutParams().width;

		//sets local style variables
		getStyle();

		Log.d("color", "en perfil NUMERO " + String.valueOf(styleMain));
		mainLayout.setBackgroundColor(styleMain);
		mainLayout.setBackgroundColor(styleMain);
		//		dietaBody.setBackgroundColor(styleMain);
		//		dietaHeader.setBackgroundColor(styleBright);
		//		
		//		bar1.setBackgroundColor(styleDarkest);
		//		bar2.setBackgroundColor(styleDark);
		//		bar3.setBackgroundColor(styleBright);
		//		bar4.setBackgroundColor(styleBrightest);

	}
}

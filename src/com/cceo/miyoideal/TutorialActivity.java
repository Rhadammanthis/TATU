package com.cceo.miyoideal;

import shared.ui.actionscontentview.ActionsContentView;

import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.MenuFragment;
import com.cceo.miyoideal.extra.MyArrayAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TutorialActivity extends ActionBarActivity {
	
	private ActionsContentView viewActionsContentView;
	private ListView viewActionsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline_tutorial);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.actionbar_icon_white);
		
		setUpMenu();

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		ImagePagerAdapter adapter = new ImagePagerAdapter();
		viewPager.setAdapter(adapter);
		
		CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
		indicator.bringToFront();
        indicator.setViewPager(viewPager);
        indicator.setSnap(true);
        //indicator.setBackgroundColor(Color.BLACK);
        indicator.setFillColor(Color.parseColor("#808080"));
        indicator.setPageColor(Color.WHITE);
        indicator.setStrokeColor(Color.parseColor("#323232"));
        indicator.setRadius(5f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutorial, menu);
		return true;
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
	
	private void setUpMenu()
	{
		viewActionsContentView = (ActionsContentView) findViewById(R.id.tutorial_actionsContentView);

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
				"Preguntanos", 	//8
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
		
		if(!new API(TutorialActivity.this).getFacebookID().equals(""))
		{
			MenuFragment fragment = (MenuFragment) getFragmentManager().findFragmentById(R.id.simple_menu_fragment);
			fragment.setFacebookId(new API(TutorialActivity.this).getFacebookID());
			fragment.runImageAsyncTask();
			
			TextView facebook_name = (TextView) findViewById(R.id.tvLOL_simple);
			facebook_name.setText(new API(TutorialActivity.this).getFacebookName());
			
			Font f = new Font();					
			f.changeFontRaleway(TutorialActivity.this, facebook_name);
		}
	}

	private void showActivity(int position) 
	{
		final Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(TutorialActivity.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(TutorialActivity.this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(TutorialActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(TutorialActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(TutorialActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(TutorialActivity.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;
		case 6:
			intent = new Intent(TutorialActivity.this, ShareActivity.class);
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
			intent = getOpenFacebookIntent(TutorialActivity.this);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 9:
			intent = new Intent(TutorialActivity.this, DisclaimerActivity.class);
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

	private class ImagePagerAdapter extends PagerAdapter {
		private int[] mImages = new int[] {
				R.drawable.image2,
				R.drawable.image3,
				R.drawable.image4,
		};

		@Override
		public int getCount() {
			return mImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = TutorialActivity.this;
			ImageView imageView = new ImageView(context);		  
			//imageView.setPadding(15, 15, 15, 15);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setImageResource(mImages[position]);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}
}

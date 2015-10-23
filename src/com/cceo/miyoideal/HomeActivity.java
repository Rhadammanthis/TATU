package com.cceo.miyoideal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.w3c.dom.Text;

import shared.ui.actionscontentview.ActionsContentView;
import android.R.style;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.view.MotionEventCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cceo.DAO.DAO_DietaCompletada;
import com.cceo.DAO.DAO_TutorialControl;
import com.cceo.DAO.DAO_Usuario;
import com.cceo.DB.SQLiteControl;
import com.cceo.DB.SQLiteDietaDB;
import com.cceo.DB.SQLiteTutorialControl;
import com.cceo.DB.SQLiteUserDB;
import com.cceo.DTO.DTO_DietaCompletada;
import com.cceo.DTO.DTO_Usuario;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.DialyNotificationReceiver;
import com.cceo.miyoideal.extra.DietaCompletedDialog;
import com.cceo.miyoideal.extra.Font;
import com.cceo.miyoideal.extra.FragmentNotificationDetail;
import com.cceo.miyoideal.extra.Fragment_ImagePager;
import com.cceo.miyoideal.extra.ImageAsyncTask;
import com.cceo.miyoideal.extra.MyArrayAdapter;
import com.cceo.miyoideal.extra.MyLinearLayout;
import com.cceo.miyoideal.extra.OnDismissListener;
import com.cceo.miyoideal.extra.RecomendationDialog;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.DragEdge;
import com.daimajia.swipe.SwipeLayout.OnRevealListener;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.DataSet; 
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;

public class HomeActivity extends Activity implements OnTouchListener, OnClickListener, OnDismissListener{

	//global variables;
	private int width;
	private int height;

	//layout components
	private LinearLayout linearLayout1;
	private ImageButton button_MiPerfil;
	private ActionsContentView viewActionsContentView;
	private SwipeLayout swipeLayout;
	private ListView viewActionsList;
	private Context cont;
	private CheckBox checkBoxNotif;
	private RelativeLayout layoutNotif;
	private LinearLayout backLayout;
	private RelativeLayout frontLayout;
	private RelativeLayout bodyLayout;
	private static ImageView menu_profile_pic;
	private ImageView home_down;
	private TextView tvDaysLeft;
	private Font font;

	//Control variables
	private boolean shoudlOpenDrawer = true;
	private boolean isDrawerOpen = false;


	//REQUEST IMAGE CODE
	static final int REQUEST_IMAGE_CAPTURE = 1;
	protected static final String DEBUG_TAG = "TOUCH";
	ImageView imageThumbnail;
	private String mCurrentPhotoPath;

	//graph related global variables
	private BarChart mChart;
	private List<DTO_DietaCompletada> lista;
	private BarDataSet set1;
	private int textColor, barColor, barShadow, backgroundColor;

	private Uri mImageCaptureUri; // This needs to be initialized.



	//Style variables
	private int styleMain;
	private int styleDetail;


	static final int CAMERA_PIC_REQUEST = 1337; 
	private String filePath;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private File mediaFile;
	private static final int CAMERA = 0;
	private static final int GALLERY = 1;
	private final String FACEBOOK_URL = "https://facebook.com";


	@Override

	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baseline4);
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setIcon(R.drawable.actionbar_icon_white);
		getActionBar().setTitle("");


		cont = this;
		
        Bundle intent_extras = getIntent().getExtras();
        if (intent_extras != null && intent_extras.containsKey("message"))
        {
        	String message = intent_extras.getString("message");
        	String title = intent_extras.getString("title");
        	
        	FragmentNotificationDetail recomedn_dialog = new FragmentNotificationDetail(cont, title, message);
    		recomedn_dialog.show(getFragmentManager(), "Dieta Completed");
        }
		
//		String newStringN = "rien";
//		if (savedInstanceState == null) {
//		    Bundle extras = getIntent().getExtras();
//		    if(extras == null) {
//		        newStringN= "rien";
//		    } else {
//		        newStringN= extras.getString("com.cceo.miyoideal.extra.DialyNotificationReceiver");
//		    }
//		} else {
//		    newStringN= (String) savedInstanceState.getSerializable("com.cceo.miyoideal.extra.DialyNotificationReceiver");
//		}
//		


		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.cceo.miyoideal", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String sign = Base64
						.encodeToString(md.digest(), Base64.DEFAULT);

				Log.d("stumme","Key " + sign);

			}
		} catch (NameNotFoundException e) {
		} catch (NoSuchAlgorithmException e) {
		}

		font = new Font();

		//bind layout views to local objects
		backLayout = (LinearLayout) findViewById(R.id.backLayout);
		frontLayout = (RelativeLayout) findViewById(R.id.frontLayout);
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		button_MiPerfil = (ImageButton) findViewById(R.id.abajoButtonlol);
		viewActionsContentView = (ActionsContentView) findViewById(R.id.home_actionsContentView);
		viewActionsList = (ListView) findViewById(R.id.actions);
		swipeLayout =  (SwipeLayout)findViewById(R.id.swipeLayout);
		bodyLayout = (RelativeLayout) findViewById(R.id.homeBodyLayout);
		checkBoxNotif = (CheckBox) findViewById(R.id.homeNotifCheckB);
		layoutNotif = (RelativeLayout) findViewById(R.id.notifRelativeLayout);
		menu_profile_pic = (ImageView) findViewById(R.id.menu_profile_pic);
		home_down = (ImageView) findViewById(R.id.home_down);
		tvDaysLeft = (TextView) findViewById(R.id.homeDaysLeft);
		
		tvDaysLeft.setText(getDaysLeft() + " días");
		font.changeFontRaleway(cont, tvDaysLeft);

		//Tutorial reminder. Runs just the first time the apps runs
		SharedPreferences runCheck = PreferenceManager.getDefaultSharedPreferences(cont);
		Boolean hasRun = runCheck.getBoolean("hasRun", false); //see if it's run before, default no

		//For the csv files
		ImageButton buttonCamera = (ImageButton) findViewById(R.id.buttonTopLeft);
		buttonCamera.setOnTouchListener(this);
		new DAO_DietaCompletada(cont).InsertCSVFile(null, null, null);

		//Clear the notification
		cancelNotification(this, 001);		

		//PAROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOLE

		//		Log.d("moj", "Should send notif " + String.valueOf(new API(cont).shouldSendNotif()));
		//		if(new API(cont).shouldSendNotif() && isAfterHours())
		//		{
		//			//Set the notification receiver 
		//			DialyNotificationReceiver dialyNotification = new DialyNotificationReceiver();
		//			dialyNotification.setAlarm(this);
		//		}

		//Checks if Dieta Completed Dialog should be displayed
		if(this.getIntent().hasExtra("response") || new API(cont).HasDietaBeenCompleted())
		{
			//Clears Dieta Completada notification
			cancelNotification(this, 002);

			//Reset dieta completed flag
			//Set flag in the Control DB to validate that a dieta program has been finished
			ContentValues cv = new ContentValues();
			cv.put("dietaTerminada", "0"); 
			SQLiteControl db = new SQLiteControl(cont);
			db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
			db.close();

			//Call "Update Info" dialog
			DietaCompletedDialog dialog = new DietaCompletedDialog(cont);
			dialog.show(getFragmentManager(), "Dieta Completed");

		}


		//Adapt the layout depends screen
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;		

		//populates side menu list
		setUpMenu();	

		//updates view's style
		updateStyle();

		//esto se puede quitar
		SQLiteUserDB dbUser = new SQLiteUserDB(this);
		dbUser.getReadableDatabase();
		String query = "Select * FROM " + "usuario" + " WHERE " + "id_usuario" + " =  \"" + "1" + "\"";
		Cursor cursor = dbUser.getReadableDatabase().rawQuery(query, null);

		//Gathers data to later pass onto Graph. Queries the DietaCompletada database for last 4 entries
		lista = new DAO_DietaCompletada(cont).getLastFiveDietaCompleta();

		button_MiPerfil.setOnTouchListener(new DrawerCloseListener());

		//if button is pressed, the dieta ended dialog is shown. For debugging purposes
		button_MiPerfil.setOnClickListener(this);

		//TouchListner to close the drawer when touched.
		bodyLayout.setOnTouchListener(new DrawerCloseListener());

		//Listener to open drawer and close side menu
		setDrawerListener();

		//Set peso difference
		TextView pesodif = (TextView) findViewById(R.id.homePesoDif);
		pesodif.setText(getPesoDif());
		font.changeFontRaleway(cont, pesodif);

		//Set Motivacion
		TextView motivacion = (TextView) findViewById(R.id.homeMotivacionBody);
		motivacion.setText(new API(cont).getMotivacion());
		font.changeFontRaleway(cont, motivacion);
		
		//set show mode.
		swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

		//set drag edge.
		swipeLayout.setDragEdge(SwipeLayout.DragEdge.Top);

		//prompt button tutorial
		String newString = "";
		if (savedInstanceState == null) {
			Bundle extras = getIntent().getExtras();
			if(extras == null) {
				newString= "";
			} else {
				newString= extras.getString("intent_origin");
			}
		} else {
			//newString= (String) savedInstanceState.getSerializable("intent_origin");
		}
		
		if(newString == null)
			newString = "";
		
		String compare = new DAO_TutorialControl(cont).buttonTutorialShown();

		if(compare.equals("0") && newString.equals("select"))
		{
			new CountDownTimer(1000, 1000) {

				public void onTick(long millisUntilFinished) {
				}

				public void onFinish() {
					Fragment_ImagePager recomedn_dialog = new Fragment_ImagePager(cont);
					recomedn_dialog.show(getFragmentManager(), "Dieta Completed");

					ContentValues cv = new ContentValues();
					cv.put("button_tutorial", "1");

					SQLiteTutorialControl db = new SQLiteTutorialControl(cont);
					db.getWritableDatabase().update("tutorialcontrol", cv, "id_tutorialcontrol "+"="+1, null);
					db.close();
				}

			}.start();

		}

		frontLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				home_down.setVisibility(View.VISIBLE);
				
				if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
					home_down.setVisibility(View.INVISIBLE);
				}

				return false;
			}
		});
		backLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				home_down.setVisibility(View.INVISIBLE);
				return false;
			}
		});
		
		//change font
		TextView tvSlide = (TextView) findViewById(R.id.tvSlideText);
		TextView tv5 = (TextView) findViewById(R.id.textView5);
		TextView tvDL= (TextView) findViewById(R.id.tvDaysLeft);
		
		font.changeFontRaleway(cont, tvSlide);
		font.changeFontRaleway(cont, tv5);
		font.changeFontRaleway(cont, tvDL);

	}

	private String getDaysLeft() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dfa = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date finalDate = null;
		String lol = new API(cont).getDia();
		try {
			finalDate = dfa.parse(new API(cont).getGoalDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		
		long distance = -1;
		
		if(finalDate != null)
			distance = getDateDiffString(c.getTime(), finalDate);
		distance = distance + 1;
		
		return String.valueOf(distance);
	}
	
	//returns distance between days of  two Date objects
	public long getDateDiffString(java.util.Date date, java.util.Date date2)
	{
		long timeOne = date.getTime();
		long timeTwo = date2.getTime();
		long oneDay = 1000 * 60 * 60 * 24;
		long delta = (timeTwo - timeOne) / oneDay;

//		int dia_Date1 = Integer.valueOf((String)android.text.format.DateFormat.format("dd", date)), 
//				dia_Date2 = Integer.valueOf((String)android.text.format.DateFormat.format("dd", date2));
//
//		String lol = String.valueOf(timeOne/oneDay);

		if(date2.before(date))
			return -1;
		else
			return delta;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("OnActivityResult", "OnActivityResult");
		//super.onActivityResult(requestCode, resultCode, data);
		Bundle extras = data.getExtras();

		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Intent intent = new Intent(HomeActivity.this, ShareActivity.class);
			Toast.makeText(this, "Foto Almacenada en la galeria", Toast.LENGTH_SHORT).show();
			//Uri photouri = Uri.fromFile((File) extraPhoto.get("data"));
			//intent.putExtra("URI", Uri.fromFile((File) extraPhoto.get("data")));
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Log.d("OnActivityResult", mCurrentPhotoPath);
			startActivity(intent);
		} 
		else { 
			Toast.makeText(this, "No se logro almacenar la imagen", Toast.LENGTH_SHORT).show(); 
		}
		Log.d("HomeActivity", "Termino");
	}


	public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float)height / (float)reqHeight);
			} else {
				inSampleSize = Math.round((float)width / (float)reqWidth);
			}
		}
		return inSampleSize;
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
	public void onStop()
	{
		super.onStop();
		if(viewActionsContentView != null)
			viewActionsContentView.showContent();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) 
		{
		case android.R.id.home:
			if(viewActionsContentView.isContentShown())
				viewActionsContentView.showActions();
			else
				viewActionsContentView.showContent();
			return true;
		}

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//Binds listview in side menu with their respective activities
	private void setUpMenu()
	{
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

		CheckBox notification = new CheckBox(this);
		notification.setText("Notificaciones");
		notification.setChecked(false);


		Log.d("color", new API(cont).getStyle());

		View color1 = (View) findViewById(R.id.col1);
		color1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//asigns style value to data base
				ContentValues cv = new ContentValues();
				cv.put("estilo","femenino");

				SQLiteControl db = new SQLiteControl(cont);
				db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
				db.close();

				updateStyle();
			}
		});

		View color2 = (View) findViewById(R.id.col2);
		color2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//asigns style value to data base
				ContentValues cv = new ContentValues();
				cv.put("estilo","masculino");

				SQLiteControl db = new SQLiteControl(cont);
				db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
				db.close();

				updateStyle();
			}
		});

		View color3 = (View) findViewById(R.id.col3);
		color3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//asigns style value to data base
				ContentValues cv = new ContentValues();
				cv.put("estilo","neutral");

				SQLiteControl db = new SQLiteControl(cont);
				db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
				db.close();

				updateStyle();
			}
		});

		//activates or deactivates notifications
		if(new API(cont).shouldSendNotif())
			checkBoxNotif.setChecked(true);
		else
			checkBoxNotif.setChecked(false);

		Log.d("opera", "Antes " + String.valueOf(new API(cont).shouldSendNotif()));
		//		notification.setOnClickListener(new OnClickListener() {
		//			
		//			@Override
		//			public void onClick(View v) {
		//				// TODO Auto-generated method stub
		//				if(checkBoxNotif.isChecked())
		//				{
		//					checkBoxNotif.setChecked(false);
		//					new API(cont).setNotifFalse();
		//				}
		//				else
		//				{
		//					checkBoxNotif.setChecked(true);
		//					new API(cont).setNotifTrue();
		//				}
		//			}
		//		});
		checkBoxNotif.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!checkBoxNotif.isChecked())
				{
					//checkBoxNotif.setChecked(false);
					new API(cont).setNotifFalse();
				}
				else
				{
					//checkBoxNotif.setChecked(true);
					new API(cont).setNotifTrue();
				}

				Log.d("opera", "Box Despues: " + String.valueOf(new API(cont).shouldSendNotif()));
			}
		});

		layoutNotif.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(checkBoxNotif.isChecked())
				{
					checkBoxNotif.setChecked(false);
					new API(cont).setNotifFalse();
				}
				else
				{
					checkBoxNotif.setChecked(true);
					new API(cont).setNotifTrue();
				}

				Log.d("opera", "Layo Despues: " + String.valueOf(new API(cont).shouldSendNotif()));
				return false;
			}
		});

		//Facebbok Components
		if(!new API(cont).getFacebookID().equals(""))
		{
			ImageAsyncTask programming = new ImageAsyncTask("", null);
			programming.setId(new API(cont).getFacebookID());
			programming.execute();

			TextView facebook_name = (TextView) findViewById(R.id.tvLOL);
			facebook_name.setText(new API(cont).getFacebookName());

			TextView notif = (TextView) findViewById(R.id.sideNotif);
			TextView color = (TextView) findViewById(R.id.side_color);

			Font f = new Font();
			f.changeFontRaleway(cont, notif);
			f.changeFontRaleway(cont, color);			
		}

	}

	public static void setFacebokProfilePic(Bitmap bitMap)
	{		
		if(bitMap != null)
			menu_profile_pic.setImageBitmap(API.getRoundedShape(bitMap));
	}

	//retrieves view from listview
	public View getViewByPosition(int pos, ListView listView) {
		final int firstListItemPosition = listView.getFirstVisiblePosition();
		final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

		if (pos < firstListItemPosition || pos > lastListItemPosition ) {
			return listView.getAdapter().getView(pos, null, listView);
		} else {
			final int childIndex = pos - firstListItemPosition;
			return listView.getChildAt(childIndex);
		}
	}

	//Saves selected style colors to local variables
	private void getStyle()
	{
		//get selected style from database
		String style = new API(cont).getStyle();
		//get system resources
		Resources res = getResources();

		if(style.equals("masculino"))
		{
			styleMain = res.getColor(R.color.MASCULINO_MAIN);
			styleDetail = res.getColor(R.color.MASCULINO_DETAIL);
			button_MiPerfil.setImageResource(R.drawable.selector_home_statistics_male);
			checkBoxNotif.setBackgroundResource(R.drawable.selector_checkbox);
		}
		if(style.equals("femenino"))
		{
			styleMain = res.getColor(R.color.FEMENINO_MAIN);
			styleDetail = res.getColor(R.color.FEMENINO_DETAIL);
			button_MiPerfil.setImageResource(R.drawable.selector_home_statistics_female);
			checkBoxNotif.setBackgroundResource(R.drawable.selector_checkbox_alt);
		}
		if(style.equals("neutral"))
		{
			styleMain = res.getColor(R.color.NEUTRAL_MAIN);
			styleDetail = res.getColor(R.color.NEUTRAL_DETAIL);
			button_MiPerfil.setImageResource(R.drawable.selector_home_statistics_neutral);
			checkBoxNotif.setBackgroundResource(R.drawable.selector_checkbox);
		}

	}

	//set's specific color to certain components in the layout so it achieves the desired style.
	private void updateStyle()
	{

		//sets local style variables
		getStyle();
		Log.d("color", "en home NUMERO " + String.valueOf(styleMain));
		backLayout.setBackgroundColor(styleMain);
		frontLayout.setBackgroundColor(styleMain);
		bodyLayout.setBackgroundColor(styleMain);
		setColors(Color.WHITE, styleDetail, Color.argb(0, 255, 255, 255), styleMain);

		lista = new DAO_DietaCompletada(cont).getLastFiveDietaCompleta();
		initGraph();
		setGraphLabels();
	}

	private void showActivity(int position) 
	{
		final Intent intent;
		switch (position) {
		case 0:
			intent = new Intent(HomeActivity.this, MiPerfilActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 1:
			intent = new Intent(HomeActivity.this, DietaActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 2:
			intent = new Intent(HomeActivity.this, EjercicioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 3:
			intent = new Intent(HomeActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 4:
			intent = new Intent(HomeActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 5:
			intent = new Intent(HomeActivity.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			break;
		case 6:
			intent = new Intent(HomeActivity.this, ShareActivity.class);
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
			intent = getOpenFacebookIntent(cont);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 9:
			intent = new Intent(HomeActivity.this, TutorialActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);			
			break;
		case 10:
			intent = new Intent(HomeActivity.this, DisclaimerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		default:
			return;
		}
	}

	//When the user clicks on the back-key 
	@Override
	public void onBackPressed(){
		moveTaskToBack(true);
	}

	//When click on the button for take the photo
	@Override
	public void onClick(View v){
		if(v.getId() == R.id.action_bar){
			Toast.makeText(this, "Clicked Camera", Toast.LENGTH_LONG).show();
			DietaCompletedDialog dialog = new DietaCompletedDialog(cont);
			dialog.show(getFragmentManager(), "Dieta Completed");
//						dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//			
//							@Override
//							public void onDismiss(DialogInterface dialog) {
//								// TODO Auto-generated method stub
//								List<DTO_DietaCompletada> lista = new DAO_DietaCompletada(cont).getLastFiveDietaCompleta();
//								BarEntry e = new BarEntry(Float.valueOf(lista.get(lista.size()-1).getPeso()), lista.size()-1);
//								set1.addEntry(e);
//								setData(lista.size(), 80, lista );
//								mChart.invalidate();
//								mChart.animateX(lista.size() * 250);
//							}
//						});
		}
		else
		{
			DietaCompletedDialog dialog = new DietaCompletedDialog(cont);
			dialog.show(getFragmentManager(), "Dieta Completed");
//			Intent intent = new Intent(HomeActivity.this, EstadisticasActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);



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

	private File createImageFile() throws IOException{
		// Create an image file name 
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */ 
				storageDir      /* directory */
				); 

		// Save a file: path for use with ACTION_VIEW intents 
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}


	//Clear the Notification
	private void cancelNotification(Context context, int notifyId) {
		String notificationService = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(notificationService);
		mNotificationManager.cancel(notifyId);
	}

	//Sets Graph data
	private void setData(int count, float range, List<DTO_DietaCompletada> list) {

		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			if(i==0)
				xVals.add(" ");
			else
				xVals.add(list.get(i).getId_dieta());
		}

		ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

		for (int i = 0; i < count; i++) {
			float mult = (range + 1);
			float val = (float) (Math.random() * mult) + 3;// + (float)
			// ((mult *
			// 0.1) / 10);
			//yVals.add(new Entry(val, i));
			yVals.add(new BarEntry(Float.valueOf(list.get(i).getPeso()), i));
		}

		// create a dataset and give it a type
		set1 = new BarDataSet(yVals, "Peso");

		//Set the graphics to the graph
		//setColorGraph(Color.rgb(00, 00, 00), Color.rgb(255,255,255));
		set1.setBarShadowColor(barShadow);
		set1.setColor(barColor);

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1); // add the datasets

		// create a data object with the datasets
		BarData data = new BarData(xVals, dataSets);

		// set data
		mChart.setData(data);
		mChart.invalidate();
	}

	private String getPesoDif() 
	{
		// TODO Auto-generated method stub
		String dif = null;

		//Gets current and target weight. Substracts and 
		DTO_Usuario usuario = new DAO_Usuario(cont).getUsuario();
		String pesoActual = usuario.getPeso();
		String pesoIdeal = new API(cont).getPesoIdeal();

		if(Integer.parseInt(pesoActual) < Integer.parseInt(pesoIdeal))
		{
			dif = String.valueOf((Integer.parseInt(pesoIdeal) - Integer.parseInt(pesoActual)));
		}
		else
		{
			dif = String.valueOf((Integer.parseInt(pesoActual) - Integer.parseInt(pesoIdeal)));
		}

		if (Integer.parseInt(pesoIdeal) == 0) 
		{
			return dif + " Kg";
		}
		else
		{
			return dif + " Kg";
		}
	}

	private void setGraphLabels(){
		Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		XLabels xLabels = mChart.getXLabels();
		xLabels.setPosition(XLabelPosition.BOTTOM);
		xLabels.setCenterXLabelText(true);
		xLabels.setTypeface(tf);
		xLabels.setTextColor(textColor);
		xLabels.setSpaceBetweenLabels(0);

		// add data & color
		setData(lista.size(), 80, lista);

		// get the legend (only possible after setting data)
		Legend l = mChart.getLegend();

		// modify the legend ...
		// l.setPosition(LegendPosition.LEFT_OF_CHART);
		l.setForm(LegendForm.LINE);
		l.setTypeface(tf);
		l.setTextColor(textColor);

		YLabels yl = mChart.getYLabels();
		yl.setTypeface(tf);
		yl.setTextColor(textColor);
	}

	public void setColors(int textColor, int barColor, int barShadow, int backgroundColor){
		this.textColor = textColor;
		this.barColor = barColor;
		this.barShadow = barShadow;
		this.backgroundColor = backgroundColor;
	}
	public void initGraph(){
		//Graphs
		mChart = (BarChart) findViewById(R.id.chart1);
		mChart.setValueTextColor(textColor);
		//Max number of bars	
		mChart.setMaxVisibleValueCount(5);
		mChart.set3DEnabled(false);
		mChart.setUnit(" KG");
		mChart.setDrawUnitsInChart(true);
		// if enabled, the chart will always start at zero on the y-axis
		mChart.setStartAtZero(false);
		// disable the drawing of values into the chart
		mChart.setDrawYValues(true);
		mChart.setDrawBorder(true);
		mChart.setBorderPositions(new BorderPosition[] {
				BorderPosition.BOTTOM
		});

		// no description text
		mChart.setDescription("");
		mChart.setNoDataTextDescription("You need to provide data for the chart.");
		// enable value highlighting
		mChart.setHighlightEnabled(false);
		// enable touch gestures
		mChart.setTouchEnabled(false);
		// enable scaling and dragging
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		mChart.setDrawGridBackground(false);
		mChart.setDrawVerticalGrid(true);
		mChart.setDrawHorizontalGrid(true);
		// if disabled, scaling can be done on x- and y-axis separately
		mChart.setPinchZoom(false);
		// set an alternative background color
		mChart.setBackgroundColor(backgroundColor);
		mChart.animateY(2000);
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		return super.onTouchEvent(event);
	}

	public abstract class OnFlingGestureListener implements OnTouchListener
	{
		private Rect rect;
		private final GestureDetector gdt = new GestureDetector(new GestureListener());

		@Override
		public boolean onTouch(final View v, final MotionEvent event) 
		{
			rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
			if (!viewActionsContentView.isContentShown())
			{
				Log.d("fallout4", "Closing...");
				viewActionsContentView.showContent();
				return false;
			}
			else
			{
				Log.d("fallout4", "After closing");
				if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
				{
					Log.d("fallout4", "Outside");
					Toast.makeText(cont, "s", Toast.LENGTH_SHORT).show();
				}

				if(event.getAction() == MotionEvent.ACTION_CANCEL)
				{
					//					Log.d("fallout4", "Cancel");
					//					if(v.getId() == R.id.buttonTopLeft)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon1);
					//					}
					//					if(v.getId() == R.id.buttonTopRight)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon3);
					//					}
					//					if(v.getId() == R.id.buttonDownLeft)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon2);
					//					}
					//					if(v.getId() == R.id.buttonDownRight)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon4);
					//					}
				}

				if(event.getAction() == 0){
					Log.d("fallout4", "Down");
					//rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
					//					if(v.getId() == R.id.buttonTopLeft)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon1_alt);
					//					}
					//					if(v.getId() == R.id.buttonTopRight)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon3_alt);
					//					}
					//					if(v.getId() == R.id.buttonDownLeft)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon2_alt);
					//					}
					//					if(v.getId() == R.id.buttonDownRight)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon4_alt);
					//					}

				}
				else if (event.getAction() == 1) 
				{
					Log.d("fallout4", "Up");
					//					if(v.getId() == R.id.buttonTopLeft)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon1);
					//					}
					//					if(v.getId() == R.id.buttonTopRight)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon3);
					//					}
					//					if(v.getId() == R.id.buttonDownLeft)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon2);
					//					}
					//					if(v.getId() == R.id.buttonDownRight)
					//					{
					//						((ImageButton)v).setImageResource(R.drawable.home_button_icon4);
					//					}
				}
				else if (event.getAction() == 2) {
					Log.d("fallout4", "Scroll");
				}

				if(event.getAction() == MotionEvent.ACTION_MOVE){
					Log.d("fallout4", "Move");
					if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())){
						//						if(v.getId() == R.id.buttonTopLeft)
						//						{
						//							((ImageButton)v).setImageResource(R.drawable.home_button_icon1);
						//						}
						//						if(v.getId() == R.id.buttonTopRight)
						//						{
						//							((ImageButton)v).setImageResource(R.drawable.home_button_icon3);
						//						}
						//						if(v.getId() == R.id.buttonDownLeft)
						//						{
						//							((ImageButton)v).setImageResource(R.drawable.home_button_icon2);
						//						}
						//						if(v.getId() == R.id.buttonDownRight)
						//						{
						//							((ImageButton)v).setImageResource(R.drawable.home_button_icon4);
						//						}
					}
				}

				if(gdt.onTouchEvent(event))
					return false;
				else
					return true;
			}

		}

		private final class GestureListener extends SimpleOnGestureListener 
		{

			private int SWIPE_MIN_DISTANCE = 60;
			private int SWIPE_THRESHOLD_VELOCITY = 100;

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				Log.d("fallout4", "Fling");
				if (!viewActionsContentView.isContentShown()) 
				{
					viewActionsContentView.showContent();

				}
				else
				{
					if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						//Right To Left();
						return false;
					} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
						//Left To Right();
						return true;
					}
					if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
						//Bottom To Top();
						return true;
					} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
							&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
						onTopToBottom();
						return true;
					}
					return false;
				}
				return false;
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e)
			{
				onSingleCLick();
				//				Log.d(DEBUG_TAG, "Single Tap");
				//				Toast.makeText(cont, "lol;", Toast.LENGTH_SHORT).show();
				return true;

			}


		}

		public abstract void onTopToBottom();
		public abstract void onSingleCLick();


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

	//The four buttons are given a customn TouchListener that waits for two specific events to trigger.
	//If a fling-down gesture is detected, the swipeLayot drawer is open
	//If a single click is detected, the respective activity is called when the side drawer is closed
	private void setDrawerListener() {
		final boolean isPressed=false;
		//bind layout buttons to local objects
		ImageButton downLeft = (ImageButton) findViewById(R.id.buttonDownLeft);
		final ImageButton topLeft = (ImageButton) findViewById(R.id.buttonTopLeft);		
		final ImageButton topDer = (ImageButton) findViewById(R.id.buttonTopRight);
		ImageButton downDer = (ImageButton) findViewById(R.id.buttonDownRight);

		OnClickListener myClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!viewActionsContentView.isContentShown()) 
				{
					viewActionsContentView.showContent();
				}
				else if(isDrawerOpen == false)
				{
					if (v.getId() == R.id.buttonTopLeft)
					{
						Intent intent = new Intent(HomeActivity.this, DietaActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
					if (v.getId() == R.id.buttonTopRight)
					{
						Intent intent = new Intent(HomeActivity.this, EjercicioActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
					if (v.getId() == R.id.buttonDownLeft)
					{
						Intent intent = new Intent(HomeActivity.this, CalendarioActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
					if (v.getId() == R.id.buttonDownRight)
					{
						Intent intent = new Intent(HomeActivity.this, ComparativeActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
					}
					// TODO Auto-generated method stub
				}
			}
		};

		topLeft.setOnClickListener(myClickListener);
		topDer.setOnClickListener(myClickListener);
		downLeft.setOnClickListener(myClickListener);
		downDer.setOnClickListener(myClickListener);


	}

	private boolean isAfterHours()
	{
		Calendar c = Calendar.getInstance();
		if(c.get(Calendar.HOUR_OF_DAY) == 13 && (c.get(Calendar.MINUTE) == 21))
		{ Log.d("moj", "Is after hours " + String.valueOf(false));
		return true;}
		else
		{ Log.d("moj", "Is after hours " + String.valueOf(true ));
		return false;}

	}


	@Override
	public void OnDismiss(DialogInterface dialog, int recomendation, String dieta_key) {
		// TODO Auto-generated method stub
		//Toast.makeText(cont, "Dismiss", Toast.LENGTH_SHORT).show();
		List<DTO_DietaCompletada> lista = new DAO_DietaCompletada(cont).getLastFiveDietaCompleta();
		BarEntry e = new BarEntry(Float.valueOf(lista.get(lista.size()-1).getPeso()), lista.size()-1);
		set1.addEntry(e);
		setData(lista.size(), 80, lista );
		mChart.invalidate();
		mChart.animateX(lista.size() * 250);

		RecomendationDialog recomedn_dialog = new RecomendationDialog(cont, recomendation, dieta_key);
		recomedn_dialog.show(getFragmentManager(), "Dieta Completed");
	}


}

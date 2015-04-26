package com.example.miyoideal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import shared.ui.actionscontentview.ActionsContentView;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.view.MotionEventCompat;
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

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.DragEdge;
import com.daimajia.swipe.SwipeLayout.OnRevealListener;
import com.example.DAO.DAO_DietaCompletada;
import com.example.DAO.DAO_Usuario;
import com.example.DB.SQLiteControl;
import com.example.DB.SQLiteDietaDB;
import com.example.DB.SQLiteUserDB;
import com.example.DTO.DTO_DietaCompletada;
import com.example.DTO.DTO_Usuario;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.DietaCompletedDialog;
import com.example.miyoideal.extra.MyArrayAdapter;
import com.example.miyoideal.extra.MyLinearLayout;
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

public class HomeActivity extends Activity implements OnTouchListener, OnClickListener{

	//global variables;
	private int width;
	private int height;

	//layout components
	private LinearLayout linearLayout1;
	private Button button_MiPerfil;
	private ActionsContentView viewActionsContentView;
	private SwipeLayout swipeLayout;
	private ListView viewActionsList;
	private Context cont;
	private RelativeLayout backLayout;
	private RelativeLayout frontLayout;
	private RelativeLayout bodyLayout;

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
		cont = this;


		//bind layout views to local objects
		backLayout = (RelativeLayout) findViewById(R.id.backLayout);
		frontLayout = (RelativeLayout) findViewById(R.id.frontLayout);
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		button_MiPerfil = (Button) findViewById(R.id.abajoButtonlol);
		viewActionsContentView = (ActionsContentView) findViewById(R.id.home_actionsContentView);
		viewActionsList = (ListView) findViewById(R.id.actions);
		swipeLayout =  (SwipeLayout)findViewById(R.id.swipeLayout);
		bodyLayout = (RelativeLayout) findViewById(R.id.homeBodyLayout);

		//Tutorial reminder. Runs just the first time the apps runs
		SharedPreferences runCheck = PreferenceManager.getDefaultSharedPreferences(cont);
		Boolean hasRun = runCheck.getBoolean("hasRun", false); //see if it's run before, default no

		//For the csv files
		ImageButton buttonCamera = (ImageButton) findViewById(R.id.buttonTopLeft);
		buttonCamera.setOnTouchListener(this);
		new DAO_DietaCompletada(cont).InsertCSVFile(null, null, null);

		//Clear the notification
		cancelNotification(this, 001);		

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

			DietaCompletedDialog dialog = new DietaCompletedDialog(cont);
			dialog.show();
			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					List<DTO_DietaCompletada> lista = new DAO_DietaCompletada(cont).getLastFiveDietaCompleta();
					BarEntry e = new BarEntry(Float.valueOf(lista.get(lista.size()-1).getPeso()), lista.size()-1);
					set1.addEntry(e);
					setData(lista.size(), 80, lista );
					mChart.invalidate();
					mChart.animateX(lista.size() * 250);
				}
			});
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

//		swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
//			@Override
//			public void onClose(SwipeLayout layout) {
//				//when the SurfaceView totally cover the BottomView.
//				shoudlOpenDrawer = true;
//				isDrawerOpen = false;
//			}
//
//			@Override
//			public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//				//you are swiping.
//			}
//
//			@Override
//			public void onOpen(SwipeLayout layout) {
//				//when the BottomView totally show.
//				shoudlOpenDrawer = true;
//				isDrawerOpen = true;
//			}
//
//			@Override
//			public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//				//when user's hand released.
//			}
//
//			@Override
//			public void onStartClose(SwipeLayout arg0) {
//				// TODO Auto-generated method stub
//				shoudlOpenDrawer = false;
//			}
//
//			@Override
//			public void onStartOpen(SwipeLayout arg0) {
//				// TODO Auto-generated method stub
//				shoudlOpenDrawer = false;
//				});


		//TouchListner to close the drawer when touched.
		bodyLayout.setOnTouchListener(new DrawerCloseListener());

		//Listener to open drawer and close side menu
		setDrawerListener();

		//Set peso difference
		TextView pesodif = (TextView) findViewById(R.id.homePesoDif);
		pesodif.setText(getPesoDif());

		//Set Motivacion
		TextView motivacion = (TextView) findViewById(R.id.homeMotivacionBody);
		motivacion.setText(new API(cont).getMotivacion());

		//set show mode.
		swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

		//set drag edge.
		swipeLayout.setDragEdge(SwipeLayout.DragEdge.Top);
		
		

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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//Binds listview in side menu with their respective activities
	private void setUpMenu()
	{
		final String[] values = new String[] { "Mi Perfil", "Mi Dieta", "Mi Ejercicio", 
				"Mas Dietas", "Calendario", "Estadisticas", "Preguntanos",

				"Comparte", "Tips y Sujerencias", "Seleccionar Dieta", "Comparativa", "Disclaimer"
				,"Tutorial"};

		final MyArrayAdapter adapter = new MyArrayAdapter(this,
				android.R.layout.simple_list_item_1, values);

		viewActionsList.setAdapter(adapter);

		//		for (int i = 0; i < viewActionsList.getAdapter().getCount(); i++) 
		//		{
		//			TextView textv = (TextView) getViewByPosition(i, viewActionsList);
		//			textv.setTextColor(Color.WHITE);
		//			textv.setText("LOL");
		//
		//			Log.d("menu", "Texto de hijo " + String.valueOf(i) + ":" + textv.getText());
		//		}
		//		
		//		adapter.getItem(0).toUpperCase();


		Log.d("menu", "Child count: " + String.valueOf(viewActionsList.getAdapter().getCount()));



		//		TextView v = (TextView)viewActionsList.getChildAt(5 - 
		//				viewActionsList.getFirstVisiblePosition());
		//		v.setBackgroundColor(Color.GREEN);

		//adapter.notifyDataSetChanged();

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

		//menuLinearLayout.addView(notification);
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
		}
		if(style.equals("femenino"))
		{
			styleMain = res.getColor(R.color.FEMENINO_MAIN);
			styleDetail = res.getColor(R.color.FEMENINO_DETAIL);
		}
		if(style.equals("neutral"))
		{
			styleMain = res.getColor(R.color.NEUTRAL_MAIN);
			styleDetail = res.getColor(R.color.NEUTRAL_DETAIL);
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
		case 4:
			intent = new Intent(HomeActivity.this, CalendarioActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 7:
			intent = new Intent(HomeActivity.this, ShareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 9:
			intent = new Intent(HomeActivity.this, SelecDieta.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 10:
			intent = new Intent(HomeActivity.this, ComparativeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 11:
			intent = new Intent(HomeActivity.this, DisclaimerActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		case 12:
			intent = new Intent(HomeActivity.this, TutorialActivity.class);
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
		if(v.getId() == R.id.buttonTopLeft){
			Toast.makeText(this, "Clicked Camera", Toast.LENGTH_LONG).show();
//			DietaCompletedDialog dialog = new DietaCompletedDialog(cont);
//			dialog.show();
//			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//				@Override
//				public void onDismiss(DialogInterface dialog) {
//					// TODO Auto-generated method stub
//					List<DTO_DietaCompletada> lista = new DAO_DietaCompletada(cont).getLastFiveDietaCompleta();
//					BarEntry e = new BarEntry(Float.valueOf(lista.get(lista.size()-1).getPeso()), lista.size()-1);
//					set1.addEntry(e);
//					setData(lista.size(), 80, lista );
//					mChart.invalidate();
//					mChart.animateX(lista.size() * 250);
//				}
//			});
		}
		else
		{
			Intent intent = new Intent(HomeActivity.this, EstadisticasActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
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

	public abstract class OnFlingGestureListener implements OnTouchListener {

		private final GestureDetector gdt = new GestureDetector(new GestureListener());

		@Override
		public boolean onTouch(final View v, final MotionEvent event) {
			if(gdt.onTouchEvent(event))
				return true;
			else
				return true;
		}

		private final class GestureListener extends SimpleOnGestureListener {

			private int SWIPE_MIN_DISTANCE = 60;
			private int SWIPE_THRESHOLD_VELOCITY = 100;

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					//Right To Left();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					//Left To Right();
					return true;
				}
				if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
					//Bottom To Top();
					return true;
				} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
					onTopToBottom();
					return true;
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
		//		public abstract void onRightToLeft();
		//
		//		public abstract void onLeftToRight();
		//
		//		public abstract void onBottomToTop();
		//

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

		//bind layout buttons to local objects
		ImageButton downLeft = (ImageButton) findViewById(R.id.buttonDownLeft);
		ImageButton topLeft = (ImageButton) findViewById(R.id.buttonTopLeft);		
		ImageButton topDer = (ImageButton) findViewById(R.id.buttonTopRight);
		ImageButton downDer = (ImageButton) findViewById(R.id.buttonDownRight);

		topDer.setOnTouchListener(new OnFlingGestureListener() {

			@Override
			public void onTopToBottom() {
				if (viewActionsContentView.isContentShown()) 
				{
					swipeLayout.open();
				}				
			}

			@Override
			public void onSingleCLick() {
				// TODO Auto-generated method stub
				if (!viewActionsContentView.isContentShown()) 
				{
					viewActionsContentView.showContent();
				}
				else
				{
					Toast.makeText(cont, "Top - Derecha", Toast.LENGTH_SHORT).show();
				}
			}
		});

		topLeft.setOnTouchListener(new OnFlingGestureListener() {

			@Override
			public void onTopToBottom() {
				if (viewActionsContentView.isContentShown()) 
				{
					swipeLayout.open();
				}				
			}

			@Override
			public void onSingleCLick() {
				// TODO Auto-generated method stub
				if (!viewActionsContentView.isContentShown()) 
				{
					viewActionsContentView.showContent();
				}
				else
				{
					Toast.makeText(cont, "Top - Izquierda", Toast.LENGTH_SHORT).show();
				}
			}
		});

		downLeft.setOnTouchListener(new OnFlingGestureListener() {

			@Override
			public void onTopToBottom() {
				if (viewActionsContentView.isContentShown()) 
				{
					swipeLayout.open();
				}				
			}

			@Override
			public void onSingleCLick() {
				// TODO Auto-generated method stub
				if (!viewActionsContentView.isContentShown()) 
				{
					viewActionsContentView.showContent();
				}
				else
				{
					Toast.makeText(cont, "Top - Izquierda", Toast.LENGTH_SHORT).show();
				}
			}
		});

		downDer.setOnTouchListener(new OnFlingGestureListener() {

			@Override
			public void onTopToBottom() {
				if (viewActionsContentView.isContentShown()) 
				{
					swipeLayout.open();
				}				
			}

			@Override
			public void onSingleCLick() {
				// TODO Auto-generated method stub
				if (!viewActionsContentView.isContentShown()) 
				{
					viewActionsContentView.showContent();
				}
				else
				{
					Toast.makeText(cont, "Top - Izquierda", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}
}

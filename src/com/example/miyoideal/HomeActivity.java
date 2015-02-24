package com.example.miyoideal;

import java.io.File;
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
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.DragEdge;
import com.daimajia.swipe.SwipeLayout.OnRevealListener;
import com.example.DAO.DAO_DietaCompletada;
import com.example.DB.SQLiteControl;
import com.example.DB.SQLiteUserDB;
import com.example.DTO.DTO_DietaCompletada;
import com.example.miyoideal.extra.API;
import com.example.miyoideal.extra.DietaCompletedDialog;
import com.example.miyoideal.extra.MyLinearLayout;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.github.mikephil.charting.utils.YLabels;

public class HomeActivity extends Activity implements OnTouchListener{//,GestureDetector.OnGestureListener{

	//global variables;
	private int width;
	private int height;

	//layout components
	//	private LinearLayout linearLayout1;
	private Button button_MiPerfil;
	//	private Button button_MiEjercicio;
	//	private Button button_Calendario;
	private ActionsContentView viewActionsContentView;
	private SwipeLayout swipeLayout;
	private ListView viewActionsList;
	private Context cont;
	private RelativeLayout backLayout;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.baseline4);
		cont = this;

		backLayout = (RelativeLayout) findViewById(R.id.backLayout);

		//Tutorial reminder. Runs just the first time the apps runs
		SharedPreferences runCheck = PreferenceManager.getDefaultSharedPreferences(cont);
		Boolean hasRun = runCheck.getBoolean("hasRun", false); //see if it's run before, default no


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
		//linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		//linearLayout1.getLayoutParams().width = (width/2);
		button_MiPerfil = (Button) findViewById(R.id.abajoButtonlol);

		//
		viewActionsContentView = (ActionsContentView) findViewById(R.id.home_actionsContentView);
		viewActionsList = (ListView) findViewById(R.id.actions);
		setUpMenu();	

		SQLiteUserDB dbUser = new SQLiteUserDB(this);
		dbUser.getReadableDatabase();
		String query = "Select * FROM " + "usuario" + " WHERE " + "id_usuario" + " =  \"" + "1" + "\"";
		Cursor cursor = dbUser.getReadableDatabase().rawQuery(query, null);

		if(cursor.moveToFirst()){
			cursor.moveToFirst();
			this.setTitle(cursor.getString(1));
		}

		//Gathers data to later pass onto Graph
		lista = new DAO_DietaCompletada(cont).getLastFiveDietaCompleta();

		//Initialize the Graph
		setColors(Color.WHITE, Color.BLUE, Color.argb(0, 255, 255, 255), Color.GRAY);
		initGraph();
		setGraphLabels();

		button_MiPerfil.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		});

		swipeLayout =  (SwipeLayout)findViewById(R.id.swipeLayout);

		ImageButton supDer = (ImageButton) findViewById(R.id.buttonRightTop);
		supDer.setOnTouchListener(new OnFlingGestureListener() {

			@Override
			public void onTopToBottom() {
				Log.d(DEBUG_TAG, "Top");
				swipeLayout.open();
			}

			@Override
			public void onRightToLeft() {
				Log.d(DEBUG_TAG, "Right");
			}

			@Override
			public void onLeftToRight() {
				Log.d(DEBUG_TAG, "Left");
			}

			@Override
			public void onBottomToTop() {
				Log.d(DEBUG_TAG, "Bottom");
			}
		});

		//set show mode.
		swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

		//set drag edge.
		swipeLayout.setDragEdge(SwipeLayout.DragEdge.Top);

		swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
			@Override
			public void onClose(SwipeLayout layout) {
				//when the SurfaceView totally cover the BottomView.
				shoudlOpenDrawer = true;
				isDrawerOpen = false;
			}

			@Override
			public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
				//you are swiping.
			}

			@Override
			public void onOpen(SwipeLayout layout) {
				//when the BottomView totally show.
				shoudlOpenDrawer = true;
				isDrawerOpen = true;
			}

			@Override
			public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
				//when user's hand released.
			}

			@Override
			public void onStartClose(SwipeLayout arg0) {
				// TODO Auto-generated method stub
				shoudlOpenDrawer = false;
			}

			@Override
			public void onStartOpen(SwipeLayout arg0) {
				// TODO Auto-generated method stub
				shoudlOpenDrawer = false;

			}
		});
	}


	@Override
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
		return super.onCreateOptionsMenu(menu);
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

		CheckBox notification = new CheckBox(this);
		notification.setText("Notificaciones");
		notification.setChecked(false);

		View color1 = (View) findViewById(R.id.col1);
		color1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setStyle("masculino");
			}
		});

		View color2 = (View) findViewById(R.id.col2);
		color2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setStyle("femenino");
			}
		});

		View color3 = (View) findViewById(R.id.col3);
		color3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setStyle("neutral");
			}
		});

		//menuLinearLayout.addView(notification);
	}

	private void setStyle(String style)
	{
		Resources res = getResources();
		
		if(style.equals("masculino"))
			backLayout.setBackgroundColor(res.getColor(R.color.MASCULINO_MAIN));
		if(style.equals("femenino"))
			backLayout.setBackgroundColor(res.getColor(R.color.FEMENINO_MAIN));
		if(style.equals("neutral"))
			backLayout.setBackgroundColor(res.getColor(R.color.NEUTRAL_MAIN));
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
		mChart.setTouchEnabled(true);
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
			gdt.onTouchEvent(event);
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
					onRightToLeft();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					onLeftToRight();
					return true;
				}
				if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
					onBottomToTop();
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
				Log.d(DEBUG_TAG, "Single Tap");
				Toast.makeText(cont, "lol;", Toast.LENGTH_SHORT).show();
				return true;

			}
		}

		public abstract void onRightToLeft();

		public abstract void onLeftToRight();

		public abstract void onBottomToTop();

		public abstract void onTopToBottom();

	}

}

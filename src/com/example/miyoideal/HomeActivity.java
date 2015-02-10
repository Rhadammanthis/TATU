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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.DAO.DAO_DietaCompletada;
import com.example.DB.SQLiteUserDB;
import com.example.DTO.DTO_DietaCompletada;
import com.example.miyoideal.extra.DietaCompletedDialog;
import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.Legend.LegendForm;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;

public class HomeActivity extends Activity implements View.OnClickListener, OnChartValueSelectedListener{
	
	//global variables;
	private int width;
	private int height;
	
	//layout components
	private LinearLayout linearLayout1;
	private Button button_MiPerfil;
	private Button button_MiEjercicio;
	private Button button_Calendario;
	private ActionsContentView viewActionsContentView;
	
	private LineChart mChart;
	
	private Uri fileUri;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private Context cont;
	
	
	
	private ListView viewActionsList;
	
	private Uri mImageCaptureUri; // This needs to be initialized.
    static final int CAMERA_PIC_REQUEST = 1337; 
    private String filePath;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private File mediaFile;
    //private ShareActivity activity;
    
    //REQUEST IMAGE CODE
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView imageThumbnail;
    private String mCurrentPhotoPath;

    //graph related global variables
    private List<DTO_DietaCompletada> lista;
    private LineDataSet set1;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {		
    	super.onCreate(savedInstanceState);
    	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.baseline4);
		cont = this;
		
		//Clear the notification
		cancelNotification(this, 001);
				
		//Adapt the layout depends screen
    	Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
		linearLayout1.getLayoutParams().width = (width/2);
		button_MiPerfil = (Button) findViewById(R.id.buttonHome);
		
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
		
		lista = new DAO_DietaCompletada(cont).getLastFiveDietaCompleta();
		
		Log.d("grafica", "Ya tenemos lista lista... lol");
		
		//Graphs
		mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setValueTextColor(Color.WHITE);

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
        mChart.setDrawVerticalGrid(false);
        mChart.setDrawHorizontalGrid(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.GRAY);

        // add data
        setData(lista.size(), 80, lista);

        mChart.animateX(lista.size() * 250);

        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(LegendForm.LINE);
        l.setTypeface(tf);
        l.setTextColor(Color.WHITE);

        XLabels xl = mChart.getXLabels();
        xl.setTypeface(tf);
        xl.setTextColor(Color.WHITE);

        YLabels yl = mChart.getYLabels();
        yl.setTypeface(tf);
        yl.setTextColor(Color.WHITE);
        
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
						Entry e = new Entry(Float.valueOf(lista.get(lista.size()-1).getPeso()), lista.size()-1);
						set1.addEntry(e);
						setData(lista.size(), 80, lista);
						mChart.invalidate();
						mChart.animateX(lista.size() * 250);
					}
				});
			}
		});
}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
    	super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			if(imageBitmap != null){
				//Set the image thumbnail on the Dialog
				//imageThumbnail.setImageBitmap(imageBitmap);
			}
		} 
		else { 
			Toast.makeText(this, "Image Capture Failed", Toast.LENGTH_SHORT) .show(); 
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
	
	private void setUpMenu()
	{
		final String[] values = new String[] { "Mi Perfil", "Mi Dieta", "Mi Ejercicio", 
	    		"Mas Dietas", "Calendario", "Estadisticas", "Preguntanos",
	    		"Comparte", "Tips y Sujerencias", "Seleccionar Dieta", "Disclaimer"
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
	    
	    //menuLinearLayout.addView(notification);
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
		    	intent = new Intent(HomeActivity.this, DisclaimerActivity.class);
		    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
		      break;
		    case 11:
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
		Toast.makeText(this, "Clicked", Toast.LENGTH_LONG).show();
		Dialog dialog = new Dialog(cont);
        dialog.setContentView(R.layout.baseline_share);
        dialog.setTitle("Compartir");

        Button save = (Button)dialog.findViewById(R.id.buttonGuardar_share);
        dialog.show();
        //imageThumbnail = (ImageView) dialog.findViewById(R.id.foto);
        save.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
            	// Camera exists? Then proceed... 
            	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            	if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            		File photoFile = null;
            		try{
            			//Create the Image File
            			photoFile = createImageFile();
            		}catch(IOException e){
            			e.printStackTrace();
            		}
            		//Continue only when the ImageFile was created
            		if(photoFile != null){
            			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            		}
            	}
            }
        });
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

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 3;// + (float)
                                                           // ((mult *
                                                           // 0.1) / 10);
            //yVals.add(new Entry(val, i));
            yVals.add(new Entry(Float.valueOf(list.get(i).getPeso()), i));
        }

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "Peso");
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(2f);
        set1.setCircleSize(4f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        set1.setDrawFilled(true);
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);
    }

	@Override
	public void onValueSelected(Entry e, int dataSetIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub
		
	} 
}
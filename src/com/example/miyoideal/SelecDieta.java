package com.example.miyoideal;

import java.util.ArrayList;
import java.util.List;

import com.example.DAO.DAO_Dieta;
import com.example.DB.SQLiteControl;
import com.example.DTO.DTO_Dieta;
import com.example.miyoideal.extra.SelectDialogue;

import android.R.bool;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class SelecDieta extends Activity {
	
	private LinearLayout mainLayout;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selec_dieta);
		
		con = this;
		mainLayout = (LinearLayout) findViewById(R.id.selecdieta_linearlayout);
		
		DAO_Dieta dao_dieta = new DAO_Dieta(this);
		List<DTO_Dieta> list_Dieta = dao_dieta.getAllDieta();
		
		for(DTO_Dieta temp : list_Dieta)
		{
			mainLayout.addView(this.genDietaView(temp));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.selec_dieta, menu);
		
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
	            		if(IsControlAvailable())
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
}

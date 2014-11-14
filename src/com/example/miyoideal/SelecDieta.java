package com.example.miyoideal;

import java.util.ArrayList;
import java.util.List;

import com.example.DAO.DAO_Dieta;
import com.example.DTO.DTO_Dieta;

import android.app.Activity;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selec_dieta);

		mainLayout = (LinearLayout) findViewById(R.id.selecdieta_linearlayout);
		
		DAO_Dieta dao_dieta = new DAO_Dieta(this);
		List<DTO_Dieta> list_Dieta = dao_dieta.getAllDieta();
		
		for(DTO_Dieta temp : list_Dieta)
		{
			mainLayout.addView(this.genDietaView(temp.getNombre()));
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
	
	public RelativeLayout genDietaView(String titulo)
	{
		final RelativeLayout layout = new RelativeLayout(this);
		layout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		TextView TV_titulo = new TextView(this);
		TV_titulo.setText(titulo);
		TV_titulo.setTextSize(20);
		TV_titulo.setPadding(24, 16, 0, 16);
		TV_titulo.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		
		layout.addView(TV_titulo);
		
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
	            	break;
	            }
	            return true;        
	        }
	    });
		
		return layout;
	}
}

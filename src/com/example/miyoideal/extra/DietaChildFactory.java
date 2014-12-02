package com.example.miyoideal.extra;

import java.util.List;

import com.example.DTO.DTO_Componente;

import android.R.string;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class DietaChildFactory {
	
	private String title;
	private List<String> content;
	private String time;
	private Context context;
	
	public DietaChildFactory()
	{
		
	}
	
	public DietaChild GenerateChild(Context context, String title, List<DTO_Componente> content, String time, boolean activo)
	{
		DietaChild child = new DietaChild(context, title, content, time, activo);
		return child;
	}
	
	public RelativeLayout GenerateChild(Context context, String title, String time)
	{
		this.title = title;
		this.time = time;
		this.context = context;
		
		//child parent layout
		RelativeLayout childView = new RelativeLayout(this.context);
		childView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
		//LinearLayout to wrap text 
		LinearLayout linear = new LinearLayout(this.context);
		LinearLayout.LayoutParams linear1_LP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(linear1_LP);
				
		//RelativeLayout to wrap 'title' and 'time'
		FrameLayout header = new FrameLayout(this.context);
		header.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
		//Time TextView. Color, size and text adjusted
		TextView tv_title = new TextView(this.context);
		tv_title.setTextSize(22);
		tv_title.setTextColor(Color.BLACK);
		tv_title.setText(this.time);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				
		//Title TextView. Color, size and text adjusted
		TextView tv_time = new TextView(this.context);
		tv_time.setTextSize(22);
		tv_time.setTextColor(Color.BLACK);
		tv_time.setText(this.title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
				
		//title and time added to header
		header.addView(tv_title);
		header.addView(tv_time);
		header.setPadding(0, 6, 0, 6);
				
		//header added to main container
		linear.addView(header);
		
		//main container added to view body
		childView.addView(linear);
		
		return childView;
	}	

}

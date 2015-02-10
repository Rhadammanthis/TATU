package com.example.miyoideal.extra;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.example.DTO.DTO_Componente;

public class CalendarioChildFactory {
	
	private Context context;
	
	public CalendarioChildFactory(Context con)
	{
		this.context = con;
	}
	
	public RelativeLayout GenerateChildEjercicio(String title, List<DTO_Componente> content)
	{
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
		tv_title.setText(title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				
		//title and time added to header
		header.addView(tv_title);
		header.setPadding(0, 6, 0, 6);
				
		//header added to main container
		linear.addView(header);
		
		//RelativeLayout to wrap 'title' and 'time'
		FrameLayout body = new FrameLayout(this.context);
		body.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				
		//Time TextView. Color, size and text adjusted
		TextView tv_content = new TextView(this.context);
		tv_content.setTextSize(22);
		tv_content.setTextColor(Color.BLACK);
		for (int i = 0; i < content.size(); i++) {
			if(i != content.size()-1)
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion() + "\n");
			else
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion());
		}
		tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
				
		//title and time added to header
		body.addView(tv_content);
		body.setPadding(0, 6, 0, 6);
				
		//header added to main container
		linear.addView(body);
		
		//main container added to view body
		childView.addView(linear);
		
		return childView;
	}

}

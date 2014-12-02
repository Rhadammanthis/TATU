package com.example.miyoideal.extra;

import java.util.List;

import com.example.DTO.DTO_Componente;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class DietaChild {
	
	private RelativeLayout child;
	private CheckBox checkBox;
	private DTO_Componente componente;
	
	public CheckBox getCheckBox() {
		return checkBox;
	}

	public DTO_Componente getDTO_Componente() {
		return componente;
	}
	
	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public RelativeLayout getChild() {
		return child;
	}

	public void setChild(RelativeLayout child) {
		this.child = child;
	}

	public DietaChild(Context context, String title, List<DTO_Componente> content, String time, boolean activo)
	{		
		//child parent layout
		child = new RelativeLayout(context);
		child.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		//LinearLayout to wrap text 
		LinearLayout linear = new LinearLayout(context);
		LinearLayout.LayoutParams linear1_LP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(linear1_LP);
		
		//RelativeLayout to wrap 'title' and 'time'
		FrameLayout header = new FrameLayout(context);
		header.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		//Time TextView. Color, size and text adjusted
		TextView tv_title = new TextView(context);
		tv_title.setTextSize(20);
		tv_title.setTextColor(Color.BLACK);
		tv_title.setText(time);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		
		//Title TextView. Color, size and text adjusted
		TextView tv_time = new TextView(context);
		tv_time.setTextSize(25);
		tv_time.setTextColor(Color.BLACK);
		tv_time.setText(title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		
		//title and time added to header
		header.addView(tv_title);
		header.addView(tv_time);
		
		//header added to main container
		linear.addView(header);
		
		//RelativeLayout to wrap 'content' and checkbox
		FrameLayout body = new FrameLayout(context);
		body.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		body.setPadding(12, 12, 12, 12);
		
		//Content multiline textview
		TextView tv_content = new TextView(context);
		tv_content.setPadding(0, 0, 0, 0);
		tv_content.setTextSize(20);
		tv_content.setTextColor(Color.BLACK);
		for (int i = 0; i < content.size(); i++) {
			if(i != content.size()-1)
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion() + "\n");
			else
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion());
		}
		//tv_content.getLayoutParams().width = 120;
		//tv_content.setMaxLines(this.content.size());
		tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		
		//checkbox
		checkBox = new CheckBox(context);
		checkBox.setChecked(false);
		//arrange params to send view to rightmost position
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)checkBox.getLayoutParams();
		FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            params2.gravity=Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		checkBox.setLayoutParams(params2);
		
		if(activo)
		{
			checkBox.setChecked(true);
		}

		//content and checkbox added to body
		body.addView(tv_content);
		body.addView(checkBox);
		
		//body added toi main container
		linear.addView(body);
		
		//main container added to view body
		child.addView(linear);		
		
		//reference to the first insert in the content list to be later used as passed parameter for database update scripts
		componente = content.get(0);
	}

	public DietaChild() {
		// TODO Auto-generated constructor stub
	}

}

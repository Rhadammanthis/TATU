package com.example.miyoideal.extra;

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
	private String[] content;
	private String time;
	private Context context;
	
	public DietaChildFactory()
	{
		
	}
	
	public RelativeLayout GenerateChild(Context context, String title, String[] content, String time)
	{
		this.context = context;
		this.title = title;
		this.content = new String[content.length];
		for (int i = 0; i < content.length; i++) {
			this.content[i] = content[i];
		}
		this.time = time;
		
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
		tv_title.setTextSize(20);
		tv_title.setTextColor(Color.BLACK);
		tv_title.setText(this.time);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		
		//Title TextView. Color, size and text adjusted
		TextView tv_time = new TextView(this.context);
		tv_time.setTextSize(25);
		tv_time.setTextColor(Color.BLACK);
		tv_time.setText(this.title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		
		//title and time added to header
		header.addView(tv_title);
		header.addView(tv_time);
		
		//header added to main container
		linear.addView(header);
		
		//RelativeLayout to wrap 'content' and checkbox
		FrameLayout body = new FrameLayout(this.context);
		body.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		body.setPadding(12, 12, 12, 12);
		
		//Content multiline textview
		TextView tv_content = new TextView(this.context);
		tv_content.setPadding(0, 0, 0, 0);
		tv_content.setTextSize(20);
		tv_content.setTextColor(Color.BLACK);
		for (int i = 0; i < content.length; i++) {
			tv_content.setText(tv_content.getText() + this.content[i] + "\n");
		}
		tv_content.setMaxLines(this.content.length);
		tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		
		//checkbox
		CheckBox checkBox = new CheckBox(this.context);
		checkBox.setChecked(false);
		//arrange params to send view to rightmost position
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)checkBox.getLayoutParams();
		FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            params2.gravity=Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		checkBox.setLayoutParams(params2);

		//content and checkbox added to body
		body.addView(tv_content);
		body.addView(checkBox);
		
		//body added toi main container
		linear.addView(body);
		
		//main container added to view body
		childView.addView(linear);		
		
		return childView;
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

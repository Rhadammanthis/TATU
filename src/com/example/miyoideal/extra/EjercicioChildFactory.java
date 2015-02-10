package com.example.miyoideal.extra;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class EjercicioChildFactory {
	
	private String title;
	private String[] content;
	private String time;
	private Context context;
	
	public RelativeLayout GenerateChildEjercicio(Context context, String title, String content, String time)
	{
		this.title = title;
		this.time = time;
		this.context = context;
		
		String lol[] = new String[2];
		lol[0] = "Huevo";
		lol[1] = "Pollo";
		
		//child parent layout
		RelativeLayout childView = new RelativeLayout(this.context);
		childView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		childView.setPadding(6, 6, 6, 12);
				
		//LinearLayout to wrap text 
		LinearLayout linear = new LinearLayout(this.context);
		LinearLayout.LayoutParams linear1_LP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(linear1_LP);
				
		//RelativeLayout to wrap 'title' and 'time'
		FrameLayout header = new FrameLayout(this.context);
		header.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//header.getLayoutParams().height = 50 * scale;
		header.setBackgroundColor(Color.parseColor("#bdbdbd"));
				
		//checkbox
		CheckBox checkBox = new CheckBox(this.context);
		checkBox.setChecked(false);
		//arrange params to send view to rightmost position
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)checkBox.getLayoutParams();
		FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		        params2.gravity=Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		checkBox.setLayoutParams(params2);
				
		//Title TextView. Color, size and text adjusted
		TextView tv_time = new TextView(this.context);
		tv_time.setTextSize(20);
		tv_time.setTextColor(Color.BLACK);
		tv_time.setText(this.title);
		tv_time.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		tv_time.setPadding(16, 16, 0, 16);
				
		//title and time added to header
		header.addView(tv_time);
		header.addView(checkBox);
		header.setPadding(0, 6, 0, 6);
				
		//header added to main container
		linear.addView(header);
		
		//RelativeLayout to wrap 'content' and checkbox
		FrameLayout body = new FrameLayout(this.context);
		body.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//body.getLayoutParams().height = 120 * scale;
		body.setBackgroundColor(Color.parseColor("#dbdbdb"));
		
		//Content multiline textview
		TextView tv_content = new TextView(this.context);
		tv_content.setPadding(16, 16, 0, 16);
		tv_content.setTextSize(20);
		tv_content.setTextColor(Color.BLACK);
		//for (int i = 0; i < lol.length; i++) {
			//tv_content.setText(tv_content.getText() + lol[i] + "\n");
		//}
		//tv_content.setMaxLines(lol.length);
		tv_content.setText(content);
		tv_content.setGravity(Gravity.LEFT);
		
		body.addView(tv_content);
		
		linear.addView(body);
		
		//main container added to view body
		childView.addView(linear);
		
		return childView;
	}

}

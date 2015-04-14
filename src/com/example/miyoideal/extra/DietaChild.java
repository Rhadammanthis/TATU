package com.example.miyoideal.extra;

import java.util.List;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.example.DTO.DTO_Componente;
import com.example.miyoideal.R;

public class DietaChild {

	private RelativeLayout child;
	private FrameLayout defaultLayout;
	private int pixels;
	public FrameLayout getDefaultLayout() {
		return defaultLayout;
	}

	public void setDefaultLayout(FrameLayout defaultLayout) {
		this.defaultLayout = defaultLayout;
	}

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

	public DietaChild()
	{

	}

	//This version of the DietaChild constructor will be used in Dieta
	public DietaChild(Context context, String title, List<DTO_Componente> content, String time, boolean activo)
	{		
		final float scale = context.getResources().getDisplayMetrics().density;
		pixels = (int) (60 * scale + 0.5f);

		//child main layout
		child = new RelativeLayout(context);
		child.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		child.setPadding(pixels, (int) ((int) pixels * 0.2), pixels, (int) ((int) pixels * 0.2));

		//LinearLayout to wrap content 
		LinearLayout linear = new LinearLayout(context);
		LinearLayout.LayoutParams linear1_LP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(linear1_LP);

		//FrameLayout to wrap 'title' and 'time'
		FrameLayout header = new FrameLayout(context);
		header.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//header.setBackgroundColor(Color.RED);
		//header.setPadding(pixels, 0, pixels, 0);

		//Time TextView. Color, size and text adjusted
		TextView tv_title = new TextView(context);
		tv_title.setTextSize(20);
		tv_title.setTextColor(Color.WHITE);
		tv_title.setTypeface(null, Typeface.BOLD);
		tv_title.setText(time);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		tv_title.setId(tv_title.hashCode());
		//tv_title.setPadding(0, 0, pixels, 0);

		//Title TextView. Color, size and text adjusted
		TextView tv_time = new TextView(context);
		tv_time.setTextSize(25);
		tv_time.setTextColor(Color.WHITE);
		tv_time.setTypeface(null, Typeface.BOLD);
		tv_time.setText(title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		//tv_title.setPadding(pixels, 0, 0, 0);

		//title and time added to header
		header.addView(tv_title);
		header.addView(tv_time);

		//header added to main container
		linear.addView(header);

		//RelativeLayout to wrap 'content' and checkbox
		FrameLayout body = new FrameLayout(context);
		body.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));//(int) (120 * scale + 0.5f)));
		//body.setBackgroundColor(Color.BLUE);
		//body.setPadding(12, 12, 12, 12);

		//Content multiline textview
		TextView tv_content = new TextView(context);
		//tv_content.setPadding((int) (15 * scale + 0.5f), 0, 0, 0);
		tv_content.setTextSize(17);
		tv_content.setTextColor(Color.WHITE);
		for (int i = 0; i < content.size(); i++) {
			if(i != content.size()-1)
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion() + "\n");
			else
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion());
		}
		tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

		//checkbox
		checkBox = new CheckBox(context);
		checkBox.setChecked(false);
		FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		params2.gravity=Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		params2.setMargins(0, 0, (int) (3 * scale + 0.5f), 0); 
		checkBox.setLayoutParams(params2);

		//whether checkbox is checked or not
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

	//This version of the DietaChild constructor will be used in Calendar
	public DietaChild(Context context, String title, List<DTO_Componente> content)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		pixels = (int) (60 * scale + 0.5f);

		//child main layout
		child = new RelativeLayout(context);
		child.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		child.setPadding(pixels, (int) (3 * scale + 0.5f), pixels, (int) (5 * scale + 0.5f));

		//LinearLayout to wrap content 
		LinearLayout linear = new LinearLayout(context);
		LinearLayout.LayoutParams linear1_LP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(linear1_LP);

		//FrameLayout to wrap 'title' and 'time'
		FrameLayout header = new FrameLayout(context);
		header.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//header.setBackgroundColor(Color.RED);
		//header.setPadding(pixels, 0, pixels, 0);

		//Title TextView. Color, size and text adjusted
		TextView tv_title = new TextView(context);
		tv_title.setTextSize(25);
		tv_title.setTextColor(Color.WHITE);
		tv_title.setTypeface(null, Typeface.BOLD);
		tv_title.setText(title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		//tv_title.setPadding(pixels, 0, 0, 0);

		//title and time added to header
		header.addView(tv_title);

		//header added to main container
		linear.addView(header);

		//RelativeLayout to wrap 'content' and checkbox
		FrameLayout body = new FrameLayout(context);
		body.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));//(int) (120 * scale + 0.5f)));
		//body.setBackgroundColor(Color.BLUE);
		//body.setPadding(12, 12, 12, 12);

		//Content multiline textview
		TextView tv_content = new TextView(context);
		//tv_content.setPadding((int) (15 * scale + 0.5f), 0, 0, 0);
		tv_content.setTextSize(17);
		tv_content.setTextColor(Color.WHITE);
		for (int i = 0; i < content.size(); i++) {
			if(i != content.size()-1)
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion() + "\n");
			else
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion());
		}
		tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

		//content and checkbox added to body
		body.addView(tv_content);

		//body added toi main container
		linear.addView(body);

		//main container added to view body
		child.addView(linear);		
	}

	public void genEjercicioChild(Context context, String title, String content)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		pixels = (int) (60 * scale + 0.5f);

		//child main layout
		child = new RelativeLayout(context);
		child.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		child.setPadding(pixels, (int) ((int) pixels * 0.2), pixels, (int) ((int) pixels * 0.2));

		//LinearLayout to wrap content 
		LinearLayout linear = new LinearLayout(context);
		LinearLayout.LayoutParams linear1_LP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(linear1_LP);

		//FrameLayout to wrap 'title' and 'time'
		FrameLayout header = new FrameLayout(context);
		header.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//header.setBackgroundColor(Color.RED);
		//header.setPadding(pixels, 0, pixels, 0);

		//Time TextView. Color, size and text adjusted
		TextView tv_title = new TextView(context);
		tv_title.setTextSize(20);
		tv_title.setTextColor(Color.WHITE);
		tv_title.setTypeface(null, Typeface.BOLD);
		tv_title.setText(title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		tv_title.setId(tv_title.hashCode());
		//tv_title.setPadding(0, 0, pixels, 0);

		//		//Title TextView. Color, size and text adjusted
		//		TextView tv_time = new TextView(context);
		//		tv_time.setTextSize(25);
		//		tv_time.setTextColor(Color.WHITE);
		//		tv_time.setTypeface(null, Typeface.BOLD);
		//		tv_time.setText(title);
		//		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		//		//tv_title.setPadding(pixels, 0, 0, 0);

		//title and time added to header
		header.addView(tv_title);

		//header added to main container
		linear.addView(header);

		//RelativeLayout to wrap 'content' and checkbox
		FrameLayout body = new FrameLayout(context);
		body.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));//(int) (120 * scale + 0.5f)));
		//body.setBackgroundColor(Color.BLUE);
		//body.setPadding(12, 12, 12, 12);

		//Content multiline textview
		TextView tv_content = new TextView(context);
		//tv_content.setPadding((int) (15 * scale + 0.5f), 0, 0, 0);
		tv_content.setTextSize(17);
		tv_content.setTextColor(Color.WHITE);
		tv_content.setText(content);
		tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);


		//content and checkbox added to body
		body.addView(tv_content);

		//body added toi main container
		linear.addView(body);

		//main container added to view body
		child.addView(linear);		
	}

	//This version of the DietaChild constructor will be used in Dieta
	public void genSelectDietaChild(final Context context, String title, List<String> id, List<String> content, 
			List<String> tag, final int styleDark, final int styleMain, final FragmentManager fM)
	{		
		final float scale = context.getResources().getDisplayMetrics().density;
		pixels = (int) (60 * scale + 0.5f);

		//child main layout
		child = new RelativeLayout(context);
		child.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		child.setPadding(0, (int) (15 * scale + 0.5f), 0, (int) (15 * scale + 0.5f));

		//LinearLayout to wrap content 
		LinearLayout linear = new LinearLayout(context);
		LinearLayout.LayoutParams linear1_LP = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(linear1_LP);

		//FrameLayout to wrap 'title' and 'time'
		FrameLayout header = new FrameLayout(context);
		header.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//header.setBackgroundColor(Color.RED);
		header.setPadding(pixels, (int) (3 * scale + 0.5f), pixels, (int) (3 * scale + 0.5f));

		//Time TextView. Color, size and text adjusted
		TextView tv_title = new TextView(context);
		tv_title.setTextSize(20);
		tv_title.setTextColor(Color.WHITE);
		tv_title.setTypeface(null, Typeface.BOLD);
		tv_title.setText(title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		tv_title.setId(tv_title.hashCode());
		//tv_title.setPadding(0, 0, pixels, 0);

		//title and time added to header
		header.addView(tv_title);

		//header added to main container
		linear.addView(header);

		//linear layout to hold each dieta name and tag
		LinearLayout body_frame = new LinearLayout(context);
		body_frame.setOrientation(LinearLayout.VERTICAL);
		body_frame.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));


		//creates body with every instance in both content and tga lists
		for(int i=0; i < content.size();i++)
		{
			//RelativeLayout to wrap 'content' and tag
			final FrameLayout body = new FrameLayout(context);
			body.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));//(int) (120 * scale + 0.5f)));
//			body.setPadding(0, (int) (3 * scale + 0.5f), 
//					0, (int) (3 * scale + 0.5f));
			body.setPadding(pixels, (int) (5 * scale + 0.5f), pixels, (int) (5 * scale + 0.5f));
			//body.setBackgroundColor(Color.BLUE);
			//body.setPadding(12, 12, 12, 12);

			//name of every instance textview
			TextView tv_content = new TextView(context);
			//tv_content.setPadding((int) (15 * scale + 0.5f), 0, 0, 0);
			tv_content.setTextSize(17);
			tv_content.setTextColor(Color.WHITE);
			tv_content.setText(content.get(i));
			tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

			//checkbox
			TextView tv_tag = new TextView(context);
			tv_tag.setTextSize(18);
			tv_tag.setTextColor(Color.WHITE);
			tv_tag.setText(tag.get(i));
			tv_tag.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

			//content and checkbox added to body
			body.addView(tv_content);
			body.addView(tv_tag);
			
			final String dietaId = id.get(i);
			
			body.setOnTouchListener( new View.OnTouchListener()
			{

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch(event.getAction())
					{
					case MotionEvent.ACTION_DOWN:
						body.setBackgroundColor(styleDark);
						break;
					case MotionEvent.ACTION_UP:
						//set color back to default
						body.setBackgroundColor(styleMain); 

						SelectDialogue temp1;
						//Checks whether dieta is available
						if(!new API(context).IsDietaSet())
						{
							//If it is, prompts a dialogue to ask whether the user would like to select this one
							temp1 = new SelectDialogue(context, dietaId, true);
							temp1.show(fM, "2");
						}
						else
						{
							temp1 = new SelectDialogue(context, dietaId, false);
							temp1.show(fM, "2");
						}
						break;
					}
					return true;        
				}
			});
			
			Log.d("selec", "Name " + tv_content.getText().toString());
			Log.d("selec", "Tag " + tv_tag.getText().toString());
			
			//add body to body_frame
			body_frame.addView(body);
		}


		//body added toi main container
		linear.addView(body_frame);

		//main container added to view body
		child.addView(linear);		
	}

	public DietaChild(Context context) {
		// TODO Auto-generated constructor stub
		//generate default layout
		genDefaultLayout(context);

	}

	private void genDefaultLayout(Context context)
	{
		defaultLayout =  new FrameLayout(context);
		defaultLayout.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		//Set default text
		TextView text = new TextView(context);
		text.setTextSize(35);
		text.setTextColor(Color.WHITE);
		text.setText("No hay datos para este día.");
		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		defaultLayout.addView(text);
	}

}

package com.cceo.miyoideal.extra;

import java.util.List;

import android.R.bool;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.cceo.DTO.DTO_Componente;
import com.cceo.miyoideal.R;

public class DietaChild {

	private RelativeLayout child;
	private RelativeLayout defaultLayout;
	private int pixels;
	private Rect rect;
	private Font font;

	public RelativeLayout getDefaultLayout() {
		return defaultLayout;
	}

	public void setDefaultLayout(RelativeLayout defaultLayout) {
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
		font = new Font();
	}

	//This version of the DietaChild constructor will be used in Dieta
	public DietaChild(Context context, String title, List<DTO_Componente> content, String time, boolean activo)
	{		
		font = new Font(); 

		final float scale = context.getResources().getDisplayMetrics().density;
		pixels = (int) (20 * scale + 0.5f);

		//child main layout
		child = new RelativeLayout(context);
		child.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		child.setPadding(pixels, (int) ((int) pixels), pixels, (int) ((int) pixels ));

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
		font.changeFontRaleway(context, tv_title);
		//tv_title.setPadding(0, 0, pixels, 0);

		//Title TextView. Color, size and text adjusted
		TextView tv_time = new TextView(context);
		font.changeFontRalewayHeavy(context, tv_time);

		tv_time.setTextSize(25);
		tv_time.setTextColor(Color.WHITE);
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
		font.changeFontRaleway(context, tv_content);

		//tv_content.setPadding((int) (15 * scale + 0.5f), 0, 0, 0);
		tv_content.setTextSize(17);
		tv_content.setTextColor(Color.WHITE);
		for (int i = 0; i < content.size(); i++) {
			if(i != content.size()-1)
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion() + "\n");
			else
				tv_content.setText(tv_content.getText() + content.get(i).getDescripcion());
		}
		tv_content.setLayoutParams(new android.view.ViewGroup.LayoutParams((int) ((230 * scale + 0.5f)), 
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		//tv_content.setWidth(0);//(int) (child.getLayoutParams().width * 0.3));
		tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

		//checkbox
		checkBox = new CheckBox(context);
		checkBox.setChecked(false);
		FrameLayout.LayoutParams params2 = new FrameLayout.LayoutParams(
			//LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); 
				(int) (40 * scale + 0.5f), (int) (40 * scale + 0.5f));
		params2.gravity=Gravity.RIGHT | Gravity.CENTER_VERTICAL;
	//	params2.setMargins(0, 0, (int) (-40 * scale + 0.5f), 0); 
		checkBox.setLayoutParams(params2);
	//	checkBox.setPadding((int) (45 * scale + 0.5f), (int) (45 * scale + 0.5f),  (int) (45 * scale + 0.5f), (int) (45 * scale + 0.5f));
		//checkBox.getLayoutParams().height = checkBox.getLayoutParams().height + (int) (45 * scale + 0.5f); 
		checkBox.setBackgroundResource(R.drawable.selector_checkbox_clean);
		checkBox.setButtonDrawable(null);
//		checkBox.setScaleY(0.65f);
//		checkBox.setScaleX(0.25f);
		
	//	checkBox.setBackgroundResource(R.drawable.selector_checkbox_clean);
		
//		String style = new API(context).getStyle();
//		if(style.equals("masculino"))
//		{
//			checkBox.setBackgroundResource(R.drawable.selector_checkbox);
//		}
//		if(style.equals("femenino"))
//		{
//			checkBox.setBackgroundResource(R.drawable.selector_checkbox_alt);
//		}
//		if(style.equals("neutral"))
//		{
//			checkBox.setBackgroundResource(R.drawable.selector_checkbox);
//		}
		
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
		font = new Font();
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
		font.changeFontRalewayHeavy(context, tv_title);
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
		font.changeFontRaleway(context, tv_content);
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
		font = new Font();
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
		font.changeFontRalewayHeavy(context, tv_title);
		tv_title.setText(title);
		tv_title.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		tv_title.setId(tv_title.hashCode());
		//tv_title.setPadding(0, 0, pixels, 0);

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
		font.changeFontRaleway(context, tv_content);
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
			List<String> tag, List<String> desc, final int styleDark, final int styleMain, final FragmentManager fM)
	{		
		font = new Font();
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
		font.changeFontRalewayHeavy(context, tv_title);
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

		RelativeLayout holder = new RelativeLayout(context);
		LayoutParams paramHolder = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		holder.setLayoutParams(paramHolder);

		//creates body with every instance in both content and tga lists
		for(int i=0; i < content.size();i++)
		{


			//LinearLayout to wrap content 
			LinearLayout cuerpo = new LinearLayout(context);
			LinearLayout.LayoutParams linear1_cuerpo = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			cuerpo.setOrientation(LinearLayout.VERTICAL);
			cuerpo.setLayoutParams(linear1_cuerpo);

			//RelativeLayout to wrap 'content' and tag
			final FrameLayout body = new FrameLayout(context);
			body.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));//(int) (120 * scale + 0.5f)));
			//			body.setPadding(0, (int) (3 * scale + 0.5f), 
			//					0, (int) (3 * scale + 0.5f));
			body.setPadding(pixels, (int) (5 * scale + 0.5f), pixels, (int) (2 * scale + 0.5f));
			//body.setBackgroundColor(Color.BLUE);
			//body.setPadding(12, 12, 12, 12);

			//name of every instance textview
			TextView tv_content = new TextView(context);
			font.changeFontRaleway(context, tv_content);
			//tv_content.setPadding((int) (15 * scale + 0.5f), 0, 0, 0);
			tv_content.setTextSize(17);
			tv_content.setTextColor(Color.WHITE);
			tv_content.setText(content.get(i));
			tv_content.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

			//checkbox
			TextView tv_tag = new TextView(context);
			font.changeFontRaleway(context, tv_tag);
			tv_tag.setTextSize(18);
			tv_tag.setTextColor(Color.WHITE);
			tv_tag.setText(tag.get(i));
			tv_tag.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

			//RelativeLayout to wrap 'content' and checkbox
			body.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));//(int) (120 * scale + 0.5f)));
			//body.setBackgroundColor(Color.BLUE);
			//body.setPadding(12, 12, 12, 12);

			//Content multiline textview
			final TextView descripction = new TextView(context);
			font.changeFontRaleway(context, descripction);
			//tv_content.setPadding((int) (15 * scale + 0.5f), 0, 0, 0);
			descripction.setTextSize(17);
			descripction.setTextColor(Color.WHITE);
			descripction.setText(desc.get(i));
			descripction.setPadding(pixels, (int) (0 * scale + 0.5f), pixels, (int) (5 * scale + 0.5f));
			descripction.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

			//content and checkbox added to body
			body.addView(tv_content);
			body.addView(tv_tag);

			cuerpo.addView(body);
			cuerpo.addView(descripction);

			final String dietaId = id.get(i);

			cuerpo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					// TODO Auto-generated method stub
					if(!hasFocus)
					{
						body.setBackgroundColor(styleMain); 
						descripction.setBackgroundColor(styleMain);
					}
				}
			});


			cuerpo.setOnTouchListener( new View.OnTouchListener()
			{
				private boolean shouldCancel = false;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch(event.getAction())
					{
					case MotionEvent.ACTION_DOWN:
						body.setBackgroundColor(styleDark);
						descripction.setBackgroundColor(styleDark);

						rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());

						shouldCancel = false;

						break;
					case MotionEvent.ACTION_MOVE:
						//						Toast.makeText(context, "lol", Toast.LENGTH_SHORT).show();
						if(!rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY()))
						{
							// User moved outside bounds
							shouldCancel = true;
							body.setBackgroundColor(styleMain); 
							descripction.setBackgroundColor(styleMain);
						}
						break;
					case MotionEvent.ACTION_CANCEL:
						body.setBackgroundColor(styleMain); 
						descripction.setBackgroundColor(styleMain);
						break;
					case MotionEvent.ACTION_UP:
						//set color back to default
						body.setBackgroundColor(styleMain); 
						descripction.setBackgroundColor(styleMain);

						if (!shouldCancel) 
						{
							SelectDialogue temp1;
							//Checks whether dieta is available
							if (!new API(context).IsDietaSet()) {
								//If it is, prompts a dialogue to ask whether the user would like to select this one
								temp1 = new SelectDialogue(context, dietaId,
										true);
								temp1.show(fM, "2");
							} else {
								temp1 = new SelectDialogue(context, dietaId,
										false);
								temp1.show(fM, "2");
							}

							shouldCancel = false;
						}
						break;
					}
					return true;        
				}
			});

			Log.d("selec", "Name " + tv_content.getText().toString());
			Log.d("selec", "Tag " + tv_tag.getText().toString());

			//add body to body_frame
			body_frame.addView(cuerpo);

		}

		holder.addView(body_frame);
		//body added toi main container
		linear.addView(holder);

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
		font = new Font();
		final float scale = context.getResources().getDisplayMetrics().density;
		pixels = (int) (100 * scale + 0.5f);

		defaultLayout =  new RelativeLayout(context);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				context.getResources().getDisplayMetrics().heightPixels - pixels);

		defaultLayout.setLayoutParams(params);//new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//defaultLayout.setBackgroundColor(Color.WHITE);
		//defaultLayout.setPadding(pixels, (int) ((int) pixels * 0.2), pixels, (int) ((int) pixels * 0.2));

		//defaultLayout.

		//Set default text
		TextView text = new TextView(context);
		text.setTextSize(30);
		font.changeFontRaleway(context, text);
		text.setLayoutParams(params);
		String style = new API(context).getStyle();
		//get system resources
		
		if(style.equals("masculino"))
		{
			text.setTextColor(Color.parseColor("#6675B0"));
		}
		if(style.equals("femenino"))
		{
			text.setTextColor(Color.parseColor("#BC5361"));
		}
		if(style.equals("neutral"))
		{
			text.setTextColor(Color.parseColor("#00837A"));
		}
		text.setText("No hay datos para este día.");
		text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

		//defaultLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		defaultLayout.addView(text);

	}

}

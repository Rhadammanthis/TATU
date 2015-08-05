package com.cceo.calendarcard;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import android.R.bool;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.sax.EndElementListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import com.cceo.DAO.DAO_Dieta;
import com.cceo.DTO.DTO_Dieta;
import com.cceo.miyoideal.*;
import com.cceo.miyoideal.extra.API;

public class CalendarCard extends RelativeLayout {

	private TextView cardTitle;
	private int itemLayout = R.layout.card_item_simple;
	private OnItemRender mOnItemRender;
	private OnItemRender mOnItemRenderDefault;
	private OnCellItemClick mOnCellItemClick;
	private Calendar dateDisplay;
	private ArrayList<CheckableLayout> cells = new ArrayList<CheckableLayout>();
	public LinearLayout cardGrid;
	private int checkedDay = -1;
	private int firstDay;
	private int pixels;
	private Context con;
	private boolean monthBegun;
	private boolean monthEnded;
	private CheckedTextView currentCell;
	private LayoutParams celParam;
	private int h = 0;
	private int w = 0;
	private int originh = 0;
	private int originw = 0;
	private int today;
	private int duracionDieta = 0;
	public RelativeLayout all;
	private boolean shoudlRecordDate = true;
	private java.util.Date initDate;

	public CalendarCard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CalendarCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CalendarCard(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 
	 *Maybe here we can point out which cells to enable as checked from the beginning 
	 */

	public void init(final Context ctx) {
		if (isInEditMode()) return;
		
		

		if(new API(ctx).IsDietaSet() && duracionDieta == 0)
			duracionDieta = (Integer.parseInt(new DAO_Dieta(ctx).getDieta(new API(ctx).getID_Dieta()).getDuracion()));

		final float scale = ctx.getResources().getDisplayMetrics().density;
		pixels = (int) (210 * scale + 0.5f);

		//to track which non-current months cells to display
		monthBegun = false;
		monthEnded = false;

		//save Context reference to a global value
		con=ctx;

		View layout = LayoutInflater.from(ctx).inflate(R.layout.card_view, null, false);

		if (dateDisplay == null)
			dateDisplay = Calendar.getInstance();

		all = (RelativeLayout)layout.findViewById(R.id.calendarCardID);
		cardTitle = (TextView)layout.findViewById(R.id.cardTitle);
		cardTitle.setTextColor(Color.WHITE);
		cardGrid = (LinearLayout)layout.findViewById(R.id.cardGrid);

		try
		{
			if(checkedDay == -1)
				checkedDay = Integer.parseInt(getDietaInitialDay(ctx));
		}
		catch(Exception e)
		{
			Toast.makeText(con, "Selecciona una dieta", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		firstDay = (checkedDay);

		Calendar cal = Calendar.getInstance();
		cardTitle.setText(getMonthForInt(cal.get(Calendar.MONTH)));
		//cardTitle.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(dateDisplay.getTime()));
		((RelativeLayout.LayoutParams)(cardTitle.getLayoutParams())).setMargins(0, 0, 0, (int) (7 * scale + 0.5f));

		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		((TextView)layout.findViewById(R.id.cardDay1)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay1)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay2)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay2)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay3)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay3)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay4)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay4)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay5)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay5)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay6)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay6)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay7)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay7)).setTextColor(Color.WHITE);

		LayoutInflater la = LayoutInflater.from(ctx);
		for(int y=0; y<cardGrid.getChildCount(); y++) {
			LinearLayout row = (LinearLayout)cardGrid.getChildAt(y);
			for(int x=0; x<row.getChildCount(); x++) {
				CheckableLayout cell = (CheckableLayout)row.getChildAt(x);
				cell.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//for(CheckableLayout c : cells)
						//c.setChecked(false);
						//((CheckableLayout)v).setChecked(true);

						if (getOnCellItemClick()!= null)
							getOnCellItemClick().onCellClick(v, (CardGridItem)v.getTag()); // TODO create item
					}
				});

				row.getLayoutParams().height = pixels / cardGrid.getChildCount();
				cell.getLayoutParams().height = (pixels / cardGrid.getChildCount()) - 2;

				View cellContent = la.inflate(itemLayout, cell, false);
				cell.addView(cellContent);
				cells.add(cell);
			}
		}

		addView(layout);

		mOnItemRenderDefault = new OnItemRender() {
			@Override
			public void onRender(CheckableLayout v, CardGridItem item) {

				originh = v.getLayoutParams().height;
				originw = v.getLayoutParams().width;

				celParam = (LayoutParams) v.getChildAt(0).getLayoutParams();

				if(shoudlRecordDate)
					today++;

				((TextView)v.getChildAt(0)).setText(item.getDayOfMonth().toString());
				((TextView)v.getChildAt(0)).setTextColor(Color.WHITE);

				//hide non-current-month cells
				int day = Integer.parseInt(((TextView)v.getChildAt(0)).getText().toString());
				if(day == 1)
				{
					monthBegun = true;
				}
				if(day > 22 && !monthBegun)
				{
					//v.setVisibility(View.INVISIBLE);
					((TextView)v.getChildAt(0)).setText("");

				}
				if(day == 28 && monthBegun)
				{
					monthEnded = true;
				}
				if(day < 8 && monthEnded && monthBegun)
				{
					//v.setVisibility(View.INVISIBLE);
					((TextView)v.getChildAt(0)).setText("");
				}

				//reference to today's day
				Calendar cal = Calendar.getInstance();
				int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

				//Color today's day white, and change text color to black
				if(((TextView)v.getChildAt(0)).getText().equals(String.valueOf(dayOfMonth)))
				{
					shoudlRecordDate = false;
					celParam = (LayoutParams) v.getChildAt(0).getLayoutParams();
					((TextView)v.getChildAt(0)).setTextColor(Color.BLACK);
					((TextView)v.getChildAt(0)).bringToFront();


					//save reference to a global variable
					currentCell =  (CheckedTextView) v.getChildAt(0);

					h = v.getChildAt(0).getLayoutParams().height;	
					w = v.getChildAt(0).getLayoutParams().width;
				}

				if(((TextView)v.getChildAt(0)).getText().equals(String.valueOf(checkedDay)))
				{
					((CheckableLayout)v).setChecked(true);

					if((duracionDieta + firstDay) - checkedDay > 1)
					{
						checkedDay++;
					}
				}
			}
		};

		updateCells();
	}

	public void iniMonth(final Context ctx, Calendar cal) {
		if (isInEditMode()) return;

		final float scale = ctx.getResources().getDisplayMetrics().density;
		pixels = (int) (210 * scale + 0.5f);

		//to track which non-current months cells to display
		monthBegun = false;
		monthEnded = false;

		//save Context reference to a global value
		con=ctx;

		View layout = LayoutInflater.from(ctx).inflate(R.layout.card_view, null, false);

		dateDisplay = cal;

		cardGrid = (LinearLayout)layout.findViewById(R.id.cardGrid);

		try
		{
			checkedDay = Integer.parseInt(getDietaInitialDay(ctx));
		}
		catch(Exception e)
		{
			Toast.makeText(con, "Selecciona una dieta", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		firstDay = (checkedDay);

		Log.d("equs", String.valueOf(cal.get(Calendar.MONTH)));
		cal.getDisplayName(cal.get(Calendar.MONTH), Calendar.LONG, Locale.ENGLISH);
		Log.d("equs", "strn " + getMonthForInt(cal.get(Calendar.MONTH)));

		//cardTitle.setText("ROFL");
		//cardTitle.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(dateDisplay.getTime()));
		cardTitle.setText(getMonthForInt(cal.get(Calendar.MONTH)));
		((RelativeLayout.LayoutParams)(cardTitle.getLayoutParams())).setMargins(0, 0, 0, (int) (7 * scale + 0.5f));

		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		((TextView)layout.findViewById(R.id.cardDay1)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay1)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay2)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay2)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay3)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay3)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay4)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay4)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay5)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay5)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay6)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay6)).setTextColor(Color.WHITE);
		cal.add(Calendar.DAY_OF_WEEK, 1);
		((TextView)layout.findViewById(R.id.cardDay7)).setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
		((TextView)layout.findViewById(R.id.cardDay7)).setTextColor(Color.WHITE);

		LayoutInflater la = LayoutInflater.from(ctx);
		for(int y=0; y<cardGrid.getChildCount(); y++) {
			LinearLayout row = (LinearLayout)cardGrid.getChildAt(y);
			for(int x=0; x<row.getChildCount(); x++) {
				CheckableLayout cell = (CheckableLayout)row.getChildAt(x);
				cell.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//for(CheckableLayout c : cells)
						//c.setChecked(false);
						//((CheckableLayout)v).setChecked(true);

						if (getOnCellItemClick()!= null)
							getOnCellItemClick().onCellClick(v, (CardGridItem)v.getTag()); // TODO create item
					}
				});

				row.getLayoutParams().height = pixels / cardGrid.getChildCount();
				cell.getLayoutParams().height = (pixels / cardGrid.getChildCount()) - 2;

				View cellContent = la.inflate(itemLayout, cell, false);
				cell.addView(cellContent);
				cells.add(cell);
			}
		}

		//addView(layout);

		mOnItemRenderDefault = new OnItemRender() {
			@Override
			public void onRender(CheckableLayout v, CardGridItem item) {
				originh = v.getLayoutParams().height;
				originw = v.getLayoutParams().width;

				celParam = (LayoutParams) v.getChildAt(0).getLayoutParams();

				((TextView)v.getChildAt(0)).setText(item.getDayOfMonth().toString());
				((TextView)v.getChildAt(0)).setTextColor(Color.WHITE);

				//hide non-current-month cells
				int day = Integer.parseInt(((TextView)v.getChildAt(0)).getText().toString());
				if(day == 1)
				{
					monthBegun = true;
				}
				if(day > 22 && !monthBegun)
				{
					((TextView)v.getChildAt(0)).setText("");

				}
				if(day == 28 && monthBegun)
				{
					monthEnded = true;
				}
				if(day < 8 && monthEnded && monthBegun)
				{
					((TextView)v.getChildAt(0)).setText("");
				}

				//reference to today's day
				Calendar cal = Calendar.getInstance();
				int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

				//Color today's day white, and change text color to black
				if(((TextView)v.getChildAt(0)).getText().equals(String.valueOf(dayOfMonth)))
				{
					celParam = (LayoutParams) v.getChildAt(0).getLayoutParams();

					//save reference to a global variable
					currentCell =  (CheckedTextView) v.getChildAt(0);

					h = v.getChildAt(0).getLayoutParams().height;	
					w = v.getChildAt(0).getLayoutParams().width;
				}

				if(((TextView)v.getChildAt(0)).getText().equals(String.valueOf(checkedDay)))
				{
					((CheckableLayout)v).setChecked(true);

					if((duracionDieta + firstDay) - checkedDay > 1)
					{
						checkedDay++;
					}
				}
			}
		};

		updateCells();
	}

	public int getFirstDay()
	{
		return firstDay;
	}

	public java.util.Date getInitialDate()
	{
		return initDate;
	}

	private String getDietaInitialDay(Context con)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
		int t = 0;
		java.util.Date initalDate = null;
		try {
			initalDate = df.parse(new API(con).getDia());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try{
			t = Integer.parseInt((String)android.text.format.DateFormat.format("dd", initalDate));
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		this.initDate = initalDate;

		if(t == 0)
			return "";

		return String.valueOf(t);
	}

	private int getDaySpacing(int dayOfWeek) {
		if (Calendar.SUNDAY == dayOfWeek)
			return 6;
		else
			return dayOfWeek - 2;
	}

	private int getDaySpacingEnd(int dayOfWeek) {
		return 8 - dayOfWeek;
	}

	private void updateCells() {
		Calendar cal;
		Integer counter = 0;
		if (dateDisplay != null) 
			cal = (Calendar)dateDisplay.clone();
		else
			cal = Calendar.getInstance();

		cal.set(Calendar.DAY_OF_MONTH, 1);

		int daySpacing = getDaySpacing(cal.get(Calendar.DAY_OF_WEEK));

		// INFO : wrong calculations of first line - fixed
		if (daySpacing > 0) {
			Calendar prevMonth = (Calendar)cal.clone();
			prevMonth.add(Calendar.MONTH, -1);
			prevMonth.set(Calendar.DAY_OF_MONTH, prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH) - daySpacing + 1);
			for(int i=0; i<daySpacing; i++) {
				CheckableLayout cell = cells.get(counter);
				cell.setTag(new CardGridItem(Integer.valueOf(prevMonth.get(Calendar.DAY_OF_MONTH))).setEnabled(false));
				cell.setEnabled(false);
				(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem)cell.getTag());
				counter++;
				prevMonth.add(Calendar.DAY_OF_MONTH, 1);
			}
		}

		int firstDay = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		int lastDay = cal.get(Calendar.DAY_OF_MONTH)+1;
		for(int i=firstDay; i<lastDay; i++) {
			cal.set(Calendar.DAY_OF_MONTH, i-1);
			Calendar date = (Calendar)cal.clone();
			date.add(Calendar.DAY_OF_MONTH, 1);
			CheckableLayout cell = cells.get(counter);
			cell.setTag(new CardGridItem(i).setEnabled(true).setDate(date));
			cell.setEnabled(true);
			cell.setVisibility(View.VISIBLE);
			(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem)cell.getTag());
			counter++;
		}

		if (dateDisplay != null) 
			cal = (Calendar)dateDisplay.clone();
		else
			cal = Calendar.getInstance();

		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		daySpacing = getDaySpacingEnd(cal.get(Calendar.DAY_OF_WEEK));

		if (daySpacing > 0) {
			for(int i=0; i<daySpacing; i++) {
				CheckableLayout cell = cells.get(counter);
				cell.setTag(new CardGridItem(i+1).setEnabled(false)); // .setDate((Calendar)cal.clone())
				cell.setEnabled(false);
				cell.setVisibility(View.VISIBLE);
				(mOnItemRender == null ? mOnItemRenderDefault : mOnItemRender).onRender(cell, (CardGridItem)cell.getTag());
				counter++;
			}
		}

		if (counter < cells.size()) {
			for(int i=counter; i<cells.size(); i++) {
				cells.get(i).setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		int i =0;

		if (changed && cells.size() > 0) {
			int size = (r - l) / 7;
			for(CheckableLayout cell : cells) {
				cell.getLayoutParams().height = size;

				//cell.setBackground(R.drawable.border);
				//get selected style from database
				String style = new API(con).getStyle();
				//get system resources
				Resources res = getResources();

				if(i == today - 1)
				{
					CircleView cirlcoe = new CircleView(con);
					cell.addView(cirlcoe);
					cell.getChildAt(0).bringToFront();
					Log.d("chien", String.valueOf(cell.getChildCount()));
					//cell.getChildAt(0).bringToFront();
					//cell.setBackgroundResource(R.drawable.selected_cell);
					//cell.getLayoutParams().height *= 0.95;
					//cell.setScaleY(0.50f);
					//cell.setPadding(0, 30, 0, 0);
				}


				if(style.equals("masculino"))
				{
					cell.setBackgroundResource(R.drawable.border_male);
				}
				if(style.equals("femenino"))
				{
					//cell.setBackgroundResource(R.drawable.selected_cell);
					cell.setBackgroundResource(R.drawable.border_female);
					//cell.setBackgroundResource(R.drawable.selected_cell);
				}
				if(style.equals("neutral"))
				{
					cell.setBackgroundResource(R.drawable.border_neutral);
				}


				i++;

			}

		}
	}

	public int getItemLayout() {
		return itemLayout;
	}

	public void setItemLayout(int itemLayout) {
		this.itemLayout = itemLayout;
		//mCardGridAdapter.setItemLayout(itemLayout);
	}

	public OnItemRender getOnItemRender() {
		return mOnItemRender;
	}

	public void setOnItemRender(OnItemRender mOnItemRender) {
		this.mOnItemRender = mOnItemRender;
		//mCardGridAdapter.setOnItemRender(mOnItemRender);
	}

	public Calendar getDateDisplay() {
		return dateDisplay;
	}

	public void setDateDisplay(Calendar dateDisplay) {
		this.dateDisplay = dateDisplay;
		cardTitle.setText(new SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(dateDisplay.getTime()));
	}

	public OnCellItemClick getOnCellItemClick() {
		return mOnCellItemClick;
	}

	public void setOnCellItemClick(OnCellItemClick mOnCellItemClick) {
		this.mOnCellItemClick = mOnCellItemClick;
	}

	/**
	 * call after change any input data - to refresh view
	 */
	public void notifyChanges() {
		//mCardGridAdapter.init();
		updateCells();
	}

	//returns current selected cell
	//	public CheckableLayout getCurrentCell()
	//	{
	//		return currentCell;
	//	}

	//sets current cell
	public void setCurrentCell(CheckableLayout v)
	{
		//this.currentCell = v;
	}

	public void cleanCellStyle()
	{
		for(CheckableLayout temp : cells)
		{
			if(temp.getChildCount()>1)
			{
				Log.d("chien", "Children per Cell: " + String.valueOf(temp.getChildCount()));
				temp.removeViewAt(0);
			}
		}
	}

	public void updateSelectedCellStyle(String num)
	{
		CheckableLayout v = new CheckableLayout(con);

		if(((TextView)currentCell).getText().equals(num))
			return;

		for(CheckableLayout temp : cells)
		{
			if(temp.getChildCount()>1)
			{
				Log.d("chien", "Children per Cell: " + String.valueOf(temp.getChildCount()));
				temp.removeViewAt(0);
			}
			Log.d("chien", ((TextView)temp.getChildAt(0)).getText().toString());
			if (((TextView)temp.getChildAt(0)).getText().equals(num)) 
			{
				v = temp;
				CircleView cirlcoe = new CircleView(con);
				temp.addView(cirlcoe);
				temp.getChildAt(0).bringToFront();
				Log.d("chien", String.valueOf(temp.getChildCount()));

			}
		}

		//updates new cell style
		((TextView)v.getChildAt(1)).setTextColor(Color.BLACK);


		((TextView)currentCell).setTextColor(Color.WHITE);

		//save new cell as current
		currentCell = (CheckedTextView) v.getChildAt(1);

		//if a dieta has not been selected, urge the user to do so
		if(!new API(con).IsDietaSet())
		{
			Toast.makeText(con, "Selecciona una dieta", Toast.LENGTH_SHORT).show();
		}
	}

	private String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11 ) {
			month = months[num];
		}
		return month;
	}

	private class CircleView extends View
	{

		public CircleView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
		}

		public CircleView(Context context){
			super(context);
		}

		@Override
		protected void onDraw(Canvas canvas) 
		{
			final float scale = con.getResources().getDisplayMetrics().density;
			int dist = (int) (30 * scale + 0.5f);
			super.onDraw(canvas);
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.RED);


			//canvas.drawColor(Color.WHITE);

			paint.setColor(Color.WHITE);
			canvas.drawCircle((int) (19 * scale + 0.5f), (int) (20 * scale + 0.5f), (int) (13 * scale + 0.5f), paint);
		}

	}

}
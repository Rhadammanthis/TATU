package com.example.miyoideal.extra;

import java.util.Arrays;

import com.example.miyoideal.HomeActivity;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<String> {

	Context context; 
	int layoutResourceId;    
	String data[] = null;

	public MyArrayAdapter(Context context, int layoutResourceId, String[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final float scale = context.getResources().getDisplayMetrics().density;
		int pixels = (int) (60 * scale + 0.5f);
		TextView tv = (TextView) super.getView(position, convertView, parent);

		tv.setTextColor(Color.WHITE);
		
		//LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    //llp.setMargins(0, (int) (5 * scale + 0.5f), 0, 0);
	    //tv.setLayoutParams(llp);
		//tv.setPadding((int) (20 * scale + 0.5f), (int) (1 * scale + 0.5f), 0, (int) (1 * scale + 0.5f));
		//((LinearLayout.LayoutParams)(tv.getLayoutParams())).setMargins((int) (5 * scale + 0.5f), 0, 0, 0);

		return tv;
	}

}

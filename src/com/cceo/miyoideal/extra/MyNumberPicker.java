package com.cceo.miyoideal.extra;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

public class MyNumberPicker extends NumberPicker{
	
	private Context con;

	public MyNumberPicker(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setMinValue(0);		
		con = context;
		initValues();
		
		
	}
	
	public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        con = context;
		initValues();
    }

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        con = context;
		initValues();
    }
    
    private void initValues()
    {
    	String[] nums = new String[20];
		for(int i=0; i<nums.length; i++)
		       nums[i] = Integer.toString(i);
		
		if(new API(con).getPesoIdeal().equals("")
				|| new API(con).getPesoIdeal().equals(null))
		{
			
		}

		setMinValue(1);
		setMaxValue(20);
		setWrapSelectorWheel(false);
		setDisplayedValues(nums);
		setValue(1);
		
		setFocusable(false);
		setValue(10);
    }

}

package com.example.miyoideal.extra;

import com.daimajia.swipe.SwipeLayout;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MyLinearLayout extends LinearLayout{
	
	private static final String DEBUG_TAG = "gesture";
	RelativeLayout rl;
	SwipeLayout sl;

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public MyLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		
//		//rl.onTouchEvent(ev);
//		//sl.open();
//		
//		int action = MotionEventCompat.getActionMasked(ev);
//        
//	    switch(action) {
//	        case (MotionEvent.ACTION_DOWN) :
//	            Log.d(DEBUG_TAG,"Action was DOWN");
//	            return true;
//	        case (MotionEvent.ACTION_MOVE) :
//	            Log.d(DEBUG_TAG,"Action was MOVE");
//	            return true;
//	        case (MotionEvent.ACTION_UP) :
//	            Log.d(DEBUG_TAG,"Action was UP");
//	            return true;
//	        case (MotionEvent.ACTION_CANCEL) :
//	            Log.d(DEBUG_TAG,"Action was CANCEL");
//	            return true;
//	        case (MotionEvent.ACTION_OUTSIDE) :
//	            Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
//	                    "of current screen element");
//	            return true;      
//
//	    }
//		
//		return true;		
//	}
	
	public void setRL(RelativeLayout rl)
	{
		this.rl = rl;
	}
	
	public void setSL(SwipeLayout sp)
	{
		this.sl = sp;
	}

}

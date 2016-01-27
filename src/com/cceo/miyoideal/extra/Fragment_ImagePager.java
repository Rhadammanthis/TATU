package com.cceo.miyoideal.extra;


import com.cceo.miyoideal.TutorialActivity;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import com.cceo.miyoideal.R;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

public class Fragment_ImagePager extends DialogFragment
{
	private Context con;
	
	public Fragment_ImagePager(Context con)
	{
		this.con = con;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
		final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_imagepager, null);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.fragment_imagepager, null);
		
		ViewPager viewPager = (ViewPager) v.findViewById(R.id.fragment_viewpager);
		ImagePagerAdapter adapter = new ImagePagerAdapter();
		viewPager.setAdapter(adapter);
		
		CirclePageIndicator indicator = (CirclePageIndicator)v.findViewById(R.id.fragment_indicator);
		indicator.bringToFront();
        indicator.setViewPager(viewPager);
        indicator.setSnap(true);
        //indicator.setBackgroundColor(Color.BLACK);
        indicator.setFillColor(Color.parseColor("#808080"));
        indicator.setPageColor(Color.WHITE);
        indicator.setStrokeColor(Color.parseColor("#323232"));
        indicator.setRadius(9f);
        
        

        final Drawable d = new ColorDrawable(Color.BLACK);
        d.setAlpha(200);

        dialog.getWindow().setBackgroundDrawable(d);
        dialog.getWindow().setContentView(v);
        
        return dialog;
	
	}
	
	private class ImagePagerAdapter extends PagerAdapter {
		private int[] mImages = new int[] {
				R.drawable.image3,
				R.drawable.image4,
		};

		@Override
		public int getCount() {
			return mImages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = con;
			ImageView imageView = new ImageView(context);		  
			//imageView.setPadding(15, 15, 15, 15);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setImageResource(mImages[position]);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}

}

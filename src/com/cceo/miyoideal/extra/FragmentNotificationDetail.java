package com.cceo.miyoideal.extra;

import com.cceo.miyoideal.R;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentNotificationDetail extends DialogFragment
{
	private Context con;
	private String title;
	private String message;
	
	public FragmentNotificationDetail(Context con, String title, String message)
	{
		this.con = con;
		this.title = title;
		this.message = message;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_Translucent_NoTitleBar);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.fragment_notification_detail, null);
		
		TextView tvTitle = (TextView) v.findViewById(R.id.fragment_notif_tvTitle);
		TextView tvMessage = (TextView) v.findViewById(R.id.fragment_notif_tvMessage);
		ImageView ivCheck = (ImageView) v.findViewById(R.id.fragment_notif_check);
		
		ivCheck.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		Font font = new Font();
		font.changeFontRaleway(con, tvMessage);
		font.changeFontRaleway(con, tvTitle);
		
		tvTitle.setText(title);
		tvMessage.setText(message);

        dialog.getWindow().setContentView(v);
        
        return dialog;
	
	}

}

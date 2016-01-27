package com.cceo.miyoideal.extra;

import com.cceo.*;
import com.cceo.DAO.DAO_Usuario;
import com.cceo.DTO.DTO_Usuario;
import com.cceo.miyoideal.R;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MenuFragment extends Fragment{

	private static ImageView profile_pic;
	private static Context ctx;
	String facebook_id;
	View rootGlobal;

	//	public MenuFragment(String id)
	//	{
	//		facebook_id = id;
	//	}

	public void setFacebookId(String id)
	{
		facebook_id = id;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		rootGlobal = inflater.inflate(R.layout.fragment_menu, container,
				false);
		profile_pic = (ImageView) rootGlobal.findViewById(R.id.menu_grament_picture);


		return rootGlobal;

	}

	public void runImageAsyncTask()
	{
		//profile_pic.setBackgroundResource(R.drawable.com_facebook_profile_picture_blank_portrait);
		ImageAsyncTask programming = new ImageAsyncTask("simple", profile_pic);
		programming.setId(facebook_id);
		programming.execute();
	}
	
	public void setContext(Context ctx)
	{
		this.ctx = ctx;
	}

	public static void getFacebookProfilePic(Bitmap bitMap)
	{
		//profile_pic.setBackgroundResource(R.drawable.com_facebook_profile_picture_blank_portrait);
		if(bitMap != null)
			profile_pic.setImageBitmap(API.getRoundedShape(bitMap));
	}
	
	public void setDefaultProfilePic()
	{
		DTO_Usuario user = new DAO_Usuario(ctx).getUsuario();
		if(user.getSexo().equals("Masculino"))
		{
			Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.com_facebook_profile_picture_blank_square);
			profile_pic.setImageBitmap(API.getRoundedShape(icon));
		}
		else
		{
			Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(),
                    R.drawable.female);
			profile_pic.setImageBitmap(API.getRoundedShape(icon));
		}
		
	}


}

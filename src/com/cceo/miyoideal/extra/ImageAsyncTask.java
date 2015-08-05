package com.cceo.miyoideal.extra;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.cceo.miyoideal.HomeActivity;
import com.cceo.miyoideal.MainActivity;
import com.cceo.miyoideal.ShareActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageAsyncTask extends AsyncTask<String, Integer,String>{
	
	private String id;
	private Bitmap bitmap;
	private String parent;
	ImageView imageView;


	public ImageAsyncTask(String from, ImageView imView) {
		// TODO Auto-generated constructor stub
		parent = from;
		imageView = imView;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
	    
	    final String nomimg = "https://graph.facebook.com/"+id+"/picture?type=large";
	    URL imageURL = null;

	    try {
	        imageURL = new URL(nomimg);
	    } catch (MalformedURLException e) {
	        e.printStackTrace();
	    }

	    try {
	    	InputStream in = (InputStream) imageURL.getContent();
	        bitmap = BitmapFactory.decodeStream(in);

	    } catch (IOException e) {

	        e.printStackTrace();
	    }
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(parent.equals("main"))
			MainActivity.setFacebokProfilePic(bitmap);
		
		if(parent.equals("simple"))
		{
			MenuFragment.getFacebookProfilePic(bitmap);
			//imageView.setImageBitmap(bitmap);
		}
		else
			HomeActivity.setFacebokProfilePic(bitmap);
	}

}

package com.cceo.miyoideal.extra;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.cceo.miyoideal.HomeActivity;
import com.cceo.miyoideal.ShareActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageAsynkTaskSimple extends AsyncTask<String, Void,Bitmap>{
	
	private String id;
	private Bitmap bitmap;
	private String parent;
	ImageView imageView;


	public ImageAsynkTaskSimple(String from, ImageView imView) {
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
	protected Bitmap doInBackground(String... params) {
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
		return bitmap;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if(parent.equals("simple"))
		{
			//imageView.setImageBitmap(result);
			MenuFragment.getFacebookProfilePic(bitmap);
		}
		else
			HomeActivity.setFacebokProfilePic(bitmap);
	}

}

package com.cceo.miyoideal.extra;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MLRoundedImageView extends ImageView {

	public MLRoundedImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MLRoundedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MLRoundedImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {
			return;
		}

		if (getWidth() == 0 || getHeight() == 0) {
			return; 
		}
		// BitmapDrawable d = new BitmapDrawable();
		Bitmap b =  ((BitmapDrawable)drawable).getBitmap() ;
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

		int w = getWidth(), h = getHeight();

		//Bitmap roundBitmap =  getCroppedBitmap(bitmap, w);
		//canvas.drawBitmap(roundBitmap, 0,0, null);

	}
}

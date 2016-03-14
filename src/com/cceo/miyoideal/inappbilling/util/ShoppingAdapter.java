package com.cceo.miyoideal.inappbilling.util;

import java.util.ArrayList;

import com.cceo.DAO.DAO_Shopping;
import com.cceo.DTO.ShoppingItem;
import com.cceo.miyoideal.R;
import com.cceo.miyoideal.extra.API;
import com.cceo.miyoideal.extra.Font;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingAdapter extends  RecyclerView.Adapter<ShoppingAdapter.ViewHolderShopping>{
	
    public ArrayList<ShoppingItem> dataSet;
   // private ProgramClickListener clickListener;
    private Context context;

    public ShoppingAdapter(ArrayList<ShoppingItem> dataSet, Context context)//ArrayList<ConferenceItem> dataSet, ProgramClickListener clickListener, Context context)
    {
        this.dataSet = dataSet;
//        this.clickListener = clickListener;
        this.context = context;
    }

    @Override
    public ViewHolderShopping onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        view = LayoutInflater.from(parent.getContext())
                .inflate(getStyledTemplate(), parent, false);
        ViewHolderShopping vH = new ViewHolderShopping(view, context);

        return vH;
    }

    @Override
    public void onBindViewHolder(final ViewHolderShopping holder, final int position) {
    	
        Font font = new Font();
        font.changeFontRaleway(context, holder.textItem);
    	
    	holder.textItem.setText(dataSet.get(position).getItem());
    	holder.checkBoxStatus.setChecked((dataSet.get(position).isChecked()));
    	
    	holder.checkBoxStatus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//	Toast.makeText(context, String.valueOf(holder.checkBoxStatus.isChecked()), Toast.LENGTH_LONG).show();
				//Toast.makeText(context, "id_shopping: " + String.valueOf(dataSet.get(position).getIDShopping()), Toast.LENGTH_LONG).show();
				//Toast.makeText(context, "Before: " + String.valueOf(dataSet.get(position).isChecked()), Toast.LENGTH_LONG).show();
				new DAO_Shopping(context).setStatus(holder.checkBoxStatus.isChecked(), dataSet.get(position).getIDShopping());
				//Toast.makeText(context, "After: " + String.valueOf(dataSet.get(position).isChecked()), Toast.LENGTH_LONG).show();
			}
		});

//        holder.textTitle.setText(dataSet.get(position).getTitle());
//        holder.textSpeaker.setText(dataSet.get(position).getSpeaker());
//        holder.textTime.setText("");
//        holder.ivProfile.setImageResource(dataSet.get(position).getPic());
//
//
//        holder.cardBody.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickListener.onCardClick(v,position);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    
	private int getStyledTemplate()
	{
		String style = new API(context).getStyle();
		//get system resources
		Resources res = context.getResources();

		if(style.equals("masculino"))
		{
			return R.layout.item_shopping_male;
		}
		if(style.equals("femenino"))
		{
			return R.layout.item_shopping_female;
		}
		if(style.equals("neutral"))
		{
			return R.layout.item_shopping_neutral;
		}
		
		return R.layout.item_shopping_neutral;

	}


    public static class ViewHolderShopping extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textItem;
        public CheckBox checkBoxStatus;

        public ViewHolderShopping(View v, Context context) {
            super(v);
            textItem = (TextView) v.findViewById(R.id.shoppingTextItem);
            checkBoxStatus = (CheckBox) v.findViewById(R.id.shoppingCheckBoxStstus);
            

        }
    }

}

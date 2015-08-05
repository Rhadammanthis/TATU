package com.cceo.miyoideal.extra;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cceo.DAO.DAO_DietaCompletada;
import com.cceo.DAO.DAO_Usuario;
import com.cceo.DTO.DTO_DietaCompletada;
import com.cceo.miyoideal.R;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

public class DietaCompletedDialog extends DialogFragment{

	private Button buttonGuardar;
	private Context con;
	private Dialog d;

	private EditText peso;
	private EditText talla;
	private ImageButton facebook;
	private ImageButton twitter;
	
	public DietaCompletedDialog(Context context) {
	
		// TODO Auto-generated constructor stub
		//d = this;
		con = context;
		setCancelable(false);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(con);
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.baseline_share, null);
		
		//dieta ID
		TextView message = (TextView) v.findViewById(R.id.shareText2);
        String m = message.getText().toString();
        message.setText(m.replace("XXX", new API(con).getID_Dieta()));
        
        peso = (EditText) v.findViewById(R.id.pesoEditText_Share);
        talla = (EditText) v.findViewById(R.id.tallaEditText_Share);
        facebook = (ImageButton) v.findViewById(R.id.finished_dialog_facebook);
        twitter = (ImageButton) v.findViewById(R.id.finished_dialog_twitter);

		builder.setView(v);
		builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
//				// TODO Auto-generated method stub
////                peso = (EditText) v.findViewById(R.id.pesoEditText_Share);
////                talla = (EditText) v.findViewById(R.id.tallaEditText_Share);
//
//                Calendar c = Calendar.getInstance();
//                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
//                
//                if(tryParseFloat(peso.getText().toString()) && tryParseFloat(talla.getText().toString()))
//                {
//                    DTO_DietaCompletada dietaCompletada = new DTO_DietaCompletada("", new API(con).getID_Dieta(), 
//                            peso.getText().toString(), talla.getText().toString(), df.format(c.getTime()).toString());
//
//                    new DAO_DietaCompletada(con).newDietaCompletada(dietaCompletada);
//
//
//                    //clear control information
//                    new API(con).clearDieta();
//                    new DAO_Usuario(con).updatePesoActual(peso.getText().toString());
//
//                    d.dismiss();
//                }
//                else
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(con);
//                    builder.setMessage("No se han llenado todos los campos")
//                    .setCancelable(false)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                        	
//                        }
//                    });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }
			}
		});
		
		return builder.create();
	}
	
    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if (activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
    
    @Override
    public void onStart()
    {
        super.onStart();    //super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after this point
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
        	//twitter share
        	twitter.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String tweet;
					
					if(tryParseFloat(peso.getText().toString()) && tryParseFloat(talla.getText().toString()))
                    {
						tweet = "¡Ahora peso " + peso.getText().toString() + " kg gracias a #YoIdeal!";
                    }
					else
					{
						tweet = "Termine mi Dieta! con asjhgdoaudh";
					}
					
					TweetComposer.Builder builder = new TweetComposer.Builder(con)
					.text(tweet);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     
					builder.show();
				}
			});
        	
        	//facebook share
        	facebook.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ShareLinkContent content = new ShareLinkContent.Builder()
			        .setContentUrl(Uri.parse("http://miyoideal.com/"))
			        .setContentTitle("Prueba Yo Ideal ya!")
			        .build();
					ShareDialog shareD = new ShareDialog(getActivity());
					shareD.show(content);
				}
			});
        	
        	
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
            				// TODO Auto-generated method stub
                            //EditText peso = (EditText) v.findViewById(R.id.pesoEditText_Share);
                            //EditText talla = (EditText) v.findViewById(R.id.tallaEditText_Share);

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
                            
                            if(tryParseFloat(peso.getText().toString()) && tryParseFloat(talla.getText().toString()))
                            {
                                DTO_DietaCompletada dietaCompletada = new DTO_DietaCompletada("", new API(con).getID_Dieta(), 
                                        peso.getText().toString(), talla.getText().toString(), df.format(c.getTime()).toString());

                                new DAO_DietaCompletada(con).newDietaCompletada(dietaCompletada);


                                //clear control information
                                new API(con).clearDieta();
                                new DAO_Usuario(con).updatePesoActual(peso.getText().toString());

                                dismiss();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(con);
                                builder.setMessage("No se han llenado todos los campos")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    	
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    });
        }
    }


	//returns boolean after checking if string can be parsed into float
	boolean tryParseFloat(String value)  
	{  
		try  
		{  
			Float.parseFloat(value);  
			return true;  
		} catch(NumberFormatException nfe)  
		{  
			return false;  
		}  
	}


}

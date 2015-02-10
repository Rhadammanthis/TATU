package com.example.miyoideal.extra;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.DAO.DAO_DietaCompletada;
import com.example.DTO.DTO_DietaCompletada;
import com.example.miyoideal.R;

public class DietaCompletedDialog extends Dialog{

	private Button buttonGuardar;
	private Context con;
	private Dialog d;

	public DietaCompletedDialog(Context context) {
		super(context);		
		// TODO Auto-generated constructor stub
		d = this;
		con = context;

		//prevents from closing when touching outside
		setCanceledOnTouchOutside(false);
		this.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				// TODO Auto-generated method stub

				setContentView(R.layout.baseline_share);
				setTitle("Dieta Completa");

				TextView message = (TextView) findViewById(R.id.shareText2);

				String m = message.getText().toString();
				Log.d("chino", m);

				message.setText(m.replace("XXX", new API(con).getID_Dieta()));
				Log.d("chino", message.getText().toString());

				buttonGuardar = (Button) findViewById(R.id.buttonGuardar_share);
				buttonGuardar.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						EditText peso = (EditText) findViewById(R.id.pesoEditText_Share);
						EditText talla = (EditText) findViewById(R.id.tallaEditText_Share);

						Calendar c = Calendar.getInstance();
						SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");

						//checks if information has been written into the edittexts
						if(tryParseFloat(peso.getText().toString()) && tryParseFloat(talla.getText().toString()))
						{
							DTO_DietaCompletada dietaCompletada = new DTO_DietaCompletada("", new API(con).getID_Dieta(), 
									peso.getText().toString(), talla.getText().toString(), df.format(c.getTime()).toString());

							new DAO_DietaCompletada(con).newDietaCompletada(dietaCompletada);


							//clear control information
							//new API(con).clearDieta();

							d.dismiss();
						}
						else
						{
							AlertDialog.Builder builder = new AlertDialog.Builder(con);
							builder.setMessage("No se han llenado todos los campos")
							.setCancelable(false)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									//do things
								}
							});
							AlertDialog alert = builder.create();
							alert.show();
						}
					}
				});

			}
		});

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

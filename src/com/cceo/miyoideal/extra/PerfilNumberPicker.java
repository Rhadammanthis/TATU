package com.cceo.miyoideal.extra;

import com.cceo.DB.SQLiteControl;
import com.cceo.miyoideal.MiPerfilActivity;
import com.cceo.miyoideal.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class PerfilNumberPicker extends DialogFragment{

	private Context con;
	private EditText np;
	private boolean shouldRecord = false;
	String get_id, get_name, get_gender, get_email, get_birthday, get_locale, get_location;

	public PerfilNumberPicker(Context context) {

		// TODO Auto-generated constructor stub

		con = context;

	}

	@Override
	public void onDismiss(final DialogInterface dialog) {
		super.onDismiss(dialog);
		if(shouldRecord)
		{
			ContentValues cv = new ContentValues();
			Log.d("chino", "np val " + np.getText().toString());
			cv.put("peso_ideal", String.valueOf(np.getText().toString()));

			SQLiteControl db = new SQLiteControl(con);
			db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
			db.close();
		}

		final Activity activity = getActivity();
		if (activity != null && activity instanceof DialogInterface.OnDismissListener) {
			((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(con);
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.numberpickerdialog, null);
		np = new EditText(getActivity());
        np.setInputType(InputType.TYPE_CLASS_NUMBER);
        np.requestFocus();
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(np)//inflater.inflate(R.layout.numberpickerdialog, null))
		// Add action buttons
		.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// Save picked value in DBCOntrol
				shouldRecord = true;
				
				String value = np.getText().toString();
		        Log.d("chino", "value " + value);
		        MiPerfilActivity callingActivity = (MiPerfilActivity) getActivity();
		        callingActivity.onUserSelectValue(value);
		        dialog.dismiss();
				
				/*ContentValues cv = new ContentValues();
				Log.d("chino", "np val " + np.getText().toString());
				cv.put("peso_ideal", String.valueOf(np.getText().toString()));

				SQLiteControl db = new SQLiteControl(con);
				db.getWritableDatabase().update("control", cv, "id_control "+"="+1, null);
				db.close();*/

			}
		})
		.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				PerfilNumberPicker.this.getDialog().cancel();
			}
		})
		.setTitle("Selecciona tu peso deseado")
		.setMessage("Please Enter Quantity");      

		//return new AlertDialog.Builder(getActivity()).setTitle(R.string.app_name).setMessage("Please Enter Quantity")
          //      .setPositiveButton("OK", this).setNegativeButton("CANCEL", null).setView(editQuantity).create();
		return builder.create();
	}


}
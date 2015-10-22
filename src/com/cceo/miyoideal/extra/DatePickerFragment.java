package com.cceo.miyoideal.extra;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

public class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {
	
	private OnDatePickerDismissListener datePickerDismissListener;
	private DatePickerDialog datePickerDialog;
	
	public DatePickerFragment(OnDatePickerDismissListener listener)
	{
		this.datePickerDismissListener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);

		// Create a new instance of DatePickerDialog and return it
		return datePickerDialog;
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		//Toast.makeText(getActivity(), String.valueOf(year), Toast.LENGTH_SHORT).show();
		
		String monthFix = "";
		if(String.valueOf(month+1).length()==1)
			monthFix = "0" + String.valueOf(month+1); 
		else
			monthFix = String.valueOf(month+1);
		
		String date = String.valueOf(day) + "-" + monthFix + "-" +String.valueOf(year); 
		
		new API(getActivity()).setGoalDate(date);
		
		//save date to BD
		
	}
	
    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        
        datePickerDismissListener.onDismissListener();
    }
}
package com.home.reservas.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by Reynaldo on 13/03/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    onDateSelectedListener dateListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Tomar la fecha actual
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Crea una nueva instancia
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dateListener = (onDateSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        GregorianCalendar calendar = new GregorianCalendar(year,month,day);
        Date fechaSeleccionada = new Date(calendar.getTimeInMillis());
        Integer viewId = new Integer(0);
        if(getArguments().get("view")!=null){
            viewId = (Integer) getArguments().get("view");
        }
        dateListener.onDateSelected(fechaSeleccionada, viewId.intValue());
    }

    public interface onDateSelectedListener{
        public void onDateSelected(Date fechaSeleccionada, int viewId);

    }

}


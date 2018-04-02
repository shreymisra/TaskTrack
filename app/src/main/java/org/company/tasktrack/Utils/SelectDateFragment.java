package org.company.tasktrack.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    String date, min, max;
    EditText et;
    Date d = null;
    Calendar minDate = Calendar.getInstance();

    Calendar maxDate = Calendar.getInstance();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            d = sdf.parse(date);
            minDate.setTime(sdf.parse(min));
            minDate.set(Calendar.HOUR_OF_DAY, minDate.getMinimum(Calendar.HOUR_OF_DAY));
            minDate.set(Calendar.MINUTE, minDate.getMinimum(Calendar.MINUTE));
            minDate.set(Calendar.SECOND, minDate.getMinimum(Calendar.SECOND));
            minDate.set(Calendar.MILLISECOND, minDate.getMinimum(Calendar.MILLISECOND));
            maxDate.setTime(sdf.parse(max));
            maxDate.set(Calendar.HOUR_OF_DAY, maxDate.getMaximum(Calendar.HOUR_OF_DAY));
            maxDate.set(Calendar.MINUTE, maxDate.getMaximum(Calendar.MINUTE));
            maxDate.set(Calendar.SECOND, maxDate.getMaximum(Calendar.SECOND));
            maxDate.set(Calendar.MILLISECOND, maxDate.getMaximum(Calendar.MILLISECOND));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, 1);
        c.set(Calendar.MILLISECOND, 1);
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);
        d.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        d.getDatePicker().setMinDate(minDate.getTimeInMillis());
        return d;
    }

    public void show(FragmentManager manager, String tag, EditText et, String minDate, String max) {
        super.show(manager, tag);
        this.date = tag;
        this.et = et;
        this.min = minDate;
        this.max = max;
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm + 1, dd);
    }

    public void populateSetDate(int year, int month, int day) {
        et.setText(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
    }
}
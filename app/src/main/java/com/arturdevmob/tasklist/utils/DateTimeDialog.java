package com.arturdevmob.tasklist.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.arturdevmob.tasklist.R;

import java.util.Calendar;

public class DateTimeDialog {
    private OnEventDate mOnEventDate;
    private Context mContext;
    private Calendar mCalendar;

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    public interface OnEventDate {
        void onSetDate(long date);
        void onRemoveDate();
    }

    public DateTimeDialog(Context context, OnEventDate onEventDate) {
        mOnEventDate = onEventDate;
        mContext = context;
        mCalendar = Calendar.getInstance();

        this.showDatePickerDialog();
    }

    private void showDatePickerDialog() {
        mDatePicker = (DatePicker) LayoutInflater.from(mContext).inflate(R.layout.date_picker, null, false);

        new AlertDialog.Builder(mContext)
                .setView(mDatePicker)
                .setNeutralButton(R.string.cancel, null)
                .setNegativeButton(R.string.delete, setOnClickButtonListenerInDateDialog())
                .setPositiveButton(R.string.apply, setOnClickButtonListenerInDateDialog())
                .create()
                .show();
    }

    private void showTimePickerDialog() {
        mTimePicker = (TimePicker) LayoutInflater.from(mContext).inflate(R.layout.time_picker, null, false);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setOnTimeChangedListener(setOnTimeChangedListenerTimeDialog());

        new AlertDialog.Builder(mContext)
                .setView(mTimePicker)
                .setNeutralButton(R.string.cancel, null)
                .setPositiveButton(R.string.apply, setOnClickButtonListenerInDateDialog())
                .create()
                .show();
    }

    // Слушатель нажатий на кнопки в диалоге с датой
    private DialogInterface.OnClickListener setOnClickButtonListenerInDateDialog() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        mOnEventDate.onRemoveDate();
                        break;

                    case DialogInterface.BUTTON_POSITIVE:
                        // Показывается диалог с датой иначе со временем
                        if (mTimePicker == null) {
                            mCalendar.set(Calendar.YEAR, mDatePicker.getYear());
                            mCalendar.set(Calendar.MONTH, mDatePicker.getMonth());
                            mCalendar.set(Calendar.DAY_OF_MONTH, mDatePicker.getDayOfMonth());

                            showTimePickerDialog();
                        } else {
                            mOnEventDate.onSetDate(mCalendar.getTimeInMillis());
                        }
                        break;
                }
            }
        };
    }

    // Слушатель изменения времени
    private TimePicker.OnTimeChangedListener setOnTimeChangedListenerTimeDialog() {
        return new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
            }
        };
    }
}

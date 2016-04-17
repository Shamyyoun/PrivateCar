package com.privateegy.privatecar.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.privateegy.privatecar.R;
import com.privateegy.privatecar.utils.DateUtil;
import com.privateegy.privatecar.utils.Utils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Shamyyoun on 2/17/2016.
 */
public class DateTimeDialog extends ParentDialog {
    private OnTimeSelectedListener listener;
    private boolean minCurrentDate; // used to prevent user select date lower than current date
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnSet;
    private Button btnCancel;

    public DateTimeDialog(Context context, final OnTimeSelectedListener listener) {
        super(context);
        this.listener = listener;
        init();
    }

    public DateTimeDialog(Context context, boolean minCurrentDate, final OnTimeSelectedListener listener) {
        super(context);
        this.minCurrentDate = minCurrentDate;
        this.listener = listener;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_date_time);

        // init views
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        btnSet = (Button) findViewById(R.id.btn_set);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // customize pickers
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        if (minCurrentDate) datePicker.setMinDate(calendar.getTimeInMillis() - 1000);

        // add set click listener
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prepare calendar object and fire the listener
                Calendar calendar = Calendar.getInstance(Locale.getDefault());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.YEAR, datePicker.getYear());

                // check date
                if (minCurrentDate && DateUtil.isPastDate(calendar)) {
                    // show error
                    Utils.showShortToast(getContext(), R.string.invalid_date);
                } else {
                    // fire listener and dismiss
                    listener.onTimeSelected(calendar);
                    dismiss();
                }
            }
        });

        // add cancel click listener
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fire listener and dismiss
                listener.onCancelled();
                dismiss();
            }
        });

        // add cancel listener to the dialog
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // fire listener and dismiss
                listener.onCancelled();
                dismiss();
            }
        });
    }

    public interface OnTimeSelectedListener {
        public void onTimeSelected(Calendar calendar);

        public void onCancelled();
    }
}

package com.example.testavocado.Dialogs;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testavocado.R;

import java.util.Calendar;

public class CallenderDialog extends DialogFragment {
    private static final String TAG = "CallenderDialog";

    public interface OnDatePickListener {
        public void onDatePick(String date);
    }

    OnDatePickListener onDatePickListener;

    CalendarView calendarView;
    TextView cancel;
    int currentYear, currentDay, currentMonth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_calender, container, false);

        calendarView = view.findViewById(R.id.calender);
        cancel = view.findViewById(R.id.cancel);
        getCurrentDate();
        attachOnClicks();
        return view;
    }



    /**
     *
     *          getting the current date
     */

    private void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(currentYear, currentMonth, currentDay);
    }


    /**
     *
     *      attaching on click for widgets
     *
     */
    private void attachOnClicks() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (year > currentYear || month > currentMonth || dayOfMonth > currentDay)
                    Toast.makeText(getContext(), "date incorrect", Toast.LENGTH_SHORT).show();
                else {
                    onDatePickListener.onDatePick(dayOfMonth + "/" + month + "/" + year);
                    dismiss();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    /**
     *
     *              on attach getting the fragment that called the dialog
     *
     * @param context
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onDatePickListener = (OnDatePickListener) getTargetFragment();
        } catch (ClassCastException e) {

        }

    }
}


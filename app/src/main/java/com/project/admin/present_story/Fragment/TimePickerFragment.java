package com.project.admin.present_story.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.project.admin.present_story.R;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    DialogResult dialogResult;

    public TimePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(),this, hour, minute, false);
        return tpd;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_picker, container, false);
        return view;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String ampm = "AM";
        if(hourOfDay > 11) {
            ampm = "PM";
        }

        int currentHour;

        if(hourOfDay > 11) {
            currentHour = hourOfDay - 12;
        } else {
            currentHour = hourOfDay;
        }

        if(dialogResult != null) {
            dialogResult.result(ampm + " " + currentHour + ":" + minute);
        }
        this.dismiss();
    }

    void setDialogResult(DialogResult dialogResult) {
        this.dialogResult = dialogResult;
    }

    public interface DialogResult {
        void result(String time);
    }
}

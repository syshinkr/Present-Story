package com.project.admin.present_story.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.project.admin.present_story.R;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private static final String TAG = "DatePickerFragment";
    DialogResult dialogResult;

    public DatePickerFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(c.YEAR);
        int month = c.get(c.MONTH) + 1;
        int date = c.get(c.DATE);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, year, month, date);
        return dpd;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    void setDialogResult(DialogResult dialogResult) {
        this.dialogResult = dialogResult;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(dialogResult != null) {
            Log.i(TAG, year + "");
            Log.i(TAG, month + "");
            Log.i(TAG, dayOfMonth + "");
            dialogResult.result(year, month, dayOfMonth);
        }
    }



    public interface DialogResult{
        // TODO: Update argument type and name
        void result(int year, int month, int dayOfMonth);
    }
}

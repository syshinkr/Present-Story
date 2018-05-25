package com.project.admin.present_story.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.project.admin.present_story.R;

import java.util.Calendar;

public class CalendarFragment extends Fragment {
    public CalendarFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        CalendarView calendarView = view.findViewById(R.id.fragment_calendar_calendarView);
//        calendarView.setMinDate(); // 2018.01.01
//        calendarView.setMaxDate(); // 2018.09.28
        calendarView.setFirstDayOfWeek(0); //표를 일요일부터 시작

        return view;
    }

    int getFirstDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.YEAR, cal.MONTH, 1);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);
        return day_of_week;
    }
}
